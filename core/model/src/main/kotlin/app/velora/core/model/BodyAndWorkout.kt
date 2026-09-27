package app.velora.core.model

data class Exercise(
    val id: String,
    val name: String,
    val cue: String,
    val equipment: String,
    val userCreated: Boolean,
)

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val exerciseIds: List<String>,
)

data class WorkoutSetEntry(
    val id: String,
    val exerciseId: String,
    val exerciseName: String,
    val reps: Int?,
    val weightKilograms: Double?,
    val durationSeconds: Int?,
    val completed: Boolean,
)

data class WorkoutSessionRecord(
    val id: String,
    val name: String,
    val localDate: String,
    val timeline: SessionTimeline,
    val sets: List<WorkoutSetEntry>,
)

data class BodyMeasurement(
    val id: String,
    val localDate: String,
    val name: String,
    val centimeters: Double,
)

data class ProgressPhoto(
    val id: String,
    val localDate: String,
    val relativePath: String,
)

data class WaterDay(
    val localDate: String,
    val glasses: Int,
)
