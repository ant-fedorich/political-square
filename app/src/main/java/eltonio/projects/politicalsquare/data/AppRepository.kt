package eltonio.projects.politicalsquare.data

import androidx.lifecycle.LiveData
import eltonio.projects.politicalsquare.data.QuizResult

class AppRepository(
    private val quizResultDao: QuizResultDao,
    private val questionDao: QuestionDao
) {

    val getQuizResults: List<QuizResult> = quizResultDao.getQuizResults()

    suspend fun addQuizResult(quizResult: QuizResult) {
        quizResultDao.addQuizResult(quizResult)
    }

     suspend fun getAllQuestions(): List<Question> {
        return questionDao.getAllQuestions()
    }
}