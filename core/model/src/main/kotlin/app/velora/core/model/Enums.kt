package app.velora.core.model

enum class BiologicalSex {
    FEMALE,
    MALE,
    UNSPECIFIED,
}

enum class FitnessGoal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT,
    IMPROVE_FITNESS,
    INCREASE_ACTIVITY,
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    VERY_ACTIVE,
}

enum class MeasurementSystem {
    METRIC,
    IMPERIAL,
}

enum class MealSlot {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}

enum class NutrientProvenance {
    VERIFIED_REFERENCE,
    USER_ENTERED,
    ESTIMATED,
}

enum class ActivityKind {
    WALKING,
    RUNNING,
    CYCLING,
    WORKOUT,
    GENERAL,
}

enum class StepSource {
    HEALTH_CONNECT,
    MANUAL,
}
