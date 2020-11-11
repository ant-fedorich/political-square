package eltonio.projects.politicalsquare.data

import eltonio.projects.politicalsquare.models.Question
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizResult

class DatabaseRepository(
    private val quizResultDao: QuizResultDao,
    private val questionDao: QuestionDao
) {

    // SQL DB
    suspend fun addQuizResult(quizResult: QuizResult) {
        quizResultDao.addQuizResult(quizResult)
    }

    suspend fun deleteQuizResult(quizResult: QuizResult) {
        quizResultDao.deleteQuizResult(quizResult)
    }

    suspend fun getQuizResults(): List<QuizResult> {
        return quizResultDao.getQuizResults()
    }

    suspend fun getAllQuestions(): List<Question> {
        return questionDao.getAllQuestions()
    }

    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
        return questionDao.getQuestionsWithAnswers(quizId)
    }
}