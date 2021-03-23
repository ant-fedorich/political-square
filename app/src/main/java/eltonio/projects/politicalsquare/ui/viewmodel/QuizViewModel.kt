package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.model.QuestionWithAnswers
import eltonio.projects.politicalsquare.model.Step
import eltonio.projects.politicalsquare.repository.CloudRepository
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private var localRepo: LocalRepository,
    private var cloudRepo: CloudRepository
) : ViewModel() {
    // TODO: Refactor


    private var questionCounterTotalLiveData: MutableLiveData<Int> = MutableLiveData()
    private var questionCounterLiveData: MutableLiveData<Int> = MutableLiveData()
    private var questionNew: MutableLiveData<String> = MutableLiveData()
    private var questionOld: MutableLiveData<String> = MutableLiveData()
    private var quizFinishedEvent: MutableLiveData<Boolean> = MutableLiveData()
    private var previousStepLiveData: MutableLiveData<Step> = MutableLiveData()
    private var FABButtonShowEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var questionCountTotal = 0
    private var questionCounter = 0

    private lateinit var currentQuestion: QuestionWithAnswers

    private var horizontalScore = 0f
    private var verticalScore = 0f
    private var previousStep: Step? = null
    private var isPreviousStep = false

    init {
        viewModelScope.launch (Dispatchers.IO) {
            cloudRepo.logQuizBeginEvent()
        }
        questionCountTotal = App.appQuestionsWithAnswers.size
        questionCounterTotalLiveData.value = questionCountTotal
    }
    fun getQuestionCounter(): LiveData<Int> {
        return questionCounterLiveData
    }

    fun getQuestionCounterTotal(): LiveData<Int> {
        return questionCounterTotalLiveData
    }

    fun getQuizFinishedEvent(): LiveData<Boolean> {
        return quizFinishedEvent
    }

    fun getQuestionNew(): LiveData<String> {
        return questionNew
    }

    fun getQuestionOld(): LiveData<String> {
        return questionOld
    }

    fun getFABButtonShowEvent(): LiveData<Boolean> {
        return FABButtonShowEvent
    }

    fun getPreviousStep(): LiveData<Step> {
        return previousStepLiveData
    }

    fun showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            quizFinishedEvent.value = false
            currentQuestion = App.appQuestionsWithAnswers[questionCounter]

            when (localRepo.getLang()) {
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
            quizFinishedEvent.value = true
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

            when (localRepo.getLang()) {
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