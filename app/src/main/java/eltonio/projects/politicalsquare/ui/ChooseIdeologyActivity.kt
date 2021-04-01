package eltonio.projects.politicalsquare.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityChooseIdeologyBinding
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.ui.viewmodel.ChooseViewViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyStringIdByResId
import eltonio.projects.politicalsquare.util.AppUtil.slideLeft
import eltonio.projects.politicalsquare.util.AppUtil.toast
import eltonio.projects.politicalsquare.views.ChoosePointView
import kotlinx.coroutines.*

@AndroidEntryPoint
class ChooseIdeologyActivity: BaseActivity() {
    private val viewmodel: ChooseViewViewModel by viewModels()
    private val binding: ActivityChooseIdeologyBinding by lazy { ActivityChooseIdeologyBinding.inflate(layoutInflater)}

    //Chosen view vars
    private var ideologyStringId = ""
    private var x = -1f
    private var y = -1f
    private var horStartScore = -1
    private var verStartScore = -1
    private var oldIdeologyHover: ImageView? = null
    private var quizId = -1

    private var ideologyIsChosen = false

    //Graphic vars
    private val pointFrame: ConstraintLayout by lazy { ConstraintLayout(this) }
    private val oldPointFrame: ConstraintLayout by lazy { ConstraintLayout(this) }
    private val layoutFrameParams: ConstraintLayout.LayoutParams by lazy {
        ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
    }
    private val oldLayoutFrameParams: ConstraintLayout.LayoutParams by lazy {
        ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
    }
    private var pointView: ChoosePointView? = null
    private var oldPointView: ChoosePointView? = null

    private var oldPointExists = false
    private var oldLeftMargin = -1
    private var oldTopMargin = -1
    private var oldRightMargin = -1
    private var oldBottomMargin = -1

    private val bigRadiusInDp = 16f
    private val radiusInDp = 12f
    private val diameterInPx by lazy { convertDpToPx(this, bigRadiusInDp).toInt()*2 }

    private var containerHeight = -1
    private var containerWidth = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        //Listeners
        binding.buttonStartQuiz.setOnClickListener {
            if (ideologyIsChosen) {
                viewmodel.setQuizIsActive(true)
                viewmodel.loadQuizOption()
                viewmodel.saveChosenIdeology(x, y, horStartScore, verStartScore, ideologyStringId, quizId)

                startActivity(Intent(this, QuizActivity::class.java))
                slideLeft(this)
                finish()
            } else {
                toast(this, getString(R.string.chooseview_toast_choose_first))
            }
        }

        binding.buttonCompassInfo.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        binding.frame1.setOnTouchListener { v, event ->
            containerHeight = binding.frame1.height
            containerWidth = binding.frame1.width

            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawHover(event)

                    if (oldPointExists) {
                        addOldPointFrame()
                        startOldPointViewAnimation()
                    }
                    addPointFrame(event)
                    startPointViewAnimation()
                }
                MotionEvent.ACTION_MOVE -> {
                    startMovePoint(event)
                    drawHover(event)
                }
                MotionEvent.ACTION_UP -> {
                    startChangeSizePointAnimation()
                    saveOldPointParams(event)

                    ideologyIsChosen = true

                    v?.performClick()
                }
            }
            return@setOnTouchListener true
        }

        setContentViewForBase(binding.root)
    }

    private fun subscribeToObservers() {
        viewmodel.quizOptionId.observe(this) {
            quizId = it
        }
        viewmodel.horStartScore.observe(this) {
            horStartScore = it
        }
        viewmodel.verStartScore.observe(this) {
            verStartScore = it
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtil.slideRight(this)
    }

    /** GRAPHIC METHODS */
    @SuppressLint("SetTextI18n")
    private fun drawHover(event: MotionEvent) {
        x = event.x
        y = event.y

        val endX = binding.imageForCanvas.width
        val endY = binding.imageForCanvas.height

        val bitmap = Bitmap.createBitmap(endX, endY, Bitmap.Config.ARGB_8888)
        binding.imageForCanvas.visibility = View.VISIBLE
        binding.imageForCanvas.setImageBitmap(bitmap)
        // end

        viewmodel.getXandYForHover(x, y, endX, endY)
        viewmodel.getIdeologyResId()

        viewmodel.ideologyResId.observe(this) {
            ideologyStringId = getIdeologyStringIdByResId(it)
            when (it) {
                Ideologies.AUTHORITARIAN_LEFT.resId -> showThisIdeologyHover(binding.imageAuthoLeftHover)
                Ideologies.RADICAL_NATIONALISM.resId  -> showThisIdeologyHover(binding.imageNationHover)
                Ideologies.POWER_CENTRISM.resId  -> showThisIdeologyHover(binding.imageGovHover)
                Ideologies.SOCIAL_DEMOCRACY.resId  -> showThisIdeologyHover(binding.imageSocDemoHover)
                Ideologies.SOCIALISM.resId  -> showThisIdeologyHover(binding.imageSocHover)

                Ideologies.AUTHORITARIAN_RIGHT.resId  -> showThisIdeologyHover(
                    binding.imageAuthoRightHover
                )
                Ideologies.RADICAL_CAPITALISM.resId  -> showThisIdeologyHover(binding.imageRadicalCapHover)
                Ideologies.CONSERVATISM.resId  -> showThisIdeologyHover(binding.imageConsHover)
                Ideologies.PROGRESSIVISM.resId  -> showThisIdeologyHover(binding.imageProgHover)

                Ideologies.RIGHT_ANARCHY.resId  -> showThisIdeologyHover(binding.imageRightAnarHover)
                Ideologies.ANARCHY.resId  -> showThisIdeologyHover(binding.imageAnarHover)
                Ideologies.LIBERALISM.resId  -> showThisIdeologyHover(binding.imageLibHover)
                Ideologies.LIBERTARIANISM.resId  -> showThisIdeologyHover(binding.imageLibertarHover)

                Ideologies.LEFT_ANARCHY.resId  -> showThisIdeologyHover(binding.imageLeftAnarHover)
                Ideologies.LIBERTARIAN_SOCIALISM.resId  -> showThisIdeologyHover(binding.imageLibSoc)

                else -> showThisIdeologyHover(null)
            }
        }
    }

    private fun showThisIdeologyHover(ideologyHover: ImageView?) {
        // If Ideology same, break
        if (oldIdeologyHover == ideologyHover) {
            return
        }
        // Hide this ideology
        if (oldIdeologyHover != null) {
            oldIdeologyHover?.alpha = 0.5f
            oldIdeologyHover?.animate()?.apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                alpha(0f)
            }
        }
        // show this ideology
        ideologyHover?.visibility = View.VISIBLE
        ideologyHover?.alpha = 0f

        ideologyHover?.animate()?.apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            alpha(0.5f)
        }

        oldIdeologyHover = ideologyHover
    }

    private fun saveOldPointParams(event: MotionEvent) {
        oldPointExists = true
        oldLeftMargin = event.x.toInt() - diameterInPx
        oldTopMargin = event.y.toInt() - diameterInPx
        oldRightMargin = containerWidth - event.x.toInt() - diameterInPx
        oldBottomMargin = containerHeight - event.y.toInt() - diameterInPx
    }

    private fun startChangeSizePointAnimation() {
        ObjectAnimator.ofFloat(bigRadiusInDp, radiusInDp).apply {
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                pointView = ChoosePointView(
                    this@ChooseIdeologyActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                pointFrame.removeAllViews()
                pointFrame.addView(pointView)
            }
        }.start()
    }

    private fun startMovePoint(event: MotionEvent) {
        layoutFrameParams.leftMargin = event.x.toInt() - diameterInPx
        layoutFrameParams.topMargin = event.y.toInt() - diameterInPx
        layoutFrameParams.rightMargin = containerWidth - event.x.toInt() - diameterInPx
        layoutFrameParams.bottomMargin = containerHeight - event.y.toInt() - diameterInPx
        pointFrame.layoutParams = layoutFrameParams
    }

    private fun addOldPointFrame() {
        oldLayoutFrameParams.leftMargin = oldLeftMargin
        oldLayoutFrameParams.topMargin = oldTopMargin
        oldLayoutFrameParams.rightMargin = oldRightMargin
        oldLayoutFrameParams.bottomMargin = oldBottomMargin
        oldPointFrame.layoutParams = oldLayoutFrameParams
        binding.frame1.removeAllViews()
        binding.frame1.addView(oldPointFrame)
    }

    private fun startOldPointViewAnimation() {
        ObjectAnimator.ofFloat(radiusInDp, 0f).apply {
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                oldPointView = ChoosePointView(
                    this@ChooseIdeologyActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                oldPointFrame.removeAllViews()
                oldPointFrame.addView(oldPointView)
            }
        }.start()
    }

    private fun addPointFrame(event: MotionEvent) {
        layoutFrameParams.leftMargin = event.x.toInt() - diameterInPx
        layoutFrameParams.topMargin = event.y.toInt() - diameterInPx
        layoutFrameParams.rightMargin = containerWidth - event.x.toInt() - diameterInPx
        layoutFrameParams.bottomMargin = containerHeight - event.y.toInt() - diameterInPx
        pointFrame.layoutParams = layoutFrameParams
        binding.frame1.removeView(pointFrame)
        binding.frame1.addView(pointFrame)
    }

    private fun startPointViewAnimation() {
        ObjectAnimator.ofFloat(0f, bigRadiusInDp).apply {
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                pointView = ChoosePointView(
                    this@ChooseIdeologyActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                pointFrame.removeAllViews()
                pointFrame.addView(pointView)
            }
        }.start()
    }

}