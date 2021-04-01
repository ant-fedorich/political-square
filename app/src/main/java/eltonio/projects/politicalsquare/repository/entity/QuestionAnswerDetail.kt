package eltonio.projects.politicalsquare.repository.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT qa.id, qa.questionId, a.answer_uk, a.answer_ru, a.answer_en, qa.point " +
        "FROM QuestionsAnswers as qa JOIN Answers as a " +
        "on qa.answerId = a.id")
data class QuestionAnswerDetail (
    val id: Int,
    val questionId: Int,
    @ColumnInfo(name = "answer_uk")
    val answerUk: String,
    @ColumnInfo(name = "answer_ru")
    val answerRu: String,
    @ColumnInfo(name = "answer_en")
    val answerEn: String,
    val point: Float
)