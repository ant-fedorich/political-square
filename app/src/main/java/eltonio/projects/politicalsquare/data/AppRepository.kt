package eltonio.projects.politicalsquare.data

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.util.*
import java.util.*

class AppRepository {

    class Local {

        private val prefSettings =
            appContext.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)

        private val pref =
            appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        fun clearPref() = pref.edit().clear().apply()

        fun setSplashIsAppeared() {
            prefSettings.edit()
                .putBoolean(PREF_SPLASH_APPEARED, true)
                .apply()
        }

        fun setSplashIsNotAppeared() {
            prefSettings.edit()
                .putBoolean(PREF_SPLASH_APPEARED, false)
                .apply()
        }

        fun getSplashAppeared(): Boolean {
            return prefSettings.getBoolean(PREF_SPLASH_APPEARED, false)
        }

        fun setLang(context: Context, lang: String) {
            val locale = Locale(lang)
            Log.d(TAG, "Lang: $lang, Default Lang: ${Locale.getDefault().language}")
            val config = Configuration()
            config.setLocale(locale)
            // TODO: Deprecated. Change it
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            prefSettings.edit().putString(PREF_LANG, lang).apply()

            defaultLang = lang
        }

        fun getLang(): String {
            val defLang = Locale.getDefault().language
            val language = prefSettings.getString(PREF_LANG, defLang)
            return language ?: defLang
        }

        fun saveQuizOption(quizOptionId: Int) =
            prefSettings.edit().putInt(PREF_QUIZ_OPTION, quizOptionId).apply()

        fun loadQuizOption(): Int {
            val defQuizOption: Int = QuizOptions.WORLD.id
            return prefSettings.getInt(PREF_QUIZ_OPTION, defQuizOption)
        }

        fun saveChosenView(
            x: Float,
            y: Float,
            horStartScore: Int,
            verStartScore: Int,
            ideology: String,
            quizId: Int,
            startedAt: String
        ) {
            pref.edit().apply {
                putFloat(PREF_CHOSEN_VIEW_X, x)
                putFloat(PREF_CHOSEN_VIEW_Y, y)
                putInt(PREF_HORIZONTAL_START_SCORE, horStartScore)
                putInt(PREF_VERTICAL_START_SCORE, verStartScore)
                putString(PREF_CHOSEN_IDEOLOGY, ideology)
                putInt(PREF_QUIZ_ID, quizId)
                putString(PREF_STARTED_AT, startedAt)
            }.apply()
        }

        fun getChosenViewX() = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
        fun getChosenViewY() = pref.getFloat(PREF_CHOSEN_VIEW_Y, 0f)
        fun getHorStartScore() = pref.getInt(PREF_HORIZONTAL_START_SCORE, 0)
        fun getVerStartScore() = pref.getInt(PREF_VERTICAL_START_SCORE, 0)
        fun getChosenIdeology() = pref.getString(PREF_CHOSEN_IDEOLOGY, "").toString()
        fun getStartedAt() = pref.getString(PREF_STARTED_AT, "").toString()
        fun getZeroAnswerCnt() = pref.getInt(PREF_ZERO_ANSWER_CNT, -1)

        fun getHorScore() = pref.getInt(PREF_HORIZONTAL_SCORE, -100)
        fun getVerScore() = pref.getInt(PREF_VERTICAL_SCORE, -100)

        fun setZeroAnswerCht(zeroAnswerCnt: Int) = pref.edit().putInt(PREF_ZERO_ANSWER_CNT, zeroAnswerCnt).apply()

        fun setHorScore(horizontalScore: Int) = pref.edit().putInt(PREF_HORIZONTAL_SCORE, horizontalScore).apply()
        fun setVerScore(verticalScore: Int) = pref.edit().putInt(PREF_VERTICAL_SCORE, verticalScore).apply()

        fun setSessionStarted() = pref.edit().putBoolean(PREF_SESSION_STARTED, true).apply()
        fun getSessionStarted(): Boolean = pref.getBoolean(PREF_SESSION_STARTED, false)

        fun setIntroOpened() = prefSettings.edit().putBoolean(PREF_IS_INTRO_OPENED, true).apply()
        fun getIntroOpened(): Boolean = prefSettings.getBoolean(PREF_IS_INTRO_OPENED, false)

        fun getViewPagerScreenList(): MutableList<ScreenItem> {
            return mutableListOf(
                ScreenItem(appContext.getString(R.string.intro_title_1), R.drawable.gif_intro_1),
                ScreenItem(appContext.getString(R.string.intro_title_2), R.drawable.gif_intro_2),
                ScreenItem(appContext.getString(R.string.intro_title_3), R.drawable.gif_intro_3)
            )
        }
    }

    class DB (
        private val quizResultDao: QuizResultDao,
        private val questionDao: QuestionDao
    )
    {
        suspend fun addQuizResult(quizResult: QuizResult) {
            quizResultDao.addQuizResult(quizResult)
        }

        suspend fun deleteQuizResult(quizResult: QuizResult) {
            quizResultDao.deleteQuizResult(quizResult)
        }

        suspend fun getQuizResults(): List<QuizResult> {
            return quizResultDao.getQuizResults()
        }

        suspend fun getAllQuestions(): List<Question> {
            return questionDao.getAllQuestions()
        }

        suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
            return questionDao.getQuestionsWithAnswers(quizId)
        }
    }

    class Cloud {
        var usersRef = Firebase.database.getReference("Users")
        var firebaseAuth: FirebaseAuth? = null
        var firebaseUser: FirebaseUser? = null  // LiveData
        private var userLoggedIn = false // LiveData
        private var userExists = true // FOR TEST
        private val localRepo = Local()

        init {
            firebaseAuth = Firebase.auth

            if (Firebase.auth.currentUser != null) {
                firebaseUser = Firebase.auth.currentUser
                userLoggedIn = true
            }
        }

        fun createAndSignInAnonymously() {
            firebaseAuth?.signInAnonymously()
                ?.addOnSuccessListener { result ->
                    Log.i(TAG, "signInAnonymously: SUCCESS")
                    firebaseUser = result.user
                    userLoggedIn = true // FOR TEST

                    val userId = result.user?.uid
                    val lastSessionStarted = getDateTime()

                    if (userId != null) {
                        setUserCreationDate(userId, lastSessionStarted) //TODO: Does not work
                        updateUser(userId, lastSessionStarted)
                    }
                }
                ?.addOnFailureListener { e ->
                    Log.e(TAG, "signInAnonymously: FAILURE: $e")
                }
        }

        fun addQuizResult(userId: String, quizResult: QuizResult) {
            val quizResultRef = Firebase.database.getReference("QuizResults").push()
            quizResultRef.setValue(quizResult)
            quizResultRef.child("userId").setValue(userId)
        }

        fun updateUser(userId: String, lastLogInDate: String) {
            usersRef?.child(userId)?.apply {
                child("name").setValue("Noname")
                child("email").setValue("Noname@mail.com")
                child("lastSessionStarted").setValue(lastLogInDate)
            }
        }

        fun setUserCreationDate(userId: String, userCreationDate: String) =
            usersRef?.child(userId)?.child("creationDate")?.setValue(userCreationDate)


        fun setAnalyticsUserId(userId: String) = App.analytics.setUserId(userId)

        fun setUserLangProperty(loadedLang: String) = App.analytics.setUserProperty(
            EVENT_PREFERRED_LANG, loadedLang)

        fun logSessionStartEvent() = App.analytics.logEvent(EVENT_QUIZ_SESSION_START, null)

        fun logAnonymLoginEvent(lastLogInDate: String) = App.analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, "anonymously")
            param(PARAM_LOGIN_DATE, lastLogInDate)
        }

        fun logQuizBeginEvent() = App.analytics.logEvent(EVENT_QUIZ_BEGIN) {
            param(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
        }

        fun logQuizCompleteEvent() = App.analytics.logEvent(EVENT_QUIZ_COMPLETE) {
            param(FirebaseAnalytics.Param.END_DATE, System.currentTimeMillis())
        }

        fun logDetailedInfoEvent() = App.analytics.logEvent(EVENT_DETAILED_INFO, null)

        fun logChangeQuizOptionEvent(quizOption: Int) {
            val bundle = Bundle().apply {
                putInt(FirebaseAnalytics.Param.CONTENT, quizOption)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "change_quiz_option")
            }
            App.analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

        fun logCrash(message: String) = App.crashlytics.log(message)
    }
}