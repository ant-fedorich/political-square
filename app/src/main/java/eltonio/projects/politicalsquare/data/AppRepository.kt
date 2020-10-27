package eltonio.projects.politicalsquare.data

import androidx.lifecycle.LiveData
import eltonio.projects.politicalsquare.data.QuizResult

class AppRepository(private val quizResultDao: QuizResultDao) {

    val getQuizResults: List<QuizResult> = quizResultDao.getQuizResults()

    suspend fun addQuizResult(quizResult: QuizResult) {
        quizResultDao.addQuizResult(quizResult)
    }
}