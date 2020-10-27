package eltonio.projects.politicalsquare.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuizResultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuizResult(quizResult: QuizResult)

    @Delete
    suspend fun deleteQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM QuizResult")
    fun getQuizResults(): List<QuizResult>
}