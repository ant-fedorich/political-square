package eltonio.projects.politicalcompassquiz.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.other.*
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Log.e(eltonio.projects.politicalcompassquiz.other.TAG, "loadIsIntroOpened(): " + loadIsIntroOpened())

            //== Fading ==
        // Create fading animation
/*        splashAnimationTime = 600L // For Test without Into*/

        val compassAnimation1 = AnimationUtils.loadAnimation(this, R.anim.splash_fading)
        compassAnimation1.apply {
            duration = splashAnimationTime
            interpolator = DecelerateInterpolator(2f)
        }
        // Start fading animation
        view_foreground.startAnimation(compassAnimation1)
        // Do action after fading
        Handler().postDelayed({
            view_foreground.visibility = View.INVISIBLE
        }, splashAnimationTime) //600

        //== Moving ==
        // Create moving animation
        val compassAnimation2 = AnimationUtils.loadAnimation(this, R.anim.splash_move_left_up)
        compassAnimation2.apply {
            duration = splashAnimationTime //600
            interpolator = DecelerateInterpolator(2f)
        }
        val compassAnimation3 = AnimationUtils.loadAnimation(this, R.anim.splash_move_left)
        compassAnimation3.apply {
            duration = 700
            startOffset = splashAnimationTime //600
            interpolator = DecelerateInterpolator(5f)
        }

        val animationSet = AnimationSet(false).apply {
            addAnimation(compassAnimation2)
            addAnimation(compassAnimation3)
        }
        // Start moving animation
        image_compass_only_strokes.startAnimation(animationSet)
        // Do action after moving
        Handler().postDelayed({
            val prefs = getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE).edit()
            prefs.putBoolean(PREF_SPLASH_APPEARED, true)
            prefs.apply()

            finish()
            overridePendingTransition(0, 0)
        }, 600 + splashAnimationTime) //1300
    }
}