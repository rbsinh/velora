package app.velora.feature.profile;

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
public final class PlayBillingGateway_Factory implements Factory<PlayBillingGateway> {
  private final Provider<Context> contextProvider;

  private PlayBillingGateway_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PlayBillingGateway get() {
    return newInstance(contextProvider.get());
  }

  public static PlayBillingGateway_Factory create(Provider<Context> contextProvider) {
    return new PlayBillingGateway_Factory(contextProvider);
  }

  public static PlayBillingGateway newInstance(Context context) {
    return new PlayBillingGateway(context);
  }
}
