package app.velora.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import app.velora.core.analytics.AnalyticsTracker
import app.velora.core.designsystem.EmptyPane
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.SecondaryButton
import app.velora.core.designsystem.VeloraCard
import app.velora.core.domain.PersonalRecords
import app.velora.core.domain.WorkoutRepository
import app.velora.core.domain.WorkoutSessionEngine
import app.velora.core.model.Exercise
import app.velora.core.model.SessionState
import app.velora.core.model.SetRecord
import app.velora.core.model.WorkoutSessionRecord
import app.velora.core.model.WorkoutSetEntry
import app.velora.core.model.WorkoutTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workouts: WorkoutRepository,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    val exercises = workouts.observeExercises().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val templates = workouts.observeTemplates().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val sessions = workouts.observeSessions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    var selected by mutableStateOf<Set<String>>(emptySet())
    var templateName by mutableStateOf("Full body")
    var active by mutableStateOf<WorkoutSessionRecord?>(null)
    var reps by mutableStateOf("8")
    var weight by mutableStateOf("")
    var restUntil by mutableStateOf(0L)
    var customName by mutableStateOf("")
    var customCue by mutableStateOf("")

    fun toggle(id: String) {
        selected = if (id in selected) selected - id else selected + id
    }

    fun saveTemplate() {
        viewModelScope.launch {
            workouts.upsertTemplate(WorkoutTemplate(UUID.randomUUID().toString(), templateName.ifBlank { "Workout" }, selected.toList()))
        }
    }

    fun addExercise() {
        if (customName.isBlank()) return
        viewModelScope.launch {
            workouts.upsertExercise(Exercise(UUID.randomUUID().toString(), customName.trim(), customCue.ifBlank { "Move with control." }, "Your equipment", true))
            customName = ""
        }
    }

    fun start(template: WorkoutTemplate, library: List<Exercise>) {
        val now = System.currentTimeMillis()
        val sets = template.exerciseIds.map { id ->
            val name = library.firstOrNull { it.id == id }?.name ?: "Exercise"
            WorkoutSetEntry(UUID.randomUUID().toString(), id, name, null, null, null, false)
        }
        active = WorkoutSessionRecord(UUID.randomUUID().toString(), template.name, LocalDate.now().toString(), WorkoutSessionEngine.start(now), sets)
        analytics.track("workout_started")
    }

    fun pauseOrResume() {
        val session = active ?: return
        val now = System.currentTimeMillis()
        val timeline = if (session.timeline.state == SessionState.IN_PROGRESS) {
            WorkoutSessionEngine.pause(session.timeline, now)
        } else {
            WorkoutSessionEngine.resume(session.timeline, now)
        }
        active = session.copy(timeline = timeline)
    }

    fun completeSet(exerciseId: String, exerciseName: String) {
        val session = active ?: return
        val entry = WorkoutSetEntry(
            UUID.randomUUID().toString(), exerciseId, exerciseName,
            reps.toIntOrNull(), weight.toDoubleOrNull(), null, true,
        )
        active = session.copy(sets = session.sets + entry)
        restUntil = System.currentTimeMillis() + 60_000
        viewModelScope.launch {
            delay(60_000)
            if (restUntil <= System.currentTimeMillis()) restUntil = 0
        }
    }

    fun finish() {
        val session = active ?: return
        val completed = session.copy(timeline = WorkoutSessionEngine.complete(session.timeline, System.currentTimeMillis()))
        viewModelScope.launch {
            workouts.saveSession(completed)
            analytics.track("workout_completed")
            active = null
        }
    }

    fun records(): Map<String, Double> = PersonalRecords.maxWeightByExercise(
        sessions.value.flatMap { session -> session.sets.map { SetRecord(it.exerciseId, it.weightKilograms, it.reps) } },
    )
}

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = hiltViewModel()) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val templates by viewModel.templates.collectAsStateWithLifecycle()
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val active = viewModel.active
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Workouts")
        if (active != null) {
            val elapsed = WorkoutSessionEngine.elapsedMillis(active.timeline, System.currentTimeMillis()) / 1000
            VeloraCard {
                Text("${active.name} · ${active.timeline.state.name.lowercase()} · ${elapsed}s moving")
                if (viewModel.restUntil > System.currentTimeMillis()) Text("Rest until the minute ends. Leaving the app can pause this timer.")
                OutlinedTextField(viewModel.reps, { viewModel.reps = it }, label = { Text("Reps") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(viewModel.weight, { viewModel.weight = it }, label = { Text("Weight kg, optional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                exercises.filter { it.id in active.sets.map { set -> set.exerciseId } || it.id in viewModel.selected }.forEach { exercise ->
                    SecondaryButton("Log set · ${exercise.name}") { viewModel.completeSet(exercise.id, exercise.name) }
                }
                SecondaryButton(if (active.timeline.state == SessionState.IN_PROGRESS) "Pause" else "Resume", onClick = viewModel::pauseOrResume)
                PrimaryButton("Finish workout", onClick = viewModel::finish)
            }
        }
        Text("Library")
        if (exercises.isEmpty()) EmptyPane("Exercises appear after the first open finishes seeding.", "Pull this screen again in a moment.")
        exercises.forEach { exercise ->
            VeloraCard {
                Text(exercise.name)
                Text(exercise.cue)
                Text(exercise.equipment)
                SecondaryButton(if (exercise.id in viewModel.selected) "Selected" else "Select") { viewModel.toggle(exercise.id) }
            }
        }
        OutlinedTextField(viewModel.customName, { viewModel.customName = it }, label = { Text("Your exercise name") })
        OutlinedTextField(viewModel.customCue, { viewModel.customCue = it }, label = { Text("Cue") })
        SecondaryButton("Add exercise", onClick = viewModel::addExercise)
        OutlinedTextField(viewModel.templateName, { viewModel.templateName = it }, label = { Text("Template name") })
        PrimaryButton("Save selected as a workout", onClick = viewModel::saveTemplate)
        templates.forEach { template ->
            SecondaryButton("Start ${template.name}") { viewModel.start(template, exercises) }
        }
        Text("History")
        sessions.forEach { session -> Text("${session.localDate} ${session.name} · ${session.timeline.state.name.lowercase()}") }
        Text("Heaviest logged weight")
        viewModel.records().forEach { (id, kg) ->
            val name = exercises.firstOrNull { it.id == id }?.name ?: id
            Text("$name · $kg kg")
        }
    }
}
