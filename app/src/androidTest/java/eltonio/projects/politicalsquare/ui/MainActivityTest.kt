package eltonio.projects.politicalsquare.ui

import android.content.Intent
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)



    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun test_onButtonStart_goToChooseView() = runBlocking {


        val scenario = launchActivity<MainActivity>()
        scenario.onActivity { activity ->

        }

        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.button_start_quiz)).perform(click())
        //assertThat(scenario.get)
    }
}