package eltonio.projects.politicalcompassquiz.util

import android.app.Activity
//import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.repository.ProdLocalRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


object AppUtil {
    /** Functions **/
    fun getDateTime(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

    fun toast(contex: Context, msg: String) = Toast.makeText(contex, msg, Toast.LENGTH_SHORT).show()
    fun toastLong(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

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
            MainScope().launch {
                ProdLocalRepository(context).setQuizIsActive(false)
            }

            onOkBlock()
        }
        setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.all_dialog_no)) { _, _ ->
            return@setButton
        }
        show()
    }
}

    fun convertDpToPx(context: Context, dp: Float): Float = dp * context.resources.displayMetrics.density

    fun getScreenResolution(context: Context): Point {
        context as Activity
        val display = context.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    // Get an ideology from a Compass score
    fun getIdeologyFromScore(context: Context, horScore: Int, verScore: Int): String {
        return when {
            // border
            (horScore in -40..-20 && verScore in -40..-35) || (horScore in -40..-35 && verScore in -35..-20) ->
                Ideologies.AUTHORITARIAN_LEFT.resId.resString(context)

            horScore in -20..20 && verScore in -40..-35 ->
                Ideologies.RADICAL_NATIONALISM.resId.resString(context)

            (horScore in 20..40 && verScore in -40..-35) || (horScore in 35..40 && verScore in -35..-20) ->
                Ideologies.AUTHORITARIAN_RIGHT.resId.resString(context)

            horScore in 35..40 && verScore in -20..20 ->
                Ideologies.RADICAL_CAPITALISM.resId.resString(context)

            (horScore in 35..40 && verScore in 20..40) || (horScore in 20..40 && verScore in 35..40) ->
                Ideologies.RIGHT_ANARCHY.resId.resString(context)

            horScore in -20..20 && verScore in 35..40 ->
                Ideologies.ANARCHY.resId.resString(context)

            (horScore in -40..-20 && verScore in 35..40) || (horScore in -40..-35 && verScore in 20..40) ->
                Ideologies.LEFT_ANARCHY.resId.resString(context)

            horScore in -40..-35 && verScore in -20..20 ->
                Ideologies.SOCIALISM.resId.resString(context)

            // main
            horScore in -35..0 && verScore in -35..-20 -> Ideologies.POWER_CENTRISM.resId.resString(context)
            horScore in -35..0 && verScore in -20..0 -> Ideologies.SOCIAL_DEMOCRACY.resId.resString(context)

            horScore in 0..35 && verScore in -35..-20 -> Ideologies.CONSERVATISM.resId.resString(context)
            horScore in 0..35 && verScore in -20..0 -> Ideologies.PROGRESSIVISM.resId.resString(context)

            horScore in 0..35 && verScore in 20..35 -> Ideologies.LIBERTARIANISM.resId.resString(context)
            horScore in -35..0 && verScore in 20..35 -> Ideologies.LIBERTARIAN_SOCIALISM.resId.resString(context)
            horScore in -35..35 && verScore in 0..20 -> Ideologies.LIBERALISM.resId.resString(context)
            else -> "none"
        }
    }

    fun getIdeologyResIdFromScore(horScore: Int, verScore: Int): Int {
        return when {
            // border
            (horScore in -40..-20 && verScore in -40..-35) || (horScore in -40..-35 && verScore in -35..-20) ->
                Ideologies.AUTHORITARIAN_LEFT.resId

            horScore in -20..20 && verScore in -40..-35 ->
                Ideologies.RADICAL_NATIONALISM.resId

            (horScore in 20..40 && verScore in -40..-35) || (horScore in 35..40 && verScore in -35..-20) ->
                Ideologies.AUTHORITARIAN_RIGHT.resId

            horScore in 35..40 && verScore in -20..20 ->
                Ideologies.RADICAL_CAPITALISM.resId

            (horScore in 35..40 && verScore in 20..40) || (horScore in 20..40 && verScore in 35..40) ->
                Ideologies.RIGHT_ANARCHY.resId

            horScore in -20..20 && verScore in 35..40 ->
                Ideologies.ANARCHY.resId

            (horScore in -40..-20 && verScore in 35..40) || (horScore in -40..-35 && verScore in 20..40) ->
                Ideologies.LEFT_ANARCHY.resId

            horScore in -40..-35 && verScore in -20..20 ->
                Ideologies.SOCIALISM.resId

            // main
            horScore in -35..0 && verScore in -35..-20 -> Ideologies.POWER_CENTRISM.resId
            horScore in -35..0 && verScore in -20..0 -> Ideologies.SOCIAL_DEMOCRACY.resId

            horScore in 0..35 && verScore in -35..-20 -> Ideologies.CONSERVATISM.resId
            horScore in 0..35 && verScore in -20..0 -> Ideologies.PROGRESSIVISM.resId

            horScore in 0..35 && verScore in 20..35 -> Ideologies.LIBERTARIANISM.resId
            horScore in -35..0 && verScore in 20..35 -> Ideologies.LIBERTARIAN_SOCIALISM.resId
            horScore in -35..35 && verScore in 0..20 -> Ideologies.LIBERALISM.resId
            else -> -1
        }
    }

    fun getIdeologyStringIdByResId(ideologyResId: Int): String {
        var stringId = "none"

        for (ideo in Ideologies.values()) {
            if (ideologyResId == ideo.resId) stringId = ideo.stringId
        }
        return stringId
    }

    fun getIdeologyResIdByStringId(ideologyStringId: String): Int {
        var resId = 0

        for (ideo in Ideologies.values()) {
            if (ideologyStringId == ideo.stringId) resId = ideo.resId
        }
        return resId
    }

    fun playGif(context: Context, screenImage: Int, containerImageView: ImageView) {
        Glide.with(context)
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

    fun Int.resString(context: Context): String {
        return context.getString(this)
    }

    object DefaultTransitionListener : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    }

    val defaultOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    val defaultOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {}
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    val defaultAnimationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {}
        override fun onAnimationEnd(p0: Animation?) {}
        override fun onAnimationRepeat(p0: Animation?) {}
    }



}
