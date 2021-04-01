package eltonio.projects.politicalsquare.repository

import androidx.room.Dao
import androidx.room.Insert
import eltonio.projects.politicalsquare.repository.entity.Quiz

@Dao
interface QuizDao {
    @Insert
    fun addQuiz(quiz: Quiz)
}