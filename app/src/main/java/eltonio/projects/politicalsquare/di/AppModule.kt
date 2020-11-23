package eltonio.projects.politicalsquare.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule  {
    @Singleton
    @Provides
    fun provideGlideInstance(application: Application): RequestManager {
        return Glide.with(application)
    }
//    companion object {
//
//    }

    /*
    * retrofit, glide, application context
    * */
}