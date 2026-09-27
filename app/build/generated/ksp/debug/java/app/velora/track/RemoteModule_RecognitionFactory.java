package app.velora.track;

import app.velora.core.network.RemoteRecognitionDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RemoteModule_RecognitionFactory implements Factory<RemoteRecognitionDataSource> {
  @Override
  public RemoteRecognitionDataSource get() {
    return recognition();
  }

  public static RemoteModule_RecognitionFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RemoteRecognitionDataSource recognition() {
    return Preconditions.checkNotNullFromProvides(RemoteModule.INSTANCE.recognition());
  }

  private static final class InstanceHolder {
    static final RemoteModule_RecognitionFactory INSTANCE = new RemoteModule_RecognitionFactory();
  }
}
