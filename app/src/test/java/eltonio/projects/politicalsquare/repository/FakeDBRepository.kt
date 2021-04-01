package eltonio.projects.politicalsquare.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import eltonio.projects.politicalsquare.repository.entity.QuestionWithAnswers
import eltonio.projects.politicalsquare.repository.entity.QuizResult
import kotlinx.coroutines.flow.Flow

class FakeDBRepository : DBRepository {
    private val quizResultList = mutableListOf<QuizResult>() // Imitation DB
    private val quizResultListLiveData = MutableLiveData<List<QuizResult>>(quizResultList)

    private fun refreshLiveDataInDB() {
        quizResultListLiveData.postValue(quizResultList)
    }

    override suspend fun addQuizResult(quizResult: QuizResult) {
        quizResultList.add(quizResult)
        refreshLiveDataInDB()
    }

    override suspend fun deleteQuizResult(quizResult: QuizResult) {
        quizResultList.remove(quizResult)
        refreshLiveDataInDB()
    }

    override fun getQuizResults(): Flow<List<QuizResult>> {
        return quizResultListLiveData.asFlow()
    }

    override suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return emptyList()
    }
}