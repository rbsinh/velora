package app.velora.feature.workout;

import app.velora.core.analytics.AnalyticsTracker;
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
public final class WorkoutViewModel_Factory implements Factory<WorkoutViewModel> {
  private final Provider<WorkoutRepository> workoutsProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private WorkoutViewModel_Factory(Provider<WorkoutRepository> workoutsProvider,
      Provider<AnalyticsTracker> analyticsProvider) {
    this.workoutsProvider = workoutsProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public WorkoutViewModel get() {
    return newInstance(workoutsProvider.get(), analyticsProvider.get());
  }

  public static WorkoutViewModel_Factory create(Provider<WorkoutRepository> workoutsProvider,
      Provider<AnalyticsTracker> analyticsProvider) {
    return new WorkoutViewModel_Factory(workoutsProvider, analyticsProvider);
  }

  public static WorkoutViewModel newInstance(WorkoutRepository workouts,
      AnalyticsTracker analytics) {
    return new WorkoutViewModel(workouts, analytics);
  }
}
