package eltonio.projects.politicalsquare.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Answers")
data class Answer(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    val id: Int,
    @ColumnInfo(name = "answer_uk")
    val answerUk: String,
    @ColumnInfo(name = "answer_ru")
    val answerRu: String,
    @ColumnInfo(name = "answer_en")
    val answerEn: String
)