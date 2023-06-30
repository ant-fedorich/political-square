package eltonio.projects.politicalcompassquiz.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.adapter.QuizRecycleAdapter
import eltonio.projects.politicalcompassquiz.getOrAwait
import eltonio.projects.politicalcompassquiz.repository.entity.Quiz
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.repository.AppDatabase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class SavedResultsActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: AppDatabase


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun showNotEmptySavedResultList_swipeToDelete_returnsItemDeletedFromDB() = runBlockingTest {
        // given - setup
        val startResultFromDB = database.quizResultDao().getQuizResults().asLiveData().getOrAwait()

        val quizItem = Quiz(1, "NewQuiz", "Developer", "Test quiz")
        database.quizDaoForTesting().addQuiz(quizItem)
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

        database.quizResultDao().addQuizResult(quizResult)
        val resultFromDB = database.quizResultDao().getQuizResults().asLiveData().getOrAwait()

        val scenario = launchActivity<SavedResultsActivity>()
        var resultFromActivity = listOf<QuizResult>()

        // when - action
        scenario.onActivity { activity ->
            resultFromActivity = activity.getResultList()
        }

        onView(withId(R.id.recycler_results_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<QuizRecycleAdapter.QuizRecycleViewHolder>(0, swipeLeft()))

        val resultAfterDeleting = database.quizResultDao().getQuizResults().asLiveData().getOrAwait()

        // then - verify

        assertThat(startResultFromDB).isEmpty()
        assertThat(resultFromDB).isNotEmpty()
        assertThat(resultFromActivity.size).isEqualTo(resultFromDB.size)
        assertThat(resultAfterDeleting).isEmpty()

    }
}