package eltonio.projects.politicalsquare.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
   tableName = "Questions",
   foreignKeys = [ForeignKey(
       entity = Quiz::class,
       parentColumns = ["id"],
       childColumns = ["quizId"],
       onDelete = ForeignKey.NO_ACTION
   )]
)
data class Question (
    @PrimaryKey(autoGenerate = true)
   val id: Int,
   val quizId: Int,
    @ColumnInfo(name = "question_uk")
   val questionUk: String,
    @ColumnInfo(name = "question_ru")
   val questionRu: String,
    @ColumnInfo(name = "question_en")
   val questionEn: String,
   val scale: Int
)