package app.velora.core.database;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class CatalogSeeder_Factory implements Factory<CatalogSeeder> {
  private final Provider<Context> contextProvider;

  private final Provider<RoomFoodRepository> foodsProvider;

  private final Provider<RoomWorkoutRepository> workoutsProvider;

  private final Provider<VeloraDao> daoProvider;

  private CatalogSeeder_Factory(Provider<Context> contextProvider,
      Provider<RoomFoodRepository> foodsProvider, Provider<RoomWorkoutRepository> workoutsProvider,
      Provider<VeloraDao> daoProvider) {
    this.contextProvider = contextProvider;
    this.foodsProvider = foodsProvider;
    this.workoutsProvider = workoutsProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public CatalogSeeder get() {
    return newInstance(contextProvider.get(), foodsProvider.get(), workoutsProvider.get(), daoProvider.get());
  }

  public static CatalogSeeder_Factory create(Provider<Context> contextProvider,
      Provider<RoomFoodRepository> foodsProvider, Provider<RoomWorkoutRepository> workoutsProvider,
      Provider<VeloraDao> daoProvider) {
    return new CatalogSeeder_Factory(contextProvider, foodsProvider, workoutsProvider, daoProvider);
  }

  public static CatalogSeeder newInstance(Context context, RoomFoodRepository foods,
      RoomWorkoutRepository workouts, VeloraDao dao) {
    return new CatalogSeeder(context, foods, workouts, dao);
  }
}
