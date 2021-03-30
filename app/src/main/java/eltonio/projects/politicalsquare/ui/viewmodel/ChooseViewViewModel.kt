package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx
import eltonio.projects.politicalsquare.util.AppUtil.getDateTime
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyResIdFromScore
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ChooseViewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localRepo: LocalRepository,
) : ViewModel() {
    // TODO: Inner LiveData, why
    private var _ideologyIsChosenEvent: MutableLiveData<Boolean> = MutableLiveData()
    private var _ideologyResId: MutableLiveData<Int> = MutableLiveData()

    private var _quizOptionId: MutableLiveData<Int> = MutableLiveData()
    val quizOptionId: LiveData<Int> = _quizOptionId
    private var _horStartScore: MutableLiveData<Int> = MutableLiveData()
    val horStartScore: LiveData<Int> = _horStartScore
    private var _verStartScore: MutableLiveData<Int> = MutableLiveData()
    val verStartScore: LiveData<Int> = _verStartScore

    private var x = 0f
    private var y = 0f

    /** METHODS */
    fun loadQuizOption() = runBlocking {
        _quizOptionId.value = localRepo.loadQuizOption()
    }



    fun setQuizIsActive(isActive: Boolean) = runBlocking {
            localRepo.setQuizIsActive(isActive)
        }

    fun saveChosenIdeology(x: Float, y: Float, horStartScore: Int, verStartScore: Int, ideologyStringId: String, quizId: Int) {
        val startedAt = getDateTime()

        runBlocking {
            localRepo.saveChosenIdeology(x, y, horStartScore, verStartScore, ideologyStringId, quizId, startedAt, -1, -1)
        }
    }

    fun getXandYForHover(inputX: Float, inputY: Float, endX: Int, endY: Int) {
        this.x = inputX
        this.y = inputY
        when {
            x >= 0 && x <= endX && y >= 0 && y <= endY -> {
                x = x; y = y
            }
            x >= 0 && x <= endX && y < 0 -> {
                x = x; y = 0f
            }
            x >= 0 && x <= endX && y > endY -> {
                x = x; y = endY.toFloat()
            }
            x < 0 && y >= 0 && y <= endY -> {
                x = 0f; y = y
            }
            x > endX && y >= 0 && y <= endY -> {
                x = endX.toFloat(); y = y
            }

            // Edges
            x < 0 && y < 0 -> {
                x = 0f; y = 0f
            }
            x > endX && y > endY -> {
                x = endX.toFloat(); y = endY.toFloat()
            }
            x < 0 && y > endY -> {
                x = 0f; y = endY.toFloat()
            }
            x > endX && y < 0 -> {
                x = endX.toFloat(); y = 0f
            }
        }
    }

    fun getIdeologyResId(): LiveData<Int> {
        val step = convertDpToPx(context, 4f)
        _horStartScore.value = (x / step - 40).toInt()
        _verStartScore.value = (y / step - 40).toInt()
        _ideologyResId.value = getIdeologyResIdFromScore(_horStartScore.value!!, _verStartScore.value!!)
        return _ideologyResId
    }

    fun setIdeologyIsChosenEvent(isChosen: Boolean) {
        _ideologyIsChosenEvent.value = isChosen
    }
    fun getIdeologyIsChosenEvent(): LiveData<Boolean> {
        return _ideologyIsChosenEvent
    }
}