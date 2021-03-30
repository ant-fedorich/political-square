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
import androidx.lifecycle.*
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityChooseViewBinding
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.ui.viewmodel.ChooseViewViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx
import eltonio.projects.politicalsquare.util.AppUtil.slideLeft
import eltonio.projects.politicalsquare.util.AppUtil.toast
import eltonio.projects.politicalsquare.views.ChoosePointView
import kotlinx.coroutines.*

@AndroidEntryPoint
class ChooseViewActivity: BaseActivity(), View.OnClickListener, View.OnTouchListener {
    private val viewmodel: ChooseViewViewModel by viewModels()
    private val binding: ActivityChooseViewBinding by lazy { ActivityChooseViewBinding.inflate(layoutInflater)}


    // TODO: mvvm vars to vm???
    private var horStartScore = 0
    private var verStartScore = 0
    private var ideology = ""

    private var x = 0f
    private var y = 0f
    private var oldIdeologyHover: ImageView? = null

    private var quizId = -1

    private var pointView: ChoosePointView? = null
    private var oldPointView: ChoosePointView? = null
    private var pointFrame: ConstraintLayout? = null
    private var oldPointFrame: ConstraintLayout? = null
    private var layoutFrameParams: ConstraintLayout.LayoutParams? = null
    private var oldLayoutFrameParams: ConstraintLayout.LayoutParams? = null

    private var oldPointExists = false
    private var oldLeftMargin = -1
    private var oldTopMargin = -1
    private var oldRightMargin = -1
    private var oldBottomMargin = -1

    private val bigRadiusInDp = 16f
    private val radiusInDp = 12f
    private var diameterInPx = -1

    private var containerHeight = -1
    private var containerWidth = -1

    private var ideologyIsChosen = false

    /** METHODS */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diameterInPx = convertDpToPx(this, bigRadiusInDp).toInt()*2

        viewmodel.getQuizId().observe(this, Observer {
            quizId = it
        })
        viewmodel.getHorStartScore().observe(this, Observer {
            horStartScore = it
        })
        viewmodel.getVerStartScore().observe(this, Observer {
            verStartScore = it
        })

        // Init listeners
        binding.buttonStartQuiz.setOnClickListener(this)
        binding.buttonCompassInfo.setOnClickListener(this)
        binding.frame1.setOnTouchListener(this)

        setContentViewForBase(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtil.slideRight(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        containerHeight = binding.frame1.height
        containerWidth = binding.frame1.width

        initLayoutFrameParams()

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
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button_start_quiz -> onStartQuizClicked()
            R.id.button_compass_info -> onCompassInfoClicked()
        }
    }

    /** CUSTOM METHODS */
    @SuppressLint("SimpleDateFormat") // TODO: Get rid of it
    private fun onStartQuizClicked() {
        if (ideologyIsChosen != true) {
            return toast(this, getString(R.string.chooseview_toast_choose_first))
        } else {
            viewmodel.setQuizIsActive(true)
            viewmodel.saveChosenView(x, y, horStartScore, verStartScore, ideology, quizId)

            startActivity(Intent(this, QuizActivity::class.java))
            slideLeft(this)
            // TODO: 03/17/2021 Get rid of this
            MainActivity().finish()
            finish()
        }
    }

    private fun onCompassInfoClicked() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
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

        viewmodel.getIdeologyTitle().observe(this, Observer {
            ideology = it
            when (it) {
                // TODO: Crutch: Change titleRes to StringId
                Ideologies.AUTHORITARIAN_LEFT.titleRes.resString(this) -> showThisIdeologyHover(binding.imageAuthoLeftHover)
                Ideologies.RADICAL_NATIONALISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageNationHover)
                Ideologies.POWER_CENTRISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageProgHover)
                Ideologies.SOCIAL_DEMOCRACY.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageSocDemoHover)
                Ideologies.SOCIALISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageSocHover)

                Ideologies.AUTHORITARIAN_RIGHT.titleRes.resString(this)  -> showThisIdeologyHover(
                    binding.imageAuthoRightHover
                )
                Ideologies.RADICAL_CAPITALISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageRadicalCapHover)
                Ideologies.CONSERVATISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageConsHover)
                Ideologies.PROGRESSIVISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageProgHover)

                Ideologies.RIGHT_ANARCHY.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageRightAnarHover)
                Ideologies.ANARCHY.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageAnarHover)
                Ideologies.LIBERALISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageLibHover)
                Ideologies.LIBERTARIANISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageLibertarHover)

                Ideologies.LEFT_ANARCHY.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageLeftAnarHover)
                Ideologies.LIBERTARIAN_SOCIALISM.titleRes.resString(this)  -> showThisIdeologyHover(binding.imageLibSoc)

                else -> showThisIdeologyHover(null)
            }
        })
        //Log.d(TAG, "Area is touched: x = $x, y = $y")
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

    private fun initLayoutFrameParams() {
        layoutFrameParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        oldLayoutFrameParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
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
                    this@ChooseViewActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                pointFrame?.removeAllViews()
                pointFrame?.addView(pointView)
            }
        }.start()
    }

    private fun startMovePoint(event: MotionEvent) {
        layoutFrameParams?.leftMargin = event.x.toInt() - diameterInPx
        layoutFrameParams?.topMargin = event.y.toInt() - diameterInPx
        layoutFrameParams?.rightMargin = containerWidth - event.x.toInt() - diameterInPx
        layoutFrameParams?.bottomMargin = containerHeight - event.y.toInt() - diameterInPx
        pointFrame?.layoutParams = layoutFrameParams
    }

    private fun addOldPointFrame() {
        oldPointFrame = ConstraintLayout(this)
        oldLayoutFrameParams?.leftMargin = oldLeftMargin
        oldLayoutFrameParams?.topMargin = oldTopMargin
        oldLayoutFrameParams?.rightMargin = oldRightMargin
        oldLayoutFrameParams?.bottomMargin = oldBottomMargin
        oldPointFrame?.layoutParams = oldLayoutFrameParams
        binding.frame1.removeAllViews()
        binding.frame1.addView(oldPointFrame)
    }

    private fun startOldPointViewAnimation() {
        ObjectAnimator.ofFloat(radiusInDp, 0f).apply {
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                oldPointView = ChoosePointView(
                    this@ChooseViewActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                oldPointFrame?.removeAllViews()
                oldPointFrame?.addView(oldPointView)
            }
        }.start()
    }

    private fun addPointFrame(event: MotionEvent) {
        pointFrame = ConstraintLayout(this)
        layoutFrameParams?.leftMargin = event.x.toInt() - diameterInPx
        layoutFrameParams?.topMargin = event.y.toInt() - diameterInPx
        layoutFrameParams?.rightMargin = containerWidth - event.x.toInt() - diameterInPx
        layoutFrameParams?.bottomMargin = containerHeight - event.y.toInt() - diameterInPx
        pointFrame?.layoutParams = layoutFrameParams
        binding.frame1.removeView(pointFrame)
        binding.frame1.addView(pointFrame)
    }

    private fun startPointViewAnimation() {
        ObjectAnimator.ofFloat(0f, bigRadiusInDp).apply {
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                pointView = ChoosePointView(
                    this@ChooseViewActivity,
                    100f,
                    100f,
                    animatedValue as Float
                )
                pointFrame?.removeAllViews()
                pointFrame?.addView(pointView)
            }
        }.start()
    }

}