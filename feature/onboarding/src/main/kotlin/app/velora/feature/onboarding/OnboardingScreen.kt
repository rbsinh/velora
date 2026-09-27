package app.velora.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewModelScope
import app.velora.core.analytics.AnalyticsTracker
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.SecondaryButton
import app.velora.core.domain.CalorieTargetEngine
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.SettingsRepository
import app.velora.core.domain.TargetEstimate
import app.velora.core.domain.defaultProfile
import app.velora.core.domain.toTargetInputs
import app.velora.core.model.ActivityLevel
import app.velora.core.model.BiologicalSex
import app.velora.core.model.FitnessGoal
import app.velora.core.model.MeasurementSystem
import app.velora.core.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val profiles: ProfileRepository,
    private val settings: SettingsRepository,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    var draft by mutableStateOf(defaultProfile())
        private set
    var step by mutableStateOf(0)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var finished by mutableStateOf(false)
        private set

    fun edit(block: (UserProfile) -> UserProfile) {
        draft = block(draft)
        error = null
    }

    fun back() {
        if (step > 0) step -= 1
    }

    fun next() {
        if (step < LAST) {
            step += 1
            return
        }
        when (val estimate = CalorieTargetEngine.estimate(draft.toTargetInputs())) {
            is TargetEstimate.Invalid -> error = estimate.reason
            is TargetEstimate.Ok -> {
                if (!draft.acceptedEstimateDisclaimer) {
                    error = "Confirm that you understand these targets are estimates."
                    return
                }
                viewModelScope.launch {
                    profiles.save(draft)
                    settings.setOnboardingComplete(true)
                    analytics.track("onboarding_completed")
                    finished = true
                }
            }
        }
    }

    private companion object {
        const val LAST = 4
    }
}

@Composable
fun OnboardingScreen(onFinished: () -> Unit, viewModel: OnboardingViewModel = hiltViewModel()) {
    if (viewModel.finished) onFinished()
    val draft = viewModel.draft
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Set up Velora", style = MaterialTheme.typography.headlineMedium)
        Text("Only what the estimates need. You can change this later.", style = MaterialTheme.typography.bodyMedium)
        when (viewModel.step) {
            0 -> BodyStep(draft, viewModel::edit)
            1 -> GoalStep(draft, viewModel::edit)
            2 -> ActivityStep(draft, viewModel::edit)
            3 -> PreferenceStep(draft, viewModel::edit)
            else -> DisclaimerStep(draft, viewModel::edit)
        }
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (viewModel.step > 0) SecondaryButton("Back", onClick = viewModel::back)
        PrimaryButton(if (viewModel.step == 4) "Start" else "Continue", onClick = viewModel::next)
    }
}

@Composable
private fun BodyStep(draft: UserProfile, edit: ((UserProfile) -> UserProfile) -> Unit) {
    NumberField("Age", draft.ageYears.toString()) { value ->
        value.toIntOrNull()?.let { edit { profile -> profile.copy(ageYears = it) } }
    }
    Text("Sex used only for the published equation")
    RowChips(BiologicalSex.entries, draft.sex) { edit { profile -> profile.copy(sex = it) } }
    NumberField("Height (cm)", draft.heightCentimeters.toString()) { value ->
        value.toDoubleOrNull()?.let { edit { profile -> profile.copy(heightCentimeters = it) } }
    }
    NumberField("Weight (kg)", draft.weightKilograms.toString()) { value ->
        value.toDoubleOrNull()?.let { edit { profile -> profile.copy(weightKilograms = it) } }
    }
}

@Composable
private fun GoalStep(draft: UserProfile, edit: ((UserProfile) -> UserProfile) -> Unit) {
    RowChips(FitnessGoal.entries, draft.goal) { edit { profile -> profile.copy(goal = it) } }
    NumberField("Target weight kg, optional", draft.targetWeightKilograms?.toString().orEmpty()) { value ->
        edit { profile -> profile.copy(targetWeightKilograms = value.toDoubleOrNull()) }
    }
}

@Composable
private fun ActivityStep(draft: UserProfile, edit: ((UserProfile) -> UserProfile) -> Unit) {
    RowChips(ActivityLevel.entries, draft.activityLevel) { edit { profile -> profile.copy(activityLevel = it) } }
    NumberField("Workout days per week", draft.workoutDaysPerWeek.toString()) { value ->
        value.toIntOrNull()?.let { edit { profile -> profile.copy(workoutDaysPerWeek = it.coerceIn(0, 7)) } }
    }
    NumberField("Daily step goal", draft.dailyStepGoal.toString()) { value ->
        value.toIntOrNull()?.let { edit { profile -> profile.copy(dailyStepGoal = it.coerceIn(0, 100_000)) } }
    }
}

@Composable
private fun PreferenceStep(draft: UserProfile, edit: ((UserProfile) -> UserProfile) -> Unit) {
    RowChips(MeasurementSystem.entries, draft.measurementSystem) { edit { profile -> profile.copy(measurementSystem = it) } }
    OutlinedTextField(
        value = draft.dietaryNote.orEmpty(),
        onValueChange = { edit { profile -> profile.copy(dietaryNote = it.take(120).ifBlank { null }) } },
        label = { Text("Dietary note, optional") },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DisclaimerStep(draft: UserProfile, edit: ((UserProfile) -> UserProfile) -> Unit) {
    Text(
        "Calorie and macro targets are estimates from the Mifflin-St Jeor equation and standard activity multipliers. They are not a measurement of your metabolism and they are not medical advice.",
    )
    androidx.compose.foundation.layout.Row {
        Checkbox(
            checked = draft.acceptedEstimateDisclaimer,
            onCheckedChange = { edit { profile -> profile.copy(acceptedEstimateDisclaimer = it) } },
        )
        Text("I understand these targets are estimates.")
    }
}

@Composable
private fun NumberField(label: String, value: String, onValue: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValue,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun <T : Enum<T>> RowChips(values: List<T>, selected: T, onSelect: (T) -> Unit) {
    androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        values.forEach { value ->
            FilterChip(
                selected = value == selected,
                onClick = { onSelect(value) },
                label = { Text(value.name.lowercase().replace('_', ' ')) },
            )
        }
    }
}
