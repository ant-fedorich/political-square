package eltonio.projects.politicalsquare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.data.AppViewModel
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.ui.ResultActivity.Companion.chosenViewX
import eltonio.projects.politicalsquare.ui.ResultActivity.Companion.chosenViewY
import eltonio.projects.politicalsquare.util.chosenQuizId
import eltonio.projects.politicalsquare.util.getIdeology
import eltonio.projects.politicalsquare.util.getIdeologyStringId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private var localRepo = AppRepository.Local()
    private var cloudRepo = AppRepository.Cloud()
    private var dbRepo: AppRepository.DB

    private lateinit var appViewModel: AppViewModel
    private lateinit var scope: CoroutineScope

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



    var chosenIdeologyLiveData: MutableLiveData<String>
    var resultIdeologyLiveData: MutableLiveData<String>
    private var compassX: MutableLiveData<Int>
    private var compassY: MutableLiveData<Int>


    private var ideologyStringId: String = ""


    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        dbRepo = AppRepository.DB(quizResultDao, questionDao)
        localRepo = AppRepository.Local()

        cloudRepo.logQuizCompleteEvent()

        // LiveData
        chosenIdeology = localRepo.getChosenIdeology()
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
            quizId = chosenQuizId,
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
        }
        cloudRepo.addQuizResult(userId, quizResult)
    }

    fun getCompassX(): LiveData<Int> {
        return compassX
    }

    fun getCompassY(): LiveData<Int> {
        return compassY
    }

    fun onCompassInfoClick() {
        cloudRepo.logDetailedInfoEvent()
    }
}