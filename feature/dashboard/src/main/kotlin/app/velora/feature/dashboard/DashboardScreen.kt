package app.velora.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.velora.core.analytics.AnalyticsTracker
import app.velora.core.designsystem.CarbColor
import app.velora.core.designsystem.FatColor
import app.velora.core.designsystem.ProteinColor
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.SecondaryButton
import app.velora.core.designsystem.VeloraCard
import app.velora.core.domain.CalorieTargetEngine
import app.velora.core.domain.FoodLogRepository
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.StepAggregation
import app.velora.core.domain.StepRepository
import app.velora.core.domain.TargetEstimate
import app.velora.core.domain.WaterRepository
import app.velora.core.domain.WeightRepository
import app.velora.core.domain.WeightTrends
import app.velora.core.domain.WorkoutRepository
import app.velora.core.domain.toTargetInputs
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.StepDay
import app.velora.core.model.WeightPoint
import app.velora.core.model.WorkoutSessionRecord
import app.velora.core.model.summed
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUi(
    val date: String = "",
    val consumed: Double = 0.0,
    val target: Int? = null,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val proteinTarget: Int? = null,
    val carbTarget: Int? = null,
    val fatTarget: Int? = null,
    val steps: String = "No step data",
    val stepGoal: Int = 0,
    val weight: String = "No weight yet",
    val meals: Int = 0,
    val workout: String = "No workout logged today",
    val water: Int = 0,
    val disclaimer: String = "Targets are estimates, not medical advice.",
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    profiles: ProfileRepository,
    logs: FoodLogRepository,
    steps: StepRepository,
    weights: WeightRepository,
    workouts: WorkoutRepository,
    private val water: WaterRepository,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    private val today = LocalDate.now().toString()

    val state = combine(
        combine(profiles.observe(), logs.observeDay(today), steps.observeRange(today, today)) { profile, entries, stepDays ->
            Triple(profile, entries, stepDays)
        },
        combine(weights.observe(), workouts.observeSessions(), water.observe(today)) { weightPoints, sessions, waterDay ->
            Triple(weightPoints, sessions, waterDay)
        },
    ) { foodSide, bodySide ->
        val (profile, entries, stepDays) = foodSide
        val (weightPoints, sessions, waterDay) = bodySide
        val foods = entries.filterIsInstance<FoodLogEntry>()
        val totals = foods.map { it.nutrients }.summed()
        val plan = profile?.let { CalorieTargetEngine.estimate(it.toTargetInputs()) as? TargetEstimate.Ok }?.plan
        val resolved = stepDays.filterIsInstance<StepDay>().firstOrNull()?.let(StepAggregation::resolve)
        val points = weightPoints.filterIsInstance<WeightPoint>()
        val latestWeight = points.lastOrNull()
        val trend = WeightTrends.trend(points, LocalDate.now())
        val sessionToday = sessions.filterIsInstance<WorkoutSessionRecord>().any { it.localDate == today }
        DashboardUi(
            date = today,
            consumed = totals.caloriesKcal,
            target = plan?.appliedCalorieTarget,
            protein = totals.proteinGrams,
            carbs = totals.carbohydrateGrams,
            fat = totals.fatGrams,
            proteinTarget = plan?.proteinGrams,
            carbTarget = plan?.carbohydrateGrams,
            fatTarget = plan?.fatGrams,
            steps = resolved?.steps?.toString() ?: "No step data",
            stepGoal = profile?.dailyStepGoal ?: 0,
            weight = latestWeight?.let { "${it.kilograms} kg · ${trend.direction.name.lowercase()}" } ?: "No weight yet",
            meals = foods.size,
            workout = if (sessionToday) "A workout is on today's record" else "No workout logged today",
            water = waterDay.glasses,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUi(date = today))

    fun addWater() {
        viewModelScope.launch { water.setGlasses(today, state.value.water + 1) }
    }

    fun trackStepsViewed() = analytics.track("steps_viewed")
}

@Composable
fun DashboardScreen(
    onFood: () -> Unit,
    onMove: () -> Unit,
    onWorkout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("YOUR DAILY OVERVIEW", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Today", style = MaterialTheme.typography.displaySmall)
            Text(state.date, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        CalorieHero(state)

        Text("Nutrition", style = MaterialTheme.typography.titleLarge)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MacroTile("Protein", state.protein, state.proteinTarget, ProteinColor, Modifier.weight(1f))
            MacroTile("Carbs", state.carbs, state.carbTarget, CarbColor, Modifier.weight(1f))
            MacroTile("Fat", state.fat, state.fatTarget, FatColor, Modifier.weight(1f))
        }

        VeloraCard {
            Text("Today's rhythm", style = MaterialTheme.typography.titleLarge)
            OverviewRow("Steps", state.steps, state.stepGoal.takeIf { it > 0 }?.let { "Goal $it" } ?: "No goal")
            OverviewRow("Weight", state.weight, "Latest check-in")
            OverviewRow("Hydration", "${state.water} glasses", "Daily habit")
            Text(state.workout, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton("Activity", Modifier.weight(1f)) { viewModel.trackStepsViewed(); onMove() }
                SecondaryButton("Add water", Modifier.weight(1f), onClick = viewModel::addWater)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PrimaryButton("Log food · ${state.meals} today", onClick = onFood)
            SecondaryButton("Start a workout", onClick = onWorkout)
        }
        Text(
            state.disclaimer,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
    }
}

@Composable
private fun CalorieHero(state: DashboardUi) {
    val target = state.target
    val progress = target?.takeIf { it > 0 }?.let { (state.consumed / it).toFloat().coerceIn(0f, 1f) } ?: 0f
    val remaining = target?.let { (it - state.consumed).toInt() }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            Modifier.padding(horizontal = 22.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Box(Modifier.size(132.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                    strokeWidth = 11.dp,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${state.consumed.toInt()}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("kcal eaten", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Energy", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    when {
                        remaining == null -> "Build your daily plan"
                        remaining >= 0 -> "$remaining kcal left"
                        else -> "${-remaining} kcal over"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    target?.let { "$it kcal daily target" } ?: "Complete your profile to unlock a personalized target.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun MacroTile(label: String, value: Double, target: Int?, color: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(22.dp), color = color.copy(alpha = 0.11f)) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = color)
            Text("${value.toInt()}g", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(target?.let { "of ${it}g" } ?: "no target", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun OverviewRow(label: String, value: String, supporting: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
        Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun ratio(value: Double, target: Int?): Float? = target?.takeIf { it > 0 }?.let { (value / it).toFloat() }
