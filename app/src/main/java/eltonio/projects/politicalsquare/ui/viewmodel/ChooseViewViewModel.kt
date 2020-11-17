package eltonio.projects.politicalsquare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.util.chosenQuizId
import eltonio.projects.politicalsquare.util.convertDpToPx
import eltonio.projects.politicalsquare.util.getDateTime
import eltonio.projects.politicalsquare.util.quizIsActive
import kotlinx.android.synthetic.main.activity_choose_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChooseViewViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepo = AppRepository.Local()
    private var dbRepo: AppRepository.DB

    private var quizId = -1

    private var x = 0f
    private var y = 0f
    private var horStartScore = 0
    private var verStartScore = 0

    var ideologyIsChosenEvent: MutableLiveData<Boolean>
    var ideology: MutableLiveData<String>

    init {
        ideologyIsChosenEvent = MutableLiveData()
        ideology = MutableLiveData()

        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        dbRepo = AppRepository.DB(quizResultDao, questionDao)

        when(localRepo.loadQuizOption()) {
            QuizOptions.UKRAINE.id -> getQuestionsWithAnswers(QuizOptions.UKRAINE.id)
            QuizOptions.WORLD.id -> getQuestionsWithAnswers(QuizOptions.WORLD.id)
        }
        Collections.shuffle(App.appQuestionsWithAnswers)
    }

    /** METHODS */
    private fun getQuestionsWithAnswers(quizId: Int) {
        this.quizId = quizId
        chosenQuizId = quizId
        viewModelScope.launch {
            App.appQuestionsWithAnswers = dbRepo.getQuestionsWithAnswers(quizId)
        }
    }

    fun saveChosenView(x: Float, y: Float, horStartScore: Int, verStartScore: Int, ideology: String, quizId: Int) {
        val startedAt = getDateTime()
        localRepo.saveChosenView(x, y, horStartScore, verStartScore, ideology, quizId, startedAt) // TODO: put an object instead of attributes
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

    fun getStartScore() {
        val step = convertDpToPx(4f)
        horStartScore =
            (x / step - 40).toInt()
        verStartScore =
            (y / step - 40).toInt()
    }

    fun getIdeology() {
        ideology.value = eltonio.projects.politicalsquare.util.getIdeology(horStartScore, verStartScore)
    }

}