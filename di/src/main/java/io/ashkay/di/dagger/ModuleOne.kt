package io.ashkay.di.dagger

import dagger.Module
import dagger.Provides
import io.ashkay.di.models.RemoteConfig
import io.ashkay.di.models.UpdateManager
import io.ashkay.di.models.UpdateManagerSameTTL
import kotlin.random.Random

@Module
class ModuleOne {

    @Provides
    fun provideUpdateManagerSameTTL(): UpdateManagerSameTTL {
        return UpdateManagerSameTTL(Random.nextInt(), "http://example.com")
    }

    @Provides
    fun provideUpdateManager(remoteConfig: RemoteConfig): UpdateManager {
        return UpdateManager(remoteConfig, "http://example.com")
    }

}