package eltonio.projects.politicalsquare.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.Step
import eltonio.projects.politicalsquare.util.TAG
import eltonio.projects.politicalsquare.util.defaultLang

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    //TEMP
    private var localRepo = AppRepository.Local()
    private var cloudRepo = AppRepository.Cloud()

    var questionCountTotal = 0
    var questionCounterTotalLiveData: MutableLiveData<Int>
    var questionCounter = 0
    var questionCounterLiveData: MutableLiveData<Int>
    var appQuestionsWithAnswers: MutableLiveData<QuestionWithAnswers>
    var questionNewVisible: MutableLiveData<Boolean>
    var questionOldVisible: MutableLiveData<Boolean>

    var questionNew: MutableLiveData<String>
    var questionOld: MutableLiveData<String>
    private lateinit var currentQuestion: QuestionWithAnswers

    var quizFinishedEvent: MutableLiveData<Boolean>
    private var horizontalScore = 0f
    private var verticalScore = 0f
    var previousStep: Step? = null
    var previousStepLiveData: MutableLiveData<Step>
    var isPreviousStep = false

    var clearCheckTriggerEvent: MutableLiveData<Boolean>
    var isLastQuestion: MutableLiveData<Boolean>
    var FABButtonShowEvent: MutableLiveData<Boolean>

    var goForward: MutableLiveData<Boolean>



    init {
//        questionCounter = MutableLiveData(0)
        appQuestionsWithAnswers = MutableLiveData()
        questionNewVisible = MutableLiveData()
        questionOldVisible = MutableLiveData()

        questionCounterLiveData = MutableLiveData()
        questionCounterTotalLiveData = MutableLiveData()

        questionNew = MutableLiveData()
        questionOld = MutableLiveData()
        FABButtonShowEvent = MutableLiveData()

        clearCheckTriggerEvent = MutableLiveData()

        quizFinishedEvent = MutableLiveData()

        isLastQuestion = MutableLiveData()

        goForward = MutableLiveData()

        previousStepLiveData = MutableLiveData()

        cloudRepo.logQuizBeginEvent()
        questionCountTotal = App.appQuestionsWithAnswers.size
        questionCounterTotalLiveData.value = questionCountTotal
    }

    fun showNextQuestion() {
        goForward.value = true

        if (questionCounter < questionCountTotal) {
            quizFinishedEvent.value = false // todo: VM - livedata
            currentQuestion = App.appQuestionsWithAnswers[questionCounter]
//            questionNewVisible.value = true
//            questionOldVisible.value = true

            when (defaultLang) {
                "uk" -> {
                    if (questionCounter > 0)
                        questionOld.value = App.appQuestionsWithAnswers[questionCounter - 1].questionUk
                    questionNew.value = currentQuestion.questionUk
                }
                "ru" -> {
                    if (questionCounter > 0)
                        questionOld.value = App.appQuestionsWithAnswers[questionCounter - 1].questionRu
                    questionNew.value = currentQuestion.questionRu
                }
                "en" -> {
                    if (questionCounter > 0)
                        questionOld.value = App.appQuestionsWithAnswers[questionCounter - 1].questionEn
                    questionNew.value = currentQuestion.questionEn
                }
                // end
            }
            questionCounter++
            Log.i(TAG, "VM: questionCounter: $questionCounter")
            questionCounterLiveData.value = questionCounter
        } else {
            quizFinishedEvent.value = true // todo: VM - livedata
            Log.i(TAG, "Vertical Score: $verticalScore, horizontal score: $horizontalScore")

            localRepo.setHorScore(horizontalScore.toInt())
            localRepo.setVerScore(verticalScore.toInt())

        }
    }

    fun showPrevQuestion() {
        isPreviousStep = true
        if (questionCounter > 1) {
            questionCounter--
            questionCounterLiveData.value = questionCounter

            currentQuestion = App.appQuestionsWithAnswers[questionCounter-1]
//            questionNewVisible.value = true
//            questionOldVisible.value = true

            when (defaultLang) {
                "uk" -> {
                    questionOld.value = currentQuestion.questionUk
                }
                "ru" -> {
                    questionOld.value = currentQuestion.questionRu

                }
                "en" -> {
                    questionOld.value = currentQuestion.questionEn
                }
            }
            val prevStep = previousStep
            if (prevStep != null) {
                previousStepLiveData.value = prevStep
                if (prevStep.scale == "horizontal")
                    horizontalScore -= prevStep.point
                else
                    verticalScore -= prevStep.point
            }
        }
        previousStep = null
        isPreviousStep = false

        FABButtonShowEvent.value = false
    }

    fun checkAnswer(radioSelectedIndex: Int) {
        if (isPreviousStep) return
        val point = currentQuestion.answerList[radioSelectedIndex].point
        val scale = currentQuestion.scale
        val step = Step()
        if (scale == "horizontal") horizontalScore += point else verticalScore += point

        step.let {
            it.questionIndex = questionCounter-1
            it.rbIndex = radioSelectedIndex
            it.scale = scale
            it.point = point
        }

        // Save as a previous step
        previousStep = step

        FABButtonShowEvent.value = true
    }


}