package app.velora.feature.nutrition;

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
public final class FoodSearchViewModel_Factory implements Factory<FoodSearchViewModel> {
  private final Provider<FoodRepository> foodsProvider;

  private final Provider<RemoteFoodDataSource> remoteProvider;

  private FoodSearchViewModel_Factory(Provider<FoodRepository> foodsProvider,
      Provider<RemoteFoodDataSource> remoteProvider) {
    this.foodsProvider = foodsProvider;
    this.remoteProvider = remoteProvider;
  }

  @Override
  public FoodSearchViewModel get() {
    return newInstance(foodsProvider.get(), remoteProvider.get());
  }

  public static FoodSearchViewModel_Factory create(Provider<FoodRepository> foodsProvider,
      Provider<RemoteFoodDataSource> remoteProvider) {
    return new FoodSearchViewModel_Factory(foodsProvider, remoteProvider);
  }

  public static FoodSearchViewModel newInstance(FoodRepository foods, RemoteFoodDataSource remote) {
    return new FoodSearchViewModel(foods, remote);
  }
}
