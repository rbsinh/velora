package app.velora.feature.profile;

import app.velora.core.domain.PersonalDataRepository;
import app.velora.core.domain.ProfileRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<ProfileRepository> profilesProvider;

  private final Provider<PersonalDataRepository> personalProvider;

  private final Provider<PlayBillingGateway> billingProvider;

  private ProfileViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<ProfileRepository> profilesProvider,
      Provider<PersonalDataRepository> personalProvider,
      Provider<PlayBillingGateway> billingProvider) {
    this.settingsProvider = settingsProvider;
    this.profilesProvider = profilesProvider;
    this.personalProvider = personalProvider;
    this.billingProvider = billingProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(settingsProvider.get(), profilesProvider.get(), personalProvider.get(), billingProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<SettingsRepository> settingsProvider,
      Provider<ProfileRepository> profilesProvider,
      Provider<PersonalDataRepository> personalProvider,
      Provider<PlayBillingGateway> billingProvider) {
    return new ProfileViewModel_Factory(settingsProvider, profilesProvider, personalProvider, billingProvider);
  }

  public static ProfileViewModel newInstance(SettingsRepository settings,
      ProfileRepository profiles, PersonalDataRepository personal, PlayBillingGateway billing) {
    return new ProfileViewModel(settings, profiles, personal, billing);
  }
}
