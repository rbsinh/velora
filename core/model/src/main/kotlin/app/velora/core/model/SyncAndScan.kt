package app.velora.core.model

data class FoodCandidate(
    val label: String,
    val confidence: Double,
    val foodId: String?,
)

sealed class FoodRecognitionResult {
    data class Unavailable(val message: String) : FoodRecognitionResult()
    data class Candidates(val items: List<FoodCandidate>) : FoodRecognitionResult()
    data class Failed(val message: String, val retryable: Boolean) : FoodRecognitionResult()
}

enum class StorePurchaseState {
    PURCHASED,
    PENDING,
    UNSPECIFIED,
}

data class StorePurchaseSnapshot(
    val productIds: List<String>,
    val purchaseToken: String,
    val state: StorePurchaseState,
    val suspended: Boolean,
)

data class PremiumEntitlement(
    val premium: Boolean,
    val productId: String?,
)

data class SyncMutation(
    val clientId: String,
    val entity: String,
    val operation: String,
    val clientUpdatedAtEpochMillis: Long,
)

data class VersionedRecord(
    val id: String,
    val clientUpdatedAtEpochMillis: Long,
    val serverRevision: Long?,
    val deleted: Boolean,
)

sealed class SyncPushResult {
    data object NotConfigured : SyncPushResult()
    data class Success(val acceptedIds: Set<String>) : SyncPushResult()
    data class Failure(val retryable: Boolean, val message: String) : SyncPushResult()
}

sealed class SyncStep {
    data object Empty : SyncStep()
    data class Skipped(val pending: List<SyncMutation>) : SyncStep()
    data class Accepted(val ids: Set<String>) : SyncStep()
    data class Retry(val pending: List<SyncMutation>, val message: String) : SyncStep()
}
