package app.velora.feature.nutrition;

import androidx.lifecycle.SavedStateHandle;
import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.domain.FoodRepository;
import app.velora.core.domain.RecipeRepository;
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
public final class CustomFoodViewModel_Factory implements Factory<CustomFoodViewModel> {
  private final Provider<FoodRepository> foodsProvider;

  private final Provider<RecipeRepository> recipesProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private final Provider<SavedStateHandle> savedStateProvider;

  private CustomFoodViewModel_Factory(Provider<FoodRepository> foodsProvider,
      Provider<RecipeRepository> recipesProvider, Provider<AnalyticsTracker> analyticsProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    this.foodsProvider = foodsProvider;
    this.recipesProvider = recipesProvider;
    this.analyticsProvider = analyticsProvider;
    this.savedStateProvider = savedStateProvider;
  }

  @Override
  public CustomFoodViewModel get() {
    return newInstance(foodsProvider.get(), recipesProvider.get(), analyticsProvider.get(), savedStateProvider.get());
  }

  public static CustomFoodViewModel_Factory create(Provider<FoodRepository> foodsProvider,
      Provider<RecipeRepository> recipesProvider, Provider<AnalyticsTracker> analyticsProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    return new CustomFoodViewModel_Factory(foodsProvider, recipesProvider, analyticsProvider, savedStateProvider);
  }

  public static CustomFoodViewModel newInstance(FoodRepository foods, RecipeRepository recipes,
      AnalyticsTracker analytics, SavedStateHandle savedState) {
    return new CustomFoodViewModel(foods, recipes, analytics, savedState);
  }
}
