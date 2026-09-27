package app.velora.core.database

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "local",
    val ageYears: Int,
    val sex: String,
    val heightCentimeters: Double,
    val weightKilograms: Double,
    val goal: String,
    val targetWeightKilograms: Double?,
    val activityLevel: String,
    val workoutDaysPerWeek: Int,
    val dietaryNote: String?,
    val measurementSystem: String,
    val dailyStepGoal: Int,
    val calorieTargetOverride: Int?,
    val proteinTargetOverrideGrams: Int?,
    val carbTargetOverrideGrams: Int?,
    val fatTargetOverrideGrams: Int?,
    val acceptedEstimateDisclaimer: Boolean,
)

@Entity(tableName = "food", indices = [Index("name"), Index("barcode")])
data class FoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String?,
    val barcode: String?,
    val provenance: String,
    val sourceName: String?,
    val sourceRecordId: String?,
    val userCreated: Boolean,
    val deleted: Boolean,
    val updatedAtEpochMillis: Long,
)

@Entity(tableName = "food_serving", indices = [Index("foodId")])
data class FoodServingEntity(
    @PrimaryKey val id: String,
    val foodId: String,
    val label: String,
    val grams: Double?,
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double?,
    val sugarGrams: Double?,
    val sodiumMilligrams: Double?,
    val provenance: String,
)

@Entity(tableName = "food_log_entry", indices = [Index("localDate")])
data class FoodLogEntity(
    @PrimaryKey val id: String,
    val localDate: String,
    val mealSlot: String,
    val foodId: String?,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double?,
    val sugarGrams: Double?,
    val sodiumMilligrams: Double?,
    val provenance: String,
    val updatedAtEpochMillis: Long,
    val deleted: Boolean,
)

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val deleted: Boolean,
    val updatedAtEpochMillis: Long,
)

@Entity(tableName = "recipe_item", indices = [Index("recipeId")])
data class RecipeItemEntity(
    @PrimaryKey val id: String,
    val recipeId: String,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double?,
    val sugarGrams: Double?,
    val sodiumMilligrams: Double?,
    val provenance: String,
)

@Entity(tableName = "meal_template")
data class MealTemplateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val updatedAtEpochMillis: Long,
    val deleted: Boolean,
)

@Entity(tableName = "meal_template_item", indices = [Index("templateId")])
data class MealTemplateItemEntity(
    @PrimaryKey val id: String,
    val templateId: String,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val mealSlot: String,
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double?,
    val sugarGrams: Double?,
    val sodiumMilligrams: Double?,
    val provenance: String,
)

@Entity(tableName = "weight_entry", indices = [Index("localDate")])
data class WeightEntity(
    @PrimaryKey val id: String,
    val localDate: String,
    val kilograms: Double,
    val updatedAtEpochMillis: Long,
    val deleted: Boolean,
)

@Entity(tableName = "body_measurement")
data class MeasurementEntity(
    @PrimaryKey val id: String,
    val localDate: String,
    val name: String,
    val centimeters: Double,
    val deleted: Boolean,
)

@Entity(tableName = "progress_photo")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val localDate: String,
    val relativePath: String,
    val deleted: Boolean,
)

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cue: String,
    val equipment: String,
    val userCreated: Boolean,
)

@Entity(tableName = "workout_template")
data class WorkoutTemplateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val deleted: Boolean,
)

@Entity(tableName = "workout_template_exercise", primaryKeys = ["templateId", "position"])
data class WorkoutTemplateExerciseEntity(
    val templateId: String,
    val position: Int,
    val exerciseId: String,
)

@Entity(tableName = "workout_session")
data class WorkoutSessionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val localDate: String,
    val state: String,
    val startedAtMillis: Long,
    val pausedAtMillis: Long?,
    val accumulatedPauseMillis: Long,
    val completedAtMillis: Long?,
)

@Entity(tableName = "workout_set", indices = [Index("sessionId")])
data class WorkoutSetEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val exerciseName: String,
    val reps: Int?,
    val weightKilograms: Double?,
    val durationSeconds: Int?,
    val completed: Boolean,
)

@Entity(tableName = "activity_entry", indices = [Index("localDate")])
data class ActivityEntity(
    @PrimaryKey val id: String,
    val localDate: String,
    val kind: String,
    val durationMinutes: Int,
    val note: String?,
    val sourceLabel: String,
    val deleted: Boolean,
)

@Entity(tableName = "step_day")
data class StepDayEntity(
    @PrimaryKey val localDate: String,
    val healthConnectSteps: Long?,
    val manualSteps: Long?,
    val manualOverridesHealthConnect: Boolean,
)

@Entity(tableName = "water_day")
data class WaterEntity(
    @PrimaryKey val localDate: String,
    val glasses: Int,
)

@Entity(tableName = "sync_outbox")
data class OutboxEntity(
    @PrimaryKey val clientId: String,
    val entityName: String,
    val operation: String,
    val clientUpdatedAtEpochMillis: Long,
)
