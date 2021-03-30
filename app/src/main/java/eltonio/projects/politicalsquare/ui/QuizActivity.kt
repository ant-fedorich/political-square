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
import eltonio.projects.politicalsquare.databinding.ActivityQuizBinding
import eltonio.projects.politicalsquare.ui.viewmodel.QuizViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.showEndQuizDialogLambda
import eltonio.projects.politicalsquare.util.AppUtil.slideLeft

@AndroidEntryPoint
class QuizActivity : BaseActivity(), View.OnTouchListener {
    private val viewmodel: QuizViewModel by viewModels()
    private val binding: ActivityQuizBinding by lazy { ActivityQuizBinding.inflate(layoutInflater) }

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

        viewmodel.getQuestionCounterTotal().observe(this, Observer {
            questionCountTotal = it
            Log.i(TAG, "Activity: QuestionCountTotal = $it")

        })
        viewmodel.getQuestionCounter().observe(this, Observer {
            questionCounter = it
            Log.i(TAG, "Activity: questionCounter = $it")
            binding.textQuestionsLeft.text = "${questionCounter} / $questionCountTotal"
        })

        this.title = getString(R.string.quiz_title_actionbar)
        binding.radioAnswer1.text = getString(R.string.quiz_radio_answer_1)
        binding.radioAnswer2.text = getString(R.string.quiz_radio_answer_2)
        binding.radioAnswer3.text = getString(R.string.quiz_radio_answer_3)
        binding.radioAnswer4.text = getString(R.string.quiz_radio_answer_4)
        binding.radioAnswer5.text = getString(R.string.quiz_radio_answer_5)
        binding.radioAnswer3.visibility = View.GONE // DISABLE "Hard to answer" radio

        // Listeners
        binding.fabUndo.setOnClickListener {
            viewmodel.showPrevQuestion()
        }
        binding.radioAnswer1.setOnTouchListener(this)
        binding.radioAnswer2.setOnTouchListener(this)
        binding.radioAnswer3.setOnTouchListener(this)
        binding.radioAnswer4.setOnTouchListener(this)
        binding.radioAnswer5.setOnTouchListener(this)

        getRadioHovers()

        viewmodel.showNextQuestion()

        setContentViewForBase(binding.root)
    }

    private fun getRadioHovers() {
        // Get shape_radio_hover for every radio button
        radioShapeHover1 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover2 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover3 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover4 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover5 = ContextCompat.getDrawable(this, R.drawable.shape_radio_hover) as GradientDrawable

        (binding.radioAnswer1.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover1)
        (binding.radioAnswer2.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover2)
        (binding.radioAnswer3.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover3)
        (binding.radioAnswer4.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover4)
        (binding.radioAnswer5.background as LayerDrawable).setDrawableByLayerId(R.id.shape_radio_hover, radioShapeHover5)

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
                binding.radioGroupAnswers.clearCheck()
                binding.textQuestionNew.visibility = View.VISIBLE
                binding.textQuestionOld.visibility = View.VISIBLE

                startOldQuestionAnimation()
                startNewQuestionAnimation()
                startProgressBarAnimation()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
                slideLeft(this) //quiz in
                finish()
            }
        })

        viewmodel.getQuestionNew().observe(this, Observer {
            binding.textQuestionNew.text = it
        })

        viewmodel.getQuestionOld().observe(this, Observer {
            binding.textQuestionOld.text = it
        })

        viewmodel.getFABButtonShowEvent().observe(this, Observer {
            if (it == true)
                startShowFABAnimation()
        })
        viewmodel.getPreviousStep().observe(this, Observer {
            val prevRadioButton = binding.radioGroupAnswers[it.rbIndex] as RadioButton
            prevRadioButton.isChecked = true
            fadeInOldAnswer(prevRadioButton, radioShapeHoverList[it.rbIndex])
            startHideFABAnimation()
            startOldQuestionBackwardAnimation()
            startNewQuestionBackwardAnimation()
            startProgressBarBackwardAnimation()
            binding.textQuestionsLeft.text = "${questionCounter} / $questionCountTotal"
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            binding.radioAnswer1 -> {
                Log.i(TAG, "Checked Index: ${binding.radioGroupAnswers.indexOfChild(binding.radioAnswer1)}")

                rbSelectedIndex = binding.radioGroupAnswers.indexOfChild(binding.radioAnswer1)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(binding.radioAnswer1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.radioAnswer1.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (binding.fabUndo.isEnabled != true) binding.fabUndo.isEnabled = true
                }
            }

            binding.radioAnswer2 -> {
                Log.i(TAG, "Checked: ${binding.radioGroupAnswers.indexOfChild(binding.radioAnswer2)}")

                rbSelectedIndex = binding.radioGroupAnswers.indexOfChild(binding.radioAnswer2)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(binding.radioAnswer2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.radioAnswer2.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (binding.fabUndo.isEnabled != true) binding.fabUndo.isEnabled = true // TODO: V
                }
            }

            binding.radioAnswer3 -> {
                Log.i(TAG, "Checked: ${binding.radioGroupAnswers.indexOfChild(binding.radioAnswer3)}")

                rbSelectedIndex = binding.radioGroupAnswers.indexOfChild(binding.radioAnswer3)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(binding.radioAnswer3)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.radioAnswer3.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (binding.fabUndo.isEnabled != true) binding.fabUndo.isEnabled = true
                }
            }

            binding.radioAnswer4 -> {
                Log.i(TAG, "Checked: ${binding.radioGroupAnswers.indexOfChild(binding.radioAnswer4)}")

                rbSelectedIndex = binding.radioGroupAnswers.indexOfChild(binding.radioAnswer4)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(binding.radioAnswer4)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.radioAnswer4.isChecked = true
                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (binding.fabUndo.isEnabled != true) binding.fabUndo.isEnabled = true
                }
            }

            binding.radioAnswer5 -> {
                Log.i(TAG, "Checked: ${binding.radioGroupAnswers.indexOfChild(binding.radioAnswer5)}")

                rbSelectedIndex = binding.radioGroupAnswers.indexOfChild(binding.radioAnswer5)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(binding.radioAnswer5)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.radioAnswer5.isChecked = true

                    viewmodel.checkAnswer(rbSelectedIndex)
                    viewmodel.showNextQuestion()
                    if (binding.fabUndo.isEnabled != true) binding.fabUndo.isEnabled = true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        showEndQuizDialogLambda(this) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
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
                binding.textQuestionOld.visibility = View.INVISIBLE
            }
        })
        binding.textQuestionOld.startAnimation(quesOldAnimation)
    }
    private fun startNewQuestionAnimation() {
        val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.move_new_question)
        quesNewAnimation.duration = 300
        binding.textQuestionNew.startAnimation(quesNewAnimation)
    }
    private fun startProgressBarAnimation() {
        val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        ObjectAnimator.ofInt(binding.progressBar, "progress", oldPercent, percent).apply {
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
                binding.textQuestionNew.visibility = View.INVISIBLE
                binding.textQuestionOld.visibility = View.VISIBLE

            }
        })
        binding.textQuestionOld.startAnimation(quesOldAnimation)
    }
    private fun startNewQuestionBackwardAnimation() {
        val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_new_question)
        quesNewAnimation.duration = 250
        binding.textQuestionNew.startAnimation(quesNewAnimation)
    }
    private fun startProgressBarBackwardAnimation() {

        val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
        ObjectAnimator.ofInt(binding.progressBar, "progress", percent, oldPercent).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
        }.start()
    }

    private fun startShowFABAnimation() {
        if (binding.fabUndo.visibility == View.GONE) {
            binding.fabUndo.apply {
                scaleX = 0.2f
                scaleY = 0.2f
                alpha = 0f
                visibility = View.VISIBLE
            }
            binding.fabUndo.animate().scaleY(1f).scaleX(1f).alpha(1f)
        }
    }
    private fun startHideFABAnimation() {
        if (binding.fabUndo.visibility == View.VISIBLE) {
            binding.fabUndo.isEnabled = false
            binding.fabUndo.animate().scaleX(0.2f).scaleY(0.2f).alpha(0f)
                .withEndAction {
                    binding.fabUndo.visibility = View.GONE
                }
        }
    }
    // end
}

