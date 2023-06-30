package eltonio.projects.politicalcompassquiz.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import eltonio.projects.politicalcompassquiz.repository.entity.QuestionWithAnswers
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import kotlinx.coroutines.flow.Flow

class FakeAndroidTestDBRepository : DBRepository {
    private var quizResultList = mutableListOf<QuizResult>() // Imitation DB
    private val quizResultListLiveData = MutableLiveData<List<QuizResult>>(quizResultList)

    init {
        quizResultList = mutableListOf(
            QuizResult(
                2, 2, "ideology", 2, 2, 2, 2, "10.10.10", "10.10.10", 2, 2.0
            )
        )
    }

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