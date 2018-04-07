package io.gumil.rijksmuseum.detail

import android.arch.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.gumil.rijksmuseum.data.repository.RijksRepository
import io.gumil.rijksmuseum.viewmodel.ViewModelKey

@Module
internal abstract class RijksDetailBuilder {

    @ContributesAndroidInjector(
            modules = [RijksDetailModule::class]
    )
    internal abstract fun rijksDetailFragment(): RijksDetailFragment

}

@Module
internal class RijksDetailModule {

    @Provides
    @IntoMap
    @ViewModelKey(RijksDetailViewModel::class)
    fun provideRijksDetailViewModel(
            repository: RijksRepository
    ): ViewModel = RijksDetailViewModel(repository)
}