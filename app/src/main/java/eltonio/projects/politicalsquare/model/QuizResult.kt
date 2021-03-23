package eltonio.projects.politicalsquare.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "QuizResult",
    foreignKeys = [ForeignKey(
            entity = Quiz::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("quizId"),
            onDelete = ForeignKey.NO_ACTION
        )]
)
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val quizId: Int,
    val ideologyStringId: String,
    val horStartScore: Int,
    val verStartScore: Int,
    val horResultScore: Int,
    val verResultScore: Int,
    val startedAt: String,
    val endedAt: String,
    val duration: Int,
    val avgAnswerTime: Double
)