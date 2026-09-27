package app.velora.core.database

import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.coroutines.createFlow
import androidx.room3.util.appendPlaceholders
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room3.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL", "MemberExtensionConflict"])
internal class VeloraDao_Impl(
  __db: RoomDatabase,
) : VeloraDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfileEntity: EntityInsertAdapter<UserProfileEntity>

  private val __insertAdapterOfFoodEntity: EntityInsertAdapter<FoodEntity>

  private val __insertAdapterOfFoodServingEntity: EntityInsertAdapter<FoodServingEntity>

  private val __insertAdapterOfFoodLogEntity: EntityInsertAdapter<FoodLogEntity>

  private val __insertAdapterOfRecipeEntity: EntityInsertAdapter<RecipeEntity>

  private val __insertAdapterOfRecipeItemEntity: EntityInsertAdapter<RecipeItemEntity>

  private val __insertAdapterOfMealTemplateEntity: EntityInsertAdapter<MealTemplateEntity>

  private val __insertAdapterOfMealTemplateItemEntity: EntityInsertAdapter<MealTemplateItemEntity>

  private val __insertAdapterOfWeightEntity: EntityInsertAdapter<WeightEntity>

  private val __insertAdapterOfMeasurementEntity: EntityInsertAdapter<MeasurementEntity>

  private val __insertAdapterOfPhotoEntity: EntityInsertAdapter<PhotoEntity>

  private val __insertAdapterOfExerciseEntity: EntityInsertAdapter<ExerciseEntity>

  private val __insertAdapterOfWorkoutTemplateEntity: EntityInsertAdapter<WorkoutTemplateEntity>

  private val __insertAdapterOfWorkoutTemplateExerciseEntity:
      EntityInsertAdapter<WorkoutTemplateExerciseEntity>

  private val __insertAdapterOfWorkoutSessionEntity: EntityInsertAdapter<WorkoutSessionEntity>

  private val __insertAdapterOfWorkoutSetEntity: EntityInsertAdapter<WorkoutSetEntity>

  private val __insertAdapterOfActivityEntity: EntityInsertAdapter<ActivityEntity>

  private val __insertAdapterOfStepDayEntity: EntityInsertAdapter<StepDayEntity>

  private val __insertAdapterOfWaterEntity: EntityInsertAdapter<WaterEntity>

  private val __insertAdapterOfOutboxEntity: EntityInsertAdapter<OutboxEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfileEntity = object : EntityInsertAdapter<UserProfileEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `user_profile` (`id`,`ageYears`,`sex`,`heightCentimeters`,`weightKilograms`,`goal`,`targetWeightKilograms`,`activityLevel`,`workoutDaysPerWeek`,`dietaryNote`,`measurementSystem`,`dailyStepGoal`,`calorieTargetOverride`,`proteinTargetOverrideGrams`,`carbTargetOverrideGrams`,`fatTargetOverrideGrams`,`acceptedEstimateDisclaimer`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfileEntity) {
        statement.bindText(1, entity.id)
        statement.bindLong(2, entity.ageYears.toLong())
        statement.bindText(3, entity.sex)
        statement.bindDouble(4, entity.heightCentimeters)
        statement.bindDouble(5, entity.weightKilograms)
        statement.bindText(6, entity.goal)
        val _tmpTargetWeightKilograms: Double? = entity.targetWeightKilograms
        if (_tmpTargetWeightKilograms == null) {
          statement.bindNull(7)
        } else {
          statement.bindDouble(7, _tmpTargetWeightKilograms)
        }
        statement.bindText(8, entity.activityLevel)
        statement.bindLong(9, entity.workoutDaysPerWeek.toLong())
        val _tmpDietaryNote: String? = entity.dietaryNote
        if (_tmpDietaryNote == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpDietaryNote)
        }
        statement.bindText(11, entity.measurementSystem)
        statement.bindLong(12, entity.dailyStepGoal.toLong())
        val _tmpCalorieTargetOverride: Int? = entity.calorieTargetOverride
        if (_tmpCalorieTargetOverride == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpCalorieTargetOverride.toLong())
        }
        val _tmpProteinTargetOverrideGrams: Int? = entity.proteinTargetOverrideGrams
        if (_tmpProteinTargetOverrideGrams == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmpProteinTargetOverrideGrams.toLong())
        }
        val _tmpCarbTargetOverrideGrams: Int? = entity.carbTargetOverrideGrams
        if (_tmpCarbTargetOverrideGrams == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmpCarbTargetOverrideGrams.toLong())
        }
        val _tmpFatTargetOverrideGrams: Int? = entity.fatTargetOverrideGrams
        if (_tmpFatTargetOverrideGrams == null) {
          statement.bindNull(16)
        } else {
          statement.bindLong(16, _tmpFatTargetOverrideGrams.toLong())
        }
        val _tmp: Int = if (entity.acceptedEstimateDisclaimer) 1 else 0
        statement.bindLong(17, _tmp.toLong())
      }
    }
    this.__insertAdapterOfFoodEntity = object : EntityInsertAdapter<FoodEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `food` (`id`,`name`,`brand`,`barcode`,`provenance`,`sourceName`,`sourceRecordId`,`userCreated`,`deleted`,`updatedAtEpochMillis`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FoodEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpBrand: String? = entity.brand
        if (_tmpBrand == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpBrand)
        }
        val _tmpBarcode: String? = entity.barcode
        if (_tmpBarcode == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpBarcode)
        }
        statement.bindText(5, entity.provenance)
        val _tmpSourceName: String? = entity.sourceName
        if (_tmpSourceName == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSourceName)
        }
        val _tmpSourceRecordId: String? = entity.sourceRecordId
        if (_tmpSourceRecordId == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpSourceRecordId)
        }
        val _tmp: Int = if (entity.userCreated) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmp_1: Int = if (entity.deleted) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
        statement.bindLong(10, entity.updatedAtEpochMillis)
      }
    }
    this.__insertAdapterOfFoodServingEntity = object : EntityInsertAdapter<FoodServingEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `food_serving` (`id`,`foodId`,`label`,`grams`,`caloriesKcal`,`proteinGrams`,`carbohydrateGrams`,`fatGrams`,`fiberGrams`,`sugarGrams`,`sodiumMilligrams`,`provenance`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FoodServingEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.foodId)
        statement.bindText(3, entity.label)
        val _tmpGrams: Double? = entity.grams
        if (_tmpGrams == null) {
          statement.bindNull(4)
        } else {
          statement.bindDouble(4, _tmpGrams)
        }
        statement.bindDouble(5, entity.caloriesKcal)
        statement.bindDouble(6, entity.proteinGrams)
        statement.bindDouble(7, entity.carbohydrateGrams)
        statement.bindDouble(8, entity.fatGrams)
        val _tmpFiberGrams: Double? = entity.fiberGrams
        if (_tmpFiberGrams == null) {
          statement.bindNull(9)
        } else {
          statement.bindDouble(9, _tmpFiberGrams)
        }
        val _tmpSugarGrams: Double? = entity.sugarGrams
        if (_tmpSugarGrams == null) {
          statement.bindNull(10)
        } else {
          statement.bindDouble(10, _tmpSugarGrams)
        }
        val _tmpSodiumMilligrams: Double? = entity.sodiumMilligrams
        if (_tmpSodiumMilligrams == null) {
          statement.bindNull(11)
        } else {
          statement.bindDouble(11, _tmpSodiumMilligrams)
        }
        statement.bindText(12, entity.provenance)
      }
    }
    this.__insertAdapterOfFoodLogEntity = object : EntityInsertAdapter<FoodLogEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `food_log_entry` (`id`,`localDate`,`mealSlot`,`foodId`,`foodName`,`servingLabel`,`quantity`,`caloriesKcal`,`proteinGrams`,`carbohydrateGrams`,`fatGrams`,`fiberGrams`,`sugarGrams`,`sodiumMilligrams`,`provenance`,`updatedAtEpochMillis`,`deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FoodLogEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.localDate)
        statement.bindText(3, entity.mealSlot)
        val _tmpFoodId: String? = entity.foodId
        if (_tmpFoodId == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpFoodId)
        }
        statement.bindText(5, entity.foodName)
        statement.bindText(6, entity.servingLabel)
        statement.bindDouble(7, entity.quantity)
        statement.bindDouble(8, entity.caloriesKcal)
        statement.bindDouble(9, entity.proteinGrams)
        statement.bindDouble(10, entity.carbohydrateGrams)
        statement.bindDouble(11, entity.fatGrams)
        val _tmpFiberGrams: Double? = entity.fiberGrams
        if (_tmpFiberGrams == null) {
          statement.bindNull(12)
        } else {
          statement.bindDouble(12, _tmpFiberGrams)
        }
        val _tmpSugarGrams: Double? = entity.sugarGrams
        if (_tmpSugarGrams == null) {
          statement.bindNull(13)
        } else {
          statement.bindDouble(13, _tmpSugarGrams)
        }
        val _tmpSodiumMilligrams: Double? = entity.sodiumMilligrams
        if (_tmpSodiumMilligrams == null) {
          statement.bindNull(14)
        } else {
          statement.bindDouble(14, _tmpSodiumMilligrams)
        }
        statement.bindText(15, entity.provenance)
        statement.bindLong(16, entity.updatedAtEpochMillis)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(17, _tmp.toLong())
      }
    }
    this.__insertAdapterOfRecipeEntity = object : EntityInsertAdapter<RecipeEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `recipe` (`id`,`name`,`deleted`,`updatedAtEpochMillis`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RecipeEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        statement.bindLong(4, entity.updatedAtEpochMillis)
      }
    }
    this.__insertAdapterOfRecipeItemEntity = object : EntityInsertAdapter<RecipeItemEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `recipe_item` (`id`,`recipeId`,`foodName`,`servingLabel`,`quantity`,`caloriesKcal`,`proteinGrams`,`carbohydrateGrams`,`fatGrams`,`fiberGrams`,`sugarGrams`,`sodiumMilligrams`,`provenance`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RecipeItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.recipeId)
        statement.bindText(3, entity.foodName)
        statement.bindText(4, entity.servingLabel)
        statement.bindDouble(5, entity.quantity)
        statement.bindDouble(6, entity.caloriesKcal)
        statement.bindDouble(7, entity.proteinGrams)
        statement.bindDouble(8, entity.carbohydrateGrams)
        statement.bindDouble(9, entity.fatGrams)
        val _tmpFiberGrams: Double? = entity.fiberGrams
        if (_tmpFiberGrams == null) {
          statement.bindNull(10)
        } else {
          statement.bindDouble(10, _tmpFiberGrams)
        }
        val _tmpSugarGrams: Double? = entity.sugarGrams
        if (_tmpSugarGrams == null) {
          statement.bindNull(11)
        } else {
          statement.bindDouble(11, _tmpSugarGrams)
        }
        val _tmpSodiumMilligrams: Double? = entity.sodiumMilligrams
        if (_tmpSodiumMilligrams == null) {
          statement.bindNull(12)
        } else {
          statement.bindDouble(12, _tmpSodiumMilligrams)
        }
        statement.bindText(13, entity.provenance)
      }
    }
    this.__insertAdapterOfMealTemplateEntity = object : EntityInsertAdapter<MealTemplateEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `meal_template` (`id`,`name`,`updatedAtEpochMillis`,`deleted`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MealTemplateEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.updatedAtEpochMillis)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__insertAdapterOfMealTemplateItemEntity = object : EntityInsertAdapter<MealTemplateItemEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `meal_template_item` (`id`,`templateId`,`foodName`,`servingLabel`,`quantity`,`mealSlot`,`caloriesKcal`,`proteinGrams`,`carbohydrateGrams`,`fatGrams`,`fiberGrams`,`sugarGrams`,`sodiumMilligrams`,`provenance`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MealTemplateItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.templateId)
        statement.bindText(3, entity.foodName)
        statement.bindText(4, entity.servingLabel)
        statement.bindDouble(5, entity.quantity)
        statement.bindText(6, entity.mealSlot)
        statement.bindDouble(7, entity.caloriesKcal)
        statement.bindDouble(8, entity.proteinGrams)
        statement.bindDouble(9, entity.carbohydrateGrams)
        statement.bindDouble(10, entity.fatGrams)
        val _tmpFiberGrams: Double? = entity.fiberGrams
        if (_tmpFiberGrams == null) {
          statement.bindNull(11)
        } else {
          statement.bindDouble(11, _tmpFiberGrams)
        }
        val _tmpSugarGrams: Double? = entity.sugarGrams
        if (_tmpSugarGrams == null) {
          statement.bindNull(12)
        } else {
          statement.bindDouble(12, _tmpSugarGrams)
        }
        val _tmpSodiumMilligrams: Double? = entity.sodiumMilligrams
        if (_tmpSodiumMilligrams == null) {
          statement.bindNull(13)
        } else {
          statement.bindDouble(13, _tmpSodiumMilligrams)
        }
        statement.bindText(14, entity.provenance)
      }
    }
    this.__insertAdapterOfWeightEntity = object : EntityInsertAdapter<WeightEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `weight_entry` (`id`,`localDate`,`kilograms`,`updatedAtEpochMillis`,`deleted`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WeightEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.localDate)
        statement.bindDouble(3, entity.kilograms)
        statement.bindLong(4, entity.updatedAtEpochMillis)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__insertAdapterOfMeasurementEntity = object : EntityInsertAdapter<MeasurementEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `body_measurement` (`id`,`localDate`,`name`,`centimeters`,`deleted`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MeasurementEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.localDate)
        statement.bindText(3, entity.name)
        statement.bindDouble(4, entity.centimeters)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__insertAdapterOfPhotoEntity = object : EntityInsertAdapter<PhotoEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `progress_photo` (`id`,`localDate`,`relativePath`,`deleted`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PhotoEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.localDate)
        statement.bindText(3, entity.relativePath)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__insertAdapterOfExerciseEntity = object : EntityInsertAdapter<ExerciseEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `exercise` (`id`,`name`,`cue`,`equipment`,`userCreated`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExerciseEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.cue)
        statement.bindText(4, entity.equipment)
        val _tmp: Int = if (entity.userCreated) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__insertAdapterOfWorkoutTemplateEntity = object : EntityInsertAdapter<WorkoutTemplateEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `workout_template` (`id`,`name`,`deleted`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkoutTemplateEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(3, _tmp.toLong())
      }
    }
    this.__insertAdapterOfWorkoutTemplateExerciseEntity = object : EntityInsertAdapter<WorkoutTemplateExerciseEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `workout_template_exercise` (`templateId`,`position`,`exerciseId`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkoutTemplateExerciseEntity) {
        statement.bindText(1, entity.templateId)
        statement.bindLong(2, entity.position.toLong())
        statement.bindText(3, entity.exerciseId)
      }
    }
    this.__insertAdapterOfWorkoutSessionEntity = object : EntityInsertAdapter<WorkoutSessionEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `workout_session` (`id`,`name`,`localDate`,`state`,`startedAtMillis`,`pausedAtMillis`,`accumulatedPauseMillis`,`completedAtMillis`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkoutSessionEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.localDate)
        statement.bindText(4, entity.state)
        statement.bindLong(5, entity.startedAtMillis)
        val _tmpPausedAtMillis: Long? = entity.pausedAtMillis
        if (_tmpPausedAtMillis == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpPausedAtMillis)
        }
        statement.bindLong(7, entity.accumulatedPauseMillis)
        val _tmpCompletedAtMillis: Long? = entity.completedAtMillis
        if (_tmpCompletedAtMillis == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpCompletedAtMillis)
        }
      }
    }
    this.__insertAdapterOfWorkoutSetEntity = object : EntityInsertAdapter<WorkoutSetEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `workout_set` (`id`,`sessionId`,`exerciseId`,`exerciseName`,`reps`,`weightKilograms`,`durationSeconds`,`completed`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkoutSetEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.sessionId)
        statement.bindText(3, entity.exerciseId)
        statement.bindText(4, entity.exerciseName)
        val _tmpReps: Int? = entity.reps
        if (_tmpReps == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpReps.toLong())
        }
        val _tmpWeightKilograms: Double? = entity.weightKilograms
        if (_tmpWeightKilograms == null) {
          statement.bindNull(6)
        } else {
          statement.bindDouble(6, _tmpWeightKilograms)
        }
        val _tmpDurationSeconds: Int? = entity.durationSeconds
        if (_tmpDurationSeconds == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpDurationSeconds.toLong())
        }
        val _tmp: Int = if (entity.completed) 1 else 0
        statement.bindLong(8, _tmp.toLong())
      }
    }
    this.__insertAdapterOfActivityEntity = object : EntityInsertAdapter<ActivityEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `activity_entry` (`id`,`localDate`,`kind`,`durationMinutes`,`note`,`sourceLabel`,`deleted`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.localDate)
        statement.bindText(3, entity.kind)
        statement.bindLong(4, entity.durationMinutes.toLong())
        val _tmpNote: String? = entity.note
        if (_tmpNote == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpNote)
        }
        statement.bindText(6, entity.sourceLabel)
        val _tmp: Int = if (entity.deleted) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }
    this.__insertAdapterOfStepDayEntity = object : EntityInsertAdapter<StepDayEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `step_day` (`localDate`,`healthConnectSteps`,`manualSteps`,`manualOverridesHealthConnect`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StepDayEntity) {
        statement.bindText(1, entity.localDate)
        val _tmpHealthConnectSteps: Long? = entity.healthConnectSteps
        if (_tmpHealthConnectSteps == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpHealthConnectSteps)
        }
        val _tmpManualSteps: Long? = entity.manualSteps
        if (_tmpManualSteps == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpManualSteps)
        }
        val _tmp: Int = if (entity.manualOverridesHealthConnect) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__insertAdapterOfWaterEntity = object : EntityInsertAdapter<WaterEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `water_day` (`localDate`,`glasses`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WaterEntity) {
        statement.bindText(1, entity.localDate)
        statement.bindLong(2, entity.glasses.toLong())
      }
    }
    this.__insertAdapterOfOutboxEntity = object : EntityInsertAdapter<OutboxEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `sync_outbox` (`clientId`,`entityName`,`operation`,`clientUpdatedAtEpochMillis`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: OutboxEntity) {
        statement.bindText(1, entity.clientId)
        statement.bindText(2, entity.entityName)
        statement.bindText(3, entity.operation)
        statement.bindLong(4, entity.clientUpdatedAtEpochMillis)
      }
    }
  }

  public override suspend fun upsertProfile(entity: UserProfileEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUserProfileEntity.insert(_connection, entity)
  }

  public override suspend fun upsertFood(entity: FoodEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFoodEntity.insert(_connection, entity)
  }

  public override suspend fun upsertServing(entity: FoodServingEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFoodServingEntity.insert(_connection, entity)
  }

  public override suspend fun upsertLog(entity: FoodLogEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFoodLogEntity.insert(_connection, entity)
  }

  public override suspend fun upsertRecipe(entity: RecipeEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRecipeEntity.insert(_connection, entity)
  }

  public override suspend fun upsertRecipeItem(entity: RecipeItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRecipeItemEntity.insert(_connection, entity)
  }

  public override suspend fun upsertMeal(entity: MealTemplateEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMealTemplateEntity.insert(_connection, entity)
  }

  public override suspend fun upsertMealItem(entity: MealTemplateItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMealTemplateItemEntity.insert(_connection, entity)
  }

  public override suspend fun upsertWeight(entity: WeightEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWeightEntity.insert(_connection, entity)
  }

  public override suspend fun upsertMeasurement(entity: MeasurementEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMeasurementEntity.insert(_connection, entity)
  }

  public override suspend fun upsertPhoto(entity: PhotoEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPhotoEntity.insert(_connection, entity)
  }

  public override suspend fun upsertExercise(entity: ExerciseEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfExerciseEntity.insert(_connection, entity)
  }

  public override suspend fun upsertTemplate(entity: WorkoutTemplateEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWorkoutTemplateEntity.insert(_connection, entity)
  }

  public override suspend fun upsertTemplateExercise(entity: WorkoutTemplateExerciseEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWorkoutTemplateExerciseEntity.insert(_connection, entity)
  }

  public override suspend fun upsertSession(entity: WorkoutSessionEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWorkoutSessionEntity.insert(_connection, entity)
  }

  public override suspend fun upsertSet(entity: WorkoutSetEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWorkoutSetEntity.insert(_connection, entity)
  }

  public override suspend fun upsertActivity(entity: ActivityEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfActivityEntity.insert(_connection, entity)
  }

  public override suspend fun upsertStep(entity: StepDayEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfStepDayEntity.insert(_connection, entity)
  }

  public override suspend fun upsertWater(entity: WaterEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfWaterEntity.insert(_connection, entity)
  }

  public override suspend fun upsertOutbox(entity: OutboxEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfOutboxEntity.insert(_connection, entity)
  }

  public override fun observeProfile(): Flow<UserProfileEntity?> {
    val _sql: String = "SELECT * FROM user_profile WHERE id = 'local'"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfAgeYears: Int = getColumnIndexOrThrow(_stmt, "ageYears")
        val _columnIndexOfSex: Int = getColumnIndexOrThrow(_stmt, "sex")
        val _columnIndexOfHeightCentimeters: Int = getColumnIndexOrThrow(_stmt, "heightCentimeters")
        val _columnIndexOfWeightKilograms: Int = getColumnIndexOrThrow(_stmt, "weightKilograms")
        val _columnIndexOfGoal: Int = getColumnIndexOrThrow(_stmt, "goal")
        val _columnIndexOfTargetWeightKilograms: Int = getColumnIndexOrThrow(_stmt, "targetWeightKilograms")
        val _columnIndexOfActivityLevel: Int = getColumnIndexOrThrow(_stmt, "activityLevel")
        val _columnIndexOfWorkoutDaysPerWeek: Int = getColumnIndexOrThrow(_stmt, "workoutDaysPerWeek")
        val _columnIndexOfDietaryNote: Int = getColumnIndexOrThrow(_stmt, "dietaryNote")
        val _columnIndexOfMeasurementSystem: Int = getColumnIndexOrThrow(_stmt, "measurementSystem")
        val _columnIndexOfDailyStepGoal: Int = getColumnIndexOrThrow(_stmt, "dailyStepGoal")
        val _columnIndexOfCalorieTargetOverride: Int = getColumnIndexOrThrow(_stmt, "calorieTargetOverride")
        val _columnIndexOfProteinTargetOverrideGrams: Int = getColumnIndexOrThrow(_stmt, "proteinTargetOverrideGrams")
        val _columnIndexOfCarbTargetOverrideGrams: Int = getColumnIndexOrThrow(_stmt, "carbTargetOverrideGrams")
        val _columnIndexOfFatTargetOverrideGrams: Int = getColumnIndexOrThrow(_stmt, "fatTargetOverrideGrams")
        val _columnIndexOfAcceptedEstimateDisclaimer: Int = getColumnIndexOrThrow(_stmt, "acceptedEstimateDisclaimer")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpAgeYears: Int
          _tmpAgeYears = _stmt.getLong(_columnIndexOfAgeYears).toInt()
          val _tmpSex: String
          _tmpSex = _stmt.getText(_columnIndexOfSex)
          val _tmpHeightCentimeters: Double
          _tmpHeightCentimeters = _stmt.getDouble(_columnIndexOfHeightCentimeters)
          val _tmpWeightKilograms: Double
          _tmpWeightKilograms = _stmt.getDouble(_columnIndexOfWeightKilograms)
          val _tmpGoal: String
          _tmpGoal = _stmt.getText(_columnIndexOfGoal)
          val _tmpTargetWeightKilograms: Double?
          if (_stmt.isNull(_columnIndexOfTargetWeightKilograms)) {
            _tmpTargetWeightKilograms = null
          } else {
            _tmpTargetWeightKilograms = _stmt.getDouble(_columnIndexOfTargetWeightKilograms)
          }
          val _tmpActivityLevel: String
          _tmpActivityLevel = _stmt.getText(_columnIndexOfActivityLevel)
          val _tmpWorkoutDaysPerWeek: Int
          _tmpWorkoutDaysPerWeek = _stmt.getLong(_columnIndexOfWorkoutDaysPerWeek).toInt()
          val _tmpDietaryNote: String?
          if (_stmt.isNull(_columnIndexOfDietaryNote)) {
            _tmpDietaryNote = null
          } else {
            _tmpDietaryNote = _stmt.getText(_columnIndexOfDietaryNote)
          }
          val _tmpMeasurementSystem: String
          _tmpMeasurementSystem = _stmt.getText(_columnIndexOfMeasurementSystem)
          val _tmpDailyStepGoal: Int
          _tmpDailyStepGoal = _stmt.getLong(_columnIndexOfDailyStepGoal).toInt()
          val _tmpCalorieTargetOverride: Int?
          if (_stmt.isNull(_columnIndexOfCalorieTargetOverride)) {
            _tmpCalorieTargetOverride = null
          } else {
            _tmpCalorieTargetOverride = _stmt.getLong(_columnIndexOfCalorieTargetOverride).toInt()
          }
          val _tmpProteinTargetOverrideGrams: Int?
          if (_stmt.isNull(_columnIndexOfProteinTargetOverrideGrams)) {
            _tmpProteinTargetOverrideGrams = null
          } else {
            _tmpProteinTargetOverrideGrams = _stmt.getLong(_columnIndexOfProteinTargetOverrideGrams).toInt()
          }
          val _tmpCarbTargetOverrideGrams: Int?
          if (_stmt.isNull(_columnIndexOfCarbTargetOverrideGrams)) {
            _tmpCarbTargetOverrideGrams = null
          } else {
            _tmpCarbTargetOverrideGrams = _stmt.getLong(_columnIndexOfCarbTargetOverrideGrams).toInt()
          }
          val _tmpFatTargetOverrideGrams: Int?
          if (_stmt.isNull(_columnIndexOfFatTargetOverrideGrams)) {
            _tmpFatTargetOverrideGrams = null
          } else {
            _tmpFatTargetOverrideGrams = _stmt.getLong(_columnIndexOfFatTargetOverrideGrams).toInt()
          }
          val _tmpAcceptedEstimateDisclaimer: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfAcceptedEstimateDisclaimer).toInt()
          _tmpAcceptedEstimateDisclaimer = _tmp != 0
          _result = UserProfileEntity(_tmpId,_tmpAgeYears,_tmpSex,_tmpHeightCentimeters,_tmpWeightKilograms,_tmpGoal,_tmpTargetWeightKilograms,_tmpActivityLevel,_tmpWorkoutDaysPerWeek,_tmpDietaryNote,_tmpMeasurementSystem,_tmpDailyStepGoal,_tmpCalorieTargetOverride,_tmpProteinTargetOverrideGrams,_tmpCarbTargetOverrideGrams,_tmpFatTargetOverrideGrams,_tmpAcceptedEstimateDisclaimer)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeFoodSearch(query: String): Flow<List<FoodEntity>> {
    val _sql: String = "SELECT * FROM food WHERE deleted = 0 AND name LIKE '%' || ? || '%' ORDER BY name LIMIT 60"
    return createFlow(__db, false, arrayOf("food")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brand")
        val _columnIndexOfBarcode: Int = getColumnIndexOrThrow(_stmt, "barcode")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
        val _columnIndexOfSourceRecordId: Int = getColumnIndexOrThrow(_stmt, "sourceRecordId")
        val _columnIndexOfUserCreated: Int = getColumnIndexOrThrow(_stmt, "userCreated")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _result: MutableList<FoodEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpBarcode: String?
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpSourceName: String?
          if (_stmt.isNull(_columnIndexOfSourceName)) {
            _tmpSourceName = null
          } else {
            _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
          }
          val _tmpSourceRecordId: String?
          if (_stmt.isNull(_columnIndexOfSourceRecordId)) {
            _tmpSourceRecordId = null
          } else {
            _tmpSourceRecordId = _stmt.getText(_columnIndexOfSourceRecordId)
          }
          val _tmpUserCreated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfUserCreated).toInt()
          _tmpUserCreated = _tmp != 0
          val _tmpDeleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp_1 != 0
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          _item = FoodEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpProvenance,_tmpSourceName,_tmpSourceRecordId,_tmpUserCreated,_tmpDeleted,_tmpUpdatedAtEpochMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun foodsByBarcode(code: String): List<FoodEntity> {
    val _sql: String = "SELECT * FROM food WHERE deleted = 0 AND barcode = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, code)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brand")
        val _columnIndexOfBarcode: Int = getColumnIndexOrThrow(_stmt, "barcode")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
        val _columnIndexOfSourceRecordId: Int = getColumnIndexOrThrow(_stmt, "sourceRecordId")
        val _columnIndexOfUserCreated: Int = getColumnIndexOrThrow(_stmt, "userCreated")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _result: MutableList<FoodEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpBarcode: String?
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpSourceName: String?
          if (_stmt.isNull(_columnIndexOfSourceName)) {
            _tmpSourceName = null
          } else {
            _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
          }
          val _tmpSourceRecordId: String?
          if (_stmt.isNull(_columnIndexOfSourceRecordId)) {
            _tmpSourceRecordId = null
          } else {
            _tmpSourceRecordId = _stmt.getText(_columnIndexOfSourceRecordId)
          }
          val _tmpUserCreated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfUserCreated).toInt()
          _tmpUserCreated = _tmp != 0
          val _tmpDeleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp_1 != 0
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          _item = FoodEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpProvenance,_tmpSourceName,_tmpSourceRecordId,_tmpUserCreated,_tmpDeleted,_tmpUpdatedAtEpochMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun food(id: String): FoodEntity? {
    val _sql: String = "SELECT * FROM food WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brand")
        val _columnIndexOfBarcode: Int = getColumnIndexOrThrow(_stmt, "barcode")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
        val _columnIndexOfSourceRecordId: Int = getColumnIndexOrThrow(_stmt, "sourceRecordId")
        val _columnIndexOfUserCreated: Int = getColumnIndexOrThrow(_stmt, "userCreated")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _result: FoodEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpBarcode: String?
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpSourceName: String?
          if (_stmt.isNull(_columnIndexOfSourceName)) {
            _tmpSourceName = null
          } else {
            _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
          }
          val _tmpSourceRecordId: String?
          if (_stmt.isNull(_columnIndexOfSourceRecordId)) {
            _tmpSourceRecordId = null
          } else {
            _tmpSourceRecordId = _stmt.getText(_columnIndexOfSourceRecordId)
          }
          val _tmpUserCreated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfUserCreated).toInt()
          _tmpUserCreated = _tmp != 0
          val _tmpDeleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp_1 != 0
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          _result = FoodEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpProvenance,_tmpSourceName,_tmpSourceRecordId,_tmpUserCreated,_tmpDeleted,_tmpUpdatedAtEpochMillis)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun foodBySource(sourceId: String): FoodEntity? {
    val _sql: String = "SELECT * FROM food WHERE sourceRecordId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sourceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brand")
        val _columnIndexOfBarcode: Int = getColumnIndexOrThrow(_stmt, "barcode")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
        val _columnIndexOfSourceRecordId: Int = getColumnIndexOrThrow(_stmt, "sourceRecordId")
        val _columnIndexOfUserCreated: Int = getColumnIndexOrThrow(_stmt, "userCreated")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _result: FoodEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpBarcode: String?
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpSourceName: String?
          if (_stmt.isNull(_columnIndexOfSourceName)) {
            _tmpSourceName = null
          } else {
            _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
          }
          val _tmpSourceRecordId: String?
          if (_stmt.isNull(_columnIndexOfSourceRecordId)) {
            _tmpSourceRecordId = null
          } else {
            _tmpSourceRecordId = _stmt.getText(_columnIndexOfSourceRecordId)
          }
          val _tmpUserCreated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfUserCreated).toInt()
          _tmpUserCreated = _tmp != 0
          val _tmpDeleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp_1 != 0
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          _result = FoodEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpProvenance,_tmpSourceName,_tmpSourceRecordId,_tmpUserCreated,_tmpDeleted,_tmpUpdatedAtEpochMillis)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun servings(foodId: String): List<FoodServingEntity> {
    val _sql: String = "SELECT * FROM food_serving WHERE foodId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, foodId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "foodId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfGrams: Int = getColumnIndexOrThrow(_stmt, "grams")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _result: MutableList<FoodServingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodServingEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpFoodId: String
          _tmpFoodId = _stmt.getText(_columnIndexOfFoodId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpGrams: Double?
          if (_stmt.isNull(_columnIndexOfGrams)) {
            _tmpGrams = null
          } else {
            _tmpGrams = _stmt.getDouble(_columnIndexOfGrams)
          }
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          _item = FoodServingEntity(_tmpId,_tmpFoodId,_tmpLabel,_tmpGrams,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeLog(date: String): Flow<List<FoodLogEntity>> {
    val _sql: String = "SELECT * FROM food_log_entry WHERE localDate = ? AND deleted = 0"
    return createFlow(__db, false, arrayOf("food_log_entry")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfMealSlot: Int = getColumnIndexOrThrow(_stmt, "mealSlot")
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "foodId")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "foodName")
        val _columnIndexOfServingLabel: Int = getColumnIndexOrThrow(_stmt, "servingLabel")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<FoodLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodLogEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpMealSlot: String
          _tmpMealSlot = _stmt.getText(_columnIndexOfMealSlot)
          val _tmpFoodId: String?
          if (_stmt.isNull(_columnIndexOfFoodId)) {
            _tmpFoodId = null
          } else {
            _tmpFoodId = _stmt.getText(_columnIndexOfFoodId)
          }
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpServingLabel: String
          _tmpServingLabel = _stmt.getText(_columnIndexOfServingLabel)
          val _tmpQuantity: Double
          _tmpQuantity = _stmt.getDouble(_columnIndexOfQuantity)
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = FoodLogEntity(_tmpId,_tmpLocalDate,_tmpMealSlot,_tmpFoodId,_tmpFoodName,_tmpServingLabel,_tmpQuantity,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun logSlot(date: String, slot: String): List<FoodLogEntity> {
    val _sql: String = "SELECT * FROM food_log_entry WHERE localDate = ? AND mealSlot = ? AND deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        _argIndex = 2
        _stmt.bindText(_argIndex, slot)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfMealSlot: Int = getColumnIndexOrThrow(_stmt, "mealSlot")
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "foodId")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "foodName")
        val _columnIndexOfServingLabel: Int = getColumnIndexOrThrow(_stmt, "servingLabel")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<FoodLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodLogEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpMealSlot: String
          _tmpMealSlot = _stmt.getText(_columnIndexOfMealSlot)
          val _tmpFoodId: String?
          if (_stmt.isNull(_columnIndexOfFoodId)) {
            _tmpFoodId = null
          } else {
            _tmpFoodId = _stmt.getText(_columnIndexOfFoodId)
          }
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpServingLabel: String
          _tmpServingLabel = _stmt.getText(_columnIndexOfServingLabel)
          val _tmpQuantity: Double
          _tmpQuantity = _stmt.getDouble(_columnIndexOfQuantity)
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = FoodLogEntity(_tmpId,_tmpLocalDate,_tmpMealSlot,_tmpFoodId,_tmpFoodName,_tmpServingLabel,_tmpQuantity,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeRecipes(): Flow<List<RecipeEntity>> {
    val _sql: String = "SELECT * FROM recipe WHERE deleted = 0 ORDER BY name"
    return createFlow(__db, false, arrayOf("recipe")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _result: MutableList<RecipeEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: RecipeEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          _item = RecipeEntity(_tmpId,_tmpName,_tmpDeleted,_tmpUpdatedAtEpochMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun recipeItems(id: String): List<RecipeItemEntity> {
    val _sql: String = "SELECT * FROM recipe_item WHERE recipeId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRecipeId: Int = getColumnIndexOrThrow(_stmt, "recipeId")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "foodName")
        val _columnIndexOfServingLabel: Int = getColumnIndexOrThrow(_stmt, "servingLabel")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _result: MutableList<RecipeItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: RecipeItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpRecipeId: String
          _tmpRecipeId = _stmt.getText(_columnIndexOfRecipeId)
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpServingLabel: String
          _tmpServingLabel = _stmt.getText(_columnIndexOfServingLabel)
          val _tmpQuantity: Double
          _tmpQuantity = _stmt.getDouble(_columnIndexOfQuantity)
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          _item = RecipeItemEntity(_tmpId,_tmpRecipeId,_tmpFoodName,_tmpServingLabel,_tmpQuantity,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeMeals(): Flow<List<MealTemplateEntity>> {
    val _sql: String = "SELECT * FROM meal_template WHERE deleted = 0 ORDER BY name"
    return createFlow(__db, false, arrayOf("meal_template")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<MealTemplateEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MealTemplateEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = MealTemplateEntity(_tmpId,_tmpName,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun mealItems(id: String): List<MealTemplateItemEntity> {
    val _sql: String = "SELECT * FROM meal_template_item WHERE templateId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTemplateId: Int = getColumnIndexOrThrow(_stmt, "templateId")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "foodName")
        val _columnIndexOfServingLabel: Int = getColumnIndexOrThrow(_stmt, "servingLabel")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfMealSlot: Int = getColumnIndexOrThrow(_stmt, "mealSlot")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _result: MutableList<MealTemplateItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MealTemplateItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTemplateId: String
          _tmpTemplateId = _stmt.getText(_columnIndexOfTemplateId)
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpServingLabel: String
          _tmpServingLabel = _stmt.getText(_columnIndexOfServingLabel)
          val _tmpQuantity: Double
          _tmpQuantity = _stmt.getDouble(_columnIndexOfQuantity)
          val _tmpMealSlot: String
          _tmpMealSlot = _stmt.getText(_columnIndexOfMealSlot)
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          _item = MealTemplateItemEntity(_tmpId,_tmpTemplateId,_tmpFoodName,_tmpServingLabel,_tmpQuantity,_tmpMealSlot,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeWeights(): Flow<List<WeightEntity>> {
    val _sql: String = "SELECT * FROM weight_entry WHERE deleted = 0 ORDER BY localDate"
    return createFlow(__db, false, arrayOf("weight_entry")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfKilograms: Int = getColumnIndexOrThrow(_stmt, "kilograms")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<WeightEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WeightEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpKilograms: Double
          _tmpKilograms = _stmt.getDouble(_columnIndexOfKilograms)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = WeightEntity(_tmpId,_tmpLocalDate,_tmpKilograms,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeMeasurements(): Flow<List<MeasurementEntity>> {
    val _sql: String = "SELECT * FROM body_measurement WHERE deleted = 0 ORDER BY localDate"
    return createFlow(__db, false, arrayOf("body_measurement")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCentimeters: Int = getColumnIndexOrThrow(_stmt, "centimeters")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<MeasurementEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MeasurementEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCentimeters: Double
          _tmpCentimeters = _stmt.getDouble(_columnIndexOfCentimeters)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = MeasurementEntity(_tmpId,_tmpLocalDate,_tmpName,_tmpCentimeters,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observePhotos(): Flow<List<PhotoEntity>> {
    val _sql: String = "SELECT * FROM progress_photo WHERE deleted = 0 ORDER BY localDate"
    return createFlow(__db, false, arrayOf("progress_photo")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfRelativePath: Int = getColumnIndexOrThrow(_stmt, "relativePath")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<PhotoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PhotoEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpRelativePath: String
          _tmpRelativePath = _stmt.getText(_columnIndexOfRelativePath)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = PhotoEntity(_tmpId,_tmpLocalDate,_tmpRelativePath,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeExercises(): Flow<List<ExerciseEntity>> {
    val _sql: String = "SELECT * FROM exercise ORDER BY name"
    return createFlow(__db, false, arrayOf("exercise")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCue: Int = getColumnIndexOrThrow(_stmt, "cue")
        val _columnIndexOfEquipment: Int = getColumnIndexOrThrow(_stmt, "equipment")
        val _columnIndexOfUserCreated: Int = getColumnIndexOrThrow(_stmt, "userCreated")
        val _result: MutableList<ExerciseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExerciseEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCue: String
          _tmpCue = _stmt.getText(_columnIndexOfCue)
          val _tmpEquipment: String
          _tmpEquipment = _stmt.getText(_columnIndexOfEquipment)
          val _tmpUserCreated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfUserCreated).toInt()
          _tmpUserCreated = _tmp != 0
          _item = ExerciseEntity(_tmpId,_tmpName,_tmpCue,_tmpEquipment,_tmpUserCreated)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTemplates(): Flow<List<WorkoutTemplateEntity>> {
    val _sql: String = "SELECT * FROM workout_template WHERE deleted = 0 ORDER BY name"
    return createFlow(__db, false, arrayOf("workout_template")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<WorkoutTemplateEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkoutTemplateEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = WorkoutTemplateEntity(_tmpId,_tmpName,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun templateExercises(id: String): List<WorkoutTemplateExerciseEntity> {
    val _sql: String = "SELECT * FROM workout_template_exercise WHERE templateId = ? ORDER BY position"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfTemplateId: Int = getColumnIndexOrThrow(_stmt, "templateId")
        val _columnIndexOfPosition: Int = getColumnIndexOrThrow(_stmt, "position")
        val _columnIndexOfExerciseId: Int = getColumnIndexOrThrow(_stmt, "exerciseId")
        val _result: MutableList<WorkoutTemplateExerciseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkoutTemplateExerciseEntity
          val _tmpTemplateId: String
          _tmpTemplateId = _stmt.getText(_columnIndexOfTemplateId)
          val _tmpPosition: Int
          _tmpPosition = _stmt.getLong(_columnIndexOfPosition).toInt()
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          _item = WorkoutTemplateExerciseEntity(_tmpTemplateId,_tmpPosition,_tmpExerciseId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeSessions(): Flow<List<WorkoutSessionEntity>> {
    val _sql: String = "SELECT * FROM workout_session ORDER BY startedAtMillis DESC"
    return createFlow(__db, false, arrayOf("workout_session")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfState: Int = getColumnIndexOrThrow(_stmt, "state")
        val _columnIndexOfStartedAtMillis: Int = getColumnIndexOrThrow(_stmt, "startedAtMillis")
        val _columnIndexOfPausedAtMillis: Int = getColumnIndexOrThrow(_stmt, "pausedAtMillis")
        val _columnIndexOfAccumulatedPauseMillis: Int = getColumnIndexOrThrow(_stmt, "accumulatedPauseMillis")
        val _columnIndexOfCompletedAtMillis: Int = getColumnIndexOrThrow(_stmt, "completedAtMillis")
        val _result: MutableList<WorkoutSessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkoutSessionEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpState: String
          _tmpState = _stmt.getText(_columnIndexOfState)
          val _tmpStartedAtMillis: Long
          _tmpStartedAtMillis = _stmt.getLong(_columnIndexOfStartedAtMillis)
          val _tmpPausedAtMillis: Long?
          if (_stmt.isNull(_columnIndexOfPausedAtMillis)) {
            _tmpPausedAtMillis = null
          } else {
            _tmpPausedAtMillis = _stmt.getLong(_columnIndexOfPausedAtMillis)
          }
          val _tmpAccumulatedPauseMillis: Long
          _tmpAccumulatedPauseMillis = _stmt.getLong(_columnIndexOfAccumulatedPauseMillis)
          val _tmpCompletedAtMillis: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAtMillis)) {
            _tmpCompletedAtMillis = null
          } else {
            _tmpCompletedAtMillis = _stmt.getLong(_columnIndexOfCompletedAtMillis)
          }
          _item = WorkoutSessionEntity(_tmpId,_tmpName,_tmpLocalDate,_tmpState,_tmpStartedAtMillis,_tmpPausedAtMillis,_tmpAccumulatedPauseMillis,_tmpCompletedAtMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun sets(id: String): List<WorkoutSetEntity> {
    val _sql: String = "SELECT * FROM workout_set WHERE sessionId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfExerciseId: Int = getColumnIndexOrThrow(_stmt, "exerciseId")
        val _columnIndexOfExerciseName: Int = getColumnIndexOrThrow(_stmt, "exerciseName")
        val _columnIndexOfReps: Int = getColumnIndexOrThrow(_stmt, "reps")
        val _columnIndexOfWeightKilograms: Int = getColumnIndexOrThrow(_stmt, "weightKilograms")
        val _columnIndexOfDurationSeconds: Int = getColumnIndexOrThrow(_stmt, "durationSeconds")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _result: MutableList<WorkoutSetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkoutSetEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSessionId: String
          _tmpSessionId = _stmt.getText(_columnIndexOfSessionId)
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          val _tmpExerciseName: String
          _tmpExerciseName = _stmt.getText(_columnIndexOfExerciseName)
          val _tmpReps: Int?
          if (_stmt.isNull(_columnIndexOfReps)) {
            _tmpReps = null
          } else {
            _tmpReps = _stmt.getLong(_columnIndexOfReps).toInt()
          }
          val _tmpWeightKilograms: Double?
          if (_stmt.isNull(_columnIndexOfWeightKilograms)) {
            _tmpWeightKilograms = null
          } else {
            _tmpWeightKilograms = _stmt.getDouble(_columnIndexOfWeightKilograms)
          }
          val _tmpDurationSeconds: Int?
          if (_stmt.isNull(_columnIndexOfDurationSeconds)) {
            _tmpDurationSeconds = null
          } else {
            _tmpDurationSeconds = _stmt.getLong(_columnIndexOfDurationSeconds).toInt()
          }
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          _item = WorkoutSetEntity(_tmpId,_tmpSessionId,_tmpExerciseId,_tmpExerciseName,_tmpReps,_tmpWeightKilograms,_tmpDurationSeconds,_tmpCompleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActivity(date: String): Flow<List<ActivityEntity>> {
    val _sql: String = "SELECT * FROM activity_entry WHERE localDate = ? AND deleted = 0"
    return createFlow(__db, false, arrayOf("activity_entry")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfSourceLabel: Int = getColumnIndexOrThrow(_stmt, "sourceLabel")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<ActivityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpKind: String
          _tmpKind = _stmt.getText(_columnIndexOfKind)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          val _tmpNote: String?
          if (_stmt.isNull(_columnIndexOfNote)) {
            _tmpNote = null
          } else {
            _tmpNote = _stmt.getText(_columnIndexOfNote)
          }
          val _tmpSourceLabel: String
          _tmpSourceLabel = _stmt.getText(_columnIndexOfSourceLabel)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = ActivityEntity(_tmpId,_tmpLocalDate,_tmpKind,_tmpDurationMinutes,_tmpNote,_tmpSourceLabel,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeSteps(start: String, end: String): Flow<List<StepDayEntity>> {
    val _sql: String = "SELECT * FROM step_day WHERE localDate >= ? AND localDate <= ?"
    return createFlow(__db, false, arrayOf("step_day")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, start)
        _argIndex = 2
        _stmt.bindText(_argIndex, end)
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHealthConnectSteps: Int = getColumnIndexOrThrow(_stmt, "healthConnectSteps")
        val _columnIndexOfManualSteps: Int = getColumnIndexOrThrow(_stmt, "manualSteps")
        val _columnIndexOfManualOverridesHealthConnect: Int = getColumnIndexOrThrow(_stmt, "manualOverridesHealthConnect")
        val _result: MutableList<StepDayEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StepDayEntity
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpHealthConnectSteps: Long?
          if (_stmt.isNull(_columnIndexOfHealthConnectSteps)) {
            _tmpHealthConnectSteps = null
          } else {
            _tmpHealthConnectSteps = _stmt.getLong(_columnIndexOfHealthConnectSteps)
          }
          val _tmpManualSteps: Long?
          if (_stmt.isNull(_columnIndexOfManualSteps)) {
            _tmpManualSteps = null
          } else {
            _tmpManualSteps = _stmt.getLong(_columnIndexOfManualSteps)
          }
          val _tmpManualOverridesHealthConnect: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfManualOverridesHealthConnect).toInt()
          _tmpManualOverridesHealthConnect = _tmp != 0
          _item = StepDayEntity(_tmpLocalDate,_tmpHealthConnectSteps,_tmpManualSteps,_tmpManualOverridesHealthConnect)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun stepDay(date: String): StepDayEntity? {
    val _sql: String = "SELECT * FROM step_day WHERE localDate = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHealthConnectSteps: Int = getColumnIndexOrThrow(_stmt, "healthConnectSteps")
        val _columnIndexOfManualSteps: Int = getColumnIndexOrThrow(_stmt, "manualSteps")
        val _columnIndexOfManualOverridesHealthConnect: Int = getColumnIndexOrThrow(_stmt, "manualOverridesHealthConnect")
        val _result: StepDayEntity?
        if (_stmt.step()) {
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpHealthConnectSteps: Long?
          if (_stmt.isNull(_columnIndexOfHealthConnectSteps)) {
            _tmpHealthConnectSteps = null
          } else {
            _tmpHealthConnectSteps = _stmt.getLong(_columnIndexOfHealthConnectSteps)
          }
          val _tmpManualSteps: Long?
          if (_stmt.isNull(_columnIndexOfManualSteps)) {
            _tmpManualSteps = null
          } else {
            _tmpManualSteps = _stmt.getLong(_columnIndexOfManualSteps)
          }
          val _tmpManualOverridesHealthConnect: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfManualOverridesHealthConnect).toInt()
          _tmpManualOverridesHealthConnect = _tmp != 0
          _result = StepDayEntity(_tmpLocalDate,_tmpHealthConnectSteps,_tmpManualSteps,_tmpManualOverridesHealthConnect)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeWater(date: String): Flow<WaterEntity?> {
    val _sql: String = "SELECT * FROM water_day WHERE localDate = ?"
    return createFlow(__db, false, arrayOf("water_day")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfGlasses: Int = getColumnIndexOrThrow(_stmt, "glasses")
        val _result: WaterEntity?
        if (_stmt.step()) {
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpGlasses: Int
          _tmpGlasses = _stmt.getLong(_columnIndexOfGlasses).toInt()
          _result = WaterEntity(_tmpLocalDate,_tmpGlasses)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun outbox(): List<OutboxEntity> {
    val _sql: String = "SELECT * FROM sync_outbox ORDER BY clientUpdatedAtEpochMillis"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfClientId: Int = getColumnIndexOrThrow(_stmt, "clientId")
        val _columnIndexOfEntityName: Int = getColumnIndexOrThrow(_stmt, "entityName")
        val _columnIndexOfOperation: Int = getColumnIndexOrThrow(_stmt, "operation")
        val _columnIndexOfClientUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "clientUpdatedAtEpochMillis")
        val _result: MutableList<OutboxEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: OutboxEntity
          val _tmpClientId: String
          _tmpClientId = _stmt.getText(_columnIndexOfClientId)
          val _tmpEntityName: String
          _tmpEntityName = _stmt.getText(_columnIndexOfEntityName)
          val _tmpOperation: String
          _tmpOperation = _stmt.getText(_columnIndexOfOperation)
          val _tmpClientUpdatedAtEpochMillis: Long
          _tmpClientUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfClientUpdatedAtEpochMillis)
          _item = OutboxEntity(_tmpClientId,_tmpEntityName,_tmpOperation,_tmpClientUpdatedAtEpochMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countExercises(): Int {
    val _sql: String = "SELECT COUNT(*) FROM exercise"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun allLogs(): List<FoodLogEntity> {
    val _sql: String = "SELECT * FROM food_log_entry"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfMealSlot: Int = getColumnIndexOrThrow(_stmt, "mealSlot")
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "foodId")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "foodName")
        val _columnIndexOfServingLabel: Int = getColumnIndexOrThrow(_stmt, "servingLabel")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfCaloriesKcal: Int = getColumnIndexOrThrow(_stmt, "caloriesKcal")
        val _columnIndexOfProteinGrams: Int = getColumnIndexOrThrow(_stmt, "proteinGrams")
        val _columnIndexOfCarbohydrateGrams: Int = getColumnIndexOrThrow(_stmt, "carbohydrateGrams")
        val _columnIndexOfFatGrams: Int = getColumnIndexOrThrow(_stmt, "fatGrams")
        val _columnIndexOfFiberGrams: Int = getColumnIndexOrThrow(_stmt, "fiberGrams")
        val _columnIndexOfSugarGrams: Int = getColumnIndexOrThrow(_stmt, "sugarGrams")
        val _columnIndexOfSodiumMilligrams: Int = getColumnIndexOrThrow(_stmt, "sodiumMilligrams")
        val _columnIndexOfProvenance: Int = getColumnIndexOrThrow(_stmt, "provenance")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<FoodLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FoodLogEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpMealSlot: String
          _tmpMealSlot = _stmt.getText(_columnIndexOfMealSlot)
          val _tmpFoodId: String?
          if (_stmt.isNull(_columnIndexOfFoodId)) {
            _tmpFoodId = null
          } else {
            _tmpFoodId = _stmt.getText(_columnIndexOfFoodId)
          }
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpServingLabel: String
          _tmpServingLabel = _stmt.getText(_columnIndexOfServingLabel)
          val _tmpQuantity: Double
          _tmpQuantity = _stmt.getDouble(_columnIndexOfQuantity)
          val _tmpCaloriesKcal: Double
          _tmpCaloriesKcal = _stmt.getDouble(_columnIndexOfCaloriesKcal)
          val _tmpProteinGrams: Double
          _tmpProteinGrams = _stmt.getDouble(_columnIndexOfProteinGrams)
          val _tmpCarbohydrateGrams: Double
          _tmpCarbohydrateGrams = _stmt.getDouble(_columnIndexOfCarbohydrateGrams)
          val _tmpFatGrams: Double
          _tmpFatGrams = _stmt.getDouble(_columnIndexOfFatGrams)
          val _tmpFiberGrams: Double?
          if (_stmt.isNull(_columnIndexOfFiberGrams)) {
            _tmpFiberGrams = null
          } else {
            _tmpFiberGrams = _stmt.getDouble(_columnIndexOfFiberGrams)
          }
          val _tmpSugarGrams: Double?
          if (_stmt.isNull(_columnIndexOfSugarGrams)) {
            _tmpSugarGrams = null
          } else {
            _tmpSugarGrams = _stmt.getDouble(_columnIndexOfSugarGrams)
          }
          val _tmpSodiumMilligrams: Double?
          if (_stmt.isNull(_columnIndexOfSodiumMilligrams)) {
            _tmpSodiumMilligrams = null
          } else {
            _tmpSodiumMilligrams = _stmt.getDouble(_columnIndexOfSodiumMilligrams)
          }
          val _tmpProvenance: String
          _tmpProvenance = _stmt.getText(_columnIndexOfProvenance)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = FoodLogEntity(_tmpId,_tmpLocalDate,_tmpMealSlot,_tmpFoodId,_tmpFoodName,_tmpServingLabel,_tmpQuantity,_tmpCaloriesKcal,_tmpProteinGrams,_tmpCarbohydrateGrams,_tmpFatGrams,_tmpFiberGrams,_tmpSugarGrams,_tmpSodiumMilligrams,_tmpProvenance,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun allWeights(): List<WeightEntity> {
    val _sql: String = "SELECT * FROM weight_entry"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfKilograms: Int = getColumnIndexOrThrow(_stmt, "kilograms")
        val _columnIndexOfUpdatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMillis")
        val _columnIndexOfDeleted: Int = getColumnIndexOrThrow(_stmt, "deleted")
        val _result: MutableList<WeightEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WeightEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLocalDate: String
          _tmpLocalDate = _stmt.getText(_columnIndexOfLocalDate)
          val _tmpKilograms: Double
          _tmpKilograms = _stmt.getDouble(_columnIndexOfKilograms)
          val _tmpUpdatedAtEpochMillis: Long
          _tmpUpdatedAtEpochMillis = _stmt.getLong(_columnIndexOfUpdatedAtEpochMillis)
          val _tmpDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDeleted).toInt()
          _tmpDeleted = _tmp != 0
          _item = WeightEntity(_tmpId,_tmpLocalDate,_tmpKilograms,_tmpUpdatedAtEpochMillis,_tmpDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteServings(foodId: String) {
    val _sql: String = "DELETE FROM food_serving WHERE foodId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, foodId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteLog(id: String, updated: Long) {
    val _sql: String = "UPDATE food_log_entry SET deleted = 1, updatedAtEpochMillis = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updated)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteRecipeItems(id: String) {
    val _sql: String = "DELETE FROM recipe_item WHERE recipeId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteRecipe(id: String, updated: Long) {
    val _sql: String = "UPDATE recipe SET deleted = 1, updatedAtEpochMillis = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updated)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteMealItems(id: String) {
    val _sql: String = "DELETE FROM meal_template_item WHERE templateId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeletePhoto(id: String) {
    val _sql: String = "UPDATE progress_photo SET deleted = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTemplateExercises(id: String) {
    val _sql: String = "DELETE FROM workout_template_exercise WHERE templateId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSets(id: String) {
    val _sql: String = "DELETE FROM workout_set WHERE sessionId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteOutbox(ids: List<String>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("DELETE FROM sync_outbox WHERE clientId IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: String in ids) {
          _stmt.bindText(_argIndex, _item)
          _argIndex++
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredColumnConverters(): List<KClass<*>> = emptyList()

    public fun getRequiredDaoReturnTypeConverters(): List<KClass<*>> = emptyList()
  }
}
