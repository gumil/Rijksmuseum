package io.gumil.rijksmuseum.data.repository

import dagger.Module
import dagger.Provides
import io.gumil.rijksmuseum.data.DataScope
import io.gumil.rijksmuseum.data.network.RijksmuseumApi

@Module
internal class RepositoryModule {

    @Provides
    @DataScope
    fun providesRijksRepository(rijksmuseumApi: RijksmuseumApi): RijksRepository =
            DataRijksRepository(rijksmuseumApi)
}