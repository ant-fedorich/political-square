package eltonio.projects.politicalsquare.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalsquare.getOrAwait
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
    fun addQuizResult_OneItem_returnsDBHasThisItem() = runBlockingTest{
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
        val allQuizResults = quizResultDao.getQuizResults().getOrAwait()
        // verify
        assertThat(allQuizResults).contains(quizResult)
    }

    @Test
    fun deleteQuizResult_OneItem_returnsDBHasNoItems() = runBlockingTest {
        //setup
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
        val allQuizResults = quizResultDao.getQuizResults().getOrAwait()

        // verify
        assertThat(allQuizResults).doesNotContain(quizItem)
    }
}