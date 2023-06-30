package eltonio.projects.politicalcompassquiz.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalcompassquiz.repository.entity.QuestionWithAnswers
import eltonio.projects.politicalcompassquiz.model.Step
import eltonio.projects.politicalcompassquiz.repository.CloudRepository
import eltonio.projects.politicalcompassquiz.repository.DBRepository
import eltonio.projects.politicalcompassquiz.repository.LocalRepository
import eltonio.projects.politicalcompassquiz.util.LANG_EN
import eltonio.projects.politicalcompassquiz.util.LANG_RU
import eltonio.projects.politicalcompassquiz.util.LANG_UK
import eltonio.projects.politicalcompassquiz.util.TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private var localRepo: LocalRepository,
    private var dbRepo: DBRepository,
    private var cloudRepo: CloudRepository
) : ViewModel() {
    private var _questionCounterTotal: MutableLiveData<Int> = MutableLiveData()
    val questionCounterTotal: LiveData<Int> = _questionCounterTotal

    private var _questionCounter: MutableLiveData<Int> = MutableLiveData()
    val questionCounter: LiveData<Int> = _questionCounter

    private var _questionNew: MutableLiveData<String> = MutableLiveData()
    val questionNew: LiveData<String> = _questionNew

    private var _questionOld: MutableLiveData<String> = MutableLiveData()
    val questionOld: LiveData<String> = _questionOld

    private var _quizFinishedEvent: MutableLiveData<Boolean> = MutableLiveData()
    val quizFinishedEvent: LiveData<Boolean> = _quizFinishedEvent

    private var _previousStep: MutableLiveData<Step> = MutableLiveData()
    val previousStep: LiveData<Step> = _previousStep

    private var _FABButtonShowEvent: MutableLiveData<Boolean> = MutableLiveData()
    val FABButtonShowEvent: LiveData<Boolean> = _FABButtonShowEvent

    private var questionCountTotalLocal = 0
    private var questionCounterLocal = 0

    private var questionsWithAnswers = mutableListOf<QuestionWithAnswers>()
    private lateinit var currentQuestion: QuestionWithAnswers

    private var _questionDownloadedState = MutableLiveData<Boolean>()
    val questionDownloadedState: LiveData<Boolean> = _questionDownloadedState

    private var horizontalScore = 0f
    private var verticalScore = 0f
    private var previousStepLocal: Step? = null
    private var isPreviousStep = false

    private var langJob = viewModelScope.async { localRepo.getSavedLang() }

    init {
        viewModelScope.launch {
            val jobLoadedResult = loadAllQuestionsJob()
            _questionDownloadedState.postValue(jobLoadedResult.await())
        }
    }

    private fun loadAllQuestionsJob() = viewModelScope.async(IO) {
        cloudRepo.logQuizBeginEvent()
        questionsWithAnswers = dbRepo.getQuestionsWithAnswers(localRepo.loadQuizOption()) as MutableList<QuestionWithAnswers>
        questionsWithAnswers.shuffle()
        questionCountTotalLocal = questionsWithAnswers.size
        withContext(Main) {_questionCounterTotal.value = questionCountTotalLocal}
        true
    }

    fun showNextQuestion() = viewModelScope.launch {
        if (questionCounterLocal < questionCountTotalLocal) {
            _quizFinishedEvent.value = false
            currentQuestion = questionsWithAnswers[questionCounterLocal]

            when (langJob.await()) {
                LANG_UK -> {
                    if (questionCounterLocal > 0)
                        _questionOld.value = questionsWithAnswers[questionCounterLocal - 1].questionUk
                    _questionNew.value = currentQuestion.questionUk
                }
                LANG_RU -> {
                    if (questionCounterLocal > 0)
                        _questionOld.value = questionsWithAnswers[questionCounterLocal - 1].questionRu
                    _questionNew.value = currentQuestion.questionRu
                }
                LANG_EN -> {
                    if (questionCounterLocal > 0)
                        _questionOld.value = questionsWithAnswers[questionCounterLocal - 1].questionEn
                    _questionNew.value = currentQuestion.questionEn
                }
                // end
            }
            questionCounterLocal++
            Log.i(TAG, "VM: questionCounter: $questionCounterLocal")
            _questionCounter.value = questionCounterLocal
        } else {
            _quizFinishedEvent.value = true
            Log.i(TAG, "Vertical Score: $verticalScore, horizontal score: $horizontalScore")

            withContext(IO) {
                localRepo.loadChosenIdeologyData()?.apply {
                    localRepo.saveChosenIdeologyData(
                        x = chosenViewX,
                        y = chosenViewY,
                        horStartScore = horStartScore,
                        verStartScore = verStartScore,
                        ideology = chosenIdeologyStringId,
                        quizId = chosenQuizId ,
                        startedAt = startedAt,
                        horEndScore = horizontalScore.toInt(),
                        verEndScore = verticalScore.toInt()
                    )
                }
            }
        }
    }

    fun showPrevQuestion() = viewModelScope.launch {
        isPreviousStep = true
        if (questionCounterLocal > 1) {
            questionCounterLocal--
            _questionCounter.value = questionCounterLocal

            currentQuestion = questionsWithAnswers[questionCounterLocal-1]

            when (langJob.await()) {
                LANG_UK -> {
                    _questionOld.value = currentQuestion.questionUk
                }
                LANG_RU -> {
                    _questionOld.value = currentQuestion.questionRu

                }
                LANG_EN -> {
                    _questionOld.value = currentQuestion.questionEn
                }
            }
            val prevStep = previousStepLocal
            if (prevStep != null) {
                _previousStep.value = prevStep
                if (prevStep.scale == "horizontal")
                    horizontalScore -= prevStep.point
                else
                    verticalScore -= prevStep.point
            }
        }
        previousStepLocal = null
        isPreviousStep = false

        _FABButtonShowEvent.value = false
    }

    fun checkAnswer(radioSelectedIndex: Int) {
        if (isPreviousStep) return
        val point = currentQuestion.answerList[radioSelectedIndex].point
        val scale = currentQuestion.scale
        val step = Step()
        if (scale == "horizontal") horizontalScore += point else verticalScore += point

        step.let {
            it.questionIndex = questionCounterLocal-1
            it.rbIndex = radioSelectedIndex
            it.scale = scale
            it.point = point
        }
        // Save as a previous step
        previousStepLocal = step

        _FABButtonShowEvent.value = true
    }
}