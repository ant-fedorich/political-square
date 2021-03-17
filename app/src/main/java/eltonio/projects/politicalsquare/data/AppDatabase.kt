package eltonio.projects.politicalsquare.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.util.TAG
import java.lang.Exception

@Database(
    entities = [Question::class, QuestionAnswer::class, Answer::class, Quiz::class, QuizResult::class],
    views = [QuestionAnswerDetail::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao
    abstract fun questionDao(): QuestionDao
    abstract fun quizTestingDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "PoliticalSquare.db"
        private const val DB_PATH = "/data/data/eltonio.projects.politicalsquare/databases/"
        private var dbFullPath = ""

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset(DB_NAME)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}