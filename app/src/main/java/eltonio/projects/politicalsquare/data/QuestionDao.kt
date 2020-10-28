package eltonio.projects.politicalsquare.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions")
    suspend fun getAllQuestions(): List<Question>
}