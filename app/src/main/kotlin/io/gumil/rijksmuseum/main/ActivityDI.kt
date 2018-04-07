package io.gumil.rijksmuseum.main

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.gumil.rijksmuseum.detail.RijksDetailBuilder
import io.gumil.rijksmuseum.list.RijksListBuilder
import io.gumil.rijksmuseum.viewmodel.ViewModelBuilder
import javax.inject.Scope

@Module(includes = [AndroidSupportInjectionModule::class])
internal abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                ViewModelBuilder::class,
                ActivityModule::class,
                RijksListBuilder::class,
                RijksDetailBuilder::class
            ]
    )
    internal abstract fun bindMainActivity(): MainActivity

}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ActivityScope

@Module
internal class ActivityModule {

    @Provides
    fun provideBackstack(activity: MainActivity): Backstack = activity
}
