package eltonio.projects.politicalsquare.data

import androidx.room.*
import eltonio.projects.politicalsquare.models.QuizResult

@Dao
interface QuizResultDao {
    @Insert
    suspend fun addQuizResult(quizResult: QuizResult)

    @Delete
    suspend fun deleteQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM QuizResult")
    suspend fun getQuizResults(): List<QuizResult>
}