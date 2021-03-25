package eltonio.projects.politicalsquare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.model.QuestionWithAnswers
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.DBRepository

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

    override fun getQuizResults(): LiveData<List<QuizResult>> {
        return quizResultListLiveData
    }

    override suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return emptyList()
    }
}