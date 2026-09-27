package app.velora.core.domain

import app.velora.core.model.ActivityEntry
import app.velora.core.model.ActivityLevel
import app.velora.core.model.BiologicalSex
import app.velora.core.model.BodyMeasurement
import app.velora.core.model.Exercise
import app.velora.core.model.FitnessGoal
import app.velora.core.model.Food
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.MealSlot
import app.velora.core.model.MealTemplate
import app.velora.core.model.MeasurementSystem
import app.velora.core.model.ProgressPhoto
import app.velora.core.model.Recipe
import app.velora.core.model.StepDay
import app.velora.core.model.TargetInputs
import app.velora.core.model.UserProfile
import app.velora.core.model.WaterDay
import app.velora.core.model.WeightPoint
import app.velora.core.model.WorkoutSessionRecord
import app.velora.core.model.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observe(): Flow<UserProfile?>
    suspend fun save(profile: UserProfile)
}

interface FoodRepository {
    fun observeSearch(query: String): Flow<List<Food>>
    suspend fun findByBarcode(code: String): List<Food>
    suspend fun upsert(food: Food)
    suspend fun get(id: String): Food?
}

interface FoodLogRepository {
    fun observeDay(localDate: String): Flow<List<FoodLogEntry>>
    suspend fun entries(localDate: String, slot: MealSlot): List<FoodLogEntry>
    suspend fun upsert(entry: FoodLogEntry)
    suspend fun softDelete(id: String, updatedAtEpochMillis: Long)
}

interface RecipeRepository {
    fun observe(): Flow<List<Recipe>>
    suspend fun upsert(recipe: Recipe)
    suspend fun softDelete(id: String)
}

interface MealTemplateRepository {
    fun observe(): Flow<List<MealTemplate>>
    suspend fun upsert(template: MealTemplate)
}

interface StepRepository {
    fun observeRange(start: String, end: String): Flow<List<StepDay>>
    suspend fun saveManual(day: StepDay)
}

interface ActivityRepository {
    fun observeDay(localDate: String): Flow<List<ActivityEntry>>
    suspend fun upsert(entry: ActivityEntry)
}

interface WeightRepository {
    fun observe(): Flow<List<WeightPoint>>
    suspend fun upsert(point: WeightPoint, updatedAtEpochMillis: Long)
    fun observeMeasurements(): Flow<List<BodyMeasurement>>
    suspend fun upsertMeasurement(measurement: BodyMeasurement)
    fun observePhotos(): Flow<List<ProgressPhoto>>
    suspend fun addPhoto(photo: ProgressPhoto)
    suspend fun deletePhoto(id: String)
}

interface WorkoutRepository {
    fun observeExercises(): Flow<List<Exercise>>
    suspend fun upsertExercise(exercise: Exercise)
    fun observeTemplates(): Flow<List<WorkoutTemplate>>
    suspend fun upsertTemplate(template: WorkoutTemplate)
    fun observeSessions(): Flow<List<WorkoutSessionRecord>>
    suspend fun saveSession(session: WorkoutSessionRecord)
}

interface WaterRepository {
    fun observe(localDate: String): Flow<WaterDay>
    suspend fun setGlasses(localDate: String, glasses: Int)
}

interface SettingsRepository {
    val onboardingComplete: Flow<Boolean>
    suspend fun setOnboardingComplete(complete: Boolean)
    val theme: Flow<String>
    suspend fun setTheme(theme: String)
    val remindersEnabled: Flow<Set<String>>
    suspend fun setReminder(key: String, enabled: Boolean, hour: Int)
    val healthExportEnabled: Flow<Boolean>
    suspend fun setHealthExportEnabled(enabled: Boolean)
    fun reminderHour(key: String): Flow<Int>
}

interface PersonalDataRepository {
    suspend fun exportJson(): String
    suspend fun wipe()
}

fun UserProfile.toTargetInputs(): TargetInputs = TargetInputs(
    ageYears = ageYears,
    sex = sex,
    heightCentimeters = heightCentimeters,
    weightKilograms = weightKilograms,
    activityLevel = activityLevel,
    goal = goal,
    calorieOverride = calorieTargetOverride,
    proteinOverrideGrams = proteinTargetOverrideGrams,
    carbOverrideGrams = carbTargetOverrideGrams,
    fatOverrideGrams = fatTargetOverrideGrams,
)

fun defaultProfile(): UserProfile = UserProfile(
    ageYears = 30,
    sex = BiologicalSex.UNSPECIFIED,
    heightCentimeters = 170.0,
    weightKilograms = 70.0,
    goal = FitnessGoal.MAINTAIN_WEIGHT,
    targetWeightKilograms = null,
    activityLevel = ActivityLevel.MODERATE,
    workoutDaysPerWeek = 3,
    dietaryNote = null,
    measurementSystem = MeasurementSystem.METRIC,
    dailyStepGoal = 8000,
    calorieTargetOverride = null,
    proteinTargetOverrideGrams = null,
    carbTargetOverrideGrams = null,
    fatTargetOverrideGrams = null,
    acceptedEstimateDisclaimer = false,
)

fun emptyMealSlots(): List<MealSlot> = MealSlot.entries
