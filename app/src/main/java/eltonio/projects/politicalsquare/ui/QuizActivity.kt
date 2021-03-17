package eltonio.projects.politicalsquare.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

import eltonio.projects.politicalsquare.*
import eltonio.projects.politicalsquare.ui.viewmodel.QuizViewModel
import eltonio.projects.politicalsquare.util.*

import kotlinx.android.synthetic.main.activity_quiz.*

@AndroidEntryPoint
class QuizActivity : BaseActivity(), View.OnTouchListener {
    private val viewmodel: QuizViewModel by viewModels()

    private var questionCountTotal = 0
    private var questionCounter = 0

    private lateinit var radioShapeHover1: GradientDrawable
    private lateinit var radioShapeHover2: GradientDrawable
    private lateinit var radioShapeHover3: GradientDrawable
    private lateinit var radioShapeHover4: GradientDrawable
    private lateinit var radioShapeHover5: GradientDrawable
    private lateinit var radioShapeHoverList: MutableList<GradientDrawable>

    private var rbSelectedIndex = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        viewmodel.getQuestionCounterTotal().observe(this, Observer {
            questionCountTotal = it
            Log.i(TAG, "Activity: QuestionCountTotal = $it")

        })
        viewmodel.getQuestionCounter().observe(this, Observer {
            questionCounter = it
            Log.i(TAG, "Activity: questionCounter = $it")
            text_questions_left.text = "${questionCounter} / $questionCountTotal"
        })

        this.title = getString(R.string.quiz_title_actionbar)
        radio_answer_1.text = getString(R.string.quiz_radio_answer_1)
        radio_answer_2.text = getString(R.string.quiz_radio_answer_2)
        radio_answer_3.text = getString(R.string.quiz_radio_answer_3)
        radio_answer_4.text = getString(R.string.quiz_radio_answer_4)
        radio_answer_5.text = getString(R.string.quiz_radio_answer_5)
        radio_answer_3.visibility = View.GONE // DISABLE "Hard to answer" radio

        // Listeners
        fab_undo.setOnClickListener {
            viewmodel.showPrevQuestion()
        }
        radio_answer_1.setOnTouchListener(this)
        radio_answer_2.setOnTouchListener(this)
        radio_answer_3.setOnTouchListener(this)
        radio_answer_4.setOnTouchListener(this)
        radio_answer_5.setOnTouchListener(this)

        getRadioHovers()

        viewmodel.showNextQuestion()
    }

    private fun getRadioHovers() {
        // Get shape_radio_hover for every radio button
        radioShapeHover1 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover2 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover3 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover4 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover5 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable

        (radio_answer_1.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover1)
        (radio_answer_2.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover2)
        (radio_answer_3.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover3)
        (radio_answer_4.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover4)
        (radio_answer_5.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover5)

        radioShapeHoverList = mutableListOf(
            radioShapeHover1,
            radioShapeHover2,
            radioShapeHover3,
            radioShapeHover4,
            radioShapeHover5
        )

        // Reset all shape_radio_hover to prevent bug
        for (item in radioShapeHoverList) {
            item.setColor(ContextCompat.getColor(this@QuizActivity, android.R.color.transparent))
        }
        viewmodel.getQuizFinishedEvent().observe(this, Observer {
            if (it == false) {
                radio_group_answers.clearCheck()
                text_question_new.visibility = View.VISIBLE
                text_question_old.visibility = View.VISIBLE

                startOldQuestionAnimation()
                startNewQuestionAnimation()
                startProgressBarAnimation()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
                slideLeft(this) //quiz in
            }
        })

        viewmodel.getQuestionNew().observe(this, Observer {
            text_question_new.text = it
        })

        viewmodel.getQuestionOld().observe(this, Observer {
            text_question_old.text = it
        })

        viewmodel.getFABButtonShowEvent().observe(this, Observer {
            if (it == true)
                startShowFABAnimation()
        })
        viewmodel.getPreviousStep().observe(this, Observer {
            val prevRadioButton = radio_group_answers[it.rbIndex] as RadioButton
            prevRadioButton.isChecked = true
            fadeInOldAnswer(prevRadioButton, radioShapeHoverList[it.rbIndex])
            startHideFABAnimation()
            startOldQuestionBackwardAnimation()
            startNewQuestionBackwardAnimation()
            startProgressBarBackwardAnimation()
            text_questions_left.text = "${questionCounter} / $questionCountTotal"
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            radio_answer_1 -> {
                Log.i(TAG, "Checked Index: ${radio_group_answers.indexOfChild(radio_answer_1)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_1)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_1.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (fab_undo.isEnabled != true) fab_undo.isEnabled = true
                }
            }

            radio_answer_2 -> {
                Log.i(TAG, "Checked: ${radio_group_answers.indexOfChild(radio_answer_2)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_2)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_2.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (fab_undo.isEnabled != true) fab_undo.isEnabled = true // TODO: V
                }
            }

            radio_answer_3 -> {
                Log.i(TAG, "Checked: ${radio_group_answers.indexOfChild(radio_answer_3)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_3)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_3)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_3.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (fab_undo.isEnabled != true) fab_undo.isEnabled = true
                }
            }

            radio_answer_4 -> {
                Log.i(TAG, "Checked: ${radio_group_answers.indexOfChild(radio_answer_4)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_4)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_4)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_4.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (fab_undo.isEnabled != true) fab_undo.isEnabled = true
                }
            }

            radio_answer_5 -> {
                Log.i(TAG, "Checked: ${radio_group_answers.indexOfChild(radio_answer_5)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_5)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_5)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_5.isChecked = true

                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (fab_undo.isEnabled != true) fab_undo.isEnabled = true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        viewmodel.showEndQuizDialogLambda(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /** CUSTOM METHODS */

    private fun fadeInOldAnswer(radioButtonSelected: RadioButton?, radioShapeHover: GradientDrawable) {
        if (radioButtonSelected != null) {
            val radioBackground = radioButtonSelected.background as LayerDrawable
            val rippleEffect = radioBackground.findDrawableByLayerId(R.id.shape_radio_ripple) as RippleDrawable
            rippleEffect.apply {
                setColor(ContextCompat.getColorStateList(this@QuizActivity, R.color.selector_ripple_effect_oldselected))
                setHotspot(10f, 10f)
                state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
                setColor(ContextCompat.getColorStateList(this@QuizActivity, R.color.selector_ripple_effect_normal))
            }

            Handler().postDelayed({
                rippleEffect.state = intArrayOf()
                rippleEffect.setColor(ContextCompat.getColorStateList(this, R.color.selector_ripple_effect_oldselected))
                radioShapeHover.setColor(ContextCompat.getColor(this@QuizActivity, R.color.quiz_answer_old_selected))

                radioShapeHover.alpha = 0
                ValueAnimator.ofArgb(0, 255).apply {
                    duration = 200 //200
                    addUpdateListener {
                        radioShapeHover.setColor(ContextCompat.getColor(this@QuizActivity, R.color.quiz_answer_old_selected))
                        radioShapeHover.alpha = this.animatedValue as Int
                    }
                    start()
                }
            }, 200)
        }
    }

    private fun resetRadioToDefault(radioAnswer: RadioButton?) {
        // Fade out all old selections
        for (item in radioShapeHoverList) {
            item.alpha = 255
            ValueAnimator.ofArgb(255, 0).apply {
                duration = 300
                addUpdateListener {
                    item.alpha = this.animatedValue as Int
                }
                addListener (object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        item.setColor(ContextCompat.getColor(this@QuizActivity, android.R.color.transparent))
                    }
                })
                start()
            }
        }
        if (radioAnswer != null) {
            val radioBackground = radioAnswer.background as LayerDrawable
            val rippleEffect = radioBackground.findDrawableByLayerId(R.id.shape_radio_ripple) as RippleDrawable
            val colorStateList = ContextCompat.getColorStateList(this, R.color.selector_ripple_effect_normal)
            rippleEffect.setColor(colorStateList)
        }
    }

    // ANIMATION METHODS
    private fun startOldQuestionAnimation() {
        val quesOldAnimation = AnimationUtils.loadAnimation(this, R.anim.move_old_question)
        quesOldAnimation.duration = 300
        quesOldAnimation.setAnimationListener (object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                text_question_old.visibility = View.INVISIBLE
            }
        })
        text_question_old.startAnimation(quesOldAnimation)
    }
    private fun startNewQuestionAnimation() {
        val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.move_new_question)
        quesNewAnimation.duration = 300
        text_question_new.startAnimation(quesNewAnimation)
    }
    private fun startProgressBarAnimation() {
        val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        ObjectAnimator.ofInt(progress_bar, "progress", oldPercent, percent).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
        }.start()
    }

    private fun startOldQuestionBackwardAnimation() {
        val quesOldAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_old_question)
        quesOldAnimation.duration = 250

        quesOldAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                text_question_new.visibility = View.INVISIBLE
                text_question_old.visibility = View.VISIBLE

            }
        })
        text_question_old.startAnimation(quesOldAnimation)
    }
    private fun startNewQuestionBackwardAnimation() {
        val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_new_question)
        quesNewAnimation.duration = 250
        text_question_new.startAnimation(quesNewAnimation)
    }
    private fun startProgressBarBackwardAnimation() {

        val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        ObjectAnimator.ofInt(progress_bar, "progress", percent, oldPercent).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
        }.start()
    }

    private fun startShowFABAnimation() {
        if (fab_undo.visibility == View.GONE) {
            fab_undo.apply {
                scaleX = 0.2f
                scaleY = 0.2f
                alpha = 0f
                visibility = View.VISIBLE
            }
            fab_undo.animate().scaleY(1f).scaleX(1f).alpha(1f)
        }
    }
    private fun startHideFABAnimation() {
        if (fab_undo.visibility == View.VISIBLE) {
            fab_undo.isEnabled = false
            fab_undo.animate().scaleX(0.2f).scaleY(0.2f).alpha(0f)
                .withEndAction {
                    fab_undo.visibility = View.GONE
                }
        }
    }
    // end
}

