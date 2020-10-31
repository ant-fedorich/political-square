package eltonio.projects.politicalsquare.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.other.App
import eltonio.projects.politicalsquare.other.TAG
import java.lang.Exception

@Database(
    entities = [Question::class, QuestionAnswer::class, Answer::class, Quiz::class, QuizResult::class],
    views = [QuestionAnswerDetail::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao
    abstract fun questionDao(): QuestionDao

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

        fun checkDBExists(): Boolean {
            dbFullPath = DB_PATH + DB_NAME
            var check: SQLiteDatabase? = null
            try {
                check = SQLiteDatabase.openDatabase(dbFullPath, null, SQLiteDatabase.OPEN_READWRITE)
            } catch (e: Exception) {
                App.crashlytics.log("ERROR while checking to open DB. DB does not exist (this time): $e")
                Log.e(TAG, "ERROR while checking to open DB. DB does not exist (this time)")
            }
            check?.close()
            return check != null
        }
    }
}