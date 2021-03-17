package eltonio.projects.politicalsquare.ui


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.appContext
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
class AppTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test /** USE CASE 1. User passes the quiz **/
    fun appTest1() {

        onView(withId(R.id.button_start))
            .perform(click())
        onView(withId(R.id.button_compass_info))
            .perform(click())
        onView(withId(R.id.image_lib_hover))
            .check(matches(isDisplayed()))
            .perform(click())
        // FIXME:  Unresolved reference: layout_collapsing_toolbar
//        onView(withId(R.id.layout_collapsing_toolbar))
//            .perform(swipeUp())
//        onView(withId(R.id.toolbar_collapsing))
//            .perform(swipeDown(), swipeDown())

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.image_lib_hover))
            .perform(click())
        onView(withId(R.id.button_start_quiz))
            .perform(click())

        // Pass the quiz
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.fab_undo)) // Back
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())
        onView(withId(R.id.radio_answer_4))
            .perform(click())
        onView(withId(R.id.radio_answer_5))
            .perform(click())

        onView(withId(R.id.button_compass_info_3))
            .perform(click())

        Thread.sleep(1000)

        onView(isRoot()).perform(pressBack())

        // Check Result
        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_saved))
            .perform(click())
        onView(withId(R.id.recycler_results_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.button_compass_info_3))
            .perform(click())

        Thread.sleep(1000)

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_main))
            .perform(click())
        onView(allOf(withId(android.R.id.button1), withText(appContext.getString(R.string.all_dialog_yes))))
            .perform(click())

        Thread.sleep(1000)
    }

    @Test /** USE CASE 2. User starts another quiz and quits **/
    fun appTest2() {

        onView(withId(R.id.spinner_quiz_options))
            .perform(click())
        onData(anything()).atPosition(1)
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())
        onView(withId(R.id.image_lib_hover))
            .perform(click())
        onView(withId(R.id.button_start_quiz))
            .perform(click())
        onView(withId(R.id.radio_answer_2))
            .perform(click())
        onView(withId(R.id.fab_undo))
            .perform(click())
        onView(withId(R.id.radio_answer_1))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button2), withText(appContext.getString(R.string.all_dialog_no))))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button1), withText(appContext.getString(R.string.all_dialog_yes))))
            .perform(click())

        Thread.sleep(1000)
    }

    @Test /** USE CASE 2. User reviews all menu **/
    fun appTest3() {
        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_main))
            .perform(click())

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_saved))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_info))
            .perform(click())
        onView(withId(R.id.scroll_info))
            .perform(swipeUp(), swipeDown())
        onView(withId(R.id.image_gov_hover))
            .perform(click())
        // FIXME: Unresolved reference: layout_appbar
//        onView(withId(R.id.layout_appbar))
//            .perform(swipeUp())

        onView(isRoot()).perform(pressBack())
        onView(isRoot()).perform(pressBack())

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.card_quiz_option_1))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        Thread.sleep(1000)

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_ukr))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())

        Thread.sleep(1000)

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_rus))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())

        Thread.sleep(1000)

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_settings))
            .perform(click())
        onView(withId(R.id.image_eng))
            .perform(click())
        onView(withId(R.id.button_start))
            .perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.image_lib_hover))
            .perform(click())
        onView(withId(R.id.button_start_quiz))
            .perform(click())

        onView(isRoot()).perform(pressBack())

        onView(allOf(withId(android.R.id.button1), withText(appContext.getString(R.string.all_dialog_yes))))
            .perform(click())

        onView(withContentDescription("Open menu"))
            .perform(click())
        onView(withId(R.id.nav_about))
            .perform(click())
        onView(withId(R.id.scroll_1))
            .perform(swipeUp(), swipeDown())

        Thread.sleep(1000)


    }
}
