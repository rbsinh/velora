package app.velora.feature.profile

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import app.velora.core.designsystem.ErrorPane
import app.velora.core.designsystem.PrimaryButton
import app.velora.core.designsystem.SecondaryButton
import app.velora.core.domain.PersonalDataRepository
import app.velora.core.domain.PremiumEntitlementMapper
import app.velora.core.domain.ProfileRepository
import app.velora.core.domain.SettingsRepository
import app.velora.core.model.PremiumEntitlement
import app.velora.core.model.StorePurchaseSnapshot
import app.velora.core.model.StorePurchaseState
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsResult
import com.android.billingclient.api.QueryPurchasesParams
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Singleton
class PlayBillingGateway @Inject constructor(
    @ApplicationContext context: Context,
) {
    val entitlement = MutableStateFlow(PremiumEntitlement(premium = false, productId = null))
    val message = MutableStateFlow("Play Billing is not connected.")
    private var productDetails: ProductDetails? = null
    private val client: BillingClient = BillingClient.newBuilder(context)
        .setListener { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                apply(purchases.orEmpty())
            } else {
                message.value = "Purchase update: ${result.debugMessage}"
            }
        }
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .enableAutoServiceReconnection()
        .build()

    fun connect() {
        if (client.isReady) {
            query()
            return
        }
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    query()
                } else {
                    message.value = "Billing is unavailable (${result.debugMessage}). Premium stays off."
                    entitlement.value = PremiumEntitlement(false, null)
                }
            }

            override fun onBillingServiceDisconnected() {
                message.value = "Billing disconnected."
            }
        })
    }

    fun launch(activity: Activity) {
        val details = productDetails
        val offer = details?.subscriptionOfferDetails?.firstOrNull()?.offerToken
        if (details == null || offer == null) {
            message.value = "The subscription product is not available from Play yet."
            return
        }
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .setOfferToken(offer)
                        .build(),
                ),
            )
            .build()
        client.launchBillingFlow(activity, params)
    }

    private fun query() {
        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
        ) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) apply(purchases) else {
                message.value = result.debugMessage
            }
        }
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(PremiumEntitlementMapper.MONTHLY_PRODUCT_ID)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        client.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(listOf(product)).build(),
        ) { result, details: QueryProductDetailsResult ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                productDetails = details.productDetailsList.firstOrNull()
                if (productDetails == null) message.value = "No Play subscription is configured for this build."
            }
        }
    }

    private fun apply(purchases: List<Purchase>) {
        purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED && !it.isAcknowledged }
            .forEach { purchase ->
                val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                client.acknowledgePurchase(params) { }
            }
        val snapshots = purchases.map { purchase ->
            StorePurchaseSnapshot(
                productIds = purchase.products,
                purchaseToken = purchase.purchaseToken,
                state = when (purchase.purchaseState) {
                    Purchase.PurchaseState.PURCHASED -> StorePurchaseState.PURCHASED
                    Purchase.PurchaseState.PENDING -> StorePurchaseState.PENDING
                    else -> StorePurchaseState.UNSPECIFIED
                },
                suspended = false,
            )
        }
        entitlement.value = PremiumEntitlementMapper.map(snapshots)
        message.value = if (entitlement.value.premium) "Premium is active on this Play account." else "No active Velora subscription."
    }
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settings: SettingsRepository,
    profiles: ProfileRepository,
    private val personal: PersonalDataRepository,
    private val billing: PlayBillingGateway,
) : ViewModel() {
    val theme = settings.theme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "SYSTEM")
    val reminders = settings.remindersEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())
    val profile = profiles.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val entitlement = billing.entitlement
    val billingMessage = billing.message
    var exportText by mutableStateOf<String?>(null)
    var exportReady by mutableStateOf(false)
    var wiped by mutableStateOf(false)

    init {
        billing.connect()
    }

    fun setTheme(value: String) {
        viewModelScope.launch { settings.setTheme(value) }
    }

    fun toggleReminder(key: String, enabled: Boolean) {
        viewModelScope.launch { settings.setReminder(key, enabled, 8) }
    }

    fun export() {
        viewModelScope.launch {
            exportText = personal.exportJson()
            exportReady = true
        }
    }

    fun consumeExport() {
        exportReady = false
    }

    fun wipe() {
        viewModelScope.launch {
            personal.wipe()
            wiped = true
        }
    }

    fun buy(activity: Activity) = billing.launch(activity)
}

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    onWiped: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val entitlement by viewModel.entitlement.collectAsStateWithLifecycle()
    val billingMessage by viewModel.billingMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        val text = viewModel.exportText ?: return@rememberLauncherForActivityResult
        if (uri != null) context.contentResolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
    }
    if (viewModel.wiped) onWiped()
    LaunchedEffect(viewModel.exportReady) {
        if (viewModel.exportReady && viewModel.exportText != null) {
            exportLauncher.launch("velora-export.json")
            viewModel.consumeExport()
        }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Profile")
        Text(profile?.let { "Age ${it.ageYears} · ${it.goal.name.lowercase().replace('_', ' ')}" } ?: "No profile yet")
        SecondaryButton("Edit personalization", onClick = onEditProfile)
        Text("Appearance")
        listOf("SYSTEM", "LIGHT", "DARK").forEach { mode ->
            SecondaryButton(if (mode == theme) "$mode selected" else mode) { viewModel.setTheme(mode) }
        }
        Text("Reminders are off until you enable one. They are a daily nudge, not a stream of alerts.")
        listOf("meal", "water", "steps", "workout", "weight", "goals").forEach { key ->
            androidx.compose.foundation.layout.Row {
                Text(key)
                Switch(checked = key in reminders, onCheckedChange = { viewModel.toggleReminder(key, it) })
            }
        }
        Text(billingMessage)
        Text(if (entitlement.premium) "Premium charts are available." else "Free tracking stays available. Premium is a Play subscription, not a local switch.")
        PrimaryButton("View subscription") {
            (context as? Activity)?.let(viewModel::buy)
        }
        ErrorPane("Cloud sync and photo recognition stay off until a server can verify a purchase. A local flag cannot turn them on.", null)
        SecondaryButton("Export my data", onClick = viewModel::export)
        SecondaryButton("Delete all local data", onClick = viewModel::wipe)
        Text("Velora stores the diary on this phone. There is no account to delete yet.")
    }
}
