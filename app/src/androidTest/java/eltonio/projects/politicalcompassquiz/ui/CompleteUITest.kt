package eltonio.projects.politicalcompassquiz.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.getOrAwait
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.repository.AppDatabase
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat


@LargeTest
@HiltAndroidTest
class CompleteUITest { // needs DB with Room.databaseBuilder
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @ApplicationContext lateinit var context: Context

    @Inject
    lateinit var database: AppDatabase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test /** USE CASE 1. User passes the quiz **/
    fun useCase_userPassesTheQuiz() {
        launchActivity<MainActivity>()

        onView(withId(R.id.spinner_quiz_options)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.button_compass_info)).perform(click())
        onView(withId(R.id.image_lib_hover)).check(matches(isDisplayed())).perform(click())

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.image_lib_hover))
            .perform(click())
        onView(withId(R.id.button_start_quiz))
            .perform(click())

        // Pass the quiz
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.fab_undo))
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_4)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())
        onView(withId(R.id.radio_answer_5)).perform(click())

        onView(withId(R.id.button_compass_info_2)).perform(click())

        onView(isRoot()).perform(pressBack())

        // Check Result
        onView(withContentDescription(context.getString(R.string.navigation_drawer_open))).perform(click())
        onView(withId(R.id.nav_saved)).perform(click())
        onView(withId(R.id.recycler_results_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.button_compass_info_3)).perform(click())

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open))).perform(click())
        onView(withId(R.id.nav_main)).perform(click())
    }


    @Test /** USE CASE 2. User starts another quiz and quits **/
    fun useCase_userStartsAnotherQuizAndQuits() {
        launchActivity<MainActivity>()

        onView(withId(R.id.spinner_quiz_options)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.image_lib_hover)).perform(click())
        onView(withId(R.id.button_start_quiz)).perform(click())
        onView(withId(R.id.radio_answer_2)).perform(click())
        onView(withId(R.id.fab_undo)).perform(click())
        onView(withId(R.id.radio_answer_1)).perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button2), withText(context.getString(R.string.all_dialog_no))))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button1), withText(context.getString(R.string.all_dialog_yes))))
            .perform(click())

    }


    @Test /** USE CASE 3. User reviews all menu **/
    fun useCase_userReviewsAllMenu () = runBlockingTest {
        //given - setup
        val quizResult = QuizResult(
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

        //verify
        assertThat(resultFromDB).isNotEmpty()

        //
        launchActivity<MainActivity>()

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_saved)).perform(click())

        onView(isRoot()).perform(pressBack())

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_info))
            .perform(click())
        onView(withId(R.id.scroll_info))
            .perform(swipeUp(), swipeDown())
        onView(withId(R.id.image_gov_hover))
            .perform(click())

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.card_quiz_option_1))
            .perform(click())

        onView(isRoot()).perform(pressBack())


        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_ukr))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())


        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_rus))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())


        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_eng))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())


        onView(withId(R.id.image_lib_hover))
            .perform(click())
        onView(withId(R.id.button_start_quiz))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button1), withText(context.getString(R.string.all_dialog_yes))))
            .perform(click())

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open)))
            .perform(click())
        onView(withId(R.id.nav_about))
            .perform(click())
        onView(withId(R.id.scroll_1))
            .perform(swipeUp(), swipeDown())

    }
}
