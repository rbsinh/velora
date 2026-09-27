package app.velora.core.health

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

enum class HealthAvailability { AVAILABLE, UPDATE_REQUIRED, UNAVAILABLE }

data class DailyHealth(
    val date: String,
    val steps: Long?,
    val distanceMeters: Double?,
    val activeCalories: Double?,
)

sealed class HealthRead {
    data class Ready(val days: List<DailyHealth>) : HealthRead()
    data class Denied(val message: String) : HealthRead()
    data class Unavailable(val message: String) : HealthRead()
}

@Singleton
class HealthConnectGateway @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun availability(): HealthAvailability = when (HealthConnectClient.getSdkStatus(context)) {
        HealthConnectClient.SDK_AVAILABLE -> HealthAvailability.AVAILABLE
        HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> HealthAvailability.UPDATE_REQUIRED
        else -> HealthAvailability.UNAVAILABLE
    }

    fun readPermissions(): Set<String> = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
    )

    suspend fun readDays(start: LocalDate, end: LocalDate, zone: ZoneId = ZoneId.systemDefault()): HealthRead {
        if (availability() != HealthAvailability.AVAILABLE) {
            return HealthRead.Unavailable("Health Connect is not available on this device. You can enter steps manually.")
        }
        val client = HealthConnectClient.getOrCreate(context)
        val granted = client.permissionController.getGrantedPermissions()
        if (HealthPermission.getReadPermission(StepsRecord::class) !in granted) {
            return HealthRead.Denied("Step access was not granted. You can enter a daily total yourself.")
        }
        return try {
            val days = buildList {
                var date = start
                while (!date.isAfter(end)) {
                    add(readOne(client, date, zone))
                    date = date.plusDays(1)
                }
            }
            HealthRead.Ready(days)
        } catch (security: SecurityException) {
            HealthRead.Denied("Health Connect denied the read.")
        }
    }

    private suspend fun readOne(client: HealthConnectClient, date: LocalDate, zone: ZoneId): DailyHealth {
        val start = date.atStartOfDay(zone).toInstant()
        val end = date.plusDays(1).atStartOfDay(zone).toInstant()
        val response = client.aggregate(
            AggregateRequest(
                metrics = setOf(
                    StepsRecord.COUNT_TOTAL,
                    DistanceRecord.DISTANCE_TOTAL,
                    ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL,
                ),
                timeRangeFilter = TimeRangeFilter.between(start, end),
            ),
        )
        return DailyHealth(
            date = date.toString(),
            steps = response[StepsRecord.COUNT_TOTAL],
            distanceMeters = response[DistanceRecord.DISTANCE_TOTAL]?.inMeters,
            activeCalories = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories,
        )
    }
}
