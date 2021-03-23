package eltonio.projects.politicalsquare.repository

import androidx.room.Dao
import androidx.room.Query
import eltonio.projects.politicalsquare.model.Question
import eltonio.projects.politicalsquare.model.QuestionWithAnswers

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT id, quizId, question_uk, question_ru, question_en, scale FROM Questions WHERE quizId = :quizId")
    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers>
}