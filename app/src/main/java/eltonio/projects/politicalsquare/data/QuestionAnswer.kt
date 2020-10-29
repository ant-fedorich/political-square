package eltonio.projects.politicalsquare.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "QuestionsAnswers",
    foreignKeys = [
        ForeignKey(
            entity = Question::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.NO_ACTION),
        ForeignKey(
            entity = Answer::class,
            parentColumns = ["id"],
            childColumns = ["answerId"],
            onDelete = ForeignKey.NO_ACTION)
    ]
)
data class QuestionAnswer (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val questionId: Int,
    val answerId: Int,
    val point: Float
)