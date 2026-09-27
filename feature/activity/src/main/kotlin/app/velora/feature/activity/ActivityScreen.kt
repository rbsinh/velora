package app.velora.feature.activity

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.velora.core.analytics.AnalyticsTracker
import app.velora.core.designsystem.EmptyPane
import app.velora.core.designsystem.ErrorPane
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.VeloraCard
import app.velora.core.domain.ActivityRepository
import app.velora.core.domain.StepAggregation
import app.velora.core.domain.StepRepository
import app.velora.core.health.HealthAvailability
import app.velora.core.health.HealthConnectGateway
import app.velora.core.health.HealthRead
import app.velora.core.model.ActivityEntry
import app.velora.core.model.ActivityKind
import app.velora.core.model.StepDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val steps: StepRepository,
    private val activities: ActivityRepository,
    private val health: HealthConnectGateway,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    var status by mutableStateOf("Checking Health Connect…")
        private set
    var days by mutableStateOf<List<String>>(emptyList())
        private set
    var weekTotal by mutableStateOf(0L)
        private set
    var monthTotal by mutableStateOf(0L)
        private set
    var manual by mutableStateOf("")
    var kind by mutableStateOf(ActivityKind.WALKING)
    var minutes by mutableStateOf("30")
    val permissions = health.readPermissions()
    private var rangeJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            analytics.track("steps_viewed")
            when (health.availability()) {
                HealthAvailability.UNAVAILABLE -> status = "This device does not provide Health Connect. Enter steps manually. They will be labeled manual."
                HealthAvailability.UPDATE_REQUIRED -> status = "Health Connect needs an update before steps can be read."
                HealthAvailability.AVAILABLE -> Unit
            }
            val end = LocalDate.now()
            val start = end.minusDays(29)
            when (val read = health.readDays(start, end)) {
                is HealthRead.Ready -> {
                    status = "Steps are from Health Connect for days it returned a value."
                    read.days.forEach { day ->
                        if (day.steps != null) {
                            steps.saveManual(
                                StepDay(day.date, healthConnectSteps = day.steps, manualSteps = null, manualOverridesHealthConnect = false),
                            )
                        }
                    }
                }
                is HealthRead.Denied -> status = read.message
                is HealthRead.Unavailable -> status = read.message
            }
            rangeJob?.cancel()
            rangeJob = viewModelScope.launch {
                steps.observeRange(start.toString(), end.toString()).collect { rows ->
                    val range = StepAggregation.range(rows, start, end)
                    days = range.days.map { day ->
                        val value = day.steps?.toString() ?: "no data"
                        val source = day.source?.name ?: "none"
                        "${day.date}: $value ($source)"
                    }
                    weekTotal = StepAggregation.range(rows, end.minusDays(6), end).totalSteps
                    monthTotal = range.totalSteps
                }
            }
        }
    }

    fun saveManualToday() {
        val value = manual.toLongOrNull() ?: return
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            steps.saveManual(StepDay(today, null, value, manualOverridesHealthConnect = true))
            activities.upsert(
                ActivityEntry(UUID.randomUUID().toString(), today, kind, minutes.toIntOrNull() ?: 0, null, "Entered in Velora"),
            )
            manual = ""
        }
    }
}

@Composable
fun ActivityScreen(onWorkout: () -> Unit, viewModel: ActivityViewModel = hiltViewModel()) {
    val launcher = rememberLauncherForActivityResult(PermissionController.createRequestPermissionResultContract()) {
        viewModel.refresh()
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Move")
        Text(viewModel.status)
        VeloraCard {
            Text("Last 7 days with data: ${viewModel.weekTotal} steps")
            Text("Last 30 days with data: ${viewModel.monthTotal} steps")
            Text("Missing days are omitted, not treated as zero.")
        }
        if (viewModel.days.isEmpty()) EmptyPane("No step days yet", "Grant Health Connect or type today's total.")
        viewModel.days.takeLast(7).forEach { Text(it) }
        PrimaryButton("Allow Health Connect reads") { launcher.launch(viewModel.permissions) }
        OutlinedTextField(viewModel.manual, { viewModel.manual = it }, label = { Text("Manual steps today") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(viewModel.minutes, { viewModel.minutes = it }, label = { Text("Minutes") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityKind.entries.forEach { option ->
                FilterChip(
                    selected = viewModel.kind == option,
                    onClick = { viewModel.kind = option },
                    label = { Text(option.name.lowercase()) },
                )
            }
        }
        PrimaryButton("Save manual activity") { viewModel.saveManualToday() }
        ErrorPane("Distance and active calories appear only when Health Connect provides them.", null)
        PrimaryButton("Workouts", onClick = onWorkout)
    }
}
