package app.velora.core.database

import app.velora.core.domain.ActivityRepository
import app.velora.core.domain.FoodLogRepository
import app.velora.core.domain.FoodRepository
import app.velora.core.domain.MealTemplateRepository
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.RecipeRepository
import app.velora.core.domain.StepRepository
import app.velora.core.domain.WaterRepository
import app.velora.core.domain.WeightRepository
import app.velora.core.domain.WorkoutRepository
import app.velora.core.model.ActivityEntry
import app.velora.core.model.BodyMeasurement
import app.velora.core.model.Exercise
import app.velora.core.model.Food
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.FoodServing
import app.velora.core.model.MealSlot
import app.velora.core.model.MealTemplate
import app.velora.core.model.ProgressPhoto
import app.velora.core.model.Recipe
import app.velora.core.model.StepDay
import app.velora.core.model.UserProfile
import app.velora.core.model.WaterDay
import app.velora.core.model.WeightPoint
import app.velora.core.model.WorkoutSessionRecord
import app.velora.core.model.WorkoutTemplate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class RoomProfileRepository @Inject constructor(private val dao: VeloraDao) : ProfileRepository {
    override fun observe(): Flow<UserProfile?> = dao.observeProfile().map { it?.toModel() }
    override suspend fun save(profile: UserProfile) {
        dao.upsertProfile(profile.toEntity())
        dao.upsertOutbox(OutboxEntity("profile", "user_profile", "UPSERT", System.currentTimeMillis()))
    }
}

class RoomFoodRepository @Inject constructor(private val dao: VeloraDao) : FoodRepository {
    override fun observeSearch(query: String): Flow<List<Food>> =
        dao.observeFoodSearch(query).transform { foods ->
            emit(foods.map { food -> food.toModel(dao.servings(food.id).map { it.toModel() }) })
        }

    override suspend fun findByBarcode(code: String): List<Food> =
        dao.foodsByBarcode(code).map { it.toModel(dao.servings(it.id).map { serving -> serving.toModel() }) }

    override suspend fun upsert(food: Food) {
        val now = System.currentTimeMillis()
        dao.upsertFood(
            FoodEntity(
                food.id, food.name, food.brand, food.barcode, food.provenance.name, food.sourceName,
                food.sourceRecordId, food.userCreated, deleted = false, updatedAtEpochMillis = now,
            ),
        )
        dao.deleteServings(food.id)
        food.servings.forEach { serving -> dao.upsertServing(serving.toEntity(food.id)) }
        dao.upsertOutbox(OutboxEntity(food.id, "food", "UPSERT", now))
    }

    override suspend fun get(id: String): Food? {
        val food = dao.food(id) ?: return null
        return food.toModel(dao.servings(id).map { it.toModel() })
    }
}

private fun FoodServing.toEntity(foodId: String) = FoodServingEntity(
    id, foodId, label, grams, nutrients.caloriesKcal, nutrients.proteinGrams, nutrients.carbohydrateGrams,
    nutrients.fatGrams, nutrients.fiberGrams, nutrients.sugarGrams, nutrients.sodiumMilligrams, nutrients.provenance.name,
)

class RoomFoodLogRepository @Inject constructor(private val dao: VeloraDao) : FoodLogRepository {
    override fun observeDay(localDate: String): Flow<List<FoodLogEntry>> =
        dao.observeLog(localDate).map { rows -> rows.map { it.toModel() } }

    override suspend fun entries(localDate: String, slot: MealSlot): List<FoodLogEntry> =
        dao.logSlot(localDate, slot.name).map { it.toModel() }

    override suspend fun upsert(entry: FoodLogEntry) {
        dao.upsertLog(entry.toEntity())
        dao.upsertOutbox(OutboxEntity(entry.id, "food_log_entry", "UPSERT", entry.updatedAtEpochMillis))
    }

    override suspend fun softDelete(id: String, updatedAtEpochMillis: Long) {
        dao.softDeleteLog(id, updatedAtEpochMillis)
        dao.upsertOutbox(OutboxEntity(id, "food_log_entry", "DELETE", updatedAtEpochMillis))
    }
}

class RoomRecipeRepository @Inject constructor(private val dao: VeloraDao) : RecipeRepository {
    override fun observe(): Flow<List<Recipe>> = dao.observeRecipes().transform { recipes ->
        emit(recipes.map { it.toModel(dao.recipeItems(it.id)) })
    }

    override suspend fun upsert(recipe: Recipe) {
        val now = System.currentTimeMillis()
        dao.upsertRecipe(RecipeEntity(recipe.id, recipe.name, deleted = false, updatedAtEpochMillis = now))
        dao.deleteRecipeItems(recipe.id)
        recipe.items.forEach { item ->
            dao.upsertRecipeItem(
                RecipeItemEntity(
                    item.id, recipe.id, item.foodName, item.servingLabel, item.quantity,
                    item.nutrients.caloriesKcal, item.nutrients.proteinGrams, item.nutrients.carbohydrateGrams,
                    item.nutrients.fatGrams, item.nutrients.fiberGrams, item.nutrients.sugarGrams,
                    item.nutrients.sodiumMilligrams, item.nutrients.provenance.name,
                ),
            )
        }
    }

    override suspend fun softDelete(id: String) {
        dao.softDeleteRecipe(id, System.currentTimeMillis())
    }
}

class RoomMealRepository @Inject constructor(private val dao: VeloraDao) : MealTemplateRepository {
    override fun observe(): Flow<List<MealTemplate>> = dao.observeMeals().transform { meals ->
        emit(meals.map { it.toModel(dao.mealItems(it.id)) })
    }

    override suspend fun upsert(template: MealTemplate) {
        dao.upsertMeal(MealTemplateEntity(template.id, template.name, System.currentTimeMillis(), deleted = false))
        dao.deleteMealItems(template.id)
        template.items.forEach { item ->
            dao.upsertMealItem(
                MealTemplateItemEntity(
                    item.id, template.id, item.foodName, item.servingLabel, item.quantity, item.mealSlot.name,
                    item.nutrients.caloriesKcal, item.nutrients.proteinGrams, item.nutrients.carbohydrateGrams,
                    item.nutrients.fatGrams, item.nutrients.fiberGrams, item.nutrients.sugarGrams,
                    item.nutrients.sodiumMilligrams, item.nutrients.provenance.name,
                ),
            )
        }
    }
}

class RoomStepRepository @Inject constructor(private val dao: VeloraDao) : StepRepository {
    override fun observeRange(start: String, end: String): Flow<List<StepDay>> =
        dao.observeSteps(start, end).map { rows -> rows.map { it.toModel() } }

    override suspend fun saveManual(day: StepDay) {
        val existing = dao.stepDay(day.date)
        dao.upsertStep(
            StepDayEntity(
                localDate = day.date,
                healthConnectSteps = day.healthConnectSteps ?: existing?.healthConnectSteps,
                manualSteps = day.manualSteps ?: existing?.manualSteps,
                manualOverridesHealthConnect = if (day.manualSteps != null) {
                    day.manualOverridesHealthConnect
                } else {
                    existing?.manualOverridesHealthConnect ?: false
                },
            ),
        )
    }

    suspend fun cacheHealth(date: String, steps: Long) {
        val existing = dao.stepDay(date)
        dao.upsertStep(
            StepDayEntity(
                date,
                steps,
                existing?.manualSteps,
                existing?.manualOverridesHealthConnect ?: false,
            ),
        )
    }
}

class RoomActivityRepository @Inject constructor(private val dao: VeloraDao) : ActivityRepository {
    override fun observeDay(localDate: String): Flow<List<ActivityEntry>> =
        dao.observeActivity(localDate).map { rows -> rows.map { it.toModel() } }

    override suspend fun upsert(entry: ActivityEntry) {
        dao.upsertActivity(
            ActivityEntity(entry.id, entry.localDate, entry.kind.name, entry.durationMinutes, entry.note, entry.sourceLabel, false),
        )
    }
}

class RoomWeightRepository @Inject constructor(private val dao: VeloraDao) : WeightRepository {
    override fun observe(): Flow<List<WeightPoint>> = dao.observeWeights().map { rows -> rows.map { it.toModel() } }

    override suspend fun upsert(point: WeightPoint, updatedAtEpochMillis: Long) {
        dao.upsertWeight(WeightEntity(point.date, point.date, point.kilograms, updatedAtEpochMillis, false))
    }

    override fun observeMeasurements(): Flow<List<BodyMeasurement>> =
        dao.observeMeasurements().map { rows -> rows.map { it.toModel() } }

    override suspend fun upsertMeasurement(measurement: BodyMeasurement) {
        dao.upsertMeasurement(
            MeasurementEntity(measurement.id, measurement.localDate, measurement.name, measurement.centimeters, false),
        )
    }

    override fun observePhotos(): Flow<List<ProgressPhoto>> = dao.observePhotos().map { rows -> rows.map { it.toModel() } }

    override suspend fun addPhoto(photo: ProgressPhoto) {
        dao.upsertPhoto(PhotoEntity(photo.id, photo.localDate, photo.relativePath, false))
    }

    override suspend fun deletePhoto(id: String) {
        dao.softDeletePhoto(id)
    }
}

class RoomWorkoutRepository @Inject constructor(private val dao: VeloraDao) : WorkoutRepository {
    override fun observeExercises(): Flow<List<Exercise>> = dao.observeExercises().map { rows -> rows.map { it.toModel() } }

    override suspend fun upsertExercise(exercise: Exercise) {
        dao.upsertExercise(ExerciseEntity(exercise.id, exercise.name, exercise.cue, exercise.equipment, exercise.userCreated))
    }

    override fun observeTemplates(): Flow<List<WorkoutTemplate>> = dao.observeTemplates().transform { templates ->
        emit(templates.map { template ->
            WorkoutTemplate(template.id, template.name, dao.templateExercises(template.id).map { it.exerciseId })
        })
    }

    override suspend fun upsertTemplate(template: WorkoutTemplate) {
        dao.upsertTemplate(WorkoutTemplateEntity(template.id, template.name, false))
        dao.deleteTemplateExercises(template.id)
        template.exerciseIds.forEachIndexed { index, exerciseId ->
            dao.upsertTemplateExercise(WorkoutTemplateExerciseEntity(template.id, index, exerciseId))
        }
    }

    override fun observeSessions(): Flow<List<WorkoutSessionRecord>> = dao.observeSessions().transform { sessions ->
        emit(sessions.map { it.toModel(dao.sets(it.id)) })
    }

    override suspend fun saveSession(session: WorkoutSessionRecord) {
        dao.upsertSession(
            WorkoutSessionEntity(
                session.id, session.name, session.localDate, session.timeline.state.name,
                session.timeline.startedAtMillis, session.timeline.pausedAtMillis,
                session.timeline.accumulatedPauseMillis, session.timeline.completedAtMillis,
            ),
        )
        dao.deleteSets(session.id)
        session.sets.forEach { set ->
            dao.upsertSet(
                WorkoutSetEntity(
                    set.id, session.id, set.exerciseId, set.exerciseName, set.reps,
                    set.weightKilograms, set.durationSeconds, set.completed,
                ),
            )
        }
    }
}

class RoomWaterRepository @Inject constructor(private val dao: VeloraDao) : WaterRepository {
    override fun observe(localDate: String): Flow<WaterDay> =
        dao.observeWater(localDate).map { row -> WaterDay(localDate, row?.glasses ?: 0) }

    override suspend fun setGlasses(localDate: String, glasses: Int) {
        dao.upsertWater(WaterEntity(localDate, glasses.coerceIn(0, 40)))
    }
}
