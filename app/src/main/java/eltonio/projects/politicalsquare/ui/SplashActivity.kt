package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivitySplashBinding
import eltonio.projects.politicalsquare.repository.LocalRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var localRepo: LocalRepository
    private val binding: ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Glide.with(this)
            .load(R.drawable.img_compass_only_strokes)
            .into(binding.imageCompassOnlyStrokes)

        // Fading
        startFadingAnimation()
        doActionAfterFading()
        // Moving
        startMovingAnimation()
        doActionAfterMoving()

        setContentView(binding.root)
    }

   /*********************************************************************/
    private fun startFadingAnimation() {
       //splashAnimationTime = 600L // For Test without Into
       val fadingAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_fading)
       fadingAnimation.apply {
           val animationTime = runBlocking { localRepo.getSplashAnimationTime() }
           duration = animationTime
           interpolator = DecelerateInterpolator(2f)
       }
       binding.viewForeground.startAnimation(fadingAnimation)
   }

    private fun doActionAfterFading() = MainScope().launch{
        delay(localRepo.getSplashAnimationTime())  //600
        binding.viewForeground.visibility = View.INVISIBLE
    }

//    private suspend fun doActionAfterFading() {
//        Handler().postDelayed({
//            binding.viewForeground.visibility = View.INVISIBLE
//        }, localRepo.getSplashAnimationTime()) //600
//    }

    private fun startMovingAnimation() = runBlocking {
        // Create moving animation
        val moveLeftUpAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.splash_move_left_up)
        moveLeftUpAnimation.apply {
            duration = localRepo.getSplashAnimationTime() //600
            interpolator = DecelerateInterpolator(2f)
        }
        val moveLeftAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.splash_move_left)
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
        binding.imageCompassOnlyStrokes.startAnimation(animationSet)
    }




    private fun doActionAfterMoving() = MainScope().launch {
        delay(600 + localRepo.getSplashAnimationTime())

        localRepo.setSplashIsAppeared()
        finish()
        overridePendingTransition(0, 0)

    }
}