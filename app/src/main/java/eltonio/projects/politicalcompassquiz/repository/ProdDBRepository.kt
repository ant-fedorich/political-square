package eltonio.projects.politicalcompassquiz.repository

import eltonio.projects.politicalcompassquiz.repository.entity.QuestionWithAnswers
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdDBRepository @Inject constructor(
        private val quizResultDao: QuizResultDao,
        private val questionDao: QuestionDao
    ) : DBRepository {
    override suspend fun addQuizResult(quizResult: QuizResult) {
        quizResultDao.addQuizResult(quizResult)
    }

    override suspend fun deleteQuizResult(quizResult: QuizResult) {
        quizResultDao.deleteQuizResult(quizResult)
    }

    override fun getQuizResults(): Flow<List<QuizResult>> {
        return quizResultDao.getQuizResults()
    }

    override suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return questionDao.getQuestionsWithAnswers(quizId)
    }
}