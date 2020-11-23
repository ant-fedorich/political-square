package eltonio.projects.politicalsquare.di

import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import dagger.Binds
import dagger.Module
import eltonio.projects.politicalsquare.ui.viewmodel.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}