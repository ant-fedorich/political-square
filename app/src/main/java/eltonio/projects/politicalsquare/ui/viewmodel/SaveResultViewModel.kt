package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.data.MainAppRepository
import eltonio.projects.politicalsquare.models.QuizResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveResultViewModel @Inject constructor(
    repository: MainAppRepository
) : ViewModel() {
    private var dbRepo = repository.DB()

    init {
//        val quizResultDao = AppDatabase.getDatabase(application).quizResultDao()
//        val questionDao = AppDatabase.getDatabase(application).questionDao()
//        dbRepo = AppRepository.DB(quizResultDao, questionDao)
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