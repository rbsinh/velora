package app.velora.feature.dashboard;

import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.domain.FoodLogRepository;
import app.velora.core.domain.ProfileRepository;
import app.velora.core.domain.StepRepository;
import app.velora.core.domain.WaterRepository;
import app.velora.core.domain.WeightRepository;
import app.velora.core.domain.WorkoutRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ProfileRepository> profilesProvider;

  private final Provider<FoodLogRepository> logsProvider;

  private final Provider<StepRepository> stepsProvider;

  private final Provider<WeightRepository> weightsProvider;

  private final Provider<WorkoutRepository> workoutsProvider;

  private final Provider<WaterRepository> waterProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private DashboardViewModel_Factory(Provider<ProfileRepository> profilesProvider,
      Provider<FoodLogRepository> logsProvider, Provider<StepRepository> stepsProvider,
      Provider<WeightRepository> weightsProvider, Provider<WorkoutRepository> workoutsProvider,
      Provider<WaterRepository> waterProvider, Provider<AnalyticsTracker> analyticsProvider) {
    this.profilesProvider = profilesProvider;
    this.logsProvider = logsProvider;
    this.stepsProvider = stepsProvider;
    this.weightsProvider = weightsProvider;
    this.workoutsProvider = workoutsProvider;
    this.waterProvider = waterProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(profilesProvider.get(), logsProvider.get(), stepsProvider.get(), weightsProvider.get(), workoutsProvider.get(), waterProvider.get(), analyticsProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<ProfileRepository> profilesProvider,
      Provider<FoodLogRepository> logsProvider, Provider<StepRepository> stepsProvider,
      Provider<WeightRepository> weightsProvider, Provider<WorkoutRepository> workoutsProvider,
      Provider<WaterRepository> waterProvider, Provider<AnalyticsTracker> analyticsProvider) {
    return new DashboardViewModel_Factory(profilesProvider, logsProvider, stepsProvider, weightsProvider, workoutsProvider, waterProvider, analyticsProvider);
  }

  public static DashboardViewModel newInstance(ProfileRepository profiles, FoodLogRepository logs,
      StepRepository steps, WeightRepository weights, WorkoutRepository workouts,
      WaterRepository water, AnalyticsTracker analytics) {
    return new DashboardViewModel(profiles, logs, steps, weights, workouts, water, analytics);
  }
}
