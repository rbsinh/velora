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
public final class RoomWaterRepository_Factory implements Factory<RoomWaterRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomWaterRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomWaterRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomWaterRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomWaterRepository_Factory(daoProvider);
  }

  public static RoomWaterRepository newInstance(VeloraDao dao) {
    return new RoomWaterRepository(dao);
  }
}
