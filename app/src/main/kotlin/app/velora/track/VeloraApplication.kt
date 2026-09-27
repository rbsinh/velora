package app.velora.track

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import app.velora.core.network.RemoteFoodDataSource
import app.velora.core.network.RemoteRecognitionDataSource
import app.velora.core.network.RetrofitRecognitionDataSource
import app.velora.core.network.RetrofitRemoteFoodDataSource
import app.velora.core.network.UnconfiguredRecognitionDataSource
import app.velora.core.network.UnconfiguredRemoteFoodDataSource
import app.velora.track.reminders.ReminderWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@HiltAndroidApp
class VeloraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "velora-reminders",
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Named("recognitionConfigured")
    fun recognitionConfigured(): Boolean = BuildConfig.API_BASE_URL.isNotBlank()

    @Provides
    @Singleton
    fun remoteFoods(): RemoteFoodDataSource {
        val url = BuildConfig.API_BASE_URL
        return if (url.isBlank()) UnconfiguredRemoteFoodDataSource() else RetrofitRemoteFoodDataSource(url)
    }

    @Provides
    @Singleton
    fun recognition(): RemoteRecognitionDataSource {
        val url = BuildConfig.API_BASE_URL
        return if (url.isBlank()) UnconfiguredRecognitionDataSource() else RetrofitRecognitionDataSource(url)
    }
}
