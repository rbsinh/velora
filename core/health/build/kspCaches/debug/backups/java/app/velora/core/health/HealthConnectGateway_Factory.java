package app.velora.core.health;

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
public final class HealthConnectGateway_Factory implements Factory<HealthConnectGateway> {
  private final Provider<Context> contextProvider;

  private HealthConnectGateway_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public HealthConnectGateway get() {
    return newInstance(contextProvider.get());
  }

  public static HealthConnectGateway_Factory create(Provider<Context> contextProvider) {
    return new HealthConnectGateway_Factory(contextProvider);
  }

  public static HealthConnectGateway newInstance(Context context) {
    return new HealthConnectGateway(context);
  }
}
