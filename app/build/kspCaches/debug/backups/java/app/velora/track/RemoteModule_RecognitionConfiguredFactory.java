package app.velora.track;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("javax.inject.Named")
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
public final class RemoteModule_RecognitionConfiguredFactory implements Factory<Boolean> {
  @Override
  public Boolean get() {
    return recognitionConfigured();
  }

  public static RemoteModule_RecognitionConfiguredFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static boolean recognitionConfigured() {
    return RemoteModule.INSTANCE.recognitionConfigured();
  }

  private static final class InstanceHolder {
    static final RemoteModule_RecognitionConfiguredFactory INSTANCE = new RemoteModule_RecognitionConfiguredFactory();
  }
}
