package app.velora.core.model

/**
 * Nutrient amounts for one logged quantity or one serving.
 * Null fiber, sugar, or sodium means the source did not provide that value.
 * Callers must not treat null as zero.
 */
data class Nutrients(
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double?,
    val sugarGrams: Double?,
    val sodiumMilligrams: Double?,
    val provenance: NutrientProvenance,
) {
    init {
        require(caloriesKcal >= 0.0)
        require(proteinGrams >= 0.0)
        require(carbohydrateGrams >= 0.0)
        require(fatGrams >= 0.0)
        fiberGrams?.let { require(it >= 0.0) }
        sugarGrams?.let { require(it >= 0.0) }
        sodiumMilligrams?.let { require(it >= 0.0) }
    }
}

fun Nutrients.scaled(quantity: Double): Nutrients {
    require(quantity > 0.0) { "Quantity must be greater than zero." }
    return copy(
        caloriesKcal = caloriesKcal * quantity,
        proteinGrams = proteinGrams * quantity,
        carbohydrateGrams = carbohydrateGrams * quantity,
        fatGrams = fatGrams * quantity,
        fiberGrams = fiberGrams?.times(quantity),
        sugarGrams = sugarGrams?.times(quantity),
        sodiumMilligrams = sodiumMilligrams?.times(quantity),
    )
}

fun List<Nutrients>.summed(): Nutrients {
    if (isEmpty()) {
        return Nutrients(
            caloriesKcal = 0.0,
            proteinGrams = 0.0,
            carbohydrateGrams = 0.0,
            fatGrams = 0.0,
            fiberGrams = null,
            sugarGrams = null,
            sodiumMilligrams = null,
            provenance = NutrientProvenance.VERIFIED_REFERENCE,
        )
    }
    return reduce { acc, next -> acc + next }
}

operator fun Nutrients.plus(other: Nutrients): Nutrients = Nutrients(
    caloriesKcal = caloriesKcal + other.caloriesKcal,
    proteinGrams = proteinGrams + other.proteinGrams,
    carbohydrateGrams = carbohydrateGrams + other.carbohydrateGrams,
    fatGrams = fatGrams + other.fatGrams,
    fiberGrams = combineOptional(fiberGrams, other.fiberGrams),
    sugarGrams = combineOptional(sugarGrams, other.sugarGrams),
    sodiumMilligrams = combineOptional(sodiumMilligrams, other.sodiumMilligrams),
    provenance = leastCertain(provenance, other.provenance),
)

/**
 * If either side lacks a micronutrient, the total is unknown.
 * Substituting zero would publish a number the source did not support.
 */
private fun combineOptional(left: Double?, right: Double?): Double? =
    if (left == null || right == null) null else left + right

private fun leastCertain(left: NutrientProvenance, right: NutrientProvenance): NutrientProvenance =
    when {
        left == NutrientProvenance.ESTIMATED || right == NutrientProvenance.ESTIMATED ->
            NutrientProvenance.ESTIMATED
        left == NutrientProvenance.USER_ENTERED || right == NutrientProvenance.USER_ENTERED ->
            NutrientProvenance.USER_ENTERED
        else -> NutrientProvenance.VERIFIED_REFERENCE
    }
