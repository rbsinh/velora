package app.velora.track

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import app.velora.core.designsystem.EmptyPane
import app.velora.core.designsystem.VeloraTheme
import org.junit.Rule
import org.junit.Test

class EmptyStateTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun emptyPaneExposesTitleAndBody() {
        compose.setContent {
            VeloraTheme {
                EmptyPane("No weights yet", "Add one below. One entry is not drawn as a trend.")
            }
        }
        compose.onNodeWithText("No weights yet").assertIsDisplayed()
        compose.onNodeWithText("Add one below. One entry is not drawn as a trend.").assertIsDisplayed()
    }
}
