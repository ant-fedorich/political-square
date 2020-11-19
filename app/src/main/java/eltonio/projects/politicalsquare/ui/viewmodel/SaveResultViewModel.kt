package eltonio.projects.politicalsquare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.QuizResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SaveResultViewModel(application: Application) : AndroidViewModel(application) {
    private var dbRepo: AppRepository.DB

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        dbRepo = AppRepository.DB(quizResultDao, questionDao)
    }

    fun getResultList(): LiveData<List<QuizResult>> {
        return liveData {
            emit(dbRepo.getQuizResults())
        }
    }

    fun getResultListWithDelay(): LiveData<List<QuizResult>> {
        return liveData {
            delay(200)
            emit(dbRepo.getQuizResults())
        }
    }

    fun deleteQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.deleteQuizResult(quizResult)
        }
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.addQuizResult(quizResult)
        }
    }
}