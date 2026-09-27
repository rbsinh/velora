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
public final class RoomActivityRepository_Factory implements Factory<RoomActivityRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomActivityRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomActivityRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomActivityRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomActivityRepository_Factory(daoProvider);
  }

  public static RoomActivityRepository newInstance(VeloraDao dao) {
    return new RoomActivityRepository(dao);
  }
}
