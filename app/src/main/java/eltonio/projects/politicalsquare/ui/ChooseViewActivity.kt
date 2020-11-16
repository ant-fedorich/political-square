package eltonio.projects.politicalsquare.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppViewModel
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.App.Companion.appQuestionsWithAnswers
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.views.ChoosePointView
import kotlinx.android.synthetic.main.activity_choose_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChooseViewActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener {

    // TEMP
//    private val localRepo = AppRepository.Local()
    // TODO: Should I use a global var of view model and a scope
    private lateinit var viewModel: ChooseViewViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var scope: CoroutineScope

    // TODO: mvvm vars to vm???
    private var horStartScore = 0
    private var verStartScore = 0
    private var ideology = ""

    private var x = 0f
    private var y = 0f
    private var ideologyIsChosen = false
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
    private val diameterInPx = convertDpToPx(bigRadiusInDp).toInt()*2

    private var containerHeight = -1
    private var containerWidth = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_view)

        viewModel = ViewModelProvider(this).get(ChooseViewViewModel::class.java)
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        scope = CoroutineScope(Dispatchers.IO)

        // end vm
        // Init listeners
        button_start_quiz.setOnClickListener(this)
        button_compass_info.setOnClickListener(this)
        frame_1.setOnTouchListener(this)



//        // TODO: mvvm to vm
//        when(localRepo.loadQuizOption()) {
//            QuizOptions.UKRAINE.id -> getQuestionsWithAnswers(QuizOptions.UKRAINE.id)
//            QuizOptions.WORLD.id -> getQuestionsWithAnswers(QuizOptions.WORLD.id)
//        }
//        Collections.shuffle(appQuestionsWithAnswers)
//        //end
    }

    override fun onBackPressed() {
        super.onBackPressed()
        slideRight(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        // TODO: V - to method
        containerHeight = frame_1.height
        containerWidth = frame_1.width

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

//                // TODO: M - to repo
//                ideologyIsChosen = true
                viewModel.ideologyIsChosenEvent.value = true

                v?.performClick()
            }
            //end MVVM
        }

        return true
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
        // TODO: V - to animation method
        oldPointFrame = ConstraintLayout(this)
        oldLayoutFrameParams?.leftMargin = oldLeftMargin
        oldLayoutFrameParams?.topMargin = oldTopMargin
        oldLayoutFrameParams?.rightMargin = oldRightMargin
        oldLayoutFrameParams?.bottomMargin = oldBottomMargin
        oldPointFrame?.layoutParams = oldLayoutFrameParams
        frame_1.removeAllViews()
        frame_1.addView(oldPointFrame)
        // end
    }

    private fun startOldPointViewAnimation() {
        // TODO: V - to animation method
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
        //end
    }

    private fun addPointFrame(event: MotionEvent) {
        pointFrame = ConstraintLayout(this)
        layoutFrameParams?.leftMargin = event.x.toInt() - diameterInPx
        layoutFrameParams?.topMargin = event.y.toInt() - diameterInPx
        layoutFrameParams?.rightMargin = containerWidth - event.x.toInt() - diameterInPx
        layoutFrameParams?.bottomMargin = containerHeight - event.y.toInt() - diameterInPx
        pointFrame?.layoutParams = layoutFrameParams
        frame_1.removeView(pointFrame)
        frame_1.addView(pointFrame)
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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button_start_quiz -> onStartQuizClicked()
            R.id.button_compass_info -> onCompassInfoClicked()
        }
    }


    /** CUSTOM METHODS */
    @SuppressLint("SimpleDateFormat") // TODO: Get rid of it
    private fun onStartQuizClicked() {
        // TODO: VM - to vm
        viewModel.ideologyIsChosenEvent.observe(this, Observer<Boolean> {
            if (it != true) {
                return@Observer toast(getString(R.string.chooseview_toast_choose_first))
            }
        })

//        if (!ideologyIsChosen) // TODO: VM - to vm, livedata or repo?
//            return toast(getString(R.string.chooseview_toast_choose_first)) // TODO: V - trigger livedata

//        // TODO: VM - to vm Repo, works everywhere
        quizIsActive = true
        viewModel.saveChosenView(x, y, horStartScore, verStartScore, ideology, quizId)
//
//        val startedAt = getDateTime()
//        localRepo.saveChosenView(x, y, horStartScore, verStartScore, ideology, quizId, startedAt) // TODO: put an object instead of attributes
//        // end

        startActivity(Intent(this, QuizActivity::class.java))
        slideLeft(this)
        MainActivity().finish()
        finish()
    }

    private fun onCompassInfoClicked() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }




//    // TODO: mvvm to vm
//    private fun getQuestionsWithAnswers(quizId: Int) {
//        this.quizId = quizId
//        chosenQuizId = quizId
//        scope.launch {
//            appQuestionsWithAnswers = appViewModel.getQuestionsWithAnswers(quizId)
//        }
//    }
//    // end

    // Graphic
    @SuppressLint("SetTextI18n")
    private fun drawHover(event: MotionEvent) {
        x = event.x
        y = event.y

        // TODO: V - V
        val endX = image_for_canvas.width
        val endY = image_for_canvas.height

        val bitmap = Bitmap.createBitmap(endX, endY, Bitmap.Config.ARGB_8888)
        image_for_canvas.visibility = View.VISIBLE
        image_for_canvas.setImageBitmap(bitmap)
        // end

        viewModel.getXandYForHover(x, y, endX, endY)
//        // TODO: VM - to VM
//        when {
//            x >= 0 && x <= endX && y >= 0 && y <= endY -> {
//                x = x; y = y
//            }
//            x >= 0 && x <= endX && y < 0 -> {
//                x = x; y = 0f
//            }
//            x >= 0 && x <= endX && y > endY -> {
//                x = x; y = endY.toFloat()
//            }
//            x < 0 && y >= 0 && y <= endY -> {
//                x = 0f; y = y
//            }
//            x > endX && y >= 0 && y <= endY -> {
//                x = endX.toFloat(); y = y
//            }
//
//            // Edges
//            x < 0 && y < 0 -> {
//                x = 0f; y = 0f
//            }
//            x > endX && y > endY -> {
//                x = endX.toFloat(); y = endY.toFloat()
//            }
//            x < 0 && y > endY -> {
//                x = 0f; y = endY.toFloat()
//            }
//            x > endX && y < 0 -> {
//                x = endX.toFloat(); y = 0f
//            }
//        }
//        // end

        // TODO: VM - to vm
        viewModel.getStartScore()
        viewModel.getIdeology()
//        val step = convertDpToPx(4f)
//        horStartScore = // livedata
//            (x / step - 40).toInt() // TODO: Refactor 40 to var
//        verStartScore = // livedata
//            (y / step - 40).toInt() // TODO: Refactor 40 to var
//        // end


//        // TODO: VM - to vm
//        ideology = getIdeology(horStartScore, verStartScore)
        viewModel.ideology.observe(this, Observer<String> {
            when (it) {
                Ideologies.AUTHORITARIAN_LEFT.title -> showThisIdeologyHover(image_autho_left_hover) // TODO: V - livedata<ideology>
                Ideologies.RADICAL_NATIONALISM.title -> showThisIdeologyHover(image_nation_hover)
                Ideologies.POWER_CENTRISM.title -> showThisIdeologyHover(image_gov_hover)
                Ideologies.SOCIAL_DEMOCRACY.title -> showThisIdeologyHover(image_soc_demo_hover)
                Ideologies.SOCIALISM.title -> showThisIdeologyHover(image_soc_hover)

                Ideologies.AUTHORITARIAN_RIGHT.title -> showThisIdeologyHover(image_autho_right_hover)
                Ideologies.RADICAL_CAPITALISM.title -> showThisIdeologyHover(image_radical_cap_hover)
                Ideologies.CONSERVATISM.title -> showThisIdeologyHover(image_cons_hover)
                Ideologies.PROGRESSIVISM.title -> showThisIdeologyHover(image_prog_hover)

                Ideologies.RIGHT_ANARCHY.title -> showThisIdeologyHover(image_right_anar_hover)
                Ideologies.ANARCHY.title -> showThisIdeologyHover(image_anar_hover)
                Ideologies.LIBERALISM.title -> showThisIdeologyHover(image_lib_hover)
                Ideologies.LIBERTARIANISM.title -> showThisIdeologyHover(image_libertar_hover)

                Ideologies.LEFT_ANARCHY.title -> showThisIdeologyHover(image_left_anar_hover)
                Ideologies.LIBERTARIAN_SOCIALISM.title -> showThisIdeologyHover(image_lib_soc)

                else -> showThisIdeologyHover(null)
            }
        })


        // end

        Log.d(TAG, "Area is touched: x = $x, y = $y")
    }

    // TODO: V
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
    // end

}