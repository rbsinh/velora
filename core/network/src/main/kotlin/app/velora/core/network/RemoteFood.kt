package app.velora.core.network

import app.velora.core.model.Food
import app.velora.core.model.FoodCandidate
import app.velora.core.model.FoodRecognitionResult
import app.velora.core.model.FoodServing
import app.velora.core.model.NutrientProvenance
import app.velora.core.model.Nutrients
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

enum class RemoteError {
    UNAVAILABLE,
    NOT_FOUND,
    MULTIPLE,
    UNAUTHORIZED,
    RATE_LIMITED,
    SERVER,
    INVALID,
    TIMEOUT,
    OFFLINE,
}

sealed class RemoteCall<out T> {
    data class Ok<T>(val value: T) : RemoteCall<T>()
    data class Err(val error: RemoteError, val message: String) : RemoteCall<Nothing>()
}

data class BarcodeLookup(
    val foods: List<Food>,
)

interface RemoteFoodDataSource {
    suspend fun search(query: String): RemoteCall<List<Food>>
    suspend fun barcode(code: String): RemoteCall<BarcodeLookup>
}

interface RemoteRecognitionDataSource {
    suspend fun recognize(jpeg: ByteArray): FoodRecognitionResult
}

@Serializable
data class ServingWire(
    val label: String,
    val grams: Double? = null,
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbohydrateGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double? = null,
    val sugarGrams: Double? = null,
    val sodiumMilligrams: Double? = null,
)

@Serializable
data class FoodWire(
    val id: String,
    val name: String,
    val provenance: String,
    val sourceName: String? = null,
    val sourceRecordId: String? = null,
    val servings: List<ServingWire>,
)

@Serializable
data class FoodSearchWire(val foods: List<FoodWire>)

@Serializable
data class CandidateWire(
    val label: String,
    val confidence: Double? = null,
    val foodId: String? = null,
)

@Serializable
data class RecognitionWire(val candidates: List<CandidateWire> = emptyList())

private interface VeloraHttpApi {
    @GET("v1/foods/search")
    suspend fun search(@Query("q") query: String): FoodSearchWire

    @GET("v1/foods/barcode/{code}")
    suspend fun barcode(@Path("code") code: String): Response<FoodSearchWire>

    @Multipart
    @POST("v1/ai/food-recognition")
    suspend fun recognize(@Part image: MultipartBody.Part): RecognitionWire
}

class RetrofitRemoteFoodDataSource(
    baseUrl: String,
    client: OkHttpClient = defaultClient(),
) : RemoteFoodDataSource {
    private val api = retrofit(baseUrl, client).create(VeloraHttpApi::class.java)

    override suspend fun search(query: String): RemoteCall<List<Food>> {
        if (query.isBlank()) return RemoteCall.Err(RemoteError.INVALID, "Enter a food name.")
        return invoke { api.search(query).foods.mapNotNull(::toFood) }
    }

    override suspend fun barcode(code: String): RemoteCall<BarcodeLookup> {
        if (!code.all { it.isDigit() } || code.length !in 8..14) {
            return RemoteCall.Err(RemoteError.INVALID, "That barcode is not a supported product code.")
        }
        return try {
            val response = api.barcode(code)
            when {
                response.code() == 404 -> RemoteCall.Err(RemoteError.NOT_FOUND, "No product is listed for this barcode.")
                response.code() == 409 -> {
                    val raw = response.errorBody()?.string()
                    val foods = raw?.let { body ->
                        json.decodeFromString(FoodSearchWire.serializer(), body).foods.mapNotNull(::toFood)
                    }.orEmpty()
                    if (foods.size < 2) {
                        RemoteCall.Err(RemoteError.MULTIPLE, "The server reported multiple matches but did not list them.")
                    } else {
                        RemoteCall.Ok(BarcodeLookup(foods))
                    }
                }
                response.isSuccessful -> {
                    val foods = response.body()?.foods?.mapNotNull(::toFood).orEmpty()
                    if (foods.isEmpty()) {
                        RemoteCall.Err(RemoteError.INVALID, "The product response did not include usable nutrition.")
                    } else {
                        RemoteCall.Ok(BarcodeLookup(foods))
                    }
                }
                else -> mapCode(response.code(), response.message())
            }
        } catch (error: Exception) {
            mapException(error)
        }
    }
}

class RetrofitRecognitionDataSource(
    baseUrl: String,
    client: OkHttpClient = defaultClient(),
) : RemoteRecognitionDataSource {
    private val api = retrofit(baseUrl, client).create(VeloraHttpApi::class.java)

    override suspend fun recognize(jpeg: ByteArray): FoodRecognitionResult {
        if (jpeg.isEmpty() || jpeg.size > MAX_IMAGE_BYTES) {
            return FoodRecognitionResult.Failed("The photo is empty or larger than 4 MB.", retryable = false)
        }
        return try {
            val body = jpeg.toRequestBody("image/jpeg".toMediaType())
            val part = MultipartBody.Part.createFormData("image", "meal.jpg", body)
            val wire = api.recognize(part)
            val candidates = wire.candidates.mapNotNull { item ->
                val confidence = item.confidence ?: return@mapNotNull null
                if (item.label.isBlank() || confidence !in 0.0..1.0) return@mapNotNull null
                FoodCandidate(item.label, confidence, item.foodId)
            }
            if (candidates.isEmpty()) {
                FoodRecognitionResult.Failed(
                    "The recognition response did not include a confidence score.",
                    retryable = false,
                )
            } else {
                FoodRecognitionResult.Candidates(candidates)
            }
        } catch (error: Exception) {
            val mapped = mapException<Unit>(error)
            val err = mapped as RemoteCall.Err
            FoodRecognitionResult.Failed(err.message, retryable = err.error == RemoteError.TIMEOUT || err.error == RemoteError.SERVER || err.error == RemoteError.OFFLINE)
        }
    }

    private companion object {
        const val MAX_IMAGE_BYTES = 4 * 1024 * 1024
    }
}

class UnconfiguredRemoteFoodDataSource : RemoteFoodDataSource {
    override suspend fun search(query: String): RemoteCall<List<Food>> =
        RemoteCall.Err(RemoteError.UNAVAILABLE, "Food search on the network is not configured. Local foods still work.")

    override suspend fun barcode(code: String): RemoteCall<BarcodeLookup> =
        RemoteCall.Err(RemoteError.UNAVAILABLE, "No network product catalog is configured.")
}

class UnconfiguredRecognitionDataSource : RemoteRecognitionDataSource {
    override suspend fun recognize(jpeg: ByteArray): FoodRecognitionResult =
        FoodRecognitionResult.Unavailable(
            "No food-recognition provider is configured. The photo was not uploaded.",
        )
}

fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .build()

private val json = Json { ignoreUnknownKeys = true }

private fun retrofit(baseUrl: String, client: OkHttpClient): Retrofit {
    val normalized = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
    return Retrofit.Builder()
        .baseUrl(normalized)
        .client(client)
        .addConverterFactory(JsonConverterFactory(json))
        .build()
}

private fun toFood(wire: FoodWire): Food? {
    val provenance = runCatching { NutrientProvenance.valueOf(wire.provenance) }.getOrNull()
        ?: return null
    val servings = wire.servings.map { serving ->
        FoodServing(
            id = "${wire.id}:${serving.label}",
            label = serving.label,
            grams = serving.grams,
            nutrients = Nutrients(
                caloriesKcal = serving.caloriesKcal,
                proteinGrams = serving.proteinGrams,
                carbohydrateGrams = serving.carbohydrateGrams,
                fatGrams = serving.fatGrams,
                fiberGrams = serving.fiberGrams,
                sugarGrams = serving.sugarGrams,
                sodiumMilligrams = serving.sodiumMilligrams,
                provenance = provenance,
            ),
        )
    }
    if (servings.isEmpty() || wire.name.isBlank()) return null
    return Food(
        id = wire.id,
        name = wire.name,
        brand = null,
        barcode = null,
        servings = servings,
        provenance = provenance,
        sourceName = wire.sourceName,
        sourceRecordId = wire.sourceRecordId,
        userCreated = false,
    )
}

private suspend fun <T> invoke(block: suspend () -> T): RemoteCall<T> = try {
    RemoteCall.Ok(block())
} catch (error: Exception) {
    mapException(error)
}

private fun <T> mapException(error: Exception): RemoteCall<T> = when (error) {
    is SocketTimeoutException -> RemoteCall.Err(RemoteError.TIMEOUT, "The request timed out.")
    is IOException -> RemoteCall.Err(RemoteError.OFFLINE, "The network request failed.")
    is HttpException -> mapCode(error.code(), error.message())
    else -> RemoteCall.Err(RemoteError.INVALID, "The response could not be read.")
}

private fun <T> mapCode(code: Int, message: String): RemoteCall<T> = when (code) {
    401 -> RemoteCall.Err(RemoteError.UNAUTHORIZED, "Sign-in is required for this request.")
    404 -> RemoteCall.Err(RemoteError.NOT_FOUND, "Nothing matched.")
    429 -> RemoteCall.Err(RemoteError.RATE_LIMITED, "The server asked the app to slow down.")
    in 500..599 -> RemoteCall.Err(RemoteError.SERVER, "The server returned an error.")
    else -> RemoteCall.Err(RemoteError.INVALID, message.ifBlank { "Unexpected response $code." })
}

@OptIn(ExperimentalSerializationApi::class)
private class JsonConverterFactory(
    private val json: Json,
) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        val loader = serializer(type)
        return Converter { body -> json.decodeFromString(loader, body.string()) }
    }
}
