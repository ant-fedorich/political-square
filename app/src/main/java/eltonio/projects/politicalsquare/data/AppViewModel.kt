package eltonio.projects.politicalsquare.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eltonio.projects.politicalsquare.models.Question
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DatabaseRepository
    private var prefRepository: SharedPrefRepository

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        repository = DatabaseRepository(quizResultDao, questionDao)
        prefRepository = SharedPrefRepository()
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuizResult(quizResult)
        }
    }

    fun deleteQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuizResult(quizResult)
        }
    }

    suspend fun getQuizResults(): List<QuizResult> {
        return repository.getQuizResults()
    }

    suspend fun getAllQuestions(): List<Question> {
        return repository.getAllQuestions()
    }

    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return repository.getQuestionsWithAnswers(quizId)
    }

    // SharefPrefRepository


}