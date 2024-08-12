package io.ashkay.di.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.ashkay.di.DiMainActivity

@Component(modules = [ModuleOne::class])
interface MainComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MainComponent
    }

    fun inject(activity: DiMainActivity)

}