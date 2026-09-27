package app.velora.track;

import app.velora.core.network.RemoteFoodDataSource;
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
public final class RemoteModule_RemoteFoodsFactory implements Factory<RemoteFoodDataSource> {
  @Override
  public RemoteFoodDataSource get() {
    return remoteFoods();
  }

  public static RemoteModule_RemoteFoodsFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RemoteFoodDataSource remoteFoods() {
    return Preconditions.checkNotNullFromProvides(RemoteModule.INSTANCE.remoteFoods());
  }

  private static final class InstanceHolder {
    static final RemoteModule_RemoteFoodsFactory INSTANCE = new RemoteModule_RemoteFoodsFactory();
  }
}
