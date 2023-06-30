package eltonio.projects.politicalcompassquiz.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalcompassquiz.util.QuizOptions
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.model.ChosenIdeologyData
import eltonio.projects.politicalcompassquiz.repository.CloudRepository
import eltonio.projects.politicalcompassquiz.repository.DBRepository
import eltonio.projects.politicalcompassquiz.repository.LocalRepository
import eltonio.projects.politicalcompassquiz.util.AppUtil.getIdeologyStringIdByResId
import eltonio.projects.politicalcompassquiz.util.AppUtil.getIdeologyResIdFromScore
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _allDataCollectedState = MutableLiveData<Boolean>()
    val allDataCollectedState: LiveData<Boolean> = _allDataCollectedState

    init {
        viewModelScope.launch {
            cloudRepo.logQuizCompleteEvent()
            val jobResult = collectAllResultData()
            _allDataCollectedState.value = jobResult.await()
            getIdeologyData()
            getTimeData()
            addQuizResultToRepository()
        }

    }

    private fun collectAllResultData() = viewModelScope.async (IO) {
        chosenIdeology = localRepo.loadChosenIdeologyData()?.also {
            quizId = it.chosenQuizId

            horStartScore = it.horStartScore
            verStartScore = it.verStartScore
            horEndScore = it.horEndScore
            verEndScore = it.verEndScore
            startedAt = it.startedAt
            withContext(Main) {
                _chosenIdeologyStringId.value = it.chosenIdeologyStringId
                _chosenViewX.value = it.chosenViewX
                _chosenViewY.value = it.chosenViewY
                _compassX.value = it.horEndScore.plus(40)
                _compassY.value = it.verEndScore.plus(40)
            }
        }
        userId = cloudRepo.firebaseUser?.uid.toString()
        true
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

    private fun addQuizResultToRepository() = viewModelScope.launch(IO) {

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

        dbRepo.addQuizResult(quizResult)
        cloudRepo.addQuizResult(userId, quizResult)
    }

    fun onCompassInfoClick() = viewModelScope.launch {
            cloudRepo.logDetailedInfoEvent()
    }
}