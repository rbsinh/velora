package app.velora.track;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import app.velora.core.analytics.LogcatNameOnlyAnalytics;
import app.velora.core.database.CatalogSeeder;
import app.velora.core.database.DataStoreSettingsRepository;
import app.velora.core.database.DatabaseModule_DaoFactory;
import app.velora.core.database.DatabaseModule_DatabaseFactory;
import app.velora.core.database.RoomActivityRepository;
import app.velora.core.database.RoomFoodLogRepository;
import app.velora.core.database.RoomFoodRepository;
import app.velora.core.database.RoomMealRepository;
import app.velora.core.database.RoomPersonalDataRepository;
import app.velora.core.database.RoomProfileRepository;
import app.velora.core.database.RoomRecipeRepository;
import app.velora.core.database.RoomStepRepository;
import app.velora.core.database.RoomWaterRepository;
import app.velora.core.database.RoomWeightRepository;
import app.velora.core.database.RoomWorkoutRepository;
import app.velora.core.database.VeloraDao;
import app.velora.core.database.VeloraDatabase;
import app.velora.core.domain.SettingsRepository;
import app.velora.core.health.HealthConnectGateway;
import app.velora.core.network.RemoteFoodDataSource;
import app.velora.core.network.RemoteRecognitionDataSource;
import app.velora.feature.activity.ActivityViewModel;
import app.velora.feature.activity.ActivityViewModel_HiltModules;
import app.velora.feature.activity.ActivityViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.activity.ActivityViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.dashboard.DashboardViewModel;
import app.velora.feature.dashboard.DashboardViewModel_HiltModules;
import app.velora.feature.dashboard.DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.dashboard.DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.metrics.MetricsViewModel;
import app.velora.feature.metrics.MetricsViewModel_HiltModules;
import app.velora.feature.metrics.MetricsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.metrics.MetricsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.nutrition.BarcodeViewModel;
import app.velora.feature.nutrition.BarcodeViewModel_HiltModules;
import app.velora.feature.nutrition.BarcodeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.nutrition.BarcodeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.nutrition.CustomFoodViewModel;
import app.velora.feature.nutrition.CustomFoodViewModel_HiltModules;
import app.velora.feature.nutrition.CustomFoodViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.nutrition.CustomFoodViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.nutrition.FoodLogViewModel;
import app.velora.feature.nutrition.FoodLogViewModel_HiltModules;
import app.velora.feature.nutrition.FoodLogViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.nutrition.FoodLogViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.nutrition.FoodScanViewModel;
import app.velora.feature.nutrition.FoodScanViewModel_HiltModules;
import app.velora.feature.nutrition.FoodScanViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.nutrition.FoodScanViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.nutrition.FoodSearchViewModel;
import app.velora.feature.nutrition.FoodSearchViewModel_HiltModules;
import app.velora.feature.nutrition.FoodSearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.nutrition.FoodSearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.onboarding.OnboardingViewModel;
import app.velora.feature.onboarding.OnboardingViewModel_HiltModules;
import app.velora.feature.onboarding.OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.onboarding.OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.profile.PlayBillingGateway;
import app.velora.feature.profile.ProfileViewModel;
import app.velora.feature.profile.ProfileViewModel_HiltModules;
import app.velora.feature.profile.ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.profile.ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import app.velora.feature.workout.WorkoutViewModel;
import app.velora.feature.workout.WorkoutViewModel_HiltModules;
import app.velora.feature.workout.WorkoutViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import app.velora.feature.workout.WorkoutViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerVeloraApplication_HiltComponents_SingletonC {
  private DaggerVeloraApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public VeloraApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements VeloraApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements VeloraApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements VeloraApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements VeloraApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements VeloraApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements VeloraApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements VeloraApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public VeloraApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends VeloraApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends VeloraApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    FragmentCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends VeloraApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends VeloraApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    ActivityCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    ImmutableMap keySetMapOfClassOfObjectAndBooleanBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, Boolean>builderWithExpectedSize(12);
      mapBuilder.put(ActivityViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ActivityViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(AppViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AppViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(BarcodeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BarcodeViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(CustomFoodViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CustomFoodViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DashboardViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(FoodLogViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FoodLogViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(FoodScanViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FoodScanViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(FoodSearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FoodSearchViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(MetricsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, MetricsViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, OnboardingViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProfileViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(WorkoutViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, WorkoutViewModel_HiltModules.KeyModule.provide());
      return mapBuilder.build();
    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(keySetMapOfClassOfObjectAndBooleanBuilder());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends VeloraApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    Provider<ActivityViewModel> activityViewModelProvider;

    Provider<AppViewModel> appViewModelProvider;

    Provider<BarcodeViewModel> barcodeViewModelProvider;

    Provider<CustomFoodViewModel> customFoodViewModelProvider;

    Provider<DashboardViewModel> dashboardViewModelProvider;

    Provider<FoodLogViewModel> foodLogViewModelProvider;

    Provider<FoodScanViewModel> foodScanViewModelProvider;

    Provider<FoodSearchViewModel> foodSearchViewModelProvider;

    Provider<MetricsViewModel> metricsViewModelProvider;

    Provider<OnboardingViewModel> onboardingViewModelProvider;

    Provider<ProfileViewModel> profileViewModelProvider;

    Provider<WorkoutViewModel> workoutViewModelProvider;

    ViewModelCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        SavedStateHandle savedStateHandleParam, ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    ImmutableMap hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder() {
      ImmutableMap.Builder mapBuilder = ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(12);
      mapBuilder.put(ActivityViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (activityViewModelProvider)));
      mapBuilder.put(AppViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (appViewModelProvider)));
      mapBuilder.put(BarcodeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (barcodeViewModelProvider)));
      mapBuilder.put(CustomFoodViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (customFoodViewModelProvider)));
      mapBuilder.put(DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (dashboardViewModelProvider)));
      mapBuilder.put(FoodLogViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (foodLogViewModelProvider)));
      mapBuilder.put(FoodScanViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (foodScanViewModelProvider)));
      mapBuilder.put(FoodSearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (foodSearchViewModelProvider)));
      mapBuilder.put(MetricsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (metricsViewModelProvider)));
      mapBuilder.put(OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (onboardingViewModelProvider)));
      mapBuilder.put(ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (profileViewModelProvider)));
      mapBuilder.put(WorkoutViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (workoutViewModelProvider)));
      return mapBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.activityViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.appViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.barcodeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.customFoodViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.foodLogViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.foodScanViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.foodSearchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.metricsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.workoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(hiltViewModelMapMapOfClassOfObjectAndProviderOfViewModelBuilder());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // app.velora.feature.activity.ActivityViewModel
          return (T) new ActivityViewModel(singletonCImpl.roomStepRepository(), singletonCImpl.roomActivityRepository(), singletonCImpl.healthConnectGatewayProvider.get(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          case 1: // app.velora.track.AppViewModel
          return (T) new AppViewModel(singletonCImpl.dataStoreSettingsRepositoryProvider.get(), singletonCImpl.catalogSeederProvider.get());

          case 2: // app.velora.feature.nutrition.BarcodeViewModel
          return (T) new BarcodeViewModel(singletonCImpl.roomFoodRepository(), singletonCImpl.remoteFoodsProvider.get(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          case 3: // app.velora.feature.nutrition.CustomFoodViewModel
          return (T) new CustomFoodViewModel(singletonCImpl.roomFoodRepository(), singletonCImpl.roomRecipeRepository(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get(), viewModelCImpl.savedStateHandle);

          case 4: // app.velora.feature.dashboard.DashboardViewModel
          return (T) new DashboardViewModel(singletonCImpl.roomProfileRepository(), singletonCImpl.roomFoodLogRepository(), singletonCImpl.roomStepRepository(), singletonCImpl.roomWeightRepository(), singletonCImpl.roomWorkoutRepository(), singletonCImpl.roomWaterRepository(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          case 5: // app.velora.feature.nutrition.FoodLogViewModel
          return (T) new FoodLogViewModel(singletonCImpl.roomFoodLogRepository(), singletonCImpl.roomMealRepository(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get(), viewModelCImpl.savedStateHandle);

          case 6: // app.velora.feature.nutrition.FoodScanViewModel
          return (T) new FoodScanViewModel(singletonCImpl.recognitionProvider.get(), RemoteModule.INSTANCE.recognitionConfigured(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          case 7: // app.velora.feature.nutrition.FoodSearchViewModel
          return (T) new FoodSearchViewModel(singletonCImpl.roomFoodRepository(), singletonCImpl.remoteFoodsProvider.get());

          case 8: // app.velora.feature.metrics.MetricsViewModel
          return (T) new MetricsViewModel(singletonCImpl.roomWeightRepository(), singletonCImpl.roomProfileRepository(), singletonCImpl.roomFoodLogRepository(), singletonCImpl.roomStepRepository());

          case 9: // app.velora.feature.onboarding.OnboardingViewModel
          return (T) new OnboardingViewModel(singletonCImpl.roomProfileRepository(), singletonCImpl.dataStoreSettingsRepositoryProvider.get(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          case 10: // app.velora.feature.profile.ProfileViewModel
          return (T) new ProfileViewModel(singletonCImpl.dataStoreSettingsRepositoryProvider.get(), singletonCImpl.roomProfileRepository(), singletonCImpl.roomPersonalDataRepositoryProvider.get(), singletonCImpl.playBillingGatewayProvider.get());

          case 11: // app.velora.feature.workout.WorkoutViewModel
          return (T) new WorkoutViewModel(singletonCImpl.roomWorkoutRepository(), singletonCImpl.logcatNameOnlyAnalyticsProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends VeloraApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends VeloraApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends VeloraApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    Provider<DataStoreSettingsRepository> dataStoreSettingsRepositoryProvider;

    Provider<VeloraDatabase> databaseProvider;

    Provider<HealthConnectGateway> healthConnectGatewayProvider;

    Provider<LogcatNameOnlyAnalytics> logcatNameOnlyAnalyticsProvider;

    Provider<CatalogSeeder> catalogSeederProvider;

    Provider<RemoteFoodDataSource> remoteFoodsProvider;

    Provider<RemoteRecognitionDataSource> recognitionProvider;

    Provider<RoomPersonalDataRepository> roomPersonalDataRepositoryProvider;

    Provider<PlayBillingGateway> playBillingGatewayProvider;

    SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    VeloraDao veloraDao() {
      return DatabaseModule_DaoFactory.dao(databaseProvider.get());
    }

    RoomStepRepository roomStepRepository() {
      return new RoomStepRepository(veloraDao());
    }

    RoomActivityRepository roomActivityRepository() {
      return new RoomActivityRepository(veloraDao());
    }

    RoomFoodRepository roomFoodRepository() {
      return new RoomFoodRepository(veloraDao());
    }

    RoomWorkoutRepository roomWorkoutRepository() {
      return new RoomWorkoutRepository(veloraDao());
    }

    RoomRecipeRepository roomRecipeRepository() {
      return new RoomRecipeRepository(veloraDao());
    }

    RoomProfileRepository roomProfileRepository() {
      return new RoomProfileRepository(veloraDao());
    }

    RoomFoodLogRepository roomFoodLogRepository() {
      return new RoomFoodLogRepository(veloraDao());
    }

    RoomWeightRepository roomWeightRepository() {
      return new RoomWeightRepository(veloraDao());
    }

    RoomWaterRepository roomWaterRepository() {
      return new RoomWaterRepository(veloraDao());
    }

    RoomMealRepository roomMealRepository() {
      return new RoomMealRepository(veloraDao());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.dataStoreSettingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<DataStoreSettingsRepository>(singletonCImpl, 0));
      this.databaseProvider = DoubleCheck.provider(new SwitchingProvider<VeloraDatabase>(singletonCImpl, 1));
      this.healthConnectGatewayProvider = DoubleCheck.provider(new SwitchingProvider<HealthConnectGateway>(singletonCImpl, 2));
      this.logcatNameOnlyAnalyticsProvider = DoubleCheck.provider(new SwitchingProvider<LogcatNameOnlyAnalytics>(singletonCImpl, 3));
      this.catalogSeederProvider = DoubleCheck.provider(new SwitchingProvider<CatalogSeeder>(singletonCImpl, 4));
      this.remoteFoodsProvider = DoubleCheck.provider(new SwitchingProvider<RemoteFoodDataSource>(singletonCImpl, 5));
      this.recognitionProvider = DoubleCheck.provider(new SwitchingProvider<RemoteRecognitionDataSource>(singletonCImpl, 6));
      this.roomPersonalDataRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RoomPersonalDataRepository>(singletonCImpl, 7));
      this.playBillingGatewayProvider = DoubleCheck.provider(new SwitchingProvider<PlayBillingGateway>(singletonCImpl, 8));
    }

    @Override
    public void injectVeloraApplication(VeloraApplication veloraApplication) {
    }

    @Override
    public SettingsRepository settings() {
      return dataStoreSettingsRepositoryProvider.get();
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // app.velora.core.database.DataStoreSettingsRepository
          return (T) new DataStoreSettingsRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // app.velora.core.database.VeloraDatabase
          return (T) DatabaseModule_DatabaseFactory.database(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // app.velora.core.health.HealthConnectGateway
          return (T) new HealthConnectGateway(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // app.velora.core.analytics.LogcatNameOnlyAnalytics
          return (T) new LogcatNameOnlyAnalytics(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // app.velora.core.database.CatalogSeeder
          return (T) new CatalogSeeder(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.roomFoodRepository(), singletonCImpl.roomWorkoutRepository(), singletonCImpl.veloraDao());

          case 5: // app.velora.core.network.RemoteFoodDataSource
          return (T) RemoteModule_RemoteFoodsFactory.remoteFoods();

          case 6: // app.velora.core.network.RemoteRecognitionDataSource
          return (T) RemoteModule_RecognitionFactory.recognition();

          case 7: // app.velora.core.database.RoomPersonalDataRepository
          return (T) new RoomPersonalDataRepository(singletonCImpl.databaseProvider.get(), singletonCImpl.dataStoreSettingsRepositoryProvider.get(), singletonCImpl.catalogSeederProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // app.velora.feature.profile.PlayBillingGateway
          return (T) new PlayBillingGateway(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
