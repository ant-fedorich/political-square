package eltonio.projects.politicalsquare.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Relation

data class QuestionWithAnswers(
    val id: Int,
    val quizId: Int,
    @ColumnInfo(name = "question_uk")
    val questionUk: String,
    @ColumnInfo(name = "question_ru")
    val questionRu: String,
    @ColumnInfo(name = "question_en")
    val questionEn: String,
    val scale: String,
    @Relation(parentColumn = "id", entityColumn = "questionId", entity = QuestionAnswerDetail::class)
    val answerList: List<QuestionAnswerDetail>
)