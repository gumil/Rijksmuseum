package io.gumil.rijksmuseum.data.network

import android.content.Context
import dagger.Module
import dagger.Provides
import io.gumil.rijksmuseum.data.DataScope

@Module
internal class ApiModule(
        private val context: Context,
        private val isDebug: Boolean
) {

    @Provides
    @DataScope
    fun provideRijksmuseumApi(): RijksmuseumApi = ApiFactory.create(context, isDebug)
}