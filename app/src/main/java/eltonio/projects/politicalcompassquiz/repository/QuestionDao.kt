package eltonio.projects.politicalcompassquiz.repository

import androidx.room.Dao
import androidx.room.Query
import eltonio.projects.politicalcompassquiz.repository.entity.Question
import eltonio.projects.politicalcompassquiz.repository.entity.QuestionWithAnswers
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT id, quizId, question_uk, question_ru, question_en, scale FROM Questions WHERE quizId = :quizId")
    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> // TODO: Change to LV
}