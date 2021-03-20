package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.data.MainAppRepository
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.ui.ResultActivity.Companion.chosenViewX
import eltonio.projects.politicalsquare.ui.ResultActivity.Companion.chosenViewY
import eltonio.projects.politicalsquare.util.AppUtil.getIdeology
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyStringId

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    repository: MainAppRepository
) : ViewModel() {
    private var localRepo = repository.Local()
    private var cloudRepo = repository.Cloud()
    private var dbRepo = repository.DB()
    private var interfaceRepo = repository.UI()

    private var chosenIdeologyLiveData: MutableLiveData<String>
    private var resultIdeologyLiveData: MutableLiveData<String>
    private var compassX: MutableLiveData<Int>
    private var compassY: MutableLiveData<Int>

    private var chosenIdeology = ""
    private var resultIdeology = ""
    private var startedAt = ""
    private var endedAt = ""
    private var duration = 0
    private var avgAnswerTime = 0.0

    private var userId = ""
    private var quizId = -1

    var verScore: Int = 0
    var horScore: Int = 0
    var horStartScore = 0
    var verStartScore = 0

    private var ideologyStringId: String = ""


    init {
        viewModelScope.launch(Dispatchers.IO) {
            cloudRepo.logQuizCompleteEvent()
        }

        // LiveData
//        chosenIdeology = localRepo.getChosenIdeology()
        chosenIdeologyLiveData = MutableLiveData()
        resultIdeologyLiveData = MutableLiveData()

        collectAllResultData()
        getIdeologyData()
        getTimeData()
        addQuizResultToRepository()

        compassX = MutableLiveData(horScore.plus(40))
        compassY = MutableLiveData(verScore.plus(40))
        chosenIdeologyLiveData.value = chosenIdeology
    }

    fun getChosenIdeology(): LiveData<String> {
        return chosenIdeologyLiveData
    }

    fun getResultIdeology(): LiveData<String> {
        return resultIdeologyLiveData
    }

    private fun collectAllResultData() {
        quizId = localRepo.loadQuizOption()

        chosenViewX = localRepo.getChosenViewX()
        chosenViewY = localRepo.getChosenViewY()
        horStartScore = localRepo.getHorStartScore()
        verStartScore = localRepo.getVerStartScore()
        chosenIdeology = localRepo.getChosenIdeology()
        startedAt = localRepo.getStartedAt()

        horScore = localRepo.getHorScore()
        verScore = localRepo.getVerScore()

        userId = cloudRepo.firebaseUser?.uid.toString()
    }

    private fun getIdeologyData() {
        if (horScore != null && verScore != null) {
            resultIdeology = getIdeology(horScore, verScore)
            resultIdeologyLiveData.value = resultIdeology
        }
        ideologyStringId = getIdeologyStringId(resultIdeology)
    }

    // TODO: Do Local Unit test
    private fun getTimeData() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val endDate = Date()
        val startedAtParsed = formatter.parse(startedAt)
        val diffInMillies = endDate.time - startedAtParsed.time
        duration = TimeUnit.MILLISECONDS.toSeconds(diffInMillies).toInt()
        endedAt = formatter.format(endDate)

        avgAnswerTime =
            if (quizId == QuizOptions.WORLD.id)
                duration/ QuizOptions.WORLD.quesAmount.toDouble()
            else
                duration/ QuizOptions.UKRAINE.quesAmount.toDouble()
    }

    private fun addQuizResultToRepository() {
        val quizResult = QuizResult(
            id = 0, //id is autoincrement
            quizId = quizId,
            ideologyStringId = ideologyStringId,
            horStartScore = horStartScore,
            verStartScore = verStartScore,
            horResultScore = horScore,
            verResultScore = verScore,
            startedAt = startedAt,
            endedAt = endedAt,
            duration = duration,
            avgAnswerTime = avgAnswerTime
        )

        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.addQuizResult(quizResult)
            cloudRepo.addQuizResult(userId, quizResult)
        }
    }

    fun getCompassX(): LiveData<Int> {
        return compassX
    }

    fun getCompassY(): LiveData<Int> {
        return compassY
    }

    fun onCompassInfoClick() {
        viewModelScope.launch(Dispatchers.IO) {
            cloudRepo.logDetailedInfoEvent()
        }
    }

    fun showEndQuizDialogLambda(context: Context, onOkBlock: () -> Unit) {
        interfaceRepo.showEndQuizDialogLambda(context, onOkBlock)
    }
}