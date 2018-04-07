package io.gumil.rijksmuseum.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.gumil.rijksmuseum.list.RijksListBuilder
import io.gumil.rijksmuseum.viewmodel.ViewModelBuilder
import javax.inject.Scope

@Module(includes = [AndroidSupportInjectionModule::class])
internal abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                ViewModelBuilder::class,
                RijksListBuilder::class
            ]
    )
    internal abstract fun bindMainActivity(): MainActivity

}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ActivityScope