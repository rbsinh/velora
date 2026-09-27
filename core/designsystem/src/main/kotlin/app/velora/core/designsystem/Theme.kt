package app.velora.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF13231F)
private val MutedInk = Color(0xFF53645F)
private val Paper = Color(0xFFF7F8F4)
private val Teal = Color(0xFF087B62)
private val TealSoft = Color(0xFFD7F2E9)
private val Coral = Color(0xFFD76645)
private val Gold = Color(0xFFB07B16)
private val Night = Color(0xFF0B1412)
private val NightSurface = Color(0xFF14201D)
private val NightCard = Color(0xFF1A2925)
private val Mist = Color(0xFFE6EEE9)

val ProteinColor = Teal
val CarbColor = Coral
val FatColor = Gold

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = TealSoft,
    onPrimaryContainer = Color(0xFF003D30),
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDBCF),
    onSecondaryContainer = Color(0xFF492015),
    tertiary = Gold,
    background = Paper,
    onBackground = Ink,
    surface = Color(0xFFFFFBFE),
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = MutedInk,
    outline = Color(0xFF81918B),
    outlineVariant = Color(0xFFD1DCD6),
    error = Color(0xFF8E1B1B),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF74DFC0),
    onPrimary = Color(0xFF00382B),
    primaryContainer = Color(0xFF07523F),
    onPrimaryContainer = Color(0xFFACF3DA),
    secondary = Color(0xFFFFB59E),
    secondaryContainer = Color(0xFF6B321F),
    onSecondaryContainer = Color(0xFFFFDBCF),
    tertiary = Color(0xFFF0C36A),
    background = Night,
    onBackground = Color(0xFFE7F2EF),
    surface = NightSurface,
    onSurface = Color(0xFFE7F2EF),
    surfaceVariant = NightCard,
    onSurfaceVariant = Color(0xFFBCCBC5),
    outline = Color(0xFF84968F),
    outlineVariant = Color(0xFF344640),
    error = Color(0xFFFFB4AB),
)

private val VeloraTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.8).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.4).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 23.sp,
        lineHeight = 29.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
)

private val VeloraShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
)

@Composable
fun VeloraTheme(themeMode: String = "SYSTEM", content: @Composable () -> Unit) {
    val dark = when (themeMode) {
        "DARK" -> true
        "LIGHT" -> false
        else -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = VeloraTypography,
        shapes = VeloraShapes,
        content = content,
    )
}
