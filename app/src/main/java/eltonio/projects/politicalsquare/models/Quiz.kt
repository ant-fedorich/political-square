package eltonio.projects.politicalsquare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Quiz")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val owner: String,
    val description: String
)