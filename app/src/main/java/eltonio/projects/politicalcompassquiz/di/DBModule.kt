package eltonio.projects.politicalcompassquiz.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eltonio.projects.politicalcompassquiz.repository.*
import eltonio.projects.politicalcompassquiz.util.DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

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
    fun provideQuizTestingDao(database: AppDatabase) = database.quizDaoForTesting()

    @Singleton
    @Provides
    fun provideDBRepository(
        quizResultDao: QuizResultDao,
        questionDao: QuestionDao
    ): DBRepository = ProdDBRepository(quizResultDao, questionDao)
}