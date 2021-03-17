package eltonio.projects.politicalsquare.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eltonio.projects.politicalsquare.models.QuizResult

@Dao
interface QuizResultDao {
    @Insert
    suspend fun addQuizResult(quizResult: QuizResult)

    @Delete
    suspend fun deleteQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM QuizResult")
    fun getQuizResults(): LiveData<List<QuizResult>>
}