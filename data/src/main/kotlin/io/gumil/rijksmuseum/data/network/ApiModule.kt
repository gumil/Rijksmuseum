package io.gumil.rijksmuseum.data.network

import dagger.Module
import dagger.Provides
import io.gumil.rijksmuseum.data.DataScope

@Module
internal class ApiModule(
        private val isDebug: Boolean
) {

    @Provides
    @DataScope
    fun provideRijksmuseumApi(): RijksmuseumApi = ApiFactory.create(isDebug)
}