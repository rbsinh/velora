package app.velora.core.database

import android.content.Context
import app.velora.core.domain.PersonalDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

@Singleton
class RoomPersonalDataRepository @Inject constructor(
    private val database: VeloraDatabase,
    private val settings: DataStoreSettingsRepository,
    private val seeder: CatalogSeeder,
    @ApplicationContext private val context: Context,
) : PersonalDataRepository {
    override suspend fun exportJson(): String = withContext(Dispatchers.IO) {
        val dao = database.dao()
        val root = JSONObject()
        val logs = JSONArray()
        dao.allLogs().forEach { row ->
            logs.put(
                JSONObject()
                    .put("date", row.localDate)
                    .put("name", row.foodName)
                    .put("calories", row.caloriesKcal)
                    .put("deleted", row.deleted),
            )
        }
        root.put("foodLog", logs)
        val weights = JSONArray()
        dao.allWeights().forEach { row ->
            weights.put(JSONObject().put("date", row.localDate).put("kilograms", row.kilograms))
        }
        root.put("weights", weights)
        root.toString()
    }

    override suspend fun wipe() = withContext(Dispatchers.IO) {
        database.clearAllTables()
        settings.clear()
        File(context.filesDir, "progress").deleteRecursively()
        seeder.seedIfNeeded()
    }
}
