package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerAppCompatActivity
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.util.appContext
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {
//    @JvmField
    @Inject
    lateinit var glideRequestManager: RequestManager

    private val localRepo = AppRepository.Local()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Log.e(eltonio.projects.politicalsquare.util.TAG, "loadIsIntroOpened(): " + localRepo.getIntroOpened())

        setCompassImage()

        // Fading
        startFadingAnimation()
        doActionAfterFading()
        // Moving
        startMovingAnimation()
        doActionAfterMoving()
    }

   /** CUSTOM METHODS */
   private fun setCompassImage() {
       glideRequestManager
           ?.load(R.drawable.img_compass_only_strokes)
           ?.into(image_compass_only_strokes)
   }


    private fun startFadingAnimation() {
       //splashAnimationTime = 600L // For Test without Into
       val fadingAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_fading)
       fadingAnimation.apply {
           duration = localRepo.getSplashAnimationTime()
           interpolator = DecelerateInterpolator(2f)
       }
       view_foreground.startAnimation(fadingAnimation)
   }

    private fun doActionAfterFading() {
        Handler().postDelayed({
            view_foreground.visibility = View.INVISIBLE
        }, localRepo.getSplashAnimationTime()) //600
    }

    private fun startMovingAnimation() {
        // Create moving animation
        val moveLeftUpAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_move_left_up)
        moveLeftUpAnimation.apply {
            duration = localRepo.getSplashAnimationTime() //600
            interpolator = DecelerateInterpolator(2f)
        }
        val moveLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_move_left)
        moveLeftAnimation.apply {
            duration = 700
            startOffset = localRepo.getSplashAnimationTime() //600
            interpolator = DecelerateInterpolator(5f)
        }
        // Create set
        val animationSet = AnimationSet(false).apply {
            addAnimation(moveLeftUpAnimation)
            addAnimation(moveLeftAnimation)
        }
        // Start animation
        image_compass_only_strokes.startAnimation(animationSet)
    }

    private fun doActionAfterMoving() {
        Handler().postDelayed({
            localRepo.setSplashIsAppeared()
            finish()
            overridePendingTransition(0, 0)
        }, 600 + localRepo.getSplashAnimationTime()) //1300
    }
}