package eltonio.projects.politicalsquare.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import eltonio.projects.politicalsquare.data.AppDatabase
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SaveResultViewModel(application: Application) : AndroidViewModel(application) {
    private var dbRepo: AppRepository.DB

    private var resultList = MutableLiveData<List<QuizResult>>()

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        dbRepo = AppRepository.DB(quizResultDao, questionDao)
    }

    fun getResultList(): LiveData<List<QuizResult>> {
        runBlocking {
            resultList.value = dbRepo.getQuizResults()
        }
        return resultList
    }

    fun getResultListWithDelay(): LiveData<List<QuizResult>> {
        runBlocking {
            delay(200)
            resultList.value = dbRepo.getQuizResults()
        }
        return resultList
    }

    fun deleteQuizResult(quizResult: QuizResult) {
        viewModelScope.launch {
            dbRepo.deleteQuizResult(quizResult)
        }
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch {
            dbRepo.addQuizResult(quizResult)
        }
    }
}