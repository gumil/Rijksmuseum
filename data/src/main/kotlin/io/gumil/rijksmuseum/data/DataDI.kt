package io.gumil.rijksmuseum.data

import dagger.Component
import io.gumil.rijksmuseum.data.network.ApiModule
import io.gumil.rijksmuseum.data.repository.RepositoryModule
import io.gumil.rijksmuseum.data.repository.RijksRepository
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class DataScope

@DataScope
@Component(modules = [ApiModule::class, RepositoryModule::class])
interface DataComponent {
    fun rijksRepository(): RijksRepository
}

object DataDiBuilder {
    fun build(isDebug: Boolean) = DaggerDataComponent.builder()
            .apiModule(ApiModule(isDebug))
            .build()
}