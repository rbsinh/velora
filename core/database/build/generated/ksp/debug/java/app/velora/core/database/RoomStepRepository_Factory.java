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
public final class RoomStepRepository_Factory implements Factory<RoomStepRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomStepRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomStepRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomStepRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomStepRepository_Factory(daoProvider);
  }

  public static RoomStepRepository newInstance(VeloraDao dao) {
    return new RoomStepRepository(dao);
  }
}
