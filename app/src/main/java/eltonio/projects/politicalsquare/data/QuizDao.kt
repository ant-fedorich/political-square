package eltonio.projects.politicalsquare.data

import androidx.room.Dao
import androidx.room.Insert
import eltonio.projects.politicalsquare.models.Quiz

@Dao
interface QuizDao {
    @Insert
    fun addQuiz(quiz: Quiz)
}