package app.velora.core.database;

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
public final class RoomRecipeRepository_Factory implements Factory<RoomRecipeRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomRecipeRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomRecipeRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomRecipeRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomRecipeRepository_Factory(daoProvider);
  }

  public static RoomRecipeRepository newInstance(VeloraDao dao) {
    return new RoomRecipeRepository(dao);
  }
}
