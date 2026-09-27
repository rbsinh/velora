package app.velora.core.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogParserTest {
    @Test
    fun parsesVerifiedBananaRecord() {
        val json = """
            {"records":[{"fdcId":173944,"description":"Bananas, raw","dataType":"SR Legacy","nutrientsPer100g":{"caloriesKcal":89.0,"proteinGrams":1.09,"carbohydrateGrams":22.8,"fatGrams":0.33,"fiberGrams":2.6,"sugarGrams":12.2,"sodiumMilligrams":1.0}}]}
        """.trimIndent()
        val food = CatalogParser.parse(json).single()
        assertEquals("usda:173944", food.id)
        assertEquals("173944", food.sourceRecordId)
        assertEquals(89.0, food.servings.single().nutrients.caloriesKcal, 0.001)
        assertEquals(2.6, food.servings.single().nutrients.fiberGrams!!, 0.001)
    }

    @Test
    fun dropsRecordMissingRequiredMacros() {
        val json = """
            {"records":[{"fdcId":1,"description":"Unknown","nutrientsPer100g":{"fiberGrams":1.0}}]}
        """.trimIndent()
        assertEquals(0, CatalogParser.parse(json).size)
        assertNull(CatalogParser.parse("""{"records":[]}""").firstOrNull())
    }
}
