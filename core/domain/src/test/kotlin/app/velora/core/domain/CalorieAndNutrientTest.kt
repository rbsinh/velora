package app.velora.core.domain

import app.velora.core.model.ActivityLevel
import app.velora.core.model.BiologicalSex
import app.velora.core.model.FitnessGoal
import app.velora.core.model.NutrientProvenance
import app.velora.core.model.Nutrients
import app.velora.core.model.TargetInputs
import app.velora.core.model.plus
import app.velora.core.model.scaled
import app.velora.core.model.summed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalorieTargetEngineTest {
    @Test
    fun maleMaintainModerateMatchesMifflinStJeor() {
        val plan = ok(
            sample(
                sex = BiologicalSex.MALE,
                age = 30,
                height = 180.0,
                weight = 80.0,
                activity = ActivityLevel.MODERATE,
                goal = FitnessGoal.MAINTAIN_WEIGHT,
            ),
        )
        // 10*80 + 6.25*180 - 5*30 + 5 = 1780
        // 1780 * 31/20 = 2759
        assertEquals(1780, plan.bmrKcal)
        assertEquals(2759, plan.tdeeKcal)
        assertEquals(2759, plan.suggestedCalorieTarget)
        assertEquals(2759, plan.appliedCalorieTarget)
        assertFalse(plan.clampedSuggestionToSafetyFloor)
        assertEquals(96, plan.proteinGrams)
        assertEquals(92, plan.fatGrams)
        assertEquals(387, plan.carbohydrateGrams)
    }

    @Test
    fun femaleLossIsClampedToSafetyFloor() {
        val plan = ok(
            sample(
                sex = BiologicalSex.FEMALE,
                age = 45,
                height = 150.0,
                weight = 50.0,
                activity = ActivityLevel.SEDENTARY,
                goal = FitnessGoal.LOSE_WEIGHT,
            ),
        )
        // BMR 1051.5 -> 1052. TDEE 1261.8 -> 1262. Adjusted 761.8 clamps to 1200.
        assertEquals(1052, plan.bmrKcal)
        assertEquals(1262, plan.tdeeKcal)
        assertEquals(1200, plan.suggestedCalorieTarget)
        assertTrue(plan.clampedSuggestionToSafetyFloor)
        assertEquals(1200, plan.minimumSafeCalorieTarget)
    }

    @Test
    fun unspecifiedSexUsesAverageConstant() {
        val plan = ok(
            sample(
                sex = BiologicalSex.UNSPECIFIED,
                age = 30,
                height = 170.0,
                weight = 70.0,
                activity = ActivityLevel.SEDENTARY,
                goal = FitnessGoal.MAINTAIN_WEIGHT,
            ),
        )
        // 10*70 + 6.25*170 - 5*30 - 78 = 1534.5 -> 1535
        // 1534.5 * 6/5 = 1841.4 -> 1841
        assertEquals(1535, plan.bmrKcal)
        assertEquals(1841, plan.tdeeKcal)
        assertEquals(1500, plan.minimumSafeCalorieTarget)
    }

    @Test
    fun overrideBelowFloorIsKeptAndFlagged() {
        val plan = ok(sample(sex = BiologicalSex.MALE).copy(calorieOverride = 1400))
        assertEquals(1400, plan.appliedCalorieTarget)
        assertTrue(plan.calorieTargetIsUserOverride)
        assertTrue(plan.overrideBelowSafetyFloor)
        assertTrue(plan.suggestedCalorieTarget >= 1500)
    }

    @Test
    fun underEighteenIsRejected() {
        val result = CalorieTargetEngine.estimate(sample(age = 17))
        assertTrue(result is TargetEstimate.Invalid)
    }

    @Test
    fun gainAddsFourHundred() {
        val maintain = ok(sample(goal = FitnessGoal.MAINTAIN_WEIGHT))
        val gain = ok(sample(goal = FitnessGoal.GAIN_WEIGHT))
        assertEquals(maintain.tdeeKcal + 400, gain.suggestedCalorieTarget)
    }

    private fun sample(
        sex: BiologicalSex = BiologicalSex.MALE,
        age: Int = 30,
        height: Double = 180.0,
        weight: Double = 80.0,
        activity: ActivityLevel = ActivityLevel.MODERATE,
        goal: FitnessGoal = FitnessGoal.MAINTAIN_WEIGHT,
    ) = TargetInputs(
        ageYears = age,
        sex = sex,
        heightCentimeters = height,
        weightKilograms = weight,
        activityLevel = activity,
        goal = goal,
        calorieOverride = null,
        proteinOverrideGrams = null,
        carbOverrideGrams = null,
        fatOverrideGrams = null,
    )

    private fun ok(input: TargetInputs) = (CalorieTargetEngine.estimate(input) as TargetEstimate.Ok).plan
}

class NutrientMathTest {
    @Test
    fun scalesAndSumsWithoutInventingMissingFiber() {
        val verified = nutrient(provenance = NutrientProvenance.VERIFIED_REFERENCE, fiber = 2.0)
        val user = nutrient(provenance = NutrientProvenance.USER_ENTERED, fiber = null)
        val total = listOf(verified.scaled(2.0), user).summed()
        assertEquals(300.0, total.caloriesKcal, 0.001)
        assertNull(total.fiberGrams)
        assertEquals(NutrientProvenance.USER_ENTERED, total.provenance)
    }

    @Test
    fun estimatedOutranksVerified() {
        val total = nutrient(NutrientProvenance.VERIFIED_REFERENCE, fiber = 1.0) +
            nutrient(NutrientProvenance.ESTIMATED, fiber = 1.0)
        assertEquals(NutrientProvenance.ESTIMATED, total.provenance)
        assertEquals(2.0, total.fiberGrams!!, 0.001)
    }

    private fun nutrient(provenance: NutrientProvenance, fiber: Double?) = Nutrients(
        caloriesKcal = 100.0,
        proteinGrams = 10.0,
        carbohydrateGrams = 10.0,
        fatGrams = 2.0,
        fiberGrams = fiber,
        sugarGrams = null,
        sodiumMilligrams = 5.0,
        provenance = provenance,
    )
}
