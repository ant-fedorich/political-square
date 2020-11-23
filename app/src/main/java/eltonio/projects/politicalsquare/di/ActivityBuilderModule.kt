package eltonio.projects.politicalsquare.di

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import eltonio.projects.politicalsquare.di.viewInfo.ViewInfoViewModelModule
import eltonio.projects.politicalsquare.ui.AboutActivity
import eltonio.projects.politicalsquare.ui.MainActivity
import eltonio.projects.politicalsquare.ui.SplashActivity
import eltonio.projects.politicalsquare.ui.ViewInfoActivity
import eltonio.projects.politicalsquare.ui.viewmodel.ViewInfoViewModel
import javax.inject.Inject

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeAboutActivity(): AboutActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity
    // You don't need .inject(this) in MainActivity
    @ContributesAndroidInjector(
        modules = [ViewInfoViewModelModule::class]
    )
    abstract fun contributeViewInfoActivity(): ViewInfoActivity
}