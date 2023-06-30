package eltonio.projects.politicalcompassquiz.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.databinding.ActivityResultBinding
import eltonio.projects.politicalcompassquiz.ui.viewmodel.ResultViewModel
import eltonio.projects.politicalcompassquiz.util.*
import eltonio.projects.politicalcompassquiz.util.AppUtil.getIdeologyResIdByStringId
import eltonio.projects.politicalcompassquiz.util.AppUtil.pushLeft
import eltonio.projects.politicalcompassquiz.util.AppUtil.resString
import eltonio.projects.politicalcompassquiz.util.AppUtil.showEndQuizDialogLambda
import eltonio.projects.politicalcompassquiz.views.ResultPointView

@AndroidEntryPoint
class ResultActivity : BaseActivity() {
    private val viewmodel: ResultViewModel by viewModels()
    private val binding: ActivityResultBinding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    private var chosenIdeologyX = 0f
    private var chosenIdeologyY = 0f
    private var compassX: Int = 0
    private var compassY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        title = getString(R.string.result_title_actionbar)

        binding.buttonCompassInfo2.setOnClickListener {
            viewmodel.onCompassInfoClick()
            val intent = Intent(this, IdeologyInfoActivity::class.java)
            viewmodel.resultIdeologyResId.observe(this) { // TODO: One time info, get rid LiveData or not
                intent.putExtra(EXTRA_IDEOLOGY_TITLE_RES, it)
            }
            startActivity(intent)
            pushLeft(this) // info in
        }

        setContentViewForBase(binding.root)
    }

    /***********************************************************/

    private fun subscribeToObservers() {
       viewmodel.allDataCollectedState.observe(this) {
           if (it == true) {
               startResultPointsAnimation()
           }
       }

        viewmodel.chosenIdeologyStringId.observe(this) {
            val youThoughtText = getString(R.string.result_subtitle_you_thought)
            binding.title22.text = "$youThoughtText: ${getIdeologyResIdByStringId(it).resString(this)}"
        }
        viewmodel.resultIdeologyResId.observe(this) {
            binding.title2.text = it.resString(this)
        }

        viewmodel.compassX.observe(this) {
            compassX = it
        }
        viewmodel.compassY.observe(this) {
            compassY = it
        }
        viewmodel.chosenViewX.observe(this) {
            chosenIdeologyX = it
        }
        viewmodel.chosenViewY.observe(this) {
            chosenIdeologyY = it
            //startResultPointsAnimation() // FIXME: get rid, replace
        }
    }

    override fun onBackPressed() {
        showEndQuizDialogLambda(this) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }


    private fun startResultPointsAnimation() {
        // Add start points
        val resultPoints = ResultPointView(
            context = this,
            radiusInDp = 0f,
            radiusResultInDp = 0f,
            chosenViewX = chosenIdeologyX,
            chosenViewY = chosenIdeologyY,
            compassX = compassX,
            compassY = compassY
        )
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
}


