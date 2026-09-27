package app.velora.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        FoodEntity::class,
        FoodServingEntity::class,
        FoodLogEntity::class,
        RecipeEntity::class,
        RecipeItemEntity::class,
        MealTemplateEntity::class,
        MealTemplateItemEntity::class,
        WeightEntity::class,
        MeasurementEntity::class,
        PhotoEntity::class,
        ExerciseEntity::class,
        WorkoutTemplateEntity::class,
        WorkoutTemplateExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class,
        ActivityEntity::class,
        StepDayEntity::class,
        WaterEntity::class,
        OutboxEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class VeloraDatabase : RoomDatabase() {
    abstract fun dao(): VeloraDao
}
