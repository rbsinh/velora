package app.velora.core.analytics;

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
public final class LogcatNameOnlyAnalytics_Factory implements Factory<LogcatNameOnlyAnalytics> {
  private final Provider<Context> contextProvider;

  private LogcatNameOnlyAnalytics_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LogcatNameOnlyAnalytics get() {
    return newInstance(contextProvider.get());
  }

  public static LogcatNameOnlyAnalytics_Factory create(Provider<Context> contextProvider) {
    return new LogcatNameOnlyAnalytics_Factory(contextProvider);
  }

  public static LogcatNameOnlyAnalytics newInstance(Context context) {
    return new LogcatNameOnlyAnalytics(context);
  }
}
