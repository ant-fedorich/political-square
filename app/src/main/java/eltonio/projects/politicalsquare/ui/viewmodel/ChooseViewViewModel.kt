package eltonio.projects.politicalsquare.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.repository.DBRepository
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.util.AppUtil
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx
import eltonio.projects.politicalsquare.util.AppUtil.getDateTime
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyFromScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChooseViewViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val localRepo: LocalRepository,
    private val dbRepo: DBRepository,
) : ViewModel() {
    private var ideologyIsChosenEvent: MutableLiveData<Boolean> = MutableLiveData()
    private var ideologyTitle: MutableLiveData<String> = MutableLiveData()
    private var quizOptionId: MutableLiveData<Int> = MutableLiveData()
    private var horStartScore: MutableLiveData<Int> = MutableLiveData()
    private var verStartScore: MutableLiveData<Int> = MutableLiveData()

    private var x = 0f
    private var y = 0f
//    private var horStartScore = 0
//    private var verStartScore = 0

    init {
//        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
//        val questionDao = AppDatabase.getDatabase(application).questionDao()

        when(localRepo.loadQuizOption()) {
            QuizOptions.UKRAINE.id -> getQuestions(QuizOptions.UKRAINE.id)
            QuizOptions.WORLD.id -> getQuestions(QuizOptions.WORLD.id)
        }
    }

    /** METHODS */
    private fun getQuestions(quizId: Int) {
        quizOptionId.value = quizId
        viewModelScope.launch (Dispatchers.IO) {
            App.appQuestionsWithAnswers = dbRepo.getQuestionsWithAnswers(quizId)
            Collections.shuffle(App.appQuestionsWithAnswers)
        }
    }

    fun getQuizId(): LiveData<Int> {
        return quizOptionId
    }

    fun getHorStartScore(): LiveData<Int> {
        return horStartScore
    }

    fun getVerStartScore(): LiveData<Int> {
        return verStartScore
    }

    fun setQuizIsActive(isActive: Boolean) {
        localRepo.setQuizIsActive(isActive)
    }

    fun saveChosenView(x: Float, y: Float, horStartScore: Int, verStartScore: Int, ideologyId: String, quizId: Int) {
        val startedAt = getDateTime()
        localRepo.saveChosenView(x, y, horStartScore, verStartScore, ideologyId, quizId, startedAt) // TODO: put an object instead of attributes
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

    fun getIdeologyTitle(): LiveData<String> {
        val step = convertDpToPx(context, 4f)
        horStartScore.value =
            (x / step - 40).toInt()
        verStartScore.value =
            (y / step - 40).toInt()
        ideologyTitle.value = getIdeologyFromScore(context, horStartScore.value!!, verStartScore.value!!)
        return ideologyTitle
    }

    fun setIdeologyIsChosenEvent(isChosen: Boolean) {
        ideologyIsChosenEvent.value = isChosen
    }
    fun getIdeologyIsChosenEvent(): LiveData<Boolean> {
        return ideologyIsChosenEvent
    }
}