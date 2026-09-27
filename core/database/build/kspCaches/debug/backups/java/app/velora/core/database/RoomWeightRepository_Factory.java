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
public final class RoomWeightRepository_Factory implements Factory<RoomWeightRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomWeightRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomWeightRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomWeightRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomWeightRepository_Factory(daoProvider);
  }

  public static RoomWeightRepository newInstance(VeloraDao dao) {
    return new RoomWeightRepository(dao);
  }
}
