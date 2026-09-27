package app.velora.core.database;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class RoomPersonalDataRepository_Factory implements Factory<RoomPersonalDataRepository> {
  private final Provider<VeloraDatabase> databaseProvider;

  private final Provider<DataStoreSettingsRepository> settingsProvider;

  private final Provider<CatalogSeeder> seederProvider;

  private final Provider<Context> contextProvider;

  private RoomPersonalDataRepository_Factory(Provider<VeloraDatabase> databaseProvider,
      Provider<DataStoreSettingsRepository> settingsProvider,
      Provider<CatalogSeeder> seederProvider, Provider<Context> contextProvider) {
    this.databaseProvider = databaseProvider;
    this.settingsProvider = settingsProvider;
    this.seederProvider = seederProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public RoomPersonalDataRepository get() {
    return newInstance(databaseProvider.get(), settingsProvider.get(), seederProvider.get(), contextProvider.get());
  }

  public static RoomPersonalDataRepository_Factory create(Provider<VeloraDatabase> databaseProvider,
      Provider<DataStoreSettingsRepository> settingsProvider,
      Provider<CatalogSeeder> seederProvider, Provider<Context> contextProvider) {
    return new RoomPersonalDataRepository_Factory(databaseProvider, settingsProvider, seederProvider, contextProvider);
  }

  public static RoomPersonalDataRepository newInstance(VeloraDatabase database,
      DataStoreSettingsRepository settings, CatalogSeeder seeder, Context context) {
    return new RoomPersonalDataRepository(database, settings, seeder, context);
  }
}
