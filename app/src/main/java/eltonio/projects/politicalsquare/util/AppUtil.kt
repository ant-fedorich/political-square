package eltonio.projects.politicalsquare.util

import android.annotation.SuppressLint
import android.app.Activity
//import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizOptions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
object AppUtil : BaseUtil() {


    /** Variables **/
    var appContext = baseContext

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

//fun showEndQuizDialogLambda(context: Context, onOkBlock: () -> Unit) {
//    AlertDialog.Builder(context).create().apply {
//        val dialogTitle = context.getString(R.string.all_dialog_do_you_want_to_end)
//        setTitle(dialogTitle)
//        setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.all_dialog_yes)) { _, _ ->
//            AppRepository.Local().setQuizIsActive(false)
////            quizIsActive = false // TODO: V - livedata? or get from Repo directly?
//            onOkBlock()
//        }
//        setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.all_dialog_no)) { _, _ ->
//            return@setButton
//        }
//        show()
//    }
//}

    // TODO: Instrumented unit test with context
    fun convertDpToPx(dp: Float): Float = dp * appContext.resources.displayMetrics.density

    // TODO: Instr unit test with context
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

    // TODO: DO Local unit test
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
            .addListener(object : RequestListener<GifDrawable> {
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


    // TODO: Instr unit test with context
    fun refreshAllСatalogs(context: Context) {
        Ideologies.refreshAll(context)
        QuizOptions.refreshAll(context)
    }

//fun Int.getTitle(): String {
//    return appContext.getString(R.string.about_desc_1)
//}

    object EmptyTransitionListener : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    }

}
