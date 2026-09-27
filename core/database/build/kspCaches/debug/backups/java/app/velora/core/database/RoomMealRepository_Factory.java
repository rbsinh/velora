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
public final class RoomMealRepository_Factory implements Factory<RoomMealRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomMealRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomMealRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomMealRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomMealRepository_Factory(daoProvider);
  }

  public static RoomMealRepository newInstance(VeloraDao dao) {
    return new RoomMealRepository(dao);
  }
}
