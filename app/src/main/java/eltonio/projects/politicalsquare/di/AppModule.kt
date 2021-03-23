package eltonio.projects.politicalsquare.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.repository.ProdLocalRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideLocalRepository(
        @ApplicationContext context: Context,
    ): LocalRepository = ProdLocalRepository(context)
}