package eltonio.projects.politicalsquare.data

import androidx.room.Database
import androidx.room.RoomDatabase
import eltonio.projects.politicalsquare.models.*

@Database(
    entities = [Question::class, QuestionAnswer::class, Answer::class, Quiz::class, QuizResult::class],
    views = [QuestionAnswerDetail::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao
    abstract fun questionDao(): QuestionDao
    abstract fun quizTestingDao(): QuizDao
}