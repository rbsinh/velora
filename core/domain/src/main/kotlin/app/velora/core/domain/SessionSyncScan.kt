package app.velora.core.domain

import app.velora.core.model.FoodCandidate
import app.velora.core.model.FoodRecognitionResult
import app.velora.core.model.PremiumEntitlement
import app.velora.core.model.SessionState
import app.velora.core.model.SessionTimeline
import app.velora.core.model.SetRecord
import app.velora.core.model.StorePurchaseSnapshot
import app.velora.core.model.StorePurchaseState
import app.velora.core.model.SyncMutation
import app.velora.core.model.SyncPushResult
import app.velora.core.model.SyncStep
import app.velora.core.model.VersionedRecord

object WorkoutSessionEngine {
    fun start(nowMillis: Long): SessionTimeline = SessionTimeline(
        state = SessionState.IN_PROGRESS,
        startedAtMillis = nowMillis,
        pausedAtMillis = null,
        accumulatedPauseMillis = 0,
        completedAtMillis = null,
    )

    fun pause(session: SessionTimeline, nowMillis: Long): SessionTimeline {
        require(session.state == SessionState.IN_PROGRESS)
        require(nowMillis >= session.startedAtMillis)
        return session.copy(state = SessionState.PAUSED, pausedAtMillis = nowMillis)
    }

    fun resume(session: SessionTimeline, nowMillis: Long): SessionTimeline {
        require(session.state == SessionState.PAUSED)
        val pausedAt = checkNotNull(session.pausedAtMillis)
        require(nowMillis >= pausedAt)
        return session.copy(
            state = SessionState.IN_PROGRESS,
            pausedAtMillis = null,
            accumulatedPauseMillis = session.accumulatedPauseMillis + (nowMillis - pausedAt),
        )
    }

    fun complete(session: SessionTimeline, nowMillis: Long): SessionTimeline {
        val paused = if (session.state == SessionState.PAUSED) resume(session, nowMillis) else session
        require(paused.state == SessionState.IN_PROGRESS)
        return paused.copy(state = SessionState.COMPLETED, completedAtMillis = nowMillis)
    }

    fun elapsedMillis(session: SessionTimeline, nowMillis: Long): Long {
        val end = when (session.state) {
            SessionState.IN_PROGRESS -> nowMillis
            SessionState.PAUSED -> checkNotNull(session.pausedAtMillis)
            SessionState.COMPLETED -> checkNotNull(session.completedAtMillis)
        }
        return (end - session.startedAtMillis - session.accumulatedPauseMillis).coerceAtLeast(0)
    }
}

object PersonalRecords {
    fun maxWeightByExercise(sets: List<SetRecord>): Map<String, Double> =
        sets.mapNotNull { set -> set.weightKilograms?.let { set.exerciseId to it } }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, weights) -> weights.max() }
}

object ConflictResolver {
    fun resolve(local: VersionedRecord, remote: VersionedRecord): VersionedRecord {
        require(local.id == remote.id)
        val localRevision = local.serverRevision
        val remoteRevision = remote.serverRevision
        if (localRevision != null && remoteRevision != null && localRevision != remoteRevision) {
            return if (localRevision > remoteRevision) local else remote
        }
        if (localRevision != null && remoteRevision == null) return local
        if (remoteRevision != null && localRevision == null) return remote
        if (local.clientUpdatedAtEpochMillis != remote.clientUpdatedAtEpochMillis) {
            return if (local.clientUpdatedAtEpochMillis > remote.clientUpdatedAtEpochMillis) local else remote
        }
        return if (local.deleted || remote.deleted) {
            local.copy(deleted = true)
        } else {
            local
        }
    }
}

class SyncCoordinator(
    private val transport: SyncTransport,
) {
    suspend fun push(pending: List<SyncMutation>): SyncStep {
        if (pending.isEmpty()) return SyncStep.Empty
        return when (val result = transport.push(pending)) {
            SyncPushResult.NotConfigured -> SyncStep.Skipped(pending)
            is SyncPushResult.Success -> SyncStep.Accepted(result.acceptedIds)
            is SyncPushResult.Failure -> SyncStep.Retry(pending, result.message)
        }
    }
}

fun interface SyncTransport {
    suspend fun push(mutations: List<SyncMutation>): SyncPushResult
}

object PremiumEntitlementMapper {
    const val MONTHLY_PRODUCT_ID = "velora_premium_monthly"

    fun map(
        purchases: List<StorePurchaseSnapshot>,
        productId: String = MONTHLY_PRODUCT_ID,
    ): PremiumEntitlement {
        val owned = purchases.any { purchase ->
            productId in purchase.productIds &&
                purchase.state == StorePurchaseState.PURCHASED &&
                purchase.purchaseToken.isNotBlank() &&
                !purchase.suspended
        }
        return if (owned) {
            PremiumEntitlement(premium = true, productId = productId)
        } else {
            PremiumEntitlement(premium = false, productId = null)
        }
    }
}

sealed class ScanState {
    data object Idle : ScanState()
    data class Preview(val imageToken: String) : ScanState()
    data class NeedsUploadConsent(val imageToken: String) : ScanState()
    data class Analyzing(val imageToken: String) : ScanState()
    data class Unavailable(val message: String) : ScanState()
    data class Candidates(val items: List<FoodCandidate>) : ScanState()
    data class Portion(val candidate: FoodCandidate, val grams: Int) : ScanState()
    data class ReadyToLog(val candidate: FoodCandidate, val grams: Int) : ScanState()
    data class Failed(val message: String) : ScanState()

    val canCommit: Boolean get() = this is ReadyToLog
    val shouldDiscardImage: Boolean
        get() = this is Unavailable || this is Failed || this is Candidates || this is Idle
}

sealed class ScanEvent {
    data class PhotoCaptured(val imageToken: String) : ScanEvent()
    data object Analyze : ScanEvent()
    data object ConfirmUpload : ScanEvent()
    data class RecognitionFinished(val result: FoodRecognitionResult) : ScanEvent()
    data class Select(val index: Int) : ScanEvent()
    data class SetGrams(val grams: Int) : ScanEvent()
    data object ConfirmPortion : ScanEvent()
    data object Retake : ScanEvent()
}

/**
 * Recognition never creates a food log by itself. [ScanState.ReadyToLog] is the
 * only state a view model may persist, and only after the user confirms a portion.
 * When no provider is configured the image token is dropped and nothing is uploaded.
 */
class FoodScanCoordinator(
    private val providerConfigured: Boolean,
) {
    fun reduce(state: ScanState, event: ScanEvent): ScanState = when (event) {
        is ScanEvent.PhotoCaptured -> ScanState.Preview(event.imageToken)
        ScanEvent.Retake -> ScanState.Idle
        ScanEvent.Analyze -> when (state) {
            is ScanState.Preview -> if (providerConfigured) {
                ScanState.NeedsUploadConsent(state.imageToken)
            } else {
                ScanState.Unavailable(
                    "No food-recognition provider is configured. The photo was not uploaded and has been discarded.",
                )
            }
            else -> state
        }
        ScanEvent.ConfirmUpload -> when (state) {
            is ScanState.NeedsUploadConsent -> ScanState.Analyzing(state.imageToken)
            else -> state
        }
        is ScanEvent.RecognitionFinished -> when (state) {
            is ScanState.Analyzing -> mapResult(event.result)
            else -> state
        }
        is ScanEvent.Select -> when (state) {
            is ScanState.Candidates -> state.items.getOrNull(event.index)
                ?.let { ScanState.Portion(it, grams = 100) }
                ?: state
            else -> state
        }
        is ScanEvent.SetGrams -> when {
            state is ScanState.Portion && event.grams in 1..5000 -> state.copy(grams = event.grams)
            else -> state
        }
        ScanEvent.ConfirmPortion -> when (state) {
            is ScanState.Portion -> ScanState.ReadyToLog(state.candidate, state.grams)
            else -> state
        }
    }

    private fun mapResult(result: FoodRecognitionResult): ScanState = when (result) {
        is FoodRecognitionResult.Unavailable -> ScanState.Unavailable(result.message)
        is FoodRecognitionResult.Failed -> ScanState.Failed(result.message)
        is FoodRecognitionResult.Candidates -> {
            val valid = result.items.filter { it.label.isNotBlank() && it.confidence in 0.0..1.0 }
            if (valid.isEmpty()) {
                ScanState.Failed("The recognition response did not include a usable match.")
            } else {
                ScanState.Candidates(valid)
            }
        }
    }
}
