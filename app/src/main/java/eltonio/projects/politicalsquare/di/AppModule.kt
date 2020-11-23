package eltonio.projects.politicalsquare.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

@Module
class AppModule  {
    companion object {
        @Provides
        fun provideGlideInstance(application: Application): RequestManager {
            return Glide.with(application)
        }
    }

    /*
    * retrofit, glide, application context
    * */
}