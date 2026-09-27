package app.velora.core.domain

import app.velora.core.model.Adherence
import app.velora.core.model.FitnessGoal
import app.velora.core.model.FoodCandidate
import app.velora.core.model.FoodRecognitionResult
import app.velora.core.model.SessionState
import app.velora.core.model.SetRecord
import app.velora.core.model.StepDay
import app.velora.core.model.StepSource
import app.velora.core.model.StorePurchaseSnapshot
import app.velora.core.model.StorePurchaseState
import app.velora.core.model.SyncMutation
import app.velora.core.model.SyncPushResult
import app.velora.core.model.SyncStep
import app.velora.core.model.TrendDirection
import app.velora.core.model.VersionedRecord
import app.velora.core.model.WeightPoint
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StepAggregationTest {
    @Test
    fun healthConnectWinsUnlessUserOverrides() {
        val day = StepDay("2026-09-21", healthConnectSteps = 1000, manualSteps = 8000, manualOverridesHealthConnect = false)
        assertEquals(StepSource.HEALTH_CONNECT, StepAggregation.resolve(day).source)
        assertEquals(1000L, StepAggregation.resolve(day).steps)
        val override = day.copy(manualOverridesHealthConnect = true)
        assertEquals(StepSource.MANUAL, StepAggregation.resolve(override).source)
        assertEquals(8000L, StepAggregation.resolve(override).steps)
    }

    @Test
    fun weekAndMonthTotalsSkipMissingDays() {
        val start = LocalDate.of(2026, 9, 21)
        val days = listOf(
            StepDay("2026-09-21", 4000, null, false),
            StepDay("2026-09-23", 6000, null, false),
        )
        val range = StepAggregation.range(days, start, start.plusDays(6))
        assertEquals(10000L, range.totalSteps)
        assertEquals(2, range.reportedDays)
        assertEquals(7, range.days.size)
        assertNull(range.days[1].steps)
        assertEquals(1, range.weeks.size)
        assertEquals("2026-09", range.months.single().label)
    }
}

class WeightTrendTest {
    @Test
    fun onePointIsNotATrend() {
        val trend = WeightTrends.trend(
            listOf(WeightPoint("2026-09-27", 80.0)),
            LocalDate.of(2026, 9, 27),
        )
        assertEquals(TrendDirection.INSUFFICIENT, trend.direction)
        assertNull(trend.weeklyDeltaKilograms)
    }

    @Test
    fun weeklyDeltaUsesAnEarlierPoint() {
        val trend = WeightTrends.trend(
            listOf(
                WeightPoint("2026-08-20", 82.0),
                WeightPoint("2026-09-20", 81.0),
                WeightPoint("2026-09-27", 80.2),
            ),
            LocalDate.of(2026, 9, 27),
        )
        assertEquals(-0.8, trend.weeklyDeltaKilograms!!, 0.001)
        assertEquals(TrendDirection.DOWN, trend.direction)
        assertEquals(-1.8, trend.monthlyDeltaKilograms!!, 0.001)
    }
}

class AdherenceTest {
    @Test
    fun emptyLogIsNotSuccessForWeightLoss() {
        assertEquals(
            Adherence.NOT_LOGGED,
            GoalAdherence.calories(FitnessGoal.LOSE_WEIGHT, consumedKcal = 0.0, targetKcal = 1800),
        )
    }

    @Test
    fun lossMetWhenAtOrUnderTarget() {
        assertEquals(Adherence.MET, GoalAdherence.calories(FitnessGoal.LOSE_WEIGHT, 1800.0, 1800))
        assertEquals(Adherence.MISSED, GoalAdherence.calories(FitnessGoal.LOSE_WEIGHT, 1801.0, 1800))
    }

    @Test
    fun maintainUsesTenPercentBand() {
        assertEquals(Adherence.MET, GoalAdherence.calories(FitnessGoal.MAINTAIN_WEIGHT, 1900.0, 2000))
        assertEquals(Adherence.MISSED, GoalAdherence.calories(FitnessGoal.MAINTAIN_WEIGHT, 1700.0, 2000))
    }
}

class WorkoutSessionTest {
    @Test
    fun pauseDoesNotCountTowardElapsed() {
        var session = WorkoutSessionEngine.start(0)
        session = WorkoutSessionEngine.pause(session, 1_000)
        session = WorkoutSessionEngine.resume(session, 4_000)
        session = WorkoutSessionEngine.complete(session, 6_000)
        assertEquals(SessionState.COMPLETED, session.state)
        assertEquals(3_000L, WorkoutSessionEngine.elapsedMillis(session, 9_000))
    }

    @Test
    fun personalRecordIsMaxWeight() {
        val records = PersonalRecords.maxWeightByExercise(
            listOf(
                SetRecord("squat", 80.0, 5),
                SetRecord("squat", 90.0, 3),
                SetRecord("row", null, 8),
            ),
        )
        assertEquals(90.0, records.getValue("squat"), 0.001)
        assertFalse(records.containsKey("row"))
    }
}

class SyncAndPremiumTest {
    @Test
    fun higherServerRevisionWins() {
        val local = VersionedRecord("1", 20, serverRevision = 2, deleted = false)
        val remote = VersionedRecord("1", 10, serverRevision = 5, deleted = false)
        assertEquals(5L, ConflictResolver.resolve(local, remote).serverRevision)
    }

    @Test
    fun equalTimestampPrefersDeletion() {
        val local = VersionedRecord("1", 10, null, deleted = false)
        val remote = VersionedRecord("1", 10, null, deleted = true)
        assertTrue(ConflictResolver.resolve(local, remote).deleted)
    }

    @Test
    fun unconfiguredTransportKeepsOutbox() = runTest {
        val pending = listOf(SyncMutation("a", "food_log_entry", "UPSERT", 1))
        val step = SyncCoordinator { SyncPushResult.NotConfigured }.push(pending)
        assertEquals(SyncStep.Skipped(pending), step)
    }

    @Test
    fun pendingPurchaseIsNotPremium() {
        val entitlement = PremiumEntitlementMapper.map(
            listOf(
                StorePurchaseSnapshot(
                    productIds = listOf(PremiumEntitlementMapper.MONTHLY_PRODUCT_ID),
                    purchaseToken = "token",
                    state = StorePurchaseState.PENDING,
                    suspended = false,
                ),
            ),
        )
        assertFalse(entitlement.premium)
    }

    @Test
    fun purchasedKnownProductIsPremium() {
        val entitlement = PremiumEntitlementMapper.map(
            listOf(
                StorePurchaseSnapshot(
                    productIds = listOf(PremiumEntitlementMapper.MONTHLY_PRODUCT_ID),
                    purchaseToken = "token",
                    state = StorePurchaseState.PURCHASED,
                    suspended = false,
                ),
            ),
        )
        assertTrue(entitlement.premium)
    }

    @Test
    fun blankTokenIsNotPremium() {
        val entitlement = PremiumEntitlementMapper.map(
            listOf(
                StorePurchaseSnapshot(
                    productIds = listOf(PremiumEntitlementMapper.MONTHLY_PRODUCT_ID),
                    purchaseToken = " ",
                    state = StorePurchaseState.PURCHASED,
                    suspended = false,
                ),
            ),
        )
        assertFalse(entitlement.premium)
    }
}

class FoodScanCoordinatorTest {
    @Test
    fun unconfiguredProviderDoesNotLogAndDiscardsImage() {
        val coordinator = FoodScanCoordinator(providerConfigured = false)
        val preview = coordinator.reduce(ScanState.Idle, ScanEvent.PhotoCaptured("img"))
        val result = coordinator.reduce(preview, ScanEvent.Analyze)
        assertTrue(result is ScanState.Unavailable)
        assertTrue(result.shouldDiscardImage)
        assertFalse(result.canCommit)
    }

    @Test
    fun candidatesRequirePortionBeforeCommit() {
        val coordinator = FoodScanCoordinator(providerConfigured = true)
        var state: ScanState = ScanState.Idle
        state = coordinator.reduce(state, ScanEvent.PhotoCaptured("img"))
        state = coordinator.reduce(state, ScanEvent.Analyze)
        assertTrue(state is ScanState.NeedsUploadConsent)
        state = coordinator.reduce(state, ScanEvent.ConfirmUpload)
        state = coordinator.reduce(
            state,
            ScanEvent.RecognitionFinished(
                FoodRecognitionResult.Candidates(
                    listOf(FoodCandidate("Chicken biryani", 0.42, foodId = null)),
                ),
            ),
        )
        assertTrue(state is ScanState.Candidates)
        assertFalse(state.canCommit)
        state = coordinator.reduce(state, ScanEvent.Select(0))
        state = coordinator.reduce(state, ScanEvent.ConfirmPortion)
        assertTrue(state.canCommit)
        val ready = state as ScanState.ReadyToLog
        assertEquals(100, ready.grams)
        assertNull(ready.candidate.foodId)
    }

    @Test
    fun missingConfidenceIsNotAMatch() {
        val coordinator = FoodScanCoordinator(providerConfigured = true)
        val state = coordinator.reduce(
            ScanState.Analyzing("img"),
            ScanEvent.RecognitionFinished(
                FoodRecognitionResult.Candidates(
                    listOf(FoodCandidate("Rice", confidence = 2.0, foodId = null)),
                ),
            ),
        )
        assertTrue(state is ScanState.Failed)
    }
}
