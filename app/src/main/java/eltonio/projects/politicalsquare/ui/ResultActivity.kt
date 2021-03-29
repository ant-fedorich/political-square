package eltonio.projects.politicalsquare.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityResultBinding
import eltonio.projects.politicalsquare.ui.viewmodel.ResultViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushLeft
import eltonio.projects.politicalsquare.util.AppUtil.showEndQuizDialogLambda
import eltonio.projects.politicalsquare.views.ResultPointView


@AndroidEntryPoint
class ResultActivity : BaseActivity(), View.OnClickListener {
    private val viewmodel: ResultViewModel by viewModels()
    private val binding: ActivityResultBinding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.result_title_actionbar)

        binding.buttonCompassInfo2.setOnClickListener(this)


        val youThoughtText = getString(R.string.result_subtitle_you_thought)
        viewmodel.getChosenIdeology().observe(this, {
            binding.title22.text = "$youThoughtText: $it"
        })
        viewmodel.getResultIdeology().observe(this, Observer {
            binding.title2.text = it
        })

        startResultPointsAnimation()

        viewmodel.getCompassX().observe(this, Observer {
            compassX = it
        })
        viewmodel.getCompassY().observe(this, Observer {
            compassY = it
        })

        setContentViewForBase(binding.root)
    }

    override fun onBackPressed() {
        showEndQuizDialogLambda(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /** INTERFACE METHODS */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_compass_info_2 -> {
                viewmodel.onCompassInfoClick()
                val intent = Intent(this, ViewInfoActivity::class.java)
                viewmodel.getResultIdeology().observe(this, Observer {
                    intent.putExtra(EXTRA_IDEOLOGY_TITLE, it)
                })
                startActivity(intent)
                pushLeft(this) // info in
            }
        }
    }

    /** CUSTOM METHODS */
    private fun startResultPointsAnimation() {
        // Add start points
        val resultPoints = ResultPointView(this, 0f, 0f)
        resultPoints.alpha = 0f
        binding.frameResultPoints.addView(resultPoints)

        // Fading animation
        val animateFade = ObjectAnimator.ofFloat(resultPoints, View.ALPHA, 1f).apply {
            duration = 200
            addUpdateListener {
                binding.frameResultPoints.removeView(resultPoints)
                binding.frameResultPoints.addView(resultPoints)
            }
        }

        // Chosen point animation 1
        val animateChosenPoint1 = ObjectAnimator.ofFloat(resultPoints, "radiusInDp", 14f).apply {
            duration = 200
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                binding.frameResultPoints.removeView(resultPoints)
                binding.frameResultPoints.addView(resultPoints)
            }
        }

        // Chosen point animation 2
        val animateChosenPoint2 = ObjectAnimator.ofFloat(resultPoints, "radiusInDp", 10f).apply {
            duration = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                binding.frameResultPoints.removeView(resultPoints)
                binding.frameResultPoints.addView(resultPoints)
            }
        }

        // Result point animation 1
        val animateResultPoint1 = ObjectAnimator.ofFloat(resultPoints, "radiusResultInDp", 14f).apply {
            duration = 200
            startDelay = 200
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                binding.frameResultPoints.removeView(resultPoints)
                binding.frameResultPoints.addView(resultPoints)
            }
        }

        // Result point animation 2
        val animateResultPoint2 = ObjectAnimator.ofFloat(resultPoints, "radiusResultInDp", 12f).apply {
            duration = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                binding.frameResultPoints.removeView(resultPoints)
                binding.frameResultPoints.addView(resultPoints)
            }
        }

        // Animate it all
        AnimatorSet().apply {
            startDelay = 400
            play(animateFade)
            playSequentially(animateChosenPoint1, animateChosenPoint2)
            playSequentially(animateResultPoint1, animateResultPoint2)
            start()
        }
    }

    companion object {
        var chosenViewX = 0f
        var chosenViewY = 0f
        var compassX: Int? = 0
        var compassY: Int? = 0
    }
}


