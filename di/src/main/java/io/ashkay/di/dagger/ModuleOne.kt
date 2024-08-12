package io.ashkay.di.dagger

import dagger.Module
import dagger.Provides
import io.ashkay.di.models.UpdateManager
import kotlin.random.Random

@Module
class ModuleOne {

    @Provides
    fun provideUpdateManager(): UpdateManager {
        return UpdateManager( Random.nextInt(), "http://example.com")
    }

}