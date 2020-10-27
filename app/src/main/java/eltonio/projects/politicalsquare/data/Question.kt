package eltonio.projects.politicalsquare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
   tableName = "Questions",
   foreignKeys = arrayOf(ForeignKey(
      entity = Quiz::class,
      parentColumns = arrayOf("id"),
      childColumns = arrayOf("quizId"),
      onDelete = ForeignKey.NO_ACTION
   )))
data class Question (
    @PrimaryKey(autoGenerate = true)
   val id: Int,
   val quizId: Int,
   val question_uk: String,
   val question_ru: String,
   val question_en: String,
   val scale: Int
)