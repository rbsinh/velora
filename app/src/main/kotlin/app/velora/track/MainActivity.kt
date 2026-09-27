package app.velora.track

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.velora.core.database.CatalogSeeder
import app.velora.core.designsystem.VeloraTheme
import app.velora.core.domain.SettingsRepository
import app.velora.feature.activity.ActivityScreen
import app.velora.feature.dashboard.DashboardScreen
import app.velora.feature.metrics.ProgressScreen
import app.velora.feature.nutrition.BarcodeScreen
import app.velora.feature.nutrition.CustomFoodScreen
import app.velora.feature.nutrition.FoodLogScreen
import app.velora.feature.nutrition.FoodScanScreen
import app.velora.feature.nutrition.FoodSearchScreen
import app.velora.feature.nutrition.RecipeScreen
import app.velora.feature.onboarding.OnboardingScreen
import app.velora.feature.profile.ProfileScreen
import app.velora.feature.workout.WorkoutScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appModel: AppViewModel = hiltViewModel()
            val theme by appModel.theme.collectAsStateWithLifecycle()
            val ready by appModel.onboardingComplete.collectAsStateWithLifecycle()
            VeloraTheme(theme) {
                if (ready != null) VeloraNav(onboardingComplete = ready == true)
            }
        }
    }
}

@HiltViewModel
class AppViewModel @Inject constructor(
    settings: SettingsRepository,
    private val seeder: CatalogSeeder,
) : ViewModel() {
    val theme = settings.theme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "SYSTEM")
    val onboardingComplete = settings.onboardingComplete
        .map<Boolean, Boolean?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch { seeder.seedIfNeeded() }
    }
}

private data class Tab(val route: String, val label: String)

@Composable
private fun VeloraNav(onboardingComplete: Boolean) {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("home", "Home"),
        Tab("food", "Food"),
        Tab("move", "Move"),
        Tab("progress", "Progress"),
        Tab("profile", "Profile"),
    )
    val backStack by nav.currentBackStackEntryAsState()
    val destination = backStack?.destination
    val showBar = destination?.route in tabs.map { it.route }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                ) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = destination?.hierarchy?.any { it.route == tab.route } == true,
                            onClick = {
                                nav.navigate(tab.route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    when (tab.route) {
                                        "home" -> Icons.Filled.Home
                                        "food" -> Icons.Filled.Restaurant
                                        "move" -> Icons.Filled.DirectionsWalk
                                        "progress" -> Icons.Filled.ShowChart
                                        else -> Icons.Filled.Person
                                    },
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = if (onboardingComplete) "home" else "onboarding",
            modifier = Modifier.padding(padding),
        ) {
            composable("onboarding") { OnboardingScreen(onFinished = { nav.navigate("home") { popUpTo("onboarding") { inclusive = true } } }) }
            composable("home") {
                DashboardScreen(
                    onFood = { nav.navigate("food") },
                    onMove = { nav.navigate("move") },
                    onWorkout = { nav.navigate("workout") },
                )
            }
            composable("food") {
                FoodLogScreen(
                    onSearch = { date -> nav.navigate("food/search/$date") },
                    onCustom = { nav.navigate("food/custom") },
                    onBarcode = { date -> nav.navigate("food/barcode/$date") },
                    onScan = { date -> nav.navigate("food/scan/$date") },
                    onRecipes = { date -> nav.navigate("food/recipes/$date") },
                )
            }
            composable(
                "food/search/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) { FoodSearchScreen(onLogged = { nav.popBackStack() }) }
            composable(
                "food/custom?barcode={barcode}",
                arguments = listOf(navArgument("barcode") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }),
            ) { CustomFoodScreen(onDone = { nav.popBackStack() }) }
            composable(
                "food/recipes/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) { RecipeScreen(onDone = { nav.popBackStack() }) }
            composable(
                "food/barcode/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) {
                BarcodeScreen(
                    onCreate = { code ->
                        nav.navigate("food/custom?barcode=${android.net.Uri.encode(code)}")
                    },
                    onLogged = { nav.popBackStack() },
                )
            }
            composable(
                "food/scan/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) { FoodScanScreen(onLogged = { nav.popBackStack() }) }
            composable("move") { ActivityScreen(onWorkout = { nav.navigate("workout") }) }
            composable("workout") { WorkoutScreen() }
            composable("progress") { ProgressScreen() }
            composable("profile") {
                ProfileScreen(
                    onEditProfile = { nav.navigate("onboarding") },
                    onWiped = { nav.navigate("onboarding") { popUpTo(nav.graph.id) { inclusive = true } } },
                )
            }
        }
    }
}
