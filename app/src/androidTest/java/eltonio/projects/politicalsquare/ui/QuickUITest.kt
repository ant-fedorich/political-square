package eltonio.projects.politicalsquare.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalsquare.R
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
class QuickUITest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun quizTestUI_atLeastItLaunchedAndWork() {
        launchActivity<MainActivity>()

        onView(withId(R.id.button_start)).perform(click())
//        onView(withId(R.id.image_lib_hover)).check(matches(isDisplayed())).perform(click())
//        onView(withId(R.id.button_start_quiz)).perform(click())
//        onView(withId(R.id.radio_answer_1)).perform(click())
//        onView(withId(R.id.radio_answer_2)).perform(click())
//        onView(withId(R.id.fab_undo)).perform(click())
//        onView(isRoot()).perform(pressBack())
//        onView(allOf(withId(android.R.id.button1), withText(R.string.all_dialog_yes))).perform(click())
//
//        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click())
//        onView(withId(R.id.nav_settings)).perform(click())
//        onView(withId(R.id.image_rus)).perform(click())
//        onView(withId(R.id.button_start)).check(matches(withText("Начать")))
    }
}