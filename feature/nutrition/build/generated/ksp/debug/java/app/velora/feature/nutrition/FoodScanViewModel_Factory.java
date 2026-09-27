package app.velora.feature.nutrition;

import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.network.RemoteRecognitionDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("javax.inject.Named")
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
public final class FoodScanViewModel_Factory implements Factory<FoodScanViewModel> {
  private final Provider<RemoteRecognitionDataSource> recognitionProvider;

  private final Provider<Boolean> configuredProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private FoodScanViewModel_Factory(Provider<RemoteRecognitionDataSource> recognitionProvider,
      Provider<Boolean> configuredProvider, Provider<AnalyticsTracker> analyticsProvider) {
    this.recognitionProvider = recognitionProvider;
    this.configuredProvider = configuredProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public FoodScanViewModel get() {
    return newInstance(recognitionProvider.get(), configuredProvider.get(), analyticsProvider.get());
  }

  public static FoodScanViewModel_Factory create(
      Provider<RemoteRecognitionDataSource> recognitionProvider,
      Provider<Boolean> configuredProvider, Provider<AnalyticsTracker> analyticsProvider) {
    return new FoodScanViewModel_Factory(recognitionProvider, configuredProvider, analyticsProvider);
  }

  public static FoodScanViewModel newInstance(RemoteRecognitionDataSource recognition,
      boolean configured, AnalyticsTracker analytics) {
    return new FoodScanViewModel(recognition, configured, analytics);
  }
}
