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
    private var dbRepo: AppRepository.DB
    private var localRepo: AppRepository.Local

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        dbRepo = AppRepository.DB(quizResultDao, questionDao)
        localRepo = AppRepository.Local()
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.addQuizResult(quizResult)
        }
    }

    fun deleteQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.deleteQuizResult(quizResult)
        }
    }

    suspend fun getQuizResults(): List<QuizResult> {
        return dbRepo.getQuizResults()
    }

    suspend fun getAllQuestions(): List<Question> {
        return dbRepo.getAllQuestions()
    }

    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return dbRepo.getQuestionsWithAnswers(quizId)
    }

}