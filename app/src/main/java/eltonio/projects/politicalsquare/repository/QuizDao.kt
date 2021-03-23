package eltonio.projects.politicalsquare.repository

import androidx.room.Dao
import androidx.room.Insert
import eltonio.projects.politicalsquare.model.Quiz

@Dao
interface QuizDao {
    @Insert
    fun addQuiz(quiz: Quiz)
}