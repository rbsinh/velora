package app.velora.core.database

import androidx.room3.InvalidationTracker
import androidx.room3.RoomOpenDelegate
import androidx.room3.migration.AutoMigrationSpec
import androidx.room3.migration.Migration
import androidx.room3.util.TableInfo
import androidx.room3.util.TableInfo.Companion.read
import androidx.room3.util.dropFtsSyncTriggers
import androidx.room3.util.performClear
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room3.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL", "MemberExtensionConflict"])
internal class VeloraDatabase_Impl : VeloraDatabase() {
  private val _veloraDao: Lazy<VeloraDao> = lazy {
    VeloraDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "a5f4de32b65bc3c8d79f8cb5ceac313f", "f79b4eca6459e728538e86c1a81f658c") {
      public override suspend fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` TEXT NOT NULL, `ageYears` INTEGER NOT NULL, `sex` TEXT NOT NULL, `heightCentimeters` REAL NOT NULL, `weightKilograms` REAL NOT NULL, `goal` TEXT NOT NULL, `targetWeightKilograms` REAL, `activityLevel` TEXT NOT NULL, `workoutDaysPerWeek` INTEGER NOT NULL, `dietaryNote` TEXT, `measurementSystem` TEXT NOT NULL, `dailyStepGoal` INTEGER NOT NULL, `calorieTargetOverride` INTEGER, `proteinTargetOverrideGrams` INTEGER, `carbTargetOverrideGrams` INTEGER, `fatTargetOverrideGrams` INTEGER, `acceptedEstimateDisclaimer` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `food` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `brand` TEXT, `barcode` TEXT, `provenance` TEXT NOT NULL, `sourceName` TEXT, `sourceRecordId` TEXT, `userCreated` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_food_name` ON `food` (`name`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_food_barcode` ON `food` (`barcode`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `food_serving` (`id` TEXT NOT NULL, `foodId` TEXT NOT NULL, `label` TEXT NOT NULL, `grams` REAL, `caloriesKcal` REAL NOT NULL, `proteinGrams` REAL NOT NULL, `carbohydrateGrams` REAL NOT NULL, `fatGrams` REAL NOT NULL, `fiberGrams` REAL, `sugarGrams` REAL, `sodiumMilligrams` REAL, `provenance` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_food_serving_foodId` ON `food_serving` (`foodId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `food_log_entry` (`id` TEXT NOT NULL, `localDate` TEXT NOT NULL, `mealSlot` TEXT NOT NULL, `foodId` TEXT, `foodName` TEXT NOT NULL, `servingLabel` TEXT NOT NULL, `quantity` REAL NOT NULL, `caloriesKcal` REAL NOT NULL, `proteinGrams` REAL NOT NULL, `carbohydrateGrams` REAL NOT NULL, `fatGrams` REAL NOT NULL, `fiberGrams` REAL, `sugarGrams` REAL, `sodiumMilligrams` REAL, `provenance` TEXT NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_food_log_entry_localDate` ON `food_log_entry` (`localDate`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `recipe` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `deleted` INTEGER NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `recipe_item` (`id` TEXT NOT NULL, `recipeId` TEXT NOT NULL, `foodName` TEXT NOT NULL, `servingLabel` TEXT NOT NULL, `quantity` REAL NOT NULL, `caloriesKcal` REAL NOT NULL, `proteinGrams` REAL NOT NULL, `carbohydrateGrams` REAL NOT NULL, `fatGrams` REAL NOT NULL, `fiberGrams` REAL, `sugarGrams` REAL, `sodiumMilligrams` REAL, `provenance` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_recipe_item_recipeId` ON `recipe_item` (`recipeId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `meal_template` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `meal_template_item` (`id` TEXT NOT NULL, `templateId` TEXT NOT NULL, `foodName` TEXT NOT NULL, `servingLabel` TEXT NOT NULL, `quantity` REAL NOT NULL, `mealSlot` TEXT NOT NULL, `caloriesKcal` REAL NOT NULL, `proteinGrams` REAL NOT NULL, `carbohydrateGrams` REAL NOT NULL, `fatGrams` REAL NOT NULL, `fiberGrams` REAL, `sugarGrams` REAL, `sodiumMilligrams` REAL, `provenance` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_template_item_templateId` ON `meal_template_item` (`templateId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `weight_entry` (`id` TEXT NOT NULL, `localDate` TEXT NOT NULL, `kilograms` REAL NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_weight_entry_localDate` ON `weight_entry` (`localDate`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `body_measurement` (`id` TEXT NOT NULL, `localDate` TEXT NOT NULL, `name` TEXT NOT NULL, `centimeters` REAL NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `progress_photo` (`id` TEXT NOT NULL, `localDate` TEXT NOT NULL, `relativePath` TEXT NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `exercise` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `cue` TEXT NOT NULL, `equipment` TEXT NOT NULL, `userCreated` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workout_template` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workout_template_exercise` (`templateId` TEXT NOT NULL, `position` INTEGER NOT NULL, `exerciseId` TEXT NOT NULL, PRIMARY KEY(`templateId`, `position`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workout_session` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `localDate` TEXT NOT NULL, `state` TEXT NOT NULL, `startedAtMillis` INTEGER NOT NULL, `pausedAtMillis` INTEGER, `accumulatedPauseMillis` INTEGER NOT NULL, `completedAtMillis` INTEGER, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workout_set` (`id` TEXT NOT NULL, `sessionId` TEXT NOT NULL, `exerciseId` TEXT NOT NULL, `exerciseName` TEXT NOT NULL, `reps` INTEGER, `weightKilograms` REAL, `durationSeconds` INTEGER, `completed` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_set_sessionId` ON `workout_set` (`sessionId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `activity_entry` (`id` TEXT NOT NULL, `localDate` TEXT NOT NULL, `kind` TEXT NOT NULL, `durationMinutes` INTEGER NOT NULL, `note` TEXT, `sourceLabel` TEXT NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_activity_entry_localDate` ON `activity_entry` (`localDate`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `step_day` (`localDate` TEXT NOT NULL, `healthConnectSteps` INTEGER, `manualSteps` INTEGER, `manualOverridesHealthConnect` INTEGER NOT NULL, PRIMARY KEY(`localDate`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `water_day` (`localDate` TEXT NOT NULL, `glasses` INTEGER NOT NULL, PRIMARY KEY(`localDate`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sync_outbox` (`clientId` TEXT NOT NULL, `entityName` TEXT NOT NULL, `operation` TEXT NOT NULL, `clientUpdatedAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`clientId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a5f4de32b65bc3c8d79f8cb5ceac313f')")
      }

      public override suspend fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `user_profile`")
        connection.execSQL("DROP TABLE IF EXISTS `food`")
        connection.execSQL("DROP TABLE IF EXISTS `food_serving`")
        connection.execSQL("DROP TABLE IF EXISTS `food_log_entry`")
        connection.execSQL("DROP TABLE IF EXISTS `recipe`")
        connection.execSQL("DROP TABLE IF EXISTS `recipe_item`")
        connection.execSQL("DROP TABLE IF EXISTS `meal_template`")
        connection.execSQL("DROP TABLE IF EXISTS `meal_template_item`")
        connection.execSQL("DROP TABLE IF EXISTS `weight_entry`")
        connection.execSQL("DROP TABLE IF EXISTS `body_measurement`")
        connection.execSQL("DROP TABLE IF EXISTS `progress_photo`")
        connection.execSQL("DROP TABLE IF EXISTS `exercise`")
        connection.execSQL("DROP TABLE IF EXISTS `workout_template`")
        connection.execSQL("DROP TABLE IF EXISTS `workout_template_exercise`")
        connection.execSQL("DROP TABLE IF EXISTS `workout_session`")
        connection.execSQL("DROP TABLE IF EXISTS `workout_set`")
        connection.execSQL("DROP TABLE IF EXISTS `activity_entry`")
        connection.execSQL("DROP TABLE IF EXISTS `step_day`")
        connection.execSQL("DROP TABLE IF EXISTS `water_day`")
        connection.execSQL("DROP TABLE IF EXISTS `sync_outbox`")
      }

      public override suspend fun onCreate(connection: SQLiteConnection) {
      }

      public override suspend fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override suspend fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override suspend fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override suspend fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsUserProfile: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserProfile.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("ageYears", TableInfo.Column("ageYears", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("sex", TableInfo.Column("sex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("heightCentimeters", TableInfo.Column("heightCentimeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("weightKilograms", TableInfo.Column("weightKilograms", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("goal", TableInfo.Column("goal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("targetWeightKilograms", TableInfo.Column("targetWeightKilograms", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("activityLevel", TableInfo.Column("activityLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("workoutDaysPerWeek", TableInfo.Column("workoutDaysPerWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("dietaryNote", TableInfo.Column("dietaryNote", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("measurementSystem", TableInfo.Column("measurementSystem", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("dailyStepGoal", TableInfo.Column("dailyStepGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("calorieTargetOverride", TableInfo.Column("calorieTargetOverride", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("proteinTargetOverrideGrams", TableInfo.Column("proteinTargetOverrideGrams", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("carbTargetOverrideGrams", TableInfo.Column("carbTargetOverrideGrams", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("fatTargetOverrideGrams", TableInfo.Column("fatTargetOverrideGrams", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("acceptedEstimateDisclaimer", TableInfo.Column("acceptedEstimateDisclaimer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserProfile: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserProfile: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserProfile: TableInfo = TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile)
        val _existingUserProfile: TableInfo = read(connection, "user_profile")
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_profile(app.velora.core.database.UserProfileEntity).
              | Expected:
              |""".trimMargin() + _infoUserProfile + """
              |
              | Found:
              |""".trimMargin() + _existingUserProfile)
        }
        val _columnsFood: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFood.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("brand", TableInfo.Column("brand", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("barcode", TableInfo.Column("barcode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("provenance", TableInfo.Column("provenance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("sourceName", TableInfo.Column("sourceName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("sourceRecordId", TableInfo.Column("sourceRecordId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("userCreated", TableInfo.Column("userCreated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFood.put("updatedAtEpochMillis", TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFood: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFood: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFood.add(TableInfo.Index("index_food_name", false, listOf("name"), listOf("ASC")))
        _indicesFood.add(TableInfo.Index("index_food_barcode", false, listOf("barcode"), listOf("ASC")))
        val _infoFood: TableInfo = TableInfo("food", _columnsFood, _foreignKeysFood, _indicesFood)
        val _existingFood: TableInfo = read(connection, "food")
        if (!_infoFood.equals(_existingFood)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |food(app.velora.core.database.FoodEntity).
              | Expected:
              |""".trimMargin() + _infoFood + """
              |
              | Found:
              |""".trimMargin() + _existingFood)
        }
        val _columnsFoodServing: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFoodServing.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("foodId", TableInfo.Column("foodId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("label", TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("grams", TableInfo.Column("grams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("caloriesKcal", TableInfo.Column("caloriesKcal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("proteinGrams", TableInfo.Column("proteinGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("carbohydrateGrams", TableInfo.Column("carbohydrateGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("fatGrams", TableInfo.Column("fatGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("fiberGrams", TableInfo.Column("fiberGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("sugarGrams", TableInfo.Column("sugarGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("sodiumMilligrams", TableInfo.Column("sodiumMilligrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodServing.put("provenance", TableInfo.Column("provenance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFoodServing: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFoodServing: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFoodServing.add(TableInfo.Index("index_food_serving_foodId", false, listOf("foodId"), listOf("ASC")))
        val _infoFoodServing: TableInfo = TableInfo("food_serving", _columnsFoodServing, _foreignKeysFoodServing, _indicesFoodServing)
        val _existingFoodServing: TableInfo = read(connection, "food_serving")
        if (!_infoFoodServing.equals(_existingFoodServing)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |food_serving(app.velora.core.database.FoodServingEntity).
              | Expected:
              |""".trimMargin() + _infoFoodServing + """
              |
              | Found:
              |""".trimMargin() + _existingFoodServing)
        }
        val _columnsFoodLogEntry: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFoodLogEntry.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("mealSlot", TableInfo.Column("mealSlot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("foodId", TableInfo.Column("foodId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("foodName", TableInfo.Column("foodName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("servingLabel", TableInfo.Column("servingLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("quantity", TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("caloriesKcal", TableInfo.Column("caloriesKcal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("proteinGrams", TableInfo.Column("proteinGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("carbohydrateGrams", TableInfo.Column("carbohydrateGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("fatGrams", TableInfo.Column("fatGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("fiberGrams", TableInfo.Column("fiberGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("sugarGrams", TableInfo.Column("sugarGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("sodiumMilligrams", TableInfo.Column("sodiumMilligrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("provenance", TableInfo.Column("provenance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("updatedAtEpochMillis", TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFoodLogEntry.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFoodLogEntry: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFoodLogEntry: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFoodLogEntry.add(TableInfo.Index("index_food_log_entry_localDate", false, listOf("localDate"), listOf("ASC")))
        val _infoFoodLogEntry: TableInfo = TableInfo("food_log_entry", _columnsFoodLogEntry, _foreignKeysFoodLogEntry, _indicesFoodLogEntry)
        val _existingFoodLogEntry: TableInfo = read(connection, "food_log_entry")
        if (!_infoFoodLogEntry.equals(_existingFoodLogEntry)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |food_log_entry(app.velora.core.database.FoodLogEntity).
              | Expected:
              |""".trimMargin() + _infoFoodLogEntry + """
              |
              | Found:
              |""".trimMargin() + _existingFoodLogEntry)
        }
        val _columnsRecipe: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRecipe.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipe.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipe.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipe.put("updatedAtEpochMillis", TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRecipe: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRecipe: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoRecipe: TableInfo = TableInfo("recipe", _columnsRecipe, _foreignKeysRecipe, _indicesRecipe)
        val _existingRecipe: TableInfo = read(connection, "recipe")
        if (!_infoRecipe.equals(_existingRecipe)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |recipe(app.velora.core.database.RecipeEntity).
              | Expected:
              |""".trimMargin() + _infoRecipe + """
              |
              | Found:
              |""".trimMargin() + _existingRecipe)
        }
        val _columnsRecipeItem: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRecipeItem.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("recipeId", TableInfo.Column("recipeId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("foodName", TableInfo.Column("foodName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("servingLabel", TableInfo.Column("servingLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("quantity", TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("caloriesKcal", TableInfo.Column("caloriesKcal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("proteinGrams", TableInfo.Column("proteinGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("carbohydrateGrams", TableInfo.Column("carbohydrateGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("fatGrams", TableInfo.Column("fatGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("fiberGrams", TableInfo.Column("fiberGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("sugarGrams", TableInfo.Column("sugarGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("sodiumMilligrams", TableInfo.Column("sodiumMilligrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecipeItem.put("provenance", TableInfo.Column("provenance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRecipeItem: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRecipeItem: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesRecipeItem.add(TableInfo.Index("index_recipe_item_recipeId", false, listOf("recipeId"), listOf("ASC")))
        val _infoRecipeItem: TableInfo = TableInfo("recipe_item", _columnsRecipeItem, _foreignKeysRecipeItem, _indicesRecipeItem)
        val _existingRecipeItem: TableInfo = read(connection, "recipe_item")
        if (!_infoRecipeItem.equals(_existingRecipeItem)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |recipe_item(app.velora.core.database.RecipeItemEntity).
              | Expected:
              |""".trimMargin() + _infoRecipeItem + """
              |
              | Found:
              |""".trimMargin() + _existingRecipeItem)
        }
        val _columnsMealTemplate: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMealTemplate.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplate.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplate.put("updatedAtEpochMillis", TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplate.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMealTemplate: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMealTemplate: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMealTemplate: TableInfo = TableInfo("meal_template", _columnsMealTemplate, _foreignKeysMealTemplate, _indicesMealTemplate)
        val _existingMealTemplate: TableInfo = read(connection, "meal_template")
        if (!_infoMealTemplate.equals(_existingMealTemplate)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |meal_template(app.velora.core.database.MealTemplateEntity).
              | Expected:
              |""".trimMargin() + _infoMealTemplate + """
              |
              | Found:
              |""".trimMargin() + _existingMealTemplate)
        }
        val _columnsMealTemplateItem: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMealTemplateItem.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("templateId", TableInfo.Column("templateId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("foodName", TableInfo.Column("foodName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("servingLabel", TableInfo.Column("servingLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("quantity", TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("mealSlot", TableInfo.Column("mealSlot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("caloriesKcal", TableInfo.Column("caloriesKcal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("proteinGrams", TableInfo.Column("proteinGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("carbohydrateGrams", TableInfo.Column("carbohydrateGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("fatGrams", TableInfo.Column("fatGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("fiberGrams", TableInfo.Column("fiberGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("sugarGrams", TableInfo.Column("sugarGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("sodiumMilligrams", TableInfo.Column("sodiumMilligrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMealTemplateItem.put("provenance", TableInfo.Column("provenance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMealTemplateItem: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMealTemplateItem: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMealTemplateItem.add(TableInfo.Index("index_meal_template_item_templateId", false, listOf("templateId"), listOf("ASC")))
        val _infoMealTemplateItem: TableInfo = TableInfo("meal_template_item", _columnsMealTemplateItem, _foreignKeysMealTemplateItem, _indicesMealTemplateItem)
        val _existingMealTemplateItem: TableInfo = read(connection, "meal_template_item")
        if (!_infoMealTemplateItem.equals(_existingMealTemplateItem)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |meal_template_item(app.velora.core.database.MealTemplateItemEntity).
              | Expected:
              |""".trimMargin() + _infoMealTemplateItem + """
              |
              | Found:
              |""".trimMargin() + _existingMealTemplateItem)
        }
        val _columnsWeightEntry: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWeightEntry.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeightEntry.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeightEntry.put("kilograms", TableInfo.Column("kilograms", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeightEntry.put("updatedAtEpochMillis", TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeightEntry.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWeightEntry: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWeightEntry: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesWeightEntry.add(TableInfo.Index("index_weight_entry_localDate", false, listOf("localDate"), listOf("ASC")))
        val _infoWeightEntry: TableInfo = TableInfo("weight_entry", _columnsWeightEntry, _foreignKeysWeightEntry, _indicesWeightEntry)
        val _existingWeightEntry: TableInfo = read(connection, "weight_entry")
        if (!_infoWeightEntry.equals(_existingWeightEntry)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |weight_entry(app.velora.core.database.WeightEntity).
              | Expected:
              |""".trimMargin() + _infoWeightEntry + """
              |
              | Found:
              |""".trimMargin() + _existingWeightEntry)
        }
        val _columnsBodyMeasurement: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBodyMeasurement.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBodyMeasurement.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBodyMeasurement.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBodyMeasurement.put("centimeters", TableInfo.Column("centimeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBodyMeasurement.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBodyMeasurement: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBodyMeasurement: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoBodyMeasurement: TableInfo = TableInfo("body_measurement", _columnsBodyMeasurement, _foreignKeysBodyMeasurement, _indicesBodyMeasurement)
        val _existingBodyMeasurement: TableInfo = read(connection, "body_measurement")
        if (!_infoBodyMeasurement.equals(_existingBodyMeasurement)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |body_measurement(app.velora.core.database.MeasurementEntity).
              | Expected:
              |""".trimMargin() + _infoBodyMeasurement + """
              |
              | Found:
              |""".trimMargin() + _existingBodyMeasurement)
        }
        val _columnsProgressPhoto: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProgressPhoto.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProgressPhoto.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProgressPhoto.put("relativePath", TableInfo.Column("relativePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProgressPhoto.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProgressPhoto: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProgressPhoto: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProgressPhoto: TableInfo = TableInfo("progress_photo", _columnsProgressPhoto, _foreignKeysProgressPhoto, _indicesProgressPhoto)
        val _existingProgressPhoto: TableInfo = read(connection, "progress_photo")
        if (!_infoProgressPhoto.equals(_existingProgressPhoto)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |progress_photo(app.velora.core.database.PhotoEntity).
              | Expected:
              |""".trimMargin() + _infoProgressPhoto + """
              |
              | Found:
              |""".trimMargin() + _existingProgressPhoto)
        }
        val _columnsExercise: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExercise.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExercise.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExercise.put("cue", TableInfo.Column("cue", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExercise.put("equipment", TableInfo.Column("equipment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExercise.put("userCreated", TableInfo.Column("userCreated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExercise: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesExercise: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoExercise: TableInfo = TableInfo("exercise", _columnsExercise, _foreignKeysExercise, _indicesExercise)
        val _existingExercise: TableInfo = read(connection, "exercise")
        if (!_infoExercise.equals(_existingExercise)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |exercise(app.velora.core.database.ExerciseEntity).
              | Expected:
              |""".trimMargin() + _infoExercise + """
              |
              | Found:
              |""".trimMargin() + _existingExercise)
        }
        val _columnsWorkoutTemplate: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkoutTemplate.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutTemplate.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutTemplate.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkoutTemplate: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWorkoutTemplate: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoWorkoutTemplate: TableInfo = TableInfo("workout_template", _columnsWorkoutTemplate, _foreignKeysWorkoutTemplate, _indicesWorkoutTemplate)
        val _existingWorkoutTemplate: TableInfo = read(connection, "workout_template")
        if (!_infoWorkoutTemplate.equals(_existingWorkoutTemplate)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workout_template(app.velora.core.database.WorkoutTemplateEntity).
              | Expected:
              |""".trimMargin() + _infoWorkoutTemplate + """
              |
              | Found:
              |""".trimMargin() + _existingWorkoutTemplate)
        }
        val _columnsWorkoutTemplateExercise: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkoutTemplateExercise.put("templateId", TableInfo.Column("templateId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutTemplateExercise.put("position", TableInfo.Column("position", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutTemplateExercise.put("exerciseId", TableInfo.Column("exerciseId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkoutTemplateExercise: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWorkoutTemplateExercise: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoWorkoutTemplateExercise: TableInfo = TableInfo("workout_template_exercise", _columnsWorkoutTemplateExercise, _foreignKeysWorkoutTemplateExercise, _indicesWorkoutTemplateExercise)
        val _existingWorkoutTemplateExercise: TableInfo = read(connection, "workout_template_exercise")
        if (!_infoWorkoutTemplateExercise.equals(_existingWorkoutTemplateExercise)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workout_template_exercise(app.velora.core.database.WorkoutTemplateExerciseEntity).
              | Expected:
              |""".trimMargin() + _infoWorkoutTemplateExercise + """
              |
              | Found:
              |""".trimMargin() + _existingWorkoutTemplateExercise)
        }
        val _columnsWorkoutSession: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkoutSession.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("state", TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("startedAtMillis", TableInfo.Column("startedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("pausedAtMillis", TableInfo.Column("pausedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("accumulatedPauseMillis", TableInfo.Column("accumulatedPauseMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSession.put("completedAtMillis", TableInfo.Column("completedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkoutSession: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWorkoutSession: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoWorkoutSession: TableInfo = TableInfo("workout_session", _columnsWorkoutSession, _foreignKeysWorkoutSession, _indicesWorkoutSession)
        val _existingWorkoutSession: TableInfo = read(connection, "workout_session")
        if (!_infoWorkoutSession.equals(_existingWorkoutSession)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workout_session(app.velora.core.database.WorkoutSessionEntity).
              | Expected:
              |""".trimMargin() + _infoWorkoutSession + """
              |
              | Found:
              |""".trimMargin() + _existingWorkoutSession)
        }
        val _columnsWorkoutSet: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkoutSet.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("sessionId", TableInfo.Column("sessionId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("exerciseId", TableInfo.Column("exerciseId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("exerciseName", TableInfo.Column("exerciseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("reps", TableInfo.Column("reps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("weightKilograms", TableInfo.Column("weightKilograms", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("durationSeconds", TableInfo.Column("durationSeconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkoutSet.put("completed", TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkoutSet: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWorkoutSet: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesWorkoutSet.add(TableInfo.Index("index_workout_set_sessionId", false, listOf("sessionId"), listOf("ASC")))
        val _infoWorkoutSet: TableInfo = TableInfo("workout_set", _columnsWorkoutSet, _foreignKeysWorkoutSet, _indicesWorkoutSet)
        val _existingWorkoutSet: TableInfo = read(connection, "workout_set")
        if (!_infoWorkoutSet.equals(_existingWorkoutSet)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workout_set(app.velora.core.database.WorkoutSetEntity).
              | Expected:
              |""".trimMargin() + _infoWorkoutSet + """
              |
              | Found:
              |""".trimMargin() + _existingWorkoutSet)
        }
        val _columnsActivityEntry: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsActivityEntry.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("localDate", TableInfo.Column("localDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("kind", TableInfo.Column("kind", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("durationMinutes", TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("note", TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("sourceLabel", TableInfo.Column("sourceLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityEntry.put("deleted", TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysActivityEntry: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesActivityEntry: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesActivityEntry.add(TableInfo.Index("index_activity_entry_localDate", false, listOf("localDate"), listOf("ASC")))
        val _infoActivityEntry: TableInfo = TableInfo("activity_entry", _columnsActivityEntry, _foreignKeysActivityEntry, _indicesActivityEntry)
        val _existingActivityEntry: TableInfo = read(connection, "activity_entry")
        if (!_infoActivityEntry.equals(_existingActivityEntry)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |activity_entry(app.velora.core.database.ActivityEntity).
              | Expected:
              |""".trimMargin() + _infoActivityEntry + """
              |
              | Found:
              |""".trimMargin() + _existingActivityEntry)
        }
        val _columnsStepDay: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStepDay.put("localDate", TableInfo.Column("localDate", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStepDay.put("healthConnectSteps", TableInfo.Column("healthConnectSteps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStepDay.put("manualSteps", TableInfo.Column("manualSteps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStepDay.put("manualOverridesHealthConnect", TableInfo.Column("manualOverridesHealthConnect", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStepDay: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStepDay: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoStepDay: TableInfo = TableInfo("step_day", _columnsStepDay, _foreignKeysStepDay, _indicesStepDay)
        val _existingStepDay: TableInfo = read(connection, "step_day")
        if (!_infoStepDay.equals(_existingStepDay)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |step_day(app.velora.core.database.StepDayEntity).
              | Expected:
              |""".trimMargin() + _infoStepDay + """
              |
              | Found:
              |""".trimMargin() + _existingStepDay)
        }
        val _columnsWaterDay: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWaterDay.put("localDate", TableInfo.Column("localDate", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWaterDay.put("glasses", TableInfo.Column("glasses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWaterDay: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWaterDay: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoWaterDay: TableInfo = TableInfo("water_day", _columnsWaterDay, _foreignKeysWaterDay, _indicesWaterDay)
        val _existingWaterDay: TableInfo = read(connection, "water_day")
        if (!_infoWaterDay.equals(_existingWaterDay)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |water_day(app.velora.core.database.WaterEntity).
              | Expected:
              |""".trimMargin() + _infoWaterDay + """
              |
              | Found:
              |""".trimMargin() + _existingWaterDay)
        }
        val _columnsSyncOutbox: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSyncOutbox.put("clientId", TableInfo.Column("clientId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncOutbox.put("entityName", TableInfo.Column("entityName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncOutbox.put("operation", TableInfo.Column("operation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncOutbox.put("clientUpdatedAtEpochMillis", TableInfo.Column("clientUpdatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSyncOutbox: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSyncOutbox: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSyncOutbox: TableInfo = TableInfo("sync_outbox", _columnsSyncOutbox, _foreignKeysSyncOutbox, _indicesSyncOutbox)
        val _existingSyncOutbox: TableInfo = read(connection, "sync_outbox")
        if (!_infoSyncOutbox.equals(_existingSyncOutbox)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sync_outbox(app.velora.core.database.OutboxEntity).
              | Expected:
              |""".trimMargin() + _infoSyncOutbox + """
              |
              | Found:
              |""".trimMargin() + _existingSyncOutbox)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_profile", "food", "food_serving", "food_log_entry", "recipe", "recipe_item", "meal_template", "meal_template_item", "weight_entry", "body_measurement", "progress_photo", "exercise", "workout_template", "workout_template_exercise", "workout_session", "workout_set", "activity_entry", "step_day", "water_day", "sync_outbox")
  }

  public override suspend fun clearAllTables() {
    performClear(this, false, "user_profile", "food", "food_serving", "food_log_entry", "recipe", "recipe_item", "meal_template", "meal_template_item", "weight_entry", "body_measurement", "progress_photo", "exercise", "workout_template", "workout_template_exercise", "workout_session", "workout_set", "activity_entry", "step_day", "water_day", "sync_outbox")
  }

  protected override fun getRequiredColumnTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _columnTypeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _columnTypeConvertersMap.put(VeloraDao::class, VeloraDao_Impl.getRequiredColumnConverters())
    return _columnTypeConvertersMap
  }

  protected override fun getRequiredDaoReturnTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _daoReturnTypeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _daoReturnTypeConvertersMap.put(VeloraDao::class, VeloraDao_Impl.getRequiredDaoReturnTypeConverters())
    return _daoReturnTypeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun dao(): VeloraDao = _veloraDao.value
}
