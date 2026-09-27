package app.velora.feature.metrics;

import app.velora.core.domain.FoodLogRepository;
import app.velora.core.domain.ProfileRepository;
import app.velora.core.domain.StepRepository;
import app.velora.core.domain.WeightRepository;
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
public final class MetricsViewModel_Factory implements Factory<MetricsViewModel> {
  private final Provider<WeightRepository> weightsProvider;

  private final Provider<ProfileRepository> profilesProvider;

  private final Provider<FoodLogRepository> logsProvider;

  private final Provider<StepRepository> stepsProvider;

  private MetricsViewModel_Factory(Provider<WeightRepository> weightsProvider,
      Provider<ProfileRepository> profilesProvider, Provider<FoodLogRepository> logsProvider,
      Provider<StepRepository> stepsProvider) {
    this.weightsProvider = weightsProvider;
    this.profilesProvider = profilesProvider;
    this.logsProvider = logsProvider;
    this.stepsProvider = stepsProvider;
  }

  @Override
  public MetricsViewModel get() {
    return newInstance(weightsProvider.get(), profilesProvider.get(), logsProvider.get(), stepsProvider.get());
  }

  public static MetricsViewModel_Factory create(Provider<WeightRepository> weightsProvider,
      Provider<ProfileRepository> profilesProvider, Provider<FoodLogRepository> logsProvider,
      Provider<StepRepository> stepsProvider) {
    return new MetricsViewModel_Factory(weightsProvider, profilesProvider, logsProvider, stepsProvider);
  }

  public static MetricsViewModel newInstance(WeightRepository weights, ProfileRepository profiles,
      FoodLogRepository logs, StepRepository steps) {
    return new MetricsViewModel(weights, profiles, logs, steps);
  }
}
