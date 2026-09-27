package app.velora.core.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VeloraDao {
    @Query("SELECT * FROM user_profile WHERE id = 'local'")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(entity: UserProfileEntity)

    @Query("SELECT * FROM food WHERE deleted = 0 AND name LIKE '%' || :query || '%' ORDER BY name LIMIT 60")
    fun observeFoodSearch(query: String): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food WHERE deleted = 0 AND barcode = :code")
    suspend fun foodsByBarcode(code: String): List<FoodEntity>

    @Query("SELECT * FROM food WHERE id = :id")
    suspend fun food(id: String): FoodEntity?

    @Query("SELECT * FROM food WHERE sourceRecordId = :sourceId LIMIT 1")
    suspend fun foodBySource(sourceId: String): FoodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFood(entity: FoodEntity)

    @Query("SELECT * FROM food_serving WHERE foodId = :foodId")
    suspend fun servings(foodId: String): List<FoodServingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertServing(entity: FoodServingEntity)

    @Query("DELETE FROM food_serving WHERE foodId = :foodId")
    suspend fun deleteServings(foodId: String)

    @Query("SELECT * FROM food_log_entry WHERE localDate = :date AND deleted = 0")
    fun observeLog(date: String): Flow<List<FoodLogEntity>>

    @Query("SELECT * FROM food_log_entry WHERE localDate = :date AND mealSlot = :slot AND deleted = 0")
    suspend fun logSlot(date: String, slot: String): List<FoodLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLog(entity: FoodLogEntity)

    @Query("UPDATE food_log_entry SET deleted = 1, updatedAtEpochMillis = :updated WHERE id = :id")
    suspend fun softDeleteLog(id: String, updated: Long)

    @Query("SELECT * FROM recipe WHERE deleted = 0 ORDER BY name")
    fun observeRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipe(entity: RecipeEntity)

    @Query("SELECT * FROM recipe_item WHERE recipeId = :id")
    suspend fun recipeItems(id: String): List<RecipeItemEntity>

    @Query("DELETE FROM recipe_item WHERE recipeId = :id")
    suspend fun deleteRecipeItems(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipeItem(entity: RecipeItemEntity)

    @Query("UPDATE recipe SET deleted = 1, updatedAtEpochMillis = :updated WHERE id = :id")
    suspend fun softDeleteRecipe(id: String, updated: Long)

    @Query("SELECT * FROM meal_template WHERE deleted = 0 ORDER BY name")
    fun observeMeals(): Flow<List<MealTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(entity: MealTemplateEntity)

    @Query("SELECT * FROM meal_template_item WHERE templateId = :id")
    suspend fun mealItems(id: String): List<MealTemplateItemEntity>

    @Query("DELETE FROM meal_template_item WHERE templateId = :id")
    suspend fun deleteMealItems(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMealItem(entity: MealTemplateItemEntity)

    @Query("SELECT * FROM weight_entry WHERE deleted = 0 ORDER BY localDate")
    fun observeWeights(): Flow<List<WeightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeight(entity: WeightEntity)

    @Query("SELECT * FROM body_measurement WHERE deleted = 0 ORDER BY localDate")
    fun observeMeasurements(): Flow<List<MeasurementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeasurement(entity: MeasurementEntity)

    @Query("SELECT * FROM progress_photo WHERE deleted = 0 ORDER BY localDate")
    fun observePhotos(): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(entity: PhotoEntity)

    @Query("UPDATE progress_photo SET deleted = 1 WHERE id = :id")
    suspend fun softDeletePhoto(id: String)

    @Query("SELECT * FROM exercise ORDER BY name")
    fun observeExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExercise(entity: ExerciseEntity)

    @Query("SELECT * FROM workout_template WHERE deleted = 0 ORDER BY name")
    fun observeTemplates(): Flow<List<WorkoutTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTemplate(entity: WorkoutTemplateEntity)

    @Query("DELETE FROM workout_template_exercise WHERE templateId = :id")
    suspend fun deleteTemplateExercises(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTemplateExercise(entity: WorkoutTemplateExerciseEntity)

    @Query("SELECT * FROM workout_template_exercise WHERE templateId = :id ORDER BY position")
    suspend fun templateExercises(id: String): List<WorkoutTemplateExerciseEntity>

    @Query("SELECT * FROM workout_session ORDER BY startedAtMillis DESC")
    fun observeSessions(): Flow<List<WorkoutSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(entity: WorkoutSessionEntity)

    @Query("DELETE FROM workout_set WHERE sessionId = :id")
    suspend fun deleteSets(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSet(entity: WorkoutSetEntity)

    @Query("SELECT * FROM workout_set WHERE sessionId = :id")
    suspend fun sets(id: String): List<WorkoutSetEntity>

    @Query("SELECT * FROM activity_entry WHERE localDate = :date AND deleted = 0")
    fun observeActivity(date: String): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertActivity(entity: ActivityEntity)

    @Query("SELECT * FROM step_day WHERE localDate >= :start AND localDate <= :end")
    fun observeSteps(start: String, end: String): Flow<List<StepDayEntity>>

    @Query("SELECT * FROM step_day WHERE localDate = :date")
    suspend fun stepDay(date: String): StepDayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStep(entity: StepDayEntity)

    @Query("SELECT * FROM water_day WHERE localDate = :date")
    fun observeWater(date: String): Flow<WaterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWater(entity: WaterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOutbox(entity: OutboxEntity)

    @Query("SELECT * FROM sync_outbox ORDER BY clientUpdatedAtEpochMillis")
    suspend fun outbox(): List<OutboxEntity>

    @Query("DELETE FROM sync_outbox WHERE clientId IN (:ids)")
    suspend fun deleteOutbox(ids: List<String>)

    @Query("SELECT COUNT(*) FROM exercise")
    suspend fun countExercises(): Int

    @Query("SELECT * FROM food_log_entry")
    suspend fun allLogs(): List<FoodLogEntity>

    @Query("SELECT * FROM weight_entry")
    suspend fun allWeights(): List<WeightEntity>
}
