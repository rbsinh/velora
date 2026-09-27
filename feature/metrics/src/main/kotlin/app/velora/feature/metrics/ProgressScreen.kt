package app.velora.feature.metrics

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import app.velora.core.designsystem.EmptyPane
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.VeloraCard
import app.velora.core.domain.CalorieTargetEngine
import app.velora.core.domain.GoalAdherence
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.StepAggregation
import app.velora.core.domain.StepRepository
import app.velora.core.domain.TargetEstimate
import app.velora.core.domain.WeightRepository
import app.velora.core.domain.WeightTrends
import app.velora.core.domain.bodyMassIndex
import app.velora.core.domain.toTargetInputs
import app.velora.core.model.BodyMeasurement
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.ProgressPhoto
import app.velora.core.domain.FoodLogRepository
import app.velora.core.model.summed
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MetricsViewModel @Inject constructor(
    private val weights: WeightRepository,
    private val profiles: ProfileRepository,
    private val logs: FoodLogRepository,
    private val steps: StepRepository,
) : ViewModel() {
    val weightPoints = weights.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val measurements = weights.observeMeasurements().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val photos = weights.observePhotos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val profile = profiles.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    var kilograms by mutableStateOf("")
    var waist by mutableStateOf("")
    var calorieOverride by mutableStateOf("")
    private val today = LocalDate.now().toString()
    val todayLog = logs.observeDay(today).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val stepDays = steps.observeRange(LocalDate.now().minusDays(6).toString(), today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveWeight() {
        val value = kilograms.toDoubleOrNull() ?: return
        viewModelScope.launch {
            weights.upsert(app.velora.core.model.WeightPoint(LocalDate.now().toString(), value), System.currentTimeMillis())
            kilograms = ""
        }
    }

    fun saveWaist() {
        val value = waist.toDoubleOrNull() ?: return
        viewModelScope.launch {
            weights.upsertMeasurement(BodyMeasurement(UUID.randomUUID().toString(), LocalDate.now().toString(), "Waist", value))
            waist = ""
        }
    }

    fun saveOverride() {
        val profile = profile.value ?: return
        viewModelScope.launch {
            profiles.save(profile.copy(calorieTargetOverride = calorieOverride.toIntOrNull()))
        }
    }

    fun storePhoto(bytes: ByteArray, directory: File) {
        viewModelScope.launch {
            val file = File(directory, "${UUID.randomUUID()}.jpg")
            file.writeBytes(bytes)
            weights.addPhoto(ProgressPhoto(UUID.randomUUID().toString(), LocalDate.now().toString(), file.name))
        }
    }
}

@Composable
fun ProgressScreen(viewModel: MetricsViewModel = hiltViewModel()) {
    val points by viewModel.weightPoints.collectAsStateWithLifecycle()
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()
    val photos by viewModel.photos.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val log by viewModel.todayLog.collectAsStateWithLifecycle()
    val steps by viewModel.stepDays.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return@rememberLauncherForActivityResult
        val dir = File(context.filesDir, "progress").apply { mkdirs() }
        viewModel.storePhoto(bytes, dir)
    }
    val trend = WeightTrends.trend(points, LocalDate.now())
    val max = points.maxOfOrNull { it.kilograms } ?: 1.0
    val plan = profile?.let { CalorieTargetEngine.estimate(it.toTargetInputs()) as? TargetEstimate.Ok }?.plan
    val eaten = log.map(FoodLogEntry::nutrients).summed().caloriesKcal
    val adherence = plan?.let { GoalAdherence.calories(profile!!.goal, eaten, it.appliedCalorieTarget) }
    val stepTotal = StepAggregation.range(steps, LocalDate.now().minusDays(6), LocalDate.now())
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Progress")
        Text("BMI is weight divided by height squared. It is not a diagnosis.")
        profile?.let { person ->
            val bmi = bodyMassIndex(person.weightKilograms, person.heightCentimeters)
            Text(bmi?.let { "BMI ${"%.1f".format(it)}" } ?: "BMI unavailable")
            Text("Goal weight ${person.targetWeightKilograms ?: "not set"} kg")
        }
        Text("Latest ${trend.latestKilograms ?: "—"} kg. Weekly change ${trend.weeklyDeltaKilograms ?: "not enough history"}. Direction ${trend.direction.name.lowercase()}.")
        if (points.isEmpty()) EmptyPane("No weights yet", "Add one below. One entry is not drawn as a trend.")
        points.forEach { point ->
            Column(Modifier.semantics { contentDescription = "${point.date} ${point.kilograms} kilograms" }) {
                Text("${point.date} · ${point.kilograms} kg")
                LinearProgressIndicator(progress = { (point.kilograms / max).toFloat() })
            }
        }
        OutlinedTextField(viewModel.kilograms, { viewModel.kilograms = it }, label = { Text("Weight kg") })
        PrimaryButton("Save weight", onClick = viewModel::saveWeight)
        OutlinedTextField(viewModel.waist, { viewModel.waist = it }, label = { Text("Waist cm") })
        PrimaryButton("Save waist", onClick = viewModel::saveWaist)
        measurements.forEach { Text("${it.localDate} ${it.name} ${it.centimeters} cm") }
        Text("Today's calorie adherence: ${adherence?.name?.lowercase() ?: "no plan"}")
        Text("Steps in the last 7 days, reported days only: ${stepTotal.totalSteps} across ${stepTotal.reportedDays} days")
        OutlinedTextField(viewModel.calorieOverride, { viewModel.calorieOverride = it }, label = { Text("Calorie override, blank to clear") })
        PrimaryButton("Save calorie override", onClick = viewModel::saveOverride)
        plan?.let { Text(it.assumptions.joinToString(" ")) }
        Text("Progress photos stay on this phone.")
        PrimaryButton("Choose a photo") {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        photos.forEach { Text("${it.localDate} photo saved locally") }
        VeloraCard { Text("Charts above are the same numbers as the text. Missing history is labeled, not filled with zeros.") }
    }
}
