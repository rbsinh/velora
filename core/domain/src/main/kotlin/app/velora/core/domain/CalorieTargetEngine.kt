package app.velora.core.domain

import app.velora.core.model.ActivityLevel
import app.velora.core.model.BiologicalSex
import app.velora.core.model.CaloriePlan
import app.velora.core.model.FitnessGoal
import app.velora.core.model.TargetInputs
import kotlin.math.roundToInt

/**
 * Estimates BMR, TDEE, a calorie target, and a macro split.
 *
 * BMR uses Mifflin-St Jeor, 1990:
 *   female: 10*kg + 6.25*cm - 5*years - 161
 *   male:   10*kg + 6.25*cm - 5*years + 5
 *   unspecified: the sex constant is the average of +5 and -161, which is -78.
 *   That average is an engineering choice so the app does not force a binary
 *   selection. It is not a third published equation.
 *
 * Activity multipliers are the common sedentary-to-very-active set
 * (1.2, 1.375, 1.55, 1.725, 1.9), applied as exact fractions so binary
 * floating point does not change the result.
 *
 * Weight loss subtracts 500 kcal, about 0.45 kg per week if the estimate
 * were exact. Gain adds 400 kcal. Fitness and activity goals do not change
 * calories; they are not weight-change prescriptions.
 *
 * Suggested targets are clamped to 1,200 kcal (female) or 1,500 kcal
 * (male and unspecified). Those floors are common unsupervised-diet
 * guardrails, not a medical minimum for a specific person. A manual
 * override may go below the floor and is flagged rather than silently raised.
 *
 * Protein starts at 1.6 g/kg for loss, gain, and fitness, otherwise 1.2 g/kg,
 * then is capped at 40% of the applied calorie target. Fat starts at 30% of
 * calories and drops to 20% if needed so carbohydrate calories are not negative.
 * Carbohydrate is the remainder.
 *
 * Nothing here is medical advice. Outputs are estimates.
 */
object CalorieTargetEngine {
    const val FORMULA_NAME = "Mifflin-St Jeor (1990) with standard activity multipliers"

    fun estimate(input: TargetInputs): TargetEstimate {
        if (input.ageYears < 18) {
            return TargetEstimate.Invalid("Velora is for adults 18 and older.")
        }
        if (input.ageYears > 100) {
            return TargetEstimate.Invalid("Enter an age of 100 or below.")
        }
        if (input.heightCentimeters !in 120.0..230.0) {
            return TargetEstimate.Invalid("Enter a height between 120 and 230 cm.")
        }
        if (input.weightKilograms !in 30.0..300.0) {
            return TargetEstimate.Invalid("Enter a weight between 30 and 300 kg.")
        }
        input.calorieOverride?.let { override ->
            if (override !in 800..6000) {
                return TargetEstimate.Invalid("Calorie override must be between 800 and 6000.")
            }
        }

        val bmrExact = 10.0 * input.weightKilograms +
            6.25 * input.heightCentimeters -
            5.0 * input.ageYears +
            sexConstant(input.sex)
        val (factorNumerator, factorDenominator) = activityFactor(input.activityLevel)
        val tdeeExact = bmrExact * factorNumerator / factorDenominator
        val adjusted = tdeeExact + goalDelta(input.goal)
        val floor = safetyFloor(input.sex)
        val clamped = adjusted < floor
        val suggested = if (clamped) floor else adjusted.roundToInt()
        val applied = input.calorieOverride ?: suggested
        val macros = macros(input, applied)

        return TargetEstimate.Ok(
            CaloriePlan(
                bmrKcal = bmrExact.roundToInt(),
                tdeeKcal = tdeeExact.roundToInt(),
                suggestedCalorieTarget = suggested,
                appliedCalorieTarget = applied,
                calorieTargetIsUserOverride = input.calorieOverride != null,
                minimumSafeCalorieTarget = floor,
                clampedSuggestionToSafetyFloor = clamped,
                overrideBelowSafetyFloor = applied < floor,
                proteinGrams = input.proteinOverrideGrams ?: macros.protein,
                carbohydrateGrams = input.carbOverrideGrams ?: macros.carbs,
                fatGrams = input.fatOverrideGrams ?: macros.fat,
                formulaName = FORMULA_NAME,
                assumptions = listOf(
                    "BMR is Mifflin-St Jeor. Unspecified sex uses the average of the female and male constants.",
                    "Activity multipliers: sedentary 1.2, light 1.375, moderate 1.55, active 1.725, very active 1.9.",
                    "Loss subtracts 500 kcal. Gain adds 400 kcal. Other goals keep estimated expenditure.",
                    "Suggestions are not lowered below $floor kcal. An override can be, and is marked when it is.",
                    "These numbers are estimates, not a measured metabolism and not medical advice.",
                ),
            ),
        )
    }

    private fun sexConstant(sex: BiologicalSex): Double = when (sex) {
        BiologicalSex.FEMALE -> -161.0
        BiologicalSex.MALE -> 5.0
        BiologicalSex.UNSPECIFIED -> -78.0
    }

    private fun activityFactor(level: ActivityLevel): Pair<Int, Int> = when (level) {
        ActivityLevel.SEDENTARY -> 6 to 5
        ActivityLevel.LIGHT -> 11 to 8
        ActivityLevel.MODERATE -> 31 to 20
        ActivityLevel.ACTIVE -> 69 to 40
        ActivityLevel.VERY_ACTIVE -> 19 to 10
    }

    private fun goalDelta(goal: FitnessGoal): Double = when (goal) {
        FitnessGoal.LOSE_WEIGHT -> -500.0
        FitnessGoal.MAINTAIN_WEIGHT -> 0.0
        FitnessGoal.GAIN_WEIGHT -> 400.0
        FitnessGoal.IMPROVE_FITNESS -> 0.0
        FitnessGoal.INCREASE_ACTIVITY -> 0.0
    }

    private fun safetyFloor(sex: BiologicalSex): Int = when (sex) {
        BiologicalSex.FEMALE -> 1200
        BiologicalSex.MALE -> 1500
        BiologicalSex.UNSPECIFIED -> 1500
    }

    private data class MacroSplit(val protein: Int, val carbs: Int, val fat: Int)

    private fun macros(input: TargetInputs, appliedCalories: Int): MacroSplit {
        val perKg = when (input.goal) {
            FitnessGoal.LOSE_WEIGHT,
            FitnessGoal.GAIN_WEIGHT,
            FitnessGoal.IMPROVE_FITNESS,
            -> 1.6
            FitnessGoal.MAINTAIN_WEIGHT,
            FitnessGoal.INCREASE_ACTIVITY,
            -> 1.2
        }
        var protein = (perKg * input.weightKilograms).roundToInt()
        val proteinCalorieCap = (appliedCalories * 0.40) / 4.0
        if (protein * 4 > appliedCalories * 0.40) {
            protein = proteinCalorieCap.toInt()
        }
        var fat = ((0.30 * appliedCalories) / 9.0).roundToInt()
        var remaining = appliedCalories - protein * 4 - fat * 9
        if (remaining < 0) {
            fat = ((0.20 * appliedCalories) / 9.0).roundToInt()
            remaining = appliedCalories - protein * 4 - fat * 9
        }
        val carbs = if (remaining <= 0) 0 else (remaining / 4.0).roundToInt()
        return MacroSplit(protein, carbs, fat)
    }
}

sealed class TargetEstimate {
    data class Ok(val plan: CaloriePlan) : TargetEstimate()
    data class Invalid(val reason: String) : TargetEstimate()
}
