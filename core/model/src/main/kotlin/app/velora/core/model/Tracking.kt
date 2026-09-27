package app.velora.core.model

data class CaloriePlan(
    val bmrKcal: Int,
    val tdeeKcal: Int,
    val suggestedCalorieTarget: Int,
    val appliedCalorieTarget: Int,
    val calorieTargetIsUserOverride: Boolean,
    val minimumSafeCalorieTarget: Int,
    val clampedSuggestionToSafetyFloor: Boolean,
    val overrideBelowSafetyFloor: Boolean,
    val proteinGrams: Int,
    val carbohydrateGrams: Int,
    val fatGrams: Int,
    val formulaName: String,
    val assumptions: List<String>,
)

data class StepDay(
    val date: String,
    val healthConnectSteps: Long?,
    val manualSteps: Long?,
    val manualOverridesHealthConnect: Boolean,
)

data class ResolvedSteps(
    val date: String,
    val steps: Long?,
    val source: StepSource?,
)

data class PeriodTotal(
    val label: String,
    val steps: Long,
    val reportedDays: Int,
)

data class StepRange(
    val days: List<ResolvedSteps>,
    val totalSteps: Long,
    val reportedDays: Int,
    val weeks: List<PeriodTotal>,
    val months: List<PeriodTotal>,
)

data class WeightPoint(
    val date: String,
    val kilograms: Double,
)

enum class TrendDirection {
    INSUFFICIENT,
    UP,
    DOWN,
    FLAT,
}

data class WeightTrend(
    val latestKilograms: Double?,
    val weeklyDeltaKilograms: Double?,
    val monthlyDeltaKilograms: Double?,
    val direction: TrendDirection,
)

enum class SessionState {
    IN_PROGRESS,
    PAUSED,
    COMPLETED,
}

data class SessionTimeline(
    val state: SessionState,
    val startedAtMillis: Long,
    val pausedAtMillis: Long?,
    val accumulatedPauseMillis: Long,
    val completedAtMillis: Long?,
)

data class SetRecord(
    val exerciseId: String,
    val weightKilograms: Double?,
    val reps: Int?,
)

data class ActivityEntry(
    val id: String,
    val localDate: String,
    val kind: ActivityKind,
    val durationMinutes: Int,
    val note: String?,
    val sourceLabel: String,
)

enum class Adherence {
    NOT_LOGGED,
    MET,
    MISSED,
}
