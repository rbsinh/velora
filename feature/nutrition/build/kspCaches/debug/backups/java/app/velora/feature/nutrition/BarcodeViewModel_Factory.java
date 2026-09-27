package app.velora.feature.nutrition;

import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.domain.FoodRepository;
import app.velora.core.network.RemoteFoodDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class BarcodeViewModel_Factory implements Factory<BarcodeViewModel> {
  private final Provider<FoodRepository> foodsProvider;

  private final Provider<RemoteFoodDataSource> remoteProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private BarcodeViewModel_Factory(Provider<FoodRepository> foodsProvider,
      Provider<RemoteFoodDataSource> remoteProvider, Provider<AnalyticsTracker> analyticsProvider) {
    this.foodsProvider = foodsProvider;
    this.remoteProvider = remoteProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public BarcodeViewModel get() {
    return newInstance(foodsProvider.get(), remoteProvider.get(), analyticsProvider.get());
  }

  public static BarcodeViewModel_Factory create(Provider<FoodRepository> foodsProvider,
      Provider<RemoteFoodDataSource> remoteProvider, Provider<AnalyticsTracker> analyticsProvider) {
    return new BarcodeViewModel_Factory(foodsProvider, remoteProvider, analyticsProvider);
  }

  public static BarcodeViewModel newInstance(FoodRepository foods, RemoteFoodDataSource remote,
      AnalyticsTracker analytics) {
    return new BarcodeViewModel(foods, remote, analytics);
  }
}
