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
public final class RoomWorkoutRepository_Factory implements Factory<RoomWorkoutRepository> {
  private final Provider<VeloraDao> daoProvider;

  private RoomWorkoutRepository_Factory(Provider<VeloraDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomWorkoutRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomWorkoutRepository_Factory create(Provider<VeloraDao> daoProvider) {
    return new RoomWorkoutRepository_Factory(daoProvider);
  }

  public static RoomWorkoutRepository newInstance(VeloraDao dao) {
    return new RoomWorkoutRepository(dao);
  }
}
