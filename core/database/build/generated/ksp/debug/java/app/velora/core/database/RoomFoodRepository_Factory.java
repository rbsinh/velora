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
public final class RoomFoodRepository_Factory implements Factory<RoomFoodRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomFoodRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomFoodRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomFoodRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomFoodRepository_Factory(daoProvider);
  }

  public static RoomFoodRepository newInstance(VeloraDao dao) {
    return new RoomFoodRepository(dao);
  }
}
