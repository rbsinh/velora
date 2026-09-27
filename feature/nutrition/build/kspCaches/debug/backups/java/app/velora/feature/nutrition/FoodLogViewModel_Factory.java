package app.velora.feature.nutrition;

import androidx.lifecycle.SavedStateHandle;
import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.domain.FoodLogRepository;
import app.velora.core.domain.MealTemplateRepository;
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
public final class FoodLogViewModel_Factory implements Factory<FoodLogViewModel> {
  private final Provider<FoodLogRepository> logsProvider;

  private final Provider<MealTemplateRepository> mealsProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private final Provider<SavedStateHandle> savedStateProvider;

  private FoodLogViewModel_Factory(Provider<FoodLogRepository> logsProvider,
      Provider<MealTemplateRepository> mealsProvider, Provider<AnalyticsTracker> analyticsProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    this.logsProvider = logsProvider;
    this.mealsProvider = mealsProvider;
    this.analyticsProvider = analyticsProvider;
    this.savedStateProvider = savedStateProvider;
  }

  @Override
  public FoodLogViewModel get() {
    return newInstance(logsProvider.get(), mealsProvider.get(), analyticsProvider.get(), savedStateProvider.get());
  }

  public static FoodLogViewModel_Factory create(Provider<FoodLogRepository> logsProvider,
      Provider<MealTemplateRepository> mealsProvider, Provider<AnalyticsTracker> analyticsProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    return new FoodLogViewModel_Factory(logsProvider, mealsProvider, analyticsProvider, savedStateProvider);
  }

  public static FoodLogViewModel newInstance(FoodLogRepository logs, MealTemplateRepository meals,
      AnalyticsTracker analytics, SavedStateHandle savedState) {
    return new FoodLogViewModel(logs, meals, analytics, savedState);
  }
}
