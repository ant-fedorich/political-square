package eltonio.projects.politicalsquare.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.App_HiltComponents
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.util.DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context)
        = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .createFromAsset(DB_NAME).build()

    @Singleton
    @Provides
    fun provideQuestionDao(database: AppDatabase) = database.questionDao()

    @Singleton
    @Provides
    fun provideQuizResultDao(database: AppDatabase) = database.quizResultDao()

    @Singleton
    @Provides
    fun provideQuizTestingDao(database: AppDatabase) = database.quizTestingDao()
}