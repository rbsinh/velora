package app.velora.core.database;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_DaoFactory implements Factory<VeloraDao> {
  private final Provider<VeloraDatabase> databaseProvider;

  private DatabaseModule_DaoFactory(Provider<VeloraDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public VeloraDao get() {
    return dao(databaseProvider.get());
  }

  public static DatabaseModule_DaoFactory create(Provider<VeloraDatabase> databaseProvider) {
    return new DatabaseModule_DaoFactory(databaseProvider);
  }

  public static VeloraDao dao(VeloraDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.dao(database));
  }
}
