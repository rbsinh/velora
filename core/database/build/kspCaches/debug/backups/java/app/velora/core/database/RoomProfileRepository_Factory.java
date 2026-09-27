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
public final class RoomProfileRepository_Factory implements Factory<RoomProfileRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomProfileRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomProfileRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomProfileRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomProfileRepository_Factory(daoProvider);
  }

  public static RoomProfileRepository newInstance(VeloraDao dao) {
    return new RoomProfileRepository(dao);
  }
}
