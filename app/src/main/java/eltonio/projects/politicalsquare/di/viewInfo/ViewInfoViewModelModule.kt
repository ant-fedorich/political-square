package eltonio.projects.politicalsquare.di.viewInfo

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import eltonio.projects.politicalsquare.di.ViewModelKey
import eltonio.projects.politicalsquare.ui.viewmodel.ViewInfoViewModel

@Module
abstract class ViewInfoViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewInfoViewModel::class)
    internal abstract fun bindViewInfoViewModel(viewModel: ViewInfoViewModel): ViewModel
}