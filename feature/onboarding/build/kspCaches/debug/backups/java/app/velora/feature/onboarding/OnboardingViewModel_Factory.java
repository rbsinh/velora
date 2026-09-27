package app.velora.feature.onboarding;

import app.velora.core.analytics.AnalyticsTracker;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<ProfileRepository> profilesProvider;

  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private OnboardingViewModel_Factory(Provider<ProfileRepository> profilesProvider,
      Provider<SettingsRepository> settingsProvider, Provider<AnalyticsTracker> analyticsProvider) {
    this.profilesProvider = profilesProvider;
    this.settingsProvider = settingsProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(profilesProvider.get(), settingsProvider.get(), analyticsProvider.get());
  }

  public static OnboardingViewModel_Factory create(Provider<ProfileRepository> profilesProvider,
      Provider<SettingsRepository> settingsProvider, Provider<AnalyticsTracker> analyticsProvider) {
    return new OnboardingViewModel_Factory(profilesProvider, settingsProvider, analyticsProvider);
  }

  public static OnboardingViewModel newInstance(ProfileRepository profiles,
      SettingsRepository settings, AnalyticsTracker analytics) {
    return new OnboardingViewModel(profiles, settings, analytics);
  }
}
