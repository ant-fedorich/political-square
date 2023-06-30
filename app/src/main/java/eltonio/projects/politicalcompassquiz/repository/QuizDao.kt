package eltonio.projects.politicalcompassquiz.repository

import androidx.room.Dao
import androidx.room.Insert
import eltonio.projects.politicalcompassquiz.repository.entity.Quiz

@Dao
interface QuizDao {
    @Insert
    fun addQuiz(quiz: Quiz)
}