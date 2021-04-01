package eltonio.projects.politicalsquare.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import eltonio.projects.politicalsquare.repository.entity.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert
    suspend fun addQuizResult(quizResult: QuizResult)

    @Delete
    suspend fun deleteQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM QuizResult")
    fun getQuizResults(): Flow<List<QuizResult>>
}