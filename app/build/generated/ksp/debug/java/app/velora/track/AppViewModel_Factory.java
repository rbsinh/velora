package app.velora.track;

import app.velora.core.database.CatalogSeeder;
import app.velora.core.domain.SettingsRepository;
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
public final class AppViewModel_Factory implements Factory<AppViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<CatalogSeeder> seederProvider;

  private AppViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<CatalogSeeder> seederProvider) {
    this.settingsProvider = settingsProvider;
    this.seederProvider = seederProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(settingsProvider.get(), seederProvider.get());
  }

  public static AppViewModel_Factory create(Provider<SettingsRepository> settingsProvider,
      Provider<CatalogSeeder> seederProvider) {
    return new AppViewModel_Factory(settingsProvider, seederProvider);
  }

  public static AppViewModel newInstance(SettingsRepository settings, CatalogSeeder seeder) {
    return new AppViewModel(settings, seeder);
  }
}
