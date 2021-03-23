package eltonio.projects.politicalsquare.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eltonio.projects.politicalsquare.di.DBModule
import eltonio.projects.politicalsquare.getOrAwaitValue
import eltonio.projects.politicalsquare.model.Quiz
import eltonio.projects.politicalsquare.model.QuizResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class QuizResultDaoWithHiltTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject lateinit var quizResultDao: QuizResultDao
    @Inject lateinit var quizDao: QuizDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testAddQuizResult() = runBlockingTest{
        val quizItem = Quiz(1, "NewQuiz", "Developer", "Test quiz")
        quizDao.addQuiz(quizItem)

        val quizResult = QuizResult(
            id = 1,
            quizId = 1,
            ideologyStringId = "anarchy",
            horStartScore = 10,
            verStartScore = 10,
            horResultScore = 20,
            verResultScore = 20,
            startedAt = "10.10.10",
            endedAt = "11.10.10",
            duration = 100,
            avgAnswerTime = 10.0)
        // action
        quizResultDao.addQuizResult(quizResult)
        val allQuizResults = quizResultDao.getQuizResults().getOrAwaitValue()
        // verify
        assertThat(allQuizResults).contains(quizResult)
    }

    @Test
    fun testDeleteQuizResult() = runBlockingTest {
        val quizItem = Quiz(1, "NewQuiz", "Developer", "Test quiz")
        quizDao.addQuiz(quizItem)

        val quizResult = QuizResult(
            id = 1,
            quizId = 1,
            ideologyStringId = "anarchy",
            horStartScore = 10,
            verStartScore = 10,
            horResultScore = 20,
            verResultScore = 20,
            startedAt = "10.10.10",
            endedAt = "11.10.10",
            duration = 100,
            avgAnswerTime = 10.0)
        // action
        quizResultDao.addQuizResult(quizResult)
        quizResultDao.deleteQuizResult(quizResult)
        val allQuizResults = quizResultDao.getQuizResults().getOrAwaitValue()

        // verify
        assertThat(allQuizResults).doesNotContain(quizItem)
    }
}

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DBModule::class]
)
@Module
object FakeAppModule {
    @Singleton
    @Provides
    fun provideFakeDatabase(@ApplicationContext context: Context)
        = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideFakeQuizResultDao(database: AppDatabase): QuizResultDao
        = database.quizResultDao()

    @Singleton
    @Provides
    fun provideFakeQuizDao(database: AppDatabase): QuizDao
        = database.quizDaoForTesting()

    @Singleton
    @Provides
    fun provideFakeQuestionDao(database: AppDatabase): QuestionDao
        = database.questionDao()
}