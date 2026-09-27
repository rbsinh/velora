package app.velora.feature.activity;

import app.velora.core.analytics.AnalyticsTracker;
import app.velora.core.domain.ActivityRepository;
import app.velora.core.domain.StepRepository;
import app.velora.core.health.HealthConnectGateway;
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
public final class ActivityViewModel_Factory implements Factory<ActivityViewModel> {
  private final Provider<StepRepository> stepsProvider;

  private final Provider<ActivityRepository> activitiesProvider;

  private final Provider<HealthConnectGateway> healthProvider;

  private final Provider<AnalyticsTracker> analyticsProvider;

  private ActivityViewModel_Factory(Provider<StepRepository> stepsProvider,
      Provider<ActivityRepository> activitiesProvider,
      Provider<HealthConnectGateway> healthProvider, Provider<AnalyticsTracker> analyticsProvider) {
    this.stepsProvider = stepsProvider;
    this.activitiesProvider = activitiesProvider;
    this.healthProvider = healthProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public ActivityViewModel get() {
    return newInstance(stepsProvider.get(), activitiesProvider.get(), healthProvider.get(), analyticsProvider.get());
  }

  public static ActivityViewModel_Factory create(Provider<StepRepository> stepsProvider,
      Provider<ActivityRepository> activitiesProvider,
      Provider<HealthConnectGateway> healthProvider, Provider<AnalyticsTracker> analyticsProvider) {
    return new ActivityViewModel_Factory(stepsProvider, activitiesProvider, healthProvider, analyticsProvider);
  }

  public static ActivityViewModel newInstance(StepRepository steps, ActivityRepository activities,
      HealthConnectGateway health, AnalyticsTracker analytics) {
    return new ActivityViewModel(steps, activities, health, analytics);
  }
}
