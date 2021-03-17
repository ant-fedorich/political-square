package eltonio.projects.politicalsquare.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.models.Quiz
import eltonio.projects.politicalsquare.models.QuizResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class QuizResultDaoTest {
    lateinit var applicationContext: Context
    lateinit var database: AppDatabase
    lateinit var quizResultDao: QuizResultDao
    lateinit var quizDao: QuizDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        quizResultDao = database.quizResultDao()
        quizDao = database.quizTestingDao()
    }
    @After
    fun teardown() {
        database.close()
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
        val allQuizResults = quizResultDao.getQuizResults()
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
        val allQuizResults = quizResultDao.getQuizResults()

        // verify
        assertThat(allQuizResults).doesNotContain(quizItem)
    }
}