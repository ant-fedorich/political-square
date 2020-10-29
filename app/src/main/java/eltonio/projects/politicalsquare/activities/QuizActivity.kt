package eltonio.projects.politicalsquare.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

import eltonio.projects.politicalsquare.*
import eltonio.projects.politicalsquare.data.AppViewModel
import eltonio.projects.politicalsquare.other.*
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.other.App.Companion.analytics
import eltonio.projects.politicalsquare.other.App.Companion.appQuestionAnswerDetail
import eltonio.projects.politicalsquare.other.App.Companion.appQuestions
import eltonio.projects.politicalsquare.other.App.Companion.appQuestionsWithAnswers

import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*
import java.util.Collections.shuffle

class QuizActivity : BaseActivity(), View.OnTouchListener {

    private var questionList = listOf<Question>()
    private var questionList2 = listOf<eltonio.projects.politicalsquare.data.Question>()
    private var quizId = -1
    private var previousStep: Step? = null
    private var isPreviousStep = false
    private var questionCountTotal = 0
    private var questionCounter = 0
    private var horizontalScore = 0f
    private var verticalScore = 0f

    private var zeroAnswerCnt = 0

    private var quizFinished = false

    private lateinit var currentQuestion: Question

    private lateinit var radioShapeHover1: GradientDrawable
    private lateinit var radioShapeHover2: GradientDrawable
    private lateinit var radioShapeHover3: GradientDrawable
    private lateinit var radioShapeHover4: GradientDrawable
    private lateinit var radioShapeHover5: GradientDrawable

    private lateinit var radioShapeHoverList: MutableList<GradientDrawable>
    private var rbSelectedIndex = -1

    lateinit var appViewModel: AppViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        this.title = getString(R.string.quiz_title_actionbar)
        analytics.logEvent(EVENT_QUIZ_BEGIN) {
            param(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
        }

        // Language
        Locale.getDefault().language

//        var quesList: List<Question>
        // ROOM DB
        Log.w(TAG, "--- appQuestList ----------------")
        appQuestions.forEach { Log.w(TAG, "$it") }
        Log.w(TAG, "--- QuestionsWithAnswers ------------")
        appQuestionsWithAnswers.forEach { question ->
            Log.w(TAG, "${question.id}, ${question.questionRu}, ${question.scale}")
            question.answerList.forEach { Log.w(TAG, "Answers: $it") }
        }

//        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
//        appViewModel.getAllQuestions().observe(this, androidx.lifecycle.Observer { list ->
//            questionList2 = list
//            Log.w(TAG, "questionList2 Inner: $questionList2")
//        })
//        Log.w(TAG, "questionList2 Outer: $questionList2")
/*        GlobalScope.launch(Dispatchers.IO) {
            var quesListInner = appViewModel.getAllQuestions()
            questList = quesListInner
            Log.w(TAG, "QuesList: $questList")

//            questionList = quesListInner
//            withContext(Dispatchers.Main) {
//                quesList = quesListInner
//            }
        }
        val localQuesList = questList*/
//        Log.w(TAG, "LocalQuesList: $localQuesList")



        // DB
        val dbHelper = QuizDbHelper(this)
        dbHelper.openDB()

        when(QuizOptionHelper.loadQuizOption(this)) {
            QuizOptions.UKRAINE.id ->
            {
                quizId = QuizOptions.UKRAINE.id
                val dbIsExist = dbHelper.checkDB()
                Log.e(TAG, "dbIsExist: $dbIsExist")
                if (dbIsExist) {
                    questionList = dbHelper.getAllQuestions(quizId)
                }

            }
            QuizOptions.WORLD.id -> {
                quizId = QuizOptions.WORLD.id
                val dbIsExist = dbHelper.checkDB()
                Log.e(TAG, "dbIsExist: $dbIsExist")
                if (dbIsExist) {
                    questionList = dbHelper.getAllQuestions(quizId)
                }
            }
        }
        questionCountTotal = questionList.size
        shuffle(questionList)

        // DISABLE "Hard to answer" radio
        radio_answer_3.visibility = View.GONE

        // Listeners
        fab_undo.setOnClickListener { showPrevQuestion() }

        radio_answer_1.setOnTouchListener(this)
        radio_answer_2.setOnTouchListener(this)
        radio_answer_3.setOnTouchListener(this)
        radio_answer_4.setOnTouchListener(this)
        radio_answer_5.setOnTouchListener(this)

        // == Radio button hovers ==
        // Get shape_radio_hover for every radio button
        radioShapeHover1 = getDrawable(R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover2 = getDrawable(R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover3 = getDrawable(R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover4 = getDrawable(R.drawable.shape_radio_hover) as GradientDrawable
        radioShapeHover5 = getDrawable(R.drawable.shape_radio_hover) as GradientDrawable

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

        showNextQuestion()

        // Room DB
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        Log.i(TAG, "------ From Callback --------")
        var allQuestions: List<eltonio.projects.politicalsquare.data.Question>
        Log.i(TAG, "----------------------------------")
/*        Log.i(TAG, "------ Blocking UI --------")
        val allQues = appViewModel.getAllQuestionsWithBlock()
        allQues?.forEach {
            Log.i(TAG, "Question ${it.id}: ${it.question_ru}, ${it.scale}")
        }
        Log.i(TAG, "------ Blocking UI --------")*/


    }

    /* for debugging
    text_hor_step_point.text = ""
    text_ver_step_point.text = ""
    // Log all questions
    for (question in questionList) {
        var answers = ""
        question.answerList.forEach { answers += "\n Answer: " + it.answer + ", " + it.point}

        Log.i(
            TAG, """ Get all questions:
            ID: ${question.id}
            Question: ${question.question}
            Answers: $answers
        """.trimIndent())
    }

     */

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            radio_answer_1 -> {
                Log.i("MyApp", "Checked Index: ${radio_group_answers.indexOfChild(radio_answer_1)}")


                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_1)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_1.isChecked = true
                    checkAnswer(rbSelectedIndex)
                    showNextQuestion()
                }
            }

            radio_answer_2 -> {
                Log.i("MyApp", "Checked: ${radio_group_answers.indexOfChild(radio_answer_2)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_2)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_2.isChecked = true
                    checkAnswer(rbSelectedIndex)
                    showNextQuestion()
                }
            }

            radio_answer_3 -> {
                Log.i("MyApp", "Checked: ${radio_group_answers.indexOfChild(radio_answer_3)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_3)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_3)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_3.isChecked = true
                    checkAnswer(rbSelectedIndex)
                    showNextQuestion()
                }
            }

            radio_answer_4 -> {
                Log.i("MyApp", "Checked: ${radio_group_answers.indexOfChild(radio_answer_4)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_4)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_4)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_4.isChecked = true
                    checkAnswer(rbSelectedIndex)
                    showNextQuestion()
                }
            }

            radio_answer_5 -> {
                Log.i("MyApp", "Checked: ${radio_group_answers.indexOfChild(radio_answer_5)}")

                rbSelectedIndex = radio_group_answers.indexOfChild(radio_answer_5)

                if (event?.action == MotionEvent.ACTION_DOWN) {
                    resetRadioToDefault(radio_answer_5)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    radio_answer_5.isChecked = true
                    checkAnswer(rbSelectedIndex)
                    showNextQuestion()
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        showEndQuizDialogLambda(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /** CUSTOM METHODS */
    private fun checkAnswer(radioSelectedIndex: Int) {
        if (isPreviousStep) return // Need to check, because setOnCheckedChangeListener shoots automatically after radio changed

        if (radio_answer_3.isChecked) zeroAnswerCnt++
        // For debug
        Log.d(TAG, "zeroAnswerCnt: $zeroAnswerCnt")

        val rbSelected = findViewById<RadioButton>(radio_group_answers.checkedRadioButtonId)
        val point = currentQuestion.answerList[radioSelectedIndex].point
        val scale = currentQuestion.scale
        val step = Step()
        if (scale == "horizontal") horizontalScore += point else verticalScore += point

        // Save a current step
        step.let {
            it.questionIndex = questionCounter-1
            it.rbIndex = radioSelectedIndex
            it.rbSelected = rbSelected
            it.scale = scale
            it.point = point
        }

        // Save as a previous step
        previousStep = step

        // Show FAB
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

    private fun showNextQuestion() {

        if (fab_undo.isEnabled != true) fab_undo.isEnabled = true

        if (questionCounter < questionCountTotal) {
            radio_group_answers.clearCheck()
            currentQuestion = questionList[questionCounter]

            text_question_new.visibility = View.VISIBLE
            text_question_old.visibility = View.VISIBLE

            if (questionCounter > 0) text_question_old.text = questionList[questionCounter - 1].question

            text_question_new.text = currentQuestion.question

            radio_answer_1.text = currentQuestion.answerList[0].answer
            radio_answer_2.text = currentQuestion.answerList[1].answer
            radio_answer_3.text = currentQuestion.answerList[2].answer
            radio_answer_4.text = currentQuestion.answerList[3].answer
            radio_answer_5.text = currentQuestion.answerList[4].answer

            // Get Screen Resolution
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y

            Log.d(TAG, "$width and $height")

            // Animate an old question
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

            // Animate a new question
            val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.move_new_question)
            quesNewAnimation.duration = 300
            text_question_new.startAnimation(quesNewAnimation)

            // Animate Progress Bar
            text_questions_left.text = "${questionCounter+1} / $questionCountTotal"
            val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
            val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
            ObjectAnimator.ofInt(progress_bar, "progress", oldPercent, percent).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }.start()

             //for debugging
/*            text_current_horizontal_score.text = "Horizontal score: " + horizontalScore.toString()
            text_current_vertical_score.text = "Vertical score: " + verticalScore.toString()*/

            questionCounter++
        } else {
            // Finish Quiz
            quizFinished = true
            Log.i(TAG, "Vertical Score: $verticalScore, horizontal score: $horizontalScore")

            // For debug
            val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            sharedPref.putInt(PREF_ZERO_ANSWER_CNT, zeroAnswerCnt)
            sharedPref.apply()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(EXTRA_HORIZONTAL_SCORE, horizontalScore.toInt())
            intent.putExtra(EXTRA_VERTICAL_SCORE, verticalScore.toInt())
            intent.putExtra(EXTRA_QUIZ_ID, quizId)

            startActivity(intent)
            slideLeft(this) //quiz in
        }
    }

    private fun showPrevQuestion() {
        isPreviousStep = true

        if (questionCounter > 1) {
            questionCounter-- // reset a next question to current

            currentQuestion = questionList[questionCounter-1] // take the previous question

            text_question_new.visibility = View.VISIBLE
            text_question_old.visibility = View.VISIBLE
            text_question_old.text = currentQuestion.question

            radio_answer_1.text = currentQuestion.answerList[0].answer
            radio_answer_2.text = currentQuestion.answerList[1].answer
            radio_answer_3.text = currentQuestion.answerList[2].answer
            radio_answer_4.text = currentQuestion.answerList[3].answer
            radio_answer_5.text = currentQuestion.answerList[4].answer


            // Animate an old question backward
            val quesOldAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_old_question)
            quesOldAnimation.duration = 250

            quesOldAnimation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    text_question_new.visibility = View.INVISIBLE
                }
            })
            text_question_old.startAnimation(quesOldAnimation)

            // Animate a new question away
            val quesNewAnimation = AnimationUtils.loadAnimation(this, R.anim.back_move_new_question)
            quesNewAnimation.duration = 250
            text_question_new.startAnimation(quesNewAnimation)

            // Animate Progress Bar backward
            val percent = (((questionCounter+1).toFloat()/questionCountTotal.toFloat())*1000).toInt()
            val oldPercent = (((questionCounter).toFloat()/questionCountTotal.toFloat())*1000).toInt()
            ObjectAnimator.ofInt(progress_bar, "progress", percent, oldPercent).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }.start()

            text_questions_left.text = "${questionCounter} / $questionCountTotal"

            // Save a previous state
            val prevStep = previousStep

            if (prevStep != null) {

                prevStep.rbSelected?.isChecked = true

                fadeInOldAnswer(prevStep.rbSelected, radioShapeHoverList[prevStep.rbIndex])

                if (prevStep.rbSelected == radio_answer_3) zeroAnswerCnt-- // a zero answer
                // For debug
                Log.d(TAG, "zeroAnswerCnt: $zeroAnswerCnt")

                if (prevStep.scale == "horizontal")
                    horizontalScore -= prevStep.point
                else
                    verticalScore -= prevStep.point
            }
        }

        // Clear the previous step
        previousStep = null
        isPreviousStep = false

        // Hide FAB
        if (fab_undo.visibility == View.VISIBLE) {
            fab_undo.isEnabled = false
            fab_undo.animate().scaleX(0.2f).scaleY(0.2f).alpha(0f)
                .withEndAction {
                    fab_undo.visibility = View.GONE
                }
        }
    }

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
}

