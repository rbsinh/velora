package app.velora.track.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.velora.core.domain.SettingsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first

class ReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val settings = EntryPointAccessors.fromApplication(applicationContext, ReminderEntryPoint::class.java).settings()
        val enabled = settings.remindersEnabled.first()
        if (enabled.isEmpty()) return Result.success()
        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        val channelId = "velora-reminders"
        manager.createNotificationChannel(
            NotificationChannel(channelId, "Velora reminders", NotificationManager.IMPORTANCE_DEFAULT),
        )
        val text = enabled.joinToString { it.replaceFirstChar { char -> char.uppercase() } }
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(app.velora.track.R.drawable.ic_mark)
            .setContentTitle("Velora reminder")
            .setContentText(text)
            .setAutoCancel(true)
            .build()
        runCatching { manager.notify(41, notification) }
        return Result.success()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderEntryPoint {
    fun settings(): SettingsRepository
}
