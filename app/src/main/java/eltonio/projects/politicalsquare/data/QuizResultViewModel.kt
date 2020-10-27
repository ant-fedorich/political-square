package eltonio.projects.politicalsquare.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizResultViewModel(application: Application) : AndroidViewModel(application) {
    private var getQuizResults: List<QuizResult>
    private var repository: AppRepository

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        repository = AppRepository(quizResultDao)
        getQuizResults = repository.getQuizResults
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuizResult(quizResult)
        }
    }
}