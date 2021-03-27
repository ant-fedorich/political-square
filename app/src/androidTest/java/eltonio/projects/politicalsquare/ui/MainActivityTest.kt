package eltonio.projects.politicalsquare.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.di.DBModule
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.*
import eltonio.projects.politicalsquare.util.DB_NAME
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.allOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@MediumTest
@HiltAndroidTest
class MainActivityTest { // needs DB with Room.databaseBuilder
    @Inject
    @ApplicationContext lateinit var context: Context
    @Inject
    lateinit var database: AppDatabase

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun checkBackStack_onOpenMenu_whilePassingQuiz() {
        launchActivity<MainActivity>()

        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.image_prog_hover)).perform(click())
        onView(withId(R.id.button_start_quiz)).perform(click())

        // Pass the quiz
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        pressBack()
        onView(allOf(withId(android.R.id.button1), withText(context.getString(R.string.all_dialog_yes))))
            .perform(click())
        onView(withId(R.id.layout_main)).check(matches(isDisplayed()))
        Thread.sleep(2000)

        onView(withContentDescription(context.getString(R.string.navigation_drawer_open))).perform(click())
        pressBack()
        onView(withId(R.id.layout_main)).check(matches(isDisplayed()))
    }
}