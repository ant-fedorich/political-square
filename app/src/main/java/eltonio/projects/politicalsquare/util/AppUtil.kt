package eltonio.projects.politicalsquare.util

import android.app.Activity
//import android.app.AlertDialog
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizOptions
import java.text.SimpleDateFormat
import java.util.*

/** Constants **/
const val TAG = "MyApp"

const val PREF_NAME = "QuizData"
const val PREF_SETTINGS = "PREF_SETTINGS"

const val PREF_LANG = "PREF_LANG"
const val PREF_QUIZ_OPTION = "PREF_QUIZ_OPTION"
const val PREF_IS_INTRO_OPENED = "PREF_IS_INTRO_OPENED"
const val PREF_SPLASH_APPEARED = "PREF_SPLASH_APPEARED"
const val PREF_SESSION_STARTED = "PREF_SESSION_STARTED"
const val PREF_CHOSEN_VIEW_X = "PREF_CHOSEN_VIEW_X"
const val PREF_CHOSEN_VIEW_Y = "PREF_CHOSEN_VIEW_Y"
const val PREF_HORIZONTAL_START_SCORE = "PREF_HORIZONTAL_START_SCORE"
const val PREF_VERTICAL_START_SCORE = "PREF_VERTICAL_START_SCORE"
const val PREF_CHOSEN_IDEOLOGY = "PREF_CHOSEN_IDEOLOGY"
const val PREF_STARTED_AT = "PREF_STARTED_AT"
const val PREF_CHOSEN_QUIZ_ID = "PREF_CHOSEN_QUIZ_ID"
const val PREF_HORIZONTAL_SCORE = "PREF_HORIZONTAL_SCORE"
const val PREF_VERTICAL_SCORE = "PREF_VERTICAL_SCORE"
const val PREF_SPLASH_ANIMATION_TIME = "PREF_SPLASH_ANIMATION_TIME"
const val PREF_QUIZ_IS_ACTIVE = "PREF_QUIZ_IS_ACTIVE"
const val PREF_MAIN_ACTIVITY_IS_IN_FRONT = "PREF_MAIN_ACTIVITY_IS_IN_FRONT"

const val EXTRA_IDEOLOGY_ID = "EXTRA_IDEOLOGY_ID"
const val EXTRA_QUIZ_ID = "EXTRA_QUIZ_ID"
const val EXTRA_ENDED_AT = "EXTRA_ENDED_AT"
const val EXTRA_HOR_START_SCORE = "EXTRA_HOR_START_SCORE"
const val EXTRA_VER_START_SCORE = "EXTRA_VER_START_SCORE"
const val EXTRA_HOR_RESULT_SCORE = "EXTRA_HOR_RESULT_SCORE"
const val EXTRA_VER_RESULT_SCORE = "EXTRA_VER_RESULT_SCORE"
const val EXTRA_IDEOLOGY_TITLE = "EXTRA_IDEOLOGY_TITLE"

const val EXTRA_TITLE_TRANSITION_NAME = "EXTRA_TITLE_TRANSITION_NAME"
const val EXTRA_DATE_TRANSITION_NAME = "EXTRA_DATE_TRANSITION_NAME"
const val EXTRA_IMAGE_TRANSITION_NAME = "EXTRA_IMAGE_TRANSITION_NAME"
const val EXTRA_ITEM_CONTAINER_TRANSITION_NAME = "EXTRA_ITEM_CONTAINER_TRANSITION_NAME"

const val EVENT_PREFERRED_LANG = "preferred_lang"
const val EVENT_QUIZ_SESSION_START = "quiz_session_start"
const val EVENT_QUIZ_BEGIN = "quiz_begin"
const val EVENT_QUIZ_COMPLETE = "quiz_complete"
const val EVENT_DETAILED_INFO = "quiz_detailed_info"
const val PARAM_LOGIN_DATE = "login_date"

/** Variables **/
var appContext = App.appContext

/** Functions **/

fun toast(msg: String) = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()

fun toastLong(msg: String) = Toast.makeText(appContext, msg, Toast.LENGTH_LONG).show()

fun getDateTime(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

// Transitions between activities
fun slideLeft(context: Context) {
    val activity = context as Activity
    activity.overridePendingTransition(
        R.anim.slide_in_left,
        R.anim.slide_out_left
    )
}

fun slideRight(context: Context) {
    val activity = context as Activity
    activity.overridePendingTransition(
        R.anim.slide_in_right,
        R.anim.slide_out_right
    )
}

fun pushLeft(context: Context) {
    val activity = context as Activity
    activity.overridePendingTransition(
        R.anim.push_in_left,
        R.anim.push_out_left
    )
}

fun pushRight(context: Context) {
    val activity = context as Activity
    activity.overridePendingTransition(
        R.anim.push_in_right,
        R.anim.push_out_right
    )
}

fun fadeIn(context: Context) {
    val activity = context as Activity
    activity.overridePendingTransition(
        R.anim.fade_in,
        R.anim.fade_out
    )
}

fun showEndQuizDialogLambda(context: Context, onOkBlock: () -> Unit) {
    AlertDialog.Builder(context).create().apply {
        val dialogTitle = context.getString(R.string.all_dialog_do_you_want_to_end)
        setTitle(dialogTitle)
        setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.all_dialog_yes)) { _, _ ->
            AppRepository.Local().setQuizIsActive(false)
//            quizIsActive = false // TODO: V - livedata? or get from Repo directly?
            onOkBlock()
        }
        setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.all_dialog_no)) { _, _ ->
            return@setButton
        }
        show()
    }
}

fun convertDpToPx(dp: Float): Float = dp * appContext.resources.displayMetrics.density

fun getScreenResolution(context: Context): Point {
    context as Activity
    val display = context.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

// Get an ideology from a Compass score
fun getIdeology(horScore: Int, verScore: Int): String {
    return when {
        // border
        (horScore in -40..-20 && verScore in -40..-35) || (horScore in -40..-35 && verScore in -35..-20) ->
            Ideologies.AUTHORITARIAN_LEFT.title

        horScore in -20..20 && verScore in -40..-35 ->
            Ideologies.RADICAL_NATIONALISM.title

        (horScore in 20..40 && verScore in -40..-35) || (horScore in 35..40 && verScore in -35..-20) ->
            Ideologies.AUTHORITARIAN_RIGHT.title

        horScore in 35..40 && verScore in -20..20 ->
            Ideologies.RADICAL_CAPITALISM.title

        (horScore in 35..40 && verScore in 20..40) || (horScore in 20..40 && verScore in 35..40) ->
            Ideologies.RIGHT_ANARCHY.title

        horScore in -20..20 && verScore in 35..40 ->
            Ideologies.ANARCHY.title

        (horScore in -40..-20 && verScore in 35..40) || (horScore in -40..-35 && verScore in 20..40) ->
            Ideologies.LEFT_ANARCHY.title

        horScore in -40..-35 && verScore in -20..20 ->
            Ideologies.SOCIALISM.title

            // main
        horScore in -35..0 && verScore in -35..-20 -> Ideologies.POWER_CENTRISM.title
        horScore in -35..0 && verScore in -20..0 -> Ideologies.SOCIAL_DEMOCRACY.title

        horScore in 0..35 && verScore in -35..-20 -> Ideologies.CONSERVATISM.title
        horScore in 0..35 && verScore in -20..0 -> Ideologies.PROGRESSIVISM.title

        horScore in 0..35 && verScore in 20..35 -> Ideologies.LIBERTARIANISM.title
        horScore in -35..0 && verScore in 20..35 -> Ideologies.LIBERTARIAN_SOCIALISM.title
        horScore in -35..35 && verScore in 0..20 -> Ideologies.LIBERALISM.title
        else -> "none"
    }
}

fun getIdeologyStringId(ideologyName: String): String {
    var stringId = "none"

    for (ideo in Ideologies.values()) {
        if (ideologyName == ideo.title) stringId = ideo.stringId
    }
    return stringId
}

fun playGif(screenImage: Int, containerImageView: ImageView) {
    Glide.with(appContext)
        .asGif()
        .load(screenImage)
        // Setup to play only once
        .addListener(object: RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean = true

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.setLoopCount(1)
                return false
            }
        })
        .into(containerImageView)
}

fun refreshAllСatalogs(context: Context) {
    Ideologies.refreshAll(context)
    QuizOptions.refreshAll(context)
}
