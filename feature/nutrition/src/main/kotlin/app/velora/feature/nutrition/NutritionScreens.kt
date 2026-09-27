package app.velora.feature.nutrition

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import app.velora.core.analytics.AnalyticsTracker
import app.velora.core.designsystem.EmptyPane
import app.velora.core.designsystem.ErrorPane
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.SecondaryButton
import app.velora.core.designsystem.SectionTitle
import app.velora.core.designsystem.VeloraCard
import app.velora.core.domain.FoodLogRepository
import app.velora.core.domain.FoodRepository
import app.velora.core.domain.FoodScanCoordinator
import app.velora.core.domain.MealTemplateRepository
import app.velora.core.domain.RecipeRepository
import app.velora.core.domain.ScanEvent
import app.velora.core.domain.ScanState
import app.velora.core.model.Food
import app.velora.core.model.FoodLogEntry
import app.velora.core.model.FoodRecognitionResult
import app.velora.core.model.MealSlot
import app.velora.core.model.MealTemplate
import app.velora.core.model.MealTemplateItem
import app.velora.core.model.NutrientProvenance
import app.velora.core.model.Nutrients
import app.velora.core.model.Recipe
import app.velora.core.model.RecipeItem
import app.velora.core.model.scaled
import app.velora.core.model.summed
import app.velora.core.network.RemoteCall
import app.velora.core.network.RemoteError
import app.velora.core.network.RemoteFoodDataSource
import app.velora.core.network.RemoteRecognitionDataSource
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodLogViewModel @Inject constructor(
    private val logs: FoodLogRepository,
    private val meals: MealTemplateRepository,
    private val analytics: AnalyticsTracker,
    savedState: SavedStateHandle,
) : ViewModel() {
    private val initialDate = savedState.get<String>("date")?.let { raw ->
        runCatching { LocalDate.parse(raw) }.getOrNull()
    } ?: LocalDate.now()
    var date by mutableStateOf(initialDate)
        private set
    private val dateFlow = MutableStateFlow(initialDate.toString())
    val entries = dateFlow.flatMapLatest { logs.observeDay(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val templates = meals.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun shift(days: Long) {
        date = date.plusDays(days)
        dateFlow.value = date.toString()
    }

    fun delete(id: String) {
        viewModelScope.launch { logs.softDelete(id, System.currentTimeMillis()) }
    }

    fun repeat(slot: MealSlot) {
        viewModelScope.launch {
            val previous = logs.entries(date.minusDays(1).toString(), slot)
            previous.forEach { entry ->
                logs.upsert(entry.copy(id = UUID.randomUUID().toString(), localDate = date.toString(), updatedAtEpochMillis = System.currentTimeMillis()))
            }
            analytics.track("food_logged")
        }
    }

    fun logFood(food: Food, slot: MealSlot, grams: Double) {
        val serving = food.servings.firstOrNull() ?: return
        val factor = if (serving.grams != null && serving.grams!! > 0) grams / serving.grams!! else grams
        if (factor <= 0) return
        viewModelScope.launch {
            logs.upsert(
                FoodLogEntry(
                    id = UUID.randomUUID().toString(),
                    localDate = date.toString(),
                    mealSlot = slot,
                    foodId = food.id,
                    foodName = food.name,
                    servingLabel = "${grams.toInt()} g",
                    quantity = factor,
                    nutrients = serving.nutrients.scaled(factor),
                    updatedAtEpochMillis = System.currentTimeMillis(),
                ),
            )
            analytics.track("food_logged")
        }
    }

    fun saveMeal(name: String, slot: MealSlot) {
        viewModelScope.launch {
            val items = entries.value.filter { it.mealSlot == slot }.map { entry ->
                MealTemplateItem(UUID.randomUUID().toString(), entry.foodName, entry.servingLabel, entry.quantity, entry.nutrients, slot)
            }
            if (items.isEmpty()) return@launch
            meals.upsert(MealTemplate(UUID.randomUUID().toString(), name.ifBlank { slot.name }, items))
            analytics.track("meal_created")
        }
    }

    fun logTemplate(template: MealTemplate) {
        viewModelScope.launch {
            template.items.forEach { item ->
                logs.upsert(
                    FoodLogEntry(
                        UUID.randomUUID().toString(), date.toString(), item.mealSlot, null, item.foodName,
                        item.servingLabel, item.quantity, item.nutrients, System.currentTimeMillis(),
                    ),
                )
            }
            analytics.track("food_logged")
        }
    }
}

@Composable
fun FoodLogScreen(
    onSearch: (String) -> Unit,
    onCustom: () -> Unit,
    onBarcode: (String) -> Unit,
    onScan: (String) -> Unit,
    onRecipes: (String) -> Unit,
    viewModel: FoodLogViewModel = hiltViewModel(),
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val templates by viewModel.templates.collectAsStateWithLifecycle()
    val totals = entries.map { it.nutrients }.summed()
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("NOURISH YOUR DAY", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Food diary", style = MaterialTheme.typography.headlineMedium)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SecondaryButton("‹ Previous", Modifier.weight(1f)) { viewModel.shift(-1) }
            Surface(
                modifier = Modifier.weight(1.15f).height(52.dp),
                shape = RoundedCornerShape(17.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(viewModel.date.toString(), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            SecondaryButton("Next ›", Modifier.weight(1f)) { viewModel.shift(1) }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Daily nutrition", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text("${totals.caloriesKcal.toInt()} kcal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    NutrientValue("Protein", "${fmt(totals.proteinGrams)}g")
                    NutrientValue("Carbs", "${fmt(totals.carbohydrateGrams)}g")
                    NutrientValue("Fat", "${fmt(totals.fatGrams)}g")
                }
                Text(
                    "Fiber ${optional(totals.fiberGrams)} · Sugar ${optional(totals.sugarGrams)} · Sodium ${optional(totals.sodiumMilligrams)} mg",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Text("Quick add", style = MaterialTheme.typography.titleLarge)
        PrimaryButton("Search foods") { onSearch(viewModel.date.toString()) }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SecondaryButton("Scan barcode", Modifier.weight(1f)) { onBarcode(viewModel.date.toString()) }
            SecondaryButton("Scan a meal", Modifier.weight(1f)) { onScan(viewModel.date.toString()) }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SecondaryButton("Custom food", Modifier.weight(1f), onClick = onCustom)
            SecondaryButton("Recipes", Modifier.weight(1f)) { onRecipes(viewModel.date.toString()) }
        }

        SectionTitle("Meals")
        MealSlot.entries.forEach { slot ->
            val slotName = slot.name.lowercase().replaceFirstChar { it.uppercase() }
            Text(slotName, style = MaterialTheme.typography.titleMedium)
            val rows = entries.filter { it.mealSlot == slot }
            if (rows.isEmpty()) EmptyPane("Nothing for ${slot.name.lowercase()}", "Add something fresh or bring forward yesterday's meal.")
            rows.forEach { entry ->
                VeloraCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(entry.foodName, style = MaterialTheme.typography.titleMedium)
                            Text(
                                entry.servingLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text("${entry.nutrients.caloriesKcal.toInt()} kcal", style = MaterialTheme.typography.titleMedium)
                    }
                    SecondaryButton("Remove") { viewModel.delete(entry.id) }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton("Repeat yesterday", Modifier.weight(1f)) { viewModel.repeat(slot) }
                SecondaryButton("Save meal", Modifier.weight(1f)) { viewModel.saveMeal(slot.name, slot) }
            }
        }
        if (templates.isNotEmpty()) {
            SectionTitle("Saved meals")
            templates.forEach { template ->
                SecondaryButton("Log ${template.name}") { viewModel.logTemplate(template) }
            }
        }
    }
}

@Composable
private fun NutrientValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = MaterialTheme.typography.titleLarge)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val foods: FoodRepository,
    private val remote: RemoteFoodDataSource,
) : ViewModel() {
    var query by mutableStateOf("")
    private val queryFlow = MutableStateFlow("")
    val local = queryFlow.flatMapLatest { foods.observeSearch(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    var remoteMessage by mutableStateOf<String?>(null)
    var slot by mutableStateOf(MealSlot.LUNCH)
    var grams by mutableStateOf("100")

    fun onQuery(value: String) {
        query = value
        queryFlow.value = value
        remoteMessage = null
    }

    fun searchRemote() {
        viewModelScope.launch {
            when (val result = remote.search(query)) {
                is RemoteCall.Ok -> remoteMessage = if (result.value.isEmpty()) {
                    "The server returned no foods."
                } else {
                    result.value.forEach { foods.upsert(it) }
                    "Cached ${result.value.size} server foods on this phone."
                }
                is RemoteCall.Err -> remoteMessage = result.message
            }
        }
    }
}

@Composable
fun FoodSearchScreen(onLogged: () -> Unit, logViewModel: FoodLogViewModel = hiltViewModel(), viewModel: FoodSearchViewModel = hiltViewModel()) {
    val local by viewModel.local.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(viewModel.query, viewModel::onQuery, label = { Text("Search") }, modifier = Modifier.fillMaxWidth())
        SecondaryButton("Also search the network catalog", onClick = viewModel::searchRemote)
        viewModel.remoteMessage?.let { Text(it) }
        Text("Meal")
        MealSlot.entries.forEach { slot ->
            Text(
                slot.name,
                modifier = Modifier.clickable { viewModel.slot = slot }.padding(8.dp),
                color = if (viewModel.slot == slot) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            )
        }
        OutlinedTextField(viewModel.grams, { viewModel.grams = it }, label = { Text("Grams") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        if (local.isEmpty()) EmptyPane("No local matches", "Add a custom food, or search the network when it is configured.")
        local.forEach { food ->
            VeloraCard {
                Text(food.name)
                Text("${food.provenance.name.lowercase().replace('_', ' ')} · ${food.sourceName ?: "Your entry"}")
                val serving = food.servings.firstOrNull()
                Text(serving?.let { "${it.nutrients.caloriesKcal.toInt()} kcal per ${it.label}" } ?: "No serving")
                PrimaryButton("Add") {
                    viewModel.grams.toDoubleOrNull()?.let { grams ->
                        logViewModel.logFood(food, viewModel.slot, grams)
                        onLogged()
                    }
                }
            }
        }
    }
}

@HiltViewModel
class CustomFoodViewModel @Inject constructor(
    private val foods: FoodRepository,
    private val recipes: RecipeRepository,
    private val analytics: AnalyticsTracker,
    savedState: SavedStateHandle,
) : ViewModel() {
    var name by mutableStateOf("")
    var calories by mutableStateOf("")
    var protein by mutableStateOf("")
    var carbs by mutableStateOf("")
    var fat by mutableStateOf("")
    var fiber by mutableStateOf("")
    var sugar by mutableStateOf("")
    var sodium by mutableStateOf("")
    var barcode by mutableStateOf(savedState.get<String>("barcode").orEmpty())
    var error by mutableStateOf<String?>(null)
    var recipeName by mutableStateOf("")
    val recipesFlow = recipes.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveFood(onDone: () -> Unit) {
        val nutrients = nutrientsOrNull() ?: return
        viewModelScope.launch {
            foods.upsert(
                Food(
                    id = UUID.randomUUID().toString(),
                    name = name.trim(),
                    brand = null,
                    barcode = barcode.trim().ifBlank { null },
                    servings = listOf(app.velora.core.model.FoodServing(UUID.randomUUID().toString(), "100 g", 100.0, nutrients)),
                    provenance = NutrientProvenance.USER_ENTERED,
                    sourceName = null,
                    sourceRecordId = null,
                    userCreated = true,
                ),
            )
            onDone()
        }
    }

    fun saveRecipe(ingredients: List<FoodLogEntry>, onDone: () -> Unit) {
        if (recipeName.isBlank() || ingredients.isEmpty()) {
            error = "Name the recipe and log at least one ingredient today first."
            return
        }
        viewModelScope.launch {
            recipes.upsert(
                Recipe(
                    UUID.randomUUID().toString(),
                    recipeName.trim(),
                    ingredients.map {
                        RecipeItem(UUID.randomUUID().toString(), it.foodName, it.servingLabel, it.quantity, it.nutrients)
                    },
                ),
            )
            analytics.track("meal_created")
            onDone()
        }
    }

    private fun nutrientsOrNull(): Nutrients? {
        val kcal = calories.toDoubleOrNull()
        val p = protein.toDoubleOrNull()
        val c = carbs.toDoubleOrNull()
        val f = fat.toDoubleOrNull()
        if (name.isBlank() || kcal == null || p == null || c == null || f == null) {
            error = "Name, calories, protein, carbs, and fat are required. Leave a micronutrient blank if you do not know it."
            return null
        }
        error = null
        return Nutrients(kcal, p, c, f, fiber.toDoubleOrNull(), sugar.toDoubleOrNull(), sodium.toDoubleOrNull(), NutrientProvenance.USER_ENTERED)
    }
}

@Composable
fun CustomFoodScreen(onDone: () -> Unit, viewModel: CustomFoodViewModel = hiltViewModel()) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom food per 100 g", style = MaterialTheme.typography.titleLarge)
        Text("Values you type are marked as user-entered. Blank fiber, sugar, or sodium stays unknown.")
        listOf(
            "Name" to viewModel.name,
        )
        OutlinedTextField(viewModel.name, { viewModel.name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(viewModel.barcode, { viewModel.barcode = it }, label = { Text("Barcode, optional") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(viewModel.calories, { viewModel.calories = it }, label = { Text("Calories") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.protein, { viewModel.protein = it }, label = { Text("Protein g") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.carbs, { viewModel.carbs = it }, label = { Text("Carbs g") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.fat, { viewModel.fat = it }, label = { Text("Fat g") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.fiber, { viewModel.fiber = it }, label = { Text("Fiber g, optional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.sugar, { viewModel.sugar = it }, label = { Text("Sugar g, optional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(viewModel.sodium, { viewModel.sodium = it }, label = { Text("Sodium mg, optional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        PrimaryButton("Save food") { viewModel.saveFood(onDone) }
    }
}

@Composable
fun RecipeScreen(onDone: () -> Unit, logViewModel: FoodLogViewModel = hiltViewModel(), viewModel: CustomFoodViewModel = hiltViewModel()) {
    val today by logViewModel.entries.collectAsStateWithLifecycle()
    val recipes by viewModel.recipesFlow.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Recipe from this day's log", style = MaterialTheme.typography.titleLarge)
        Text("Nutrition is the sum of the logged items. If any item is missing fiber, the recipe fiber is unknown.")
        OutlinedTextField(viewModel.recipeName, { viewModel.recipeName = it }, label = { Text("Recipe name") }, modifier = Modifier.fillMaxWidth())
        Text("${today.size} items will be copied")
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        PrimaryButton("Save recipe") { viewModel.saveRecipe(today, onDone) }
        recipes.forEach { recipe ->
            VeloraCard {
                Text(recipe.name)
                Text("${recipe.nutrients.caloriesKcal.toInt()} kcal · ${recipe.nutrients.provenance.name.lowercase().replace('_', ' ')}")
            }
        }
    }
}

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val foods: FoodRepository,
    private val remote: RemoteFoodDataSource,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    var message by mutableStateOf("Point the camera at a product barcode.")
    var matches by mutableStateOf<List<Food>>(emptyList())
    var unknownCode by mutableStateOf<String?>(null)

    fun onCode(code: String) {
        if (matches.isNotEmpty() || unknownCode == code) return
        viewModelScope.launch {
            analytics.track("barcode_scanned")
            val local = foods.findByBarcode(code)
            if (local.size == 1) {
                matches = local
                message = local.single().name
                return@launch
            }
            if (local.size > 1) {
                matches = local
                message = "Several saved products share this code. Choose one."
                return@launch
            }
            when (val result = remote.barcode(code)) {
                is RemoteCall.Ok -> {
                    matches = result.value.foods
                    message = if (result.value.foods.size > 1) "Multiple server matches. Choose one." else result.value.foods.single().name
                }
                is RemoteCall.Err -> when (result.error) {
                    RemoteError.NOT_FOUND, RemoteError.UNAVAILABLE -> {
                        unknownCode = code
                        message = result.message
                    }
                    RemoteError.INVALID -> message = result.message
                    else -> message = result.message
                }
            }
        }
    }
}

@Composable
fun BarcodeScreen(onCreate: (String) -> Unit, onLogged: () -> Unit, logViewModel: FoodLogViewModel = hiltViewModel(), viewModel: BarcodeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted = it }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("SMART FOOD ENTRY", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Scan a barcode", style = MaterialTheme.typography.headlineMedium)
            Text(viewModel.message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (!granted) {
            ErrorPane("Camera permission is off. You can still type a code into a custom food.") { launcher.launch(Manifest.permission.CAMERA) }
        } else {
            Surface(
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                CameraPreview(
                    onCode = viewModel::onCode,
                    modifier = Modifier.fillMaxWidth().height(360.dp).padding(5.dp).clip(RoundedCornerShape(23.dp)),
                )
            }
            Text(
                "Hold the package steady and keep the full barcode inside the frame.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        viewModel.matches.forEach { food ->
            VeloraCard {
                Text(food.name, style = MaterialTheme.typography.titleLarge)
                food.brand?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                PrimaryButton("Add to today") {
                    logViewModel.logFood(food, MealSlot.SNACK, 100.0)
                    onLogged()
                }
            }
        }
        viewModel.unknownCode?.let { code ->
            SecondaryButton("Create a product for $code") { onCreate(code) }
        }
    }
}

@Composable
private fun CameraPreview(onCode: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    DisposableEffect(lifecycleOwner) {
        val executor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val listener = Runnable {
            val provider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
            val analysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            val scanner = BarcodeScanning.getClient()
            analysis.setAnalyzer(executor) { imageProxy ->
                val media = imageProxy.image
                if (media == null) {
                    imageProxy.close()
                    return@setAnalyzer
                }
                val image = InputImage.fromMediaImage(media, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image).addOnCompleteListener {
                    imageProxy.close()
                }.addOnSuccessListener { codes ->
                    codes.firstOrNull()?.rawValue?.let(onCode)
                }
            }
            provider.unbindAll()
            provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
        }
        cameraProviderFuture.addListener(listener, ContextCompat.getMainExecutor(context))
        onDispose {
            executor.shutdown()
            runCatching { cameraProviderFuture.get().unbindAll() }
        }
    }
    AndroidView(factory = { previewView }, modifier = modifier)
}

@HiltViewModel
class FoodScanViewModel @Inject constructor(
    private val recognition: RemoteRecognitionDataSource,
    @Named("recognitionConfigured") private val configured: Boolean,
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    private val coordinator = FoodScanCoordinator(configured)
    var state by mutableStateOf<ScanState>(ScanState.Idle)
        private set
    private var jpeg: ByteArray? = null

    fun capture(bytes: ByteArray) {
        jpeg = bytes
        state = coordinator.reduce(state, ScanEvent.PhotoCaptured("local"))
    }

    fun onEvent(event: ScanEvent) {
        val next = coordinator.reduce(state, event)
        state = next
        if (next is ScanState.Analyzing) {
            val payload = jpeg
            jpeg = null
            viewModelScope.launch {
                analytics.track("ai_food_scan")
                val result = if (payload == null) {
                    FoodRecognitionResult.Failed("The photo was already discarded.", retryable = false)
                } else {
                    recognition.recognize(payload)
                }
                state = coordinator.reduce(state, ScanEvent.RecognitionFinished(result))
            }
        }
        if (next.shouldDiscardImage) jpeg = null
    }
}

@Composable
fun FoodScanScreen(onLogged: () -> Unit, logViewModel: FoodLogViewModel = hiltViewModel(), viewModel: FoodScanViewModel = hiltViewModel()) {
    val state = viewModel.state
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Food photo", style = MaterialTheme.typography.titleLarge)
        Text("A photo is not logged by itself. If a recognizer is configured, you confirm the upload and then pick a match.")
        when (state) {
            ScanState.Idle -> PrimaryButton("Use a sample capture") {
                viewModel.capture(byteArrayOf(1, 2, 3))
            }
            is ScanState.Preview -> PrimaryButton("Analyze") { viewModel.onEvent(ScanEvent.Analyze) }
            is ScanState.NeedsUploadConsent -> {
                Text("This photo will be sent to the configured recognition service.")
                PrimaryButton("Upload and analyze") { viewModel.onEvent(ScanEvent.ConfirmUpload) }
            }
            is ScanState.Analyzing -> Text("Waiting for the service.")
            is ScanState.Unavailable -> ErrorPane(state.message, null)
            is ScanState.Failed -> ErrorPane(state.message) { viewModel.onEvent(ScanEvent.Retake) }
            is ScanState.Candidates -> {
                Text("Possible matches. Nutrients are not invented for a label without a food id.")
                state.items.forEachIndexed { index, candidate ->
                    SecondaryButton("${candidate.label} · ${(candidate.confidence * 100).toInt()}%") {
                        viewModel.onEvent(ScanEvent.Select(index))
                    }
                }
            }
            is ScanState.Portion -> {
                Text(state.candidate.label)
                PrimaryButton("Log 100 g after you attach nutrition in custom food if needed") {
                    viewModel.onEvent(ScanEvent.ConfirmPortion)
                }
            }
            is ScanState.ReadyToLog -> {
                Text("Confirmed ${state.candidate.label}, ${state.grams} g. A label without a food id is not given calories.")
                if (state.candidate.foodId == null) {
                    Text("Create a custom food if you want numbers. Nothing was added automatically.")
                }
                SecondaryButton("Done", onClick = onLogged)
            }
        }
    }
}

private fun fmt(value: Double) = ((value * 10).toInt() / 10.0).toString()
private fun optional(value: Double?) = value?.let { fmt(it) } ?: "not provided"
