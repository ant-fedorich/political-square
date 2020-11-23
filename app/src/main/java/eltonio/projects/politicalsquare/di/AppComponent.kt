package eltonio.projects.politicalsquare.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import eltonio.projects.politicalsquare.App
import javax.inject.Singleton

@Singleton //AppComponent owns the Singleton scope, and all dependencies with this annotation will be owned by that scope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class, ViewModelFactoryModule::class
    ])

interface AppComponent : AndroidInjector<App> {
/*
    AndroidInjector<App> instead of inject(App)
    App is a client of AppComponent
*/
    @Component.Builder
    interface Builder {
    @BindsInstance
    fun application(application: Application): Builder
    // to bind object to component at time of construction
    fun build(): AppComponent
}

}