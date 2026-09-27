package app.velora.core.database

import app.velora.core.model.Exercise
import app.velora.core.model.Food
import app.velora.core.model.FoodServing
import app.velora.core.model.NutrientProvenance
import app.velora.core.model.Nutrients
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CatalogNutrients(
    val caloriesKcal: Double? = null,
    val proteinGrams: Double? = null,
    val carbohydrateGrams: Double? = null,
    val fatGrams: Double? = null,
    val fiberGrams: Double? = null,
    val sugarGrams: Double? = null,
    val sodiumMilligrams: Double? = null,
)

@Serializable
data class CatalogRecord(
    val fdcId: Int,
    val description: String,
    val dataType: String? = null,
    val nutrientsPer100g: CatalogNutrients,
)

@Serializable
data class CatalogDocument(
    val records: List<CatalogRecord> = emptyList(),
)

object CatalogParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(text: String): List<Food> {
        val document = json.decodeFromString(CatalogDocument.serializer(), text)
        return document.records.mapNotNull { record ->
            val calories = record.nutrientsPer100g.caloriesKcal ?: return@mapNotNull null
            val protein = record.nutrientsPer100g.proteinGrams ?: return@mapNotNull null
            val carbs = record.nutrientsPer100g.carbohydrateGrams ?: return@mapNotNull null
            val fat = record.nutrientsPer100g.fatGrams ?: return@mapNotNull null
            val nutrients = Nutrients(
                caloriesKcal = calories,
                proteinGrams = protein,
                carbohydrateGrams = carbs,
                fatGrams = fat,
                fiberGrams = record.nutrientsPer100g.fiberGrams,
                sugarGrams = record.nutrientsPer100g.sugarGrams,
                sodiumMilligrams = record.nutrientsPer100g.sodiumMilligrams,
                provenance = NutrientProvenance.VERIFIED_REFERENCE,
            )
            Food(
                id = "usda:${record.fdcId}",
                name = record.description,
                brand = null,
                barcode = null,
                servings = listOf(FoodServing("usda:${record.fdcId}:100g", "100 g", 100.0, nutrients)),
                provenance = NutrientProvenance.VERIFIED_REFERENCE,
                sourceName = "USDA FoodData Central",
                sourceRecordId = record.fdcId.toString(),
                userCreated = false,
            )
        }
    }
}

object ExerciseLibrary {
    val exercises: List<Exercise> = listOf(
        exercise("squat", "Squat", "Stand, sit the hips back, and stand again. Knees track over the mid-foot.", "Bodyweight or rack"),
        exercise("hinge", "Hip hinge", "Push the hips back with a flat back and stand by driving the hips forward.", "Bodyweight or dumbbell"),
        exercise("lunge", "Split squat", "Rear knee lowers toward the floor while the front heel stays down.", "Bodyweight"),
        exercise("pushup", "Push-up", "Body in one line. Lower the chest, then press the floor away.", "Floor"),
        exercise("row", "One-arm row", "Support one hand, pull the weight toward the hip, and lower with control.", "Dumbbell"),
        exercise("press", "Overhead press", "Press the weight overhead without leaning back hard. Lower to the shoulders.", "Dumbbell"),
        exercise("carry", "Farmer carry", "Walk tall while holding a weight in each hand.", "Dumbbell"),
        exercise("plank", "Plank", "Forearms under shoulders, ribs down, hold a straight line.", "Floor"),
        exercise("bridge", "Glute bridge", "Drive through the heels and pause with hips level.", "Floor"),
        exercise("curl", "Arm curl", "Elbow stays near the side. Lower the weight slowly.", "Dumbbell"),
        exercise("raise", "Calf raise", "Rise onto the forefoot and lower the heel under control.", "Step or floor"),
        exercise("deadbug", "Dead bug", "Lower the opposite arm and leg while the lower back stays down.", "Floor"),
        exercise("step", "Step-up", "Place the whole foot on the step and stand without pushing off the back leg.", "Step"),
        exercise("pull", "Band pull-apart", "Pull the band apart until the hands are in line with the shoulders.", "Band"),
        exercise("sit", "Sit to stand", "Stand up from a chair without using the hands, then sit back with control.", "Chair"),
    )

    private fun exercise(id: String, name: String, cue: String, equipment: String) =
        Exercise(id, name, cue, equipment, userCreated = false)
}
