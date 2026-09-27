package app.velora.core.database

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.velora.core.domain.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsStore by preferencesDataStore(name = "velora_settings")

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    @ApplicationContext context: Context,
) : SettingsRepository {
    private val store = context.applicationContext.settingsStore

    override val onboardingComplete: Flow<Boolean> =
        store.data.map { it[Keys.ONBOARDING] ?: false }

    override suspend fun setOnboardingComplete(complete: Boolean) {
        store.edit { it[Keys.ONBOARDING] = complete }
    }

    override val theme: Flow<String> = store.data.map { it[Keys.THEME] ?: "SYSTEM" }

    override suspend fun setTheme(theme: String) {
        store.edit { it[Keys.THEME] = theme }
    }

    override val remindersEnabled: Flow<Set<String>> = store.data.map { prefs ->
        REMINDER_KEYS.filter { prefs[booleanPreferencesKey("reminder_$it")] == true }.toSet()
    }

    override suspend fun setReminder(key: String, enabled: Boolean, hour: Int) {
        store.edit {
            it[booleanPreferencesKey("reminder_$key")] = enabled
            it[intPreferencesKey("reminder_hour_$key")] = hour.coerceIn(0, 23)
        }
    }

    override val healthExportEnabled: Flow<Boolean> = store.data.map { it[Keys.HEALTH_EXPORT] ?: false }

    override suspend fun setHealthExportEnabled(enabled: Boolean) {
        store.edit { it[Keys.HEALTH_EXPORT] = enabled }
    }

    override fun reminderHour(key: String): Flow<Int> =
        store.data.map { it[intPreferencesKey("reminder_hour_$key")] ?: 8 }

    suspend fun clear() {
        store.edit { it.clear() }
    }

    private object Keys {
        val ONBOARDING = booleanPreferencesKey("onboarding_complete")
        val THEME = stringPreferencesKey("theme")
        val HEALTH_EXPORT = booleanPreferencesKey("health_export")
    }

    companion object {
        val REMINDER_KEYS = listOf("meal", "water", "steps", "workout", "weight", "goals")
    }
}
