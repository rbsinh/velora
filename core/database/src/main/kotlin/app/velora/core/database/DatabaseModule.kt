package app.velora.core.database

import android.content.Context
import androidx.room3.Room
import app.velora.core.domain.ActivityRepository
import app.velora.core.domain.FoodLogRepository
import app.velora.core.domain.FoodRepository
import app.velora.core.domain.MealTemplateRepository
import app.velora.core.domain.PersonalDataRepository
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.RecipeRepository
import app.velora.core.domain.SettingsRepository
import app.velora.core.domain.StepRepository
import app.velora.core.domain.WaterRepository
import app.velora.core.domain.WeightRepository
import app.velora.core.domain.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): VeloraDatabase =
        Room.databaseBuilder(context, VeloraDatabase::class.java, "velora.db").build()

    @Provides
    fun dao(database: VeloraDatabase): VeloraDao = database.dao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun profile(impl: RoomProfileRepository): ProfileRepository
    @Binds abstract fun foods(impl: RoomFoodRepository): FoodRepository
    @Binds abstract fun logs(impl: RoomFoodLogRepository): FoodLogRepository
    @Binds abstract fun recipes(impl: RoomRecipeRepository): RecipeRepository
    @Binds abstract fun meals(impl: RoomMealRepository): MealTemplateRepository
    @Binds abstract fun steps(impl: RoomStepRepository): StepRepository
    @Binds abstract fun activity(impl: RoomActivityRepository): ActivityRepository
    @Binds abstract fun weight(impl: RoomWeightRepository): WeightRepository
    @Binds abstract fun workouts(impl: RoomWorkoutRepository): WorkoutRepository
    @Binds abstract fun water(impl: RoomWaterRepository): WaterRepository
    @Binds abstract fun settings(impl: DataStoreSettingsRepository): SettingsRepository
    @Binds abstract fun personalData(impl: RoomPersonalDataRepository): PersonalDataRepository
}

@Singleton
class CatalogSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val foods: RoomFoodRepository,
    private val workouts: RoomWorkoutRepository,
    private val dao: VeloraDao,
) {
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val text = runCatching {
            context.assets.open("usda_foundation_subset.json").bufferedReader().use { it.readText() }
        }.getOrNull()
        if (text != null) {
            CatalogParser.parse(text).forEach { food ->
                if (dao.foodBySource(food.sourceRecordId.orEmpty()) == null) {
                    foods.upsert(food)
                }
            }
        }
        if (dao.countExercises() == 0) {
            ExerciseLibrary.exercises.forEach { workouts.upsertExercise(it) }
        }
    }
}
