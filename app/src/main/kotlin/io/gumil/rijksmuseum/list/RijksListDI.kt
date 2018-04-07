package io.gumil.rijksmuseum.list

import android.arch.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.gumil.rijksmuseum.data.repository.RijksRepository
import io.gumil.rijksmuseum.viewmodel.ViewModelKey

@Module
internal abstract class RijksListBuilder {

    @ContributesAndroidInjector(
            modules = [RijksListModule::class]
    )
    internal abstract fun rijksListFragment(): RijksListFragment

}

@Module
internal class RijksListModule {

    @Provides
    @IntoMap
    @ViewModelKey(RijksListViewModel::class)
    fun provideRijksListViewModel(
            repository: RijksRepository
    ): ViewModel = RijksListViewModel(repository)
}