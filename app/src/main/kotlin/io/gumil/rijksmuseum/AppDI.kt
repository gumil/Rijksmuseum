package io.gumil.rijksmuseum

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import io.gumil.rijksmuseum.data.DataComponent
import io.gumil.rijksmuseum.main.ActivityBuilder
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@AppScope
@Component(
        modules = [AndroidSupportInjectionModule::class, ActivityBuilder::class],
        dependencies = [DataComponent::class]
)
internal interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: RijksApplication)

}