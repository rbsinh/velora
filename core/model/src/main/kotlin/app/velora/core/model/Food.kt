package app.velora.core.model

data class FoodServing(
    val id: String,
    val label: String,
    val grams: Double?,
    val nutrients: Nutrients,
)

data class Food(
    val id: String,
    val name: String,
    val brand: String?,
    val barcode: String?,
    val servings: List<FoodServing>,
    val provenance: NutrientProvenance,
    val sourceName: String?,
    val sourceRecordId: String?,
    val userCreated: Boolean,
)

data class FoodLogEntry(
    val id: String,
    val localDate: String,
    val mealSlot: MealSlot,
    val foodId: String?,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val nutrients: Nutrients,
    val updatedAtEpochMillis: Long,
    val deleted: Boolean = false,
)

data class RecipeItem(
    val id: String,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val nutrients: Nutrients,
)

data class Recipe(
    val id: String,
    val name: String,
    val items: List<RecipeItem>,
) {
    val nutrients: Nutrients get() = items.map { it.nutrients }.summed()
}

data class MealTemplateItem(
    val id: String,
    val foodName: String,
    val servingLabel: String,
    val quantity: Double,
    val nutrients: Nutrients,
    val mealSlot: MealSlot,
)

data class MealTemplate(
    val id: String,
    val name: String,
    val items: List<MealTemplateItem>,
)

data class UserProfile(
    val ageYears: Int,
    val sex: BiologicalSex,
    val heightCentimeters: Double,
    val weightKilograms: Double,
    val goal: FitnessGoal,
    val targetWeightKilograms: Double?,
    val activityLevel: ActivityLevel,
    val workoutDaysPerWeek: Int,
    val dietaryNote: String?,
    val measurementSystem: MeasurementSystem,
    val dailyStepGoal: Int,
    val calorieTargetOverride: Int?,
    val proteinTargetOverrideGrams: Int?,
    val carbTargetOverrideGrams: Int?,
    val fatTargetOverrideGrams: Int?,
    val acceptedEstimateDisclaimer: Boolean,
)

data class TargetInputs(
    val ageYears: Int,
    val sex: BiologicalSex,
    val heightCentimeters: Double,
    val weightKilograms: Double,
    val activityLevel: ActivityLevel,
    val goal: FitnessGoal,
    val calorieOverride: Int?,
    val proteinOverrideGrams: Int?,
    val carbOverrideGrams: Int?,
    val fatOverrideGrams: Int?,
)
