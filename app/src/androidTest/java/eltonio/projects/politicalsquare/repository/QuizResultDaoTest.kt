package eltonio.projects.politicalsquare.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.DatabaseRule
import eltonio.projects.politicalsquare.getOrAwait
import eltonio.projects.politicalsquare.model.Quiz
import eltonio.projects.politicalsquare.model.QuizResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*


@ExperimentalCoroutinesApi
@SmallTest
class QuizResultDaoTest {
    private lateinit var quizResultDao: QuizResultDao
    private lateinit var quizDao: QuizDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val databaseRule = DatabaseRule()

    @Before
    fun setup() {
        quizResultDao = databaseRule.database.quizResultDao()
        quizDao = databaseRule.database.quizDaoForTesting()
    }

    @Test
    fun addQuizResult_OneItem_returnsDBHasThisItem() = runBlockingTest{
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
        val allQuizResults = quizResultDao.getQuizResults().getOrAwait()
        // verify
        assertThat(allQuizResults).contains(quizResult)
    }

    @Test
    fun deleteQuizResult_OneItem_returnsDBHasNoItems() = runBlockingTest {
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