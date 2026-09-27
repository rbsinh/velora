package app.velora.core.analytics

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface AnalyticsTracker {
    fun track(event: String)
}

@Singleton
class LogcatNameOnlyAnalytics @Inject constructor(
    @ApplicationContext context: Context,
) : AnalyticsTracker {
    private val debuggable = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    override fun track(event: String) {
        if (debuggable) Log.d("VeloraAnalytics", event)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun tracker(impl: LogcatNameOnlyAnalytics): AnalyticsTracker
}
