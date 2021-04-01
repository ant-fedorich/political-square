package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SavedResultViewModel @Inject constructor(
    private var dbRepo: DBRepository
) : ViewModel() {

    fun getQuizResultList(): LiveData<List<QuizResult>> = dbRepo.getQuizResults()

    fun getResultListWithDelay(): LiveData<List<QuizResult>> {
        return liveData {
            delay(200)
            dbRepo.getQuizResults()
        }
    }

    fun deleteQuizResult(quizResult: QuizResult) = runBlocking {
            dbRepo.deleteQuizResult(quizResult)
    }

    fun addQuizResult(quizResult: QuizResult) = runBlocking {
            dbRepo.addQuizResult(quizResult)
    }
}