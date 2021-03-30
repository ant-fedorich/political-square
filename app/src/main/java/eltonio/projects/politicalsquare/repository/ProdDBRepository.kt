package eltonio.projects.politicalsquare.repository

import androidx.lifecycle.LiveData
import eltonio.projects.politicalsquare.model.Question
import eltonio.projects.politicalsquare.model.QuestionWithAnswers
import eltonio.projects.politicalsquare.model.QuizResult
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

    override fun getQuizResults(): LiveData<List<QuizResult>> {
        return quizResultDao.getQuizResults()
    }

    suspend fun getAllQuestions(): List<Question> {
        return questionDao.getAllQuestions()
    }

    override suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return questionDao.getQuestionsWithAnswers(quizId)
    }
}