package app.velora.core.domain

import app.velora.core.model.Adherence
import app.velora.core.model.FitnessGoal
import app.velora.core.model.PeriodTotal
import app.velora.core.model.ResolvedSteps
import app.velora.core.model.StepDay
import app.velora.core.model.StepRange
import app.velora.core.model.StepSource
import app.velora.core.model.TrendDirection
import app.velora.core.model.WeightPoint
import app.velora.core.model.WeightTrend
import java.time.LocalDate
import java.time.temporal.WeekFields

object StepAggregation {
    /**
     * Health Connect wins when it has a value, including zero, unless the user
     * explicitly chose the manual total. Adding the two would double-count.
     */
    fun resolve(day: StepDay): ResolvedSteps {
        val manualWins = day.manualOverridesHealthConnect && day.manualSteps != null
        val (steps, source) = when {
            manualWins -> day.manualSteps to StepSource.MANUAL
            day.healthConnectSteps != null -> day.healthConnectSteps to StepSource.HEALTH_CONNECT
            day.manualSteps != null -> day.manualSteps to StepSource.MANUAL
            else -> null to null
        }
        return ResolvedSteps(day.date, steps, source)
    }

    fun range(days: List<StepDay>, start: LocalDate, end: LocalDate): StepRange {
        require(!end.isBefore(start))
        val byDate = days.associateBy { it.date }
        val resolved = generateSequence(start) { previous ->
            val next = previous.plusDays(1)
            if (next.isAfter(end)) null else next
        }.map { date ->
            byDate[date.toString()]?.let(::resolve)
                ?: ResolvedSteps(date.toString(), null, null)
        }.toList()
        val known = resolved.mapNotNull { it.steps }
        return StepRange(
            days = resolved,
            totalSteps = known.sum(),
            reportedDays = known.size,
            weeks = bucket(resolved) { date ->
                val week = WeekFields.ISO.weekOfWeekBasedYear()
                val year = WeekFields.ISO.weekBasedYear()
                "${date.get(year)}-W${date.get(week).toString().padStart(2, '0')}"
            },
            months = bucket(resolved) { date -> "${date.year}-${date.monthValue.toString().padStart(2, '0')}" },
        )
    }

    private fun bucket(
        days: List<ResolvedSteps>,
        labelOf: (LocalDate) -> String,
    ): List<PeriodTotal> {
        val grouped = linkedMapOf<String, MutableList<Long>>()
        days.forEach { day ->
            val label = labelOf(LocalDate.parse(day.date))
            val bucket = grouped.getOrPut(label) { mutableListOf() }
            day.steps?.let(bucket::add)
        }
        return grouped.map { (label, steps) ->
            PeriodTotal(label, steps.sum(), steps.size)
        }
    }
}

object WeightTrends {
    private const val FLAT_KILOGRAMS = 0.1

    fun trend(points: List<WeightPoint>, today: LocalDate): WeightTrend {
        val ordered = points.sortedBy { it.date }
        val latest = ordered.lastOrNull()
            ?: return WeightTrend(null, null, null, TrendDirection.INSUFFICIENT)
        val weekly = deltaSince(ordered, today.minusDays(7), minimumGapDays = 5)
        val monthly = deltaSince(ordered, today.minusDays(28), minimumGapDays = 21)
        val direction = when {
            weekly == null -> TrendDirection.INSUFFICIENT
            weekly > FLAT_KILOGRAMS -> TrendDirection.UP
            weekly < -FLAT_KILOGRAMS -> TrendDirection.DOWN
            else -> TrendDirection.FLAT
        }
        return WeightTrend(latest.kilograms, weekly, monthly, direction)
    }

    private fun deltaSince(
        ordered: List<WeightPoint>,
        cutoff: LocalDate,
        minimumGapDays: Long,
    ): Double? {
        val latest = ordered.last()
        val latestDate = LocalDate.parse(latest.date)
        val earlier = ordered.lastOrNull { LocalDate.parse(it.date) <= cutoff } ?: return null
        val earlierDate = LocalDate.parse(earlier.date)
        if (latestDate.toEpochDay() - earlierDate.toEpochDay() < minimumGapDays) return null
        return latest.kilograms - earlier.kilograms
    }
}

object GoalAdherence {
    fun calories(goal: FitnessGoal, consumedKcal: Double, targetKcal: Int): Adherence {
        if (consumedKcal <= 0.0 || targetKcal <= 0) return Adherence.NOT_LOGGED
        val target = targetKcal.toDouble()
        val met = when (goal) {
            FitnessGoal.LOSE_WEIGHT -> consumedKcal <= target
            FitnessGoal.GAIN_WEIGHT -> consumedKcal in target * 0.9..(target * 1.25)
            FitnessGoal.MAINTAIN_WEIGHT,
            FitnessGoal.IMPROVE_FITNESS,
            FitnessGoal.INCREASE_ACTIVITY,
            -> consumedKcal in target * 0.9..(target * 1.1)
        }
        return if (met) Adherence.MET else Adherence.MISSED
    }

    fun steps(steps: Long?, goal: Int): Adherence = when {
        steps == null || goal <= 0 -> Adherence.NOT_LOGGED
        steps >= goal -> Adherence.MET
        else -> Adherence.MISSED
    }
}

/** BMI is mass / height^2. It is a ratio, not a diagnosis. */
fun bodyMassIndex(weightKilograms: Double, heightCentimeters: Double): Double? {
    if (weightKilograms <= 0.0 || heightCentimeters <= 0.0) return null
    val meters = heightCentimeters / 100.0
    return weightKilograms / (meters * meters)
}
