package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.repository.entity.QuizResult
import eltonio.projects.politicalsquare.repository.DBRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SavedResultViewModel @Inject constructor(
    private var dbRepo: DBRepository
) : ViewModel() {

    val quizResultList: LiveData<List<QuizResult>> = dbRepo.getQuizResults().asLiveData()

    val quizResultListWithDelay: LiveData<List<QuizResult>> = liveData {
        delay(200)
        dbRepo.getQuizResults()
    }

    fun deleteQuizResult(quizResult: QuizResult) = viewModelScope.launch(IO) {
            dbRepo.deleteQuizResult(quizResult)
    }

    fun addQuizResult(quizResult: QuizResult) = viewModelScope.launch(IO) {
            dbRepo.addQuizResult(quizResult)
    }
}