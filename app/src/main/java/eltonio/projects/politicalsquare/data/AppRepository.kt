package eltonio.projects.politicalsquare.data

class AppRepository(
    private val quizResultDao: QuizResultDao,
    private val questionDao: QuestionDao
) {
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