package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.model.ChosenIdeologyData
import eltonio.projects.politicalsquare.repository.CloudRepository
import eltonio.projects.politicalsquare.repository.DBRepository
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyStringIdByResId
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyResIdFromScore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private var localRepo: LocalRepository,
    private var dbRepo: DBRepository,
    private var cloudRepo: CloudRepository
) : ViewModel() {

    // TODO: Get rid of many vars

    private var startedAt = ""
    private var endedAt = ""
    private var duration = 0
    private var avgAnswerTime = 0.0

    private var userId = ""
    private var quizId = -1

    private var horStartScore = 0
    private var verStartScore = 0
    private var verEndScore = 0
    private var horEndScore = 0

    private var ideologyStringId: String = ""
    private var chosenIdeology: ChosenIdeologyData? = null

    private var _chosenIdeologyStringId = MutableLiveData<String>()
    val chosenIdeologyStringId: LiveData<String> = _chosenIdeologyStringId

    private var _resultIdeologyResId = MutableLiveData<Int>()
    val resultIdeologyResId: LiveData<Int> = _resultIdeologyResId

    private var _chosenViewX = MutableLiveData<Float>()
    val chosenViewX: LiveData<Float> = _chosenViewX

    private var _chosenViewY = MutableLiveData<Float>()
    val chosenViewY: LiveData<Float> = _chosenViewY

    private var _compassX = MutableLiveData<Int>()
    val compassX: LiveData<Int> = _compassX

    private var _compassY = MutableLiveData<Int>()
    val compassY: LiveData<Int> = _compassY

    init {
        runBlocking {
            cloudRepo.logQuizCompleteEvent()
        }
        collectAllResultData()
        getIdeologyData()
        getTimeData()
        addQuizResultToRepository()

//        _compassX.value = horScore.plus(40)
//        _compassY.value = verScore.plus(40)
    }

    private fun collectAllResultData() = runBlocking {
        chosenIdeology = localRepo.loadChosenIdeology()?.also {
            quizId = it.chosenQuizId
            _chosenViewX.value = it.chosenViewX
            _chosenViewY.value = it.chosenViewY
            horStartScore = it.horStartScore
            verStartScore = it.verStartScore
            horEndScore = it.horEndScore
            verEndScore = it.verEndScore
            startedAt = it.startedAt
            _chosenIdeologyStringId.value = it.chosenIdeologyStringId
            _compassX.value = it.horEndScore.plus(40)
            _compassY.value = it.verEndScore.plus(40)
        }
        userId = cloudRepo.firebaseUser?.uid.toString()

        delay(1000)
    }

    private fun getIdeologyData() {
        chosenIdeology?.let {
            _resultIdeologyResId.value = getIdeologyResIdFromScore(it.horEndScore, it.verEndScore)
        }

        ideologyStringId = getIdeologyStringIdByResId(_resultIdeologyResId.value!!)
    }

    private fun getTimeData() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
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
            horResultScore = horEndScore,
            verResultScore = verEndScore,
            startedAt = startedAt,
            endedAt = endedAt,
            duration = duration,
            avgAnswerTime = avgAnswerTime
        )

        runBlocking {
            dbRepo.addQuizResult(quizResult)
            cloudRepo.addQuizResult(userId, quizResult)
        }
    }

    fun onCompassInfoClick() = runBlocking {
            cloudRepo.logDetailedInfoEvent()
    }
}