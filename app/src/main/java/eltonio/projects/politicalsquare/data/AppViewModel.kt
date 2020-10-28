package eltonio.projects.politicalsquare.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eltonio.projects.politicalsquare.other.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var getQuizResults: List<QuizResult>
    private var repository: AppRepository
    var allQuestions: List<Question>? = null

    init {
        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        repository = AppRepository(quizResultDao, questionDao)
        getQuizResults = repository.getQuizResults
//        viewModelScope.launch {
//            getAllQuestions = repository.getAllQuestions()
//        }
    }

    fun addQuizResult(quizResult: QuizResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuizResult(quizResult)
        }
    }

    suspend fun getAllQuestions(): List<Question> {
            return repository.getAllQuestions()
    }

    fun getAllQuestionsWithBlock(): List<Question>? = runBlocking {
            val result = viewModelScope.async(Dispatchers.Main) { repository.getAllQuestions() }
            Log.i(TAG, "Thread is: ${Thread.currentThread().name}")
            allQuestions = result.await()
            return@runBlocking allQuestions
        }

}