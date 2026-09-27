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
public final class RoomFoodLogRepository_Factory implements Factory<RoomFoodLogRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomFoodLogRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomFoodLogRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomFoodLogRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomFoodLogRepository_Factory(daoProvider);
  }

  public static RoomFoodLogRepository newInstance(VeloraDao dao) {
    return new RoomFoodLogRepository(dao);
  }
}
