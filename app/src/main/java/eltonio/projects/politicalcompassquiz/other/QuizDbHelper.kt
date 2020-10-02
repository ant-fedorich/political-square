package eltonio.projects.politicalcompassquiz.other

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import eltonio.projects.politicalcompassquiz.models.Answer
import eltonio.projects.politicalcompassquiz.models.Question
import eltonio.projects.politicalcompassquiz.models.QuizResult
import eltonio.projects.politicalcompassquiz.other.App.Companion.crashlytics
import java.io.FileOutputStream
import java.lang.Exception
import eltonio.projects.politicalcompassquiz.models.QuizContract.QuestionsEntry as ques
import eltonio.projects.politicalcompassquiz.models.QuizContract.AnswersEntry as ans
import eltonio.projects.politicalcompassquiz.models.QuizContract.QuizResultEntry as res

// TODO: Refactor: should I use parameters for the superclass
class QuizDbHelper(private val context: Context) : SQLiteOpenHelper(context,
    DB_NAME, null,
    DB_VERSION
) {


    private var db: SQLiteDatabase? = null
    private var dbFullPath: String = ""

    override fun onCreate(db: SQLiteDatabase?) {
        // TODO: Refactor: should I use db?
        this.db = db

        //toast("QuizDbHelper:onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(TAG,"QuizDbHelper:onUpgrade")
        if (newVersion > oldVersion) {
            try {
                copyDB()
                Log.e(TAG, "Copying DB (onUpgrade)")
            } catch (e: Exception) {
                crashlytics.log("Error copying DB (onUpgrade): $e")
                Log.e(TAG, "Error copying DB (onUpgrade)")
            }
        }
    }

    /** CUSTOM METHODS*/

    // TODO: Refactor: Optimize logic of init
    fun initDB() {
        val dbExist = checkDB()
        Log.d(TAG, "DB: Check: $dbExist")
        if (dbExist) {
            openDB()
        } else {
            this.readableDatabase

            try {
                copyDB()
            } catch (e: Exception) {
                crashlytics.log("Error copying DB (onUpgrade): $e")
                Log.e(TAG, "Error copying DB")
            }
        }
    }

    fun checkDB(): Boolean {
        dbFullPath = DB_PATH + DB_NAME
        var check: SQLiteDatabase? = null
        try {
            check = SQLiteDatabase.openDatabase(dbFullPath, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: Exception) {
            crashlytics.log("ERROR while checking to open DB. DB does not exist (this time): $e")
            Log.e(TAG, "ERROR while checking to open DB. DB does not exist (this time)")
        }
        check?.close()
        return check != null
    }

    fun openDB() {
        dbFullPath = DB_PATH + DB_NAME
        db = SQLiteDatabase.openDatabase(dbFullPath, null, SQLiteDatabase.OPEN_READWRITE)
        Log.i(TAG, "DB is opened: $db")
    }

    fun copyDB() {
        val dbInput = context.assets.open(DB_NAME)

        val dbFile = context.getDatabasePath(DB_NAME)
        val dbOutput = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)

        while (dbInput.read(buffer) > 0) {
            dbOutput.write(buffer)
            Log.d(TAG, "DB: Writing...")
        }

        dbOutput.flush()
        dbOutput.close()
        dbInput.close()
        Log.i(TAG, "DB: copy is done!")
    }

    fun getAllQuestions(quizOption: Int): List<Question> {
        var questionList = mutableListOf<Question>()

        // TODO: Refactor: Why not to use openDB()
        SQLiteDatabase.openDatabase(dbFullPath, null, SQLiteDatabase.OPEN_READWRITE)
        db = readableDatabase

        // Get all questions
        // c1 - cursor for the questions
        var c1 = db?.rawQuery("""
            SELECT *
            FROM Questions
            WHERE quizId = $quizOption
            """.trimIndent(), null)

        if (c1 != null) {
            while (c1.moveToNext()) {

                var answerList = mutableListOf<Answer>()
                val currentQuestionId = c1.getInt(c1.getColumnIndex(ques._ID))

                // Get all answers for a current question
                // c2 - cursor for the answers
                // TODO: Refactor: Transfer answers to strings.xml
                var c2 = db?.rawQuery("""
                    SELECT *
                    FROM QuestionsAnswers as qa
                    JOIN Answers as a
                        on qa.answerId = a.id
                    WHERE qa.questionId = $currentQuestionId
                    """.trimIndent(), null)

                if (c2 != null) {
                    while (c2.moveToNext()) {
                        //add a current answer to the list
                        var answer = Answer()
                            .apply {
                            answer = c2.getString(c2.getColumnIndex("answer_$defaultLang"))
                            point = c2.getFloat(c2.getColumnIndex(ans.COLUMN_POINT))
                        }
                        answerList.add(answer)
                    }
                    c2.close()
                }

                // add a current question to the list
                var question = Question()
                    .apply {
                    this.id = c1.getInt(c1.getColumnIndex(ques._ID))
                    this.question = c1.getString(c1.getColumnIndex("question_$defaultLang"))
                    this.scale = c1.getString(c1.getColumnIndex(ques.COLUMN_SCALE))
                    this.answerList = answerList
                }
                questionList.add(question)

            }
            c1.close()
        }
        return questionList
    }

    fun getQuizResults(): MutableList<QuizResult> {
        var resultList = mutableListOf<QuizResult>()

        db = readableDatabase

        var cursor = db?.rawQuery("""
            SELECT
                ${res._ID},
            	${res.COLUMN_QUIZ_ID},
            	${res.COLUMN_IDEOLOGY_ID},
                ${res.COLUMN_HOR_START_SCORE},
                ${res.COLUMN_VER_START_SCORE},
                ${res.COLUMN_HOR_RESULT_SCORE},
                ${res.COLUMN_VER_RESULT_SCORE},
                ${res.COLUMN_ENDEDAT}
            FROM ${res.TABLE_NAME}
        """.trimIndent(), null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                var result = QuizResult()
                    .apply {
                    id = cursor.getInt(cursor.getColumnIndex(res._ID))
                    quizId = cursor.getInt(cursor.getColumnIndex(res.COLUMN_QUIZ_ID))
                    ideologyId = cursor.getString(cursor.getColumnIndex(res.COLUMN_IDEOLOGY_ID))
                    horStartScore = cursor.getInt(cursor.getColumnIndex(res.COLUMN_HOR_START_SCORE))
                    verStartScore = cursor.getInt(cursor.getColumnIndex(res.COLUMN_VER_START_SCORE))
                    horResultScore = cursor.getInt(cursor.getColumnIndex(res.COLUMN_HOR_RESULT_SCORE))
                    verResultScore = cursor.getInt(cursor.getColumnIndex(res.COLUMN_VER_RESULT_SCORE))
                    endedAt = cursor.getString(cursor.getColumnIndex(res.COLUMN_ENDEDAT))
                }
                resultList.add(result)
            }
            cursor.close()
        }
        return resultList
    }

    fun deleteQuizResult(itemId: Int) {
        db = writableDatabase
        db?.execSQL("""
            DELETE FROM ${res.TABLE_NAME}
            WHERE id = $itemId
        """.trimIndent()
        )
    }

/*
    fun addQuizResult(
        quizId: Int,
        ideologyId: String,
        horStartScore: Int,
        verStartScore: Int,
        // TODO: Refactor: add nullable in sql
        horResultScore: Int?,
        verResultScore: Int?,
        startedAt: String,
        endedAt: String,
        duration: Int,
        zeroAnswerCnt: Int,
        avgAnswerTime: Double
    ) {
        db = writableDatabase

        val values = ContentValues().apply {
            put(res.COLUMN_QUIZ_ID, quizId)
            put(res.COLUMN_IDEOLOGY_ID, ideologyId)
            put(res.COLUMN_HOR_START_SCORE, horStartScore)
            put(res.COLUMN_VER_START_SCORE, verStartScore)
            put(res.COLUMN_HOR_RESULT_SCORE, horResultScore)
            put(res.COLUMN_VER_RESULT_SCORE, verResultScore)
            put(res.COLUMN_STARTEDAT, startedAt)
            put(res.COLUMN_ENDEDAT, endedAt)
            put(res.COLUMN_DURATION, duration)
            put(res.COLUMN_ZERO_ANSWER_CNT, zeroAnswerCnt)
            put(res.COLUMN_AVG_ANSWER_TIME, avgAnswerTime)
        }

        db?.beginTransaction()
        try {
            val newRowId = db?.insert(res.TABLE_NAME, null, values)
            Log.d(TAG, "The row is put: $newRowId")
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(TAG, "The row put error: $e")
        } finally {
            db?.endTransaction()
        }

    }*/

    fun addQuizResult(quizResult: QuizResult) {
        db = writableDatabase

        val values = ContentValues().apply {
            put(res.COLUMN_QUIZ_ID, quizResult.quizId)
            put(res.COLUMN_IDEOLOGY_ID, quizResult.ideologyId)
            put(res.COLUMN_HOR_START_SCORE, quizResult.horStartScore)
            put(res.COLUMN_VER_START_SCORE, quizResult.verStartScore)
            put(res.COLUMN_HOR_RESULT_SCORE, quizResult.horResultScore)
            put(res.COLUMN_VER_RESULT_SCORE, quizResult.verResultScore)
            put(res.COLUMN_STARTEDAT, quizResult.startedAt)
            put(res.COLUMN_ENDEDAT, quizResult.endedAt)
            put(res.COLUMN_DURATION, quizResult.duration)
            put(res.COLUMN_ZERO_ANSWER_CNT, quizResult.zeroAnswerCnt)
            put(res.COLUMN_AVG_ANSWER_TIME, quizResult.avgAnswerTime)
        }

        db?.beginTransaction()
        try {
            val newRowId = db?.insert(res.TABLE_NAME, null, values)
            Log.d(TAG, "The row is put: $newRowId")
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            crashlytics.log("The row put error: $e")
            Log.d(TAG, "The row put error: $e")
        } finally {
            db?.endTransaction()
        }

    }

    companion object {
        const val DB_PATH = "/data/data/eltonio.projects.politicalcompassquiz/databases/"
        const val DB_NAME = "PoliticalCompass.db"
        const val DB_VERSION = 1
    }
}