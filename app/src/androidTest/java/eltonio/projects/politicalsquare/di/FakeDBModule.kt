package eltonio.projects.politicalsquare.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eltonio.projects.politicalsquare.repository.*
import eltonio.projects.politicalsquare.util.DB_NAME
import eltonio.projects.politicalsquare.util.TEST_DB_NAME
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DBModule::class]
)

@Module
object FakeDBModule {
    @Singleton
    @Provides
    fun provideFakeDatabase(@ApplicationContext context: Context)
        = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()


//    @Singleton
//    @Provides
//    fun provideFakeDatabase(@ApplicationContext context: Context): AppDatabase {
//        context.deleteDatabase(TEST_DB_NAME)
//
//        return Room.databaseBuilder(context, AppDatabase::class.java, TEST_DB_NAME)
//            .allowMainThreadQueries()
//            .createFromAsset(DB_NAME)
//            .build()
//    }



    @Singleton
    @Provides
    fun provideFakeQuestionDao(database: AppDatabase): QuestionDao
            = database.questionDao()

    @Singleton
    @Provides
    fun provideFakeQuizResultDao(database: AppDatabase): QuizResultDao
            = database.quizResultDao()

    @Singleton
    @Provides
    fun provideFakeQuizTestingDao(database: AppDatabase): QuizDao
            = database.quizDaoForTesting()

    @Singleton
    @Provides
    fun provideDBRepository(
        resultDao: QuizResultDao,
        questionDao: QuestionDao
    ): DBRepository
            = ProdDBRepository(resultDao, questionDao)
}