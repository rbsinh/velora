package app.velora.core.network

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteFoodDataSourceTest {
    @Test
    fun searchParsesVerifiedFood() = runTest {
        val server = MockWebServer()
        server.enqueue(
            MockResponse().setBody(
                """
                {"foods":[{"id":"usda:1","name":"Oats","provenance":"VERIFIED_REFERENCE","sourceName":"USDA FoodData Central","sourceRecordId":"1","servings":[{"label":"100 g","grams":100,"caloriesKcal":389,"proteinGrams":16.9,"carbohydrateGrams":66.3,"fatGrams":6.9,"fiberGrams":10.6,"sugarGrams":null,"sodiumMilligrams":2}]}]}
                """.trimIndent(),
            ),
        )
        server.start()
        try {
            val source = RetrofitRemoteFoodDataSource(server.url("/").toString())
            val result = source.search("oats") as RemoteCall.Ok
            assertEquals("Oats", result.value.single().name)
            assertEquals(null, result.value.single().servings.single().nutrients.sugarGrams)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun unknownBarcodeIsNotFound() = runTest {
        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(404))
        server.start()
        try {
            val source = RetrofitRemoteFoodDataSource(server.url("/").toString())
            val result = source.barcode("012345678905") as RemoteCall.Err
            assertEquals(RemoteError.NOT_FOUND, result.error)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun serverErrorIsRetryableClass() = runTest {
        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(503))
        server.start()
        try {
            val source = RetrofitRemoteFoodDataSource(server.url("/").toString())
            val result = source.search("rice") as RemoteCall.Err
            assertEquals(RemoteError.SERVER, result.error)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun recognitionWithoutConfidenceIsInvalid() = runTest {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("""{"candidates":[{"label":"Rice"}]}"""))
        server.start()
        try {
            val source = RetrofitRecognitionDataSource(server.url("/").toString())
            val result = source.recognize(byteArrayOf(1, 2, 3))
            assertTrue(result is app.velora.core.model.FoodRecognitionResult.Failed)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun invalidBarcodeDoesNotCallNetwork() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val source = RetrofitRemoteFoodDataSource(server.url("/").toString())
            val result = source.barcode("abc") as RemoteCall.Err
            assertEquals(RemoteError.INVALID, result.error)
            assertEquals(0, server.requestCount)
        } finally {
            server.shutdown()
        }
    }
}
