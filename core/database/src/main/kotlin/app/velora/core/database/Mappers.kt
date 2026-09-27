package app.velora.core.database

import app.velora.core.model.ActivityEntry
import app.velora.core.model.ActivityKind
import app.velora.core.model.ActivityLevel
import app.velora.core.model.BiologicalSex
import app.velora.core.model.BodyMeasurement
import app.velora.core.model.Exercise
import app.velora.core.model.FitnessGoal
import app.velora.core.model.Food
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.FoodServing
import app.velora.core.model.MealSlot
import app.velora.core.model.MealTemplate
import app.velora.core.model.MealTemplateItem
import app.velora.core.model.MeasurementSystem
import app.velora.core.model.NutrientProvenance
import app.velora.core.model.Nutrients
import app.velora.core.model.ProgressPhoto
import app.velora.core.model.Recipe
import app.velora.core.model.RecipeItem
import app.velora.core.model.SessionState
import app.velora.core.model.SessionTimeline
import app.velora.core.model.StepDay
import app.velora.core.model.UserProfile
import app.velora.core.model.WaterDay
import app.velora.core.model.WeightPoint
import app.velora.core.model.WorkoutSessionRecord
import app.velora.core.model.WorkoutSetEntry
import app.velora.core.model.WorkoutTemplate

internal fun UserProfileEntity.toModel() = UserProfile(
    ageYears, parseEnum(sex), heightCentimeters, weightKilograms, parseEnum(goal), targetWeightKilograms,
    parseEnum(activityLevel), workoutDaysPerWeek, dietaryNote, parseEnum(measurementSystem), dailyStepGoal,
    calorieTargetOverride, proteinTargetOverrideGrams, carbTargetOverrideGrams, fatTargetOverrideGrams,
    acceptedEstimateDisclaimer,
)

internal fun UserProfile.toEntity() = UserProfileEntity(
    ageYears = ageYears, sex = sex.name, heightCentimeters = heightCentimeters,
    weightKilograms = weightKilograms, goal = goal.name, targetWeightKilograms = targetWeightKilograms,
    activityLevel = activityLevel.name, workoutDaysPerWeek = workoutDaysPerWeek, dietaryNote = dietaryNote,
    measurementSystem = measurementSystem.name, dailyStepGoal = dailyStepGoal,
    calorieTargetOverride = calorieTargetOverride, proteinTargetOverrideGrams = proteinTargetOverrideGrams,
    carbTargetOverrideGrams = carbTargetOverrideGrams, fatTargetOverrideGrams = fatTargetOverrideGrams,
    acceptedEstimateDisclaimer = acceptedEstimateDisclaimer,
)

internal fun FoodServingEntity.toModel() = FoodServing(id, label, grams, nutrients())

internal fun FoodEntity.toModel(servings: List<FoodServing>) = Food(
    id, name, brand, barcode, servings, parseEnum(provenance), sourceName, sourceRecordId, userCreated,
)

internal fun FoodLogEntity.toModel() = FoodLogEntry(
    id, localDate, parseEnum(mealSlot), foodId, foodName, servingLabel, quantity, nutrients(),
    updatedAtEpochMillis, deleted,
)

internal fun FoodLogEntry.toEntity() = FoodLogEntity(
    id, localDate, mealSlot.name, foodId, foodName, servingLabel, quantity,
    nutrients.caloriesKcal, nutrients.proteinGrams, nutrients.carbohydrateGrams, nutrients.fatGrams,
    nutrients.fiberGrams, nutrients.sugarGrams, nutrients.sodiumMilligrams, nutrients.provenance.name,
    updatedAtEpochMillis, deleted,
)

private fun FoodServingEntity.nutrients() = Nutrients(
    caloriesKcal, proteinGrams, carbohydrateGrams, fatGrams, fiberGrams, sugarGrams, sodiumMilligrams, parseEnum(provenance),
)

private fun FoodLogEntity.nutrients() = Nutrients(
    caloriesKcal, proteinGrams, carbohydrateGrams, fatGrams, fiberGrams, sugarGrams, sodiumMilligrams, parseEnum(provenance),
)

private fun RecipeItemEntity.nutrients() = Nutrients(
    caloriesKcal, proteinGrams, carbohydrateGrams, fatGrams, fiberGrams, sugarGrams, sodiumMilligrams, parseEnum(provenance),
)

private fun MealTemplateItemEntity.nutrients() = Nutrients(
    caloriesKcal, proteinGrams, carbohydrateGrams, fatGrams, fiberGrams, sugarGrams, sodiumMilligrams, parseEnum(provenance),
)

internal fun RecipeEntity.toModel(items: List<RecipeItemEntity>) = Recipe(
    id, name, items.map { RecipeItem(it.id, it.foodName, it.servingLabel, it.quantity, it.nutrients()) },
)

internal fun MealTemplateEntity.toModel(items: List<MealTemplateItemEntity>) = MealTemplate(
    id,
    name,
    items.map {
        MealTemplateItem(it.id, it.foodName, it.servingLabel, it.quantity, it.nutrients(), parseEnum(it.mealSlot))
    },
)

internal fun WeightEntity.toModel() = WeightPoint(localDate, kilograms)
internal fun StepDayEntity.toModel() = StepDay(localDate, healthConnectSteps, manualSteps, manualOverridesHealthConnect)
internal fun ActivityEntity.toModel() = ActivityEntry(id, localDate, parseEnum(kind), durationMinutes, note, sourceLabel)
internal fun ExerciseEntity.toModel() = Exercise(id, name, cue, equipment, userCreated)
internal fun MeasurementEntity.toModel() = BodyMeasurement(id, localDate, name, centimeters)
internal fun PhotoEntity.toModel() = ProgressPhoto(id, localDate, relativePath)

internal fun WorkoutSessionEntity.toModel(sets: List<WorkoutSetEntity>) = WorkoutSessionRecord(
    id,
    name,
    localDate,
    SessionTimeline(parseEnum(state), startedAtMillis, pausedAtMillis, accumulatedPauseMillis, completedAtMillis),
    sets.map {
        WorkoutSetEntry(it.id, it.exerciseId, it.exerciseName, it.reps, it.weightKilograms, it.durationSeconds, it.completed)
    },
)

private inline fun <reified T : Enum<T>> parseEnum(value: String): T = enumValueOf(value)
