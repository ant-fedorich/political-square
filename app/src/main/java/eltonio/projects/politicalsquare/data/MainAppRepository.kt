package eltonio.projects.politicalsquare.data

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.App
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.appContext
import eltonio.projects.politicalsquare.util.AppUtil.getDateTime
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MainAppRepository @Inject constructor(
    private val questionDao: QuestionDao,
    private val quizResultDao: QuizResultDao,
    @ApplicationContext private val context: Context
) : AppRepository {
    inner class Local : AppRepository.Local {

        // TODO: DI: Inject appContext
        private val prefSettings =
            context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)

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

        override fun setLang(context: Context, lang: String) {
            val locale = Locale(lang)
            Log.d(TAG, "Lang: $lang, Default Lang: ${Locale.getDefault().language}")
            val config = Configuration()
            config.setLocale(locale)
            // TODO: Deprecated. Change it
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            prefSettings.edit().putString(PREF_LANG, lang).apply()
//            defaultLang = lang
        }


        override fun getLang(): String {
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
                putInt(PREF_CHOSEN_QUIZ_ID, quizId)
                putString(PREF_STARTED_AT, startedAt)
            }.apply()
        }

        fun getChosenViewX() = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
        fun getChosenViewY() = pref.getFloat(PREF_CHOSEN_VIEW_Y, 0f)
        fun getHorStartScore() = pref.getInt(PREF_HORIZONTAL_START_SCORE, 0)
        fun getVerStartScore() = pref.getInt(PREF_VERTICAL_START_SCORE, 0)
        fun getChosenIdeology() = pref.getString(PREF_CHOSEN_IDEOLOGY, "").toString()
        fun getStartedAt() = pref.getString(PREF_STARTED_AT, "").toString()

        fun getHorScore() = pref.getInt(PREF_HORIZONTAL_SCORE, -100)
        fun getVerScore() = pref.getInt(PREF_VERTICAL_SCORE, -100)

        fun setHorScore(horizontalScore: Int) = pref.edit().putInt(PREF_HORIZONTAL_SCORE, horizontalScore).apply()
        fun setVerScore(verticalScore: Int) = pref.edit().putInt(PREF_VERTICAL_SCORE, verticalScore).apply()

        fun setSessionStarted() = pref.edit().putBoolean(PREF_SESSION_STARTED, true).apply()
        fun getSessionStarted(): Boolean = pref.getBoolean(PREF_SESSION_STARTED, false)

        fun setIntroOpened() = prefSettings.edit().putBoolean(PREF_IS_INTRO_OPENED, true).apply()
        fun getIntroOpened(): Boolean = prefSettings.getBoolean(PREF_IS_INTRO_OPENED, false)

        fun setSplashAnimationTime(animationTime: Long) = pref.edit().putLong(PREF_SPLASH_ANIMATION_TIME, animationTime).apply()
        fun getSplashAnimationTime() = pref.getLong(PREF_SPLASH_ANIMATION_TIME, 600L)

        fun setQuizIsActive(isActive: Boolean) = pref.edit().putBoolean(PREF_QUIZ_IS_ACTIVE, isActive).apply()
        fun getQuizIsActive() = pref.getBoolean(PREF_QUIZ_IS_ACTIVE, false)

        fun setMainActivityIsInFront(isInFront: Boolean) = pref.edit().putBoolean(PREF_MAIN_ACTIVITY_IS_IN_FRONT, isInFront).apply()
        fun getMainActivityIsInFront() = pref.getBoolean(PREF_MAIN_ACTIVITY_IS_IN_FRONT, false)


        fun getViewPagerScreenList(): MutableList<ScreenItem> {
            return mutableListOf(
                ScreenItem(appContext.getString(R.string.intro_title_1), R.drawable.gif_intro_1),
                ScreenItem(appContext.getString(R.string.intro_title_2), R.drawable.gif_intro_2),
                ScreenItem(appContext.getString(R.string.intro_title_3), R.drawable.gif_intro_3)
            )
        }
    }

    inner class DB : AppRepository.DB {
        override suspend fun addQuizResult(quizResult: QuizResult) {
            quizResultDao.addQuizResult(quizResult)
        }

        override suspend fun deleteQuizResult(quizResult: QuizResult) {
            quizResultDao.deleteQuizResult(quizResult)
        }

        override fun getQuizResults(): LiveData<List<QuizResult>> {
            return quizResultDao.getQuizResults()
        }

        suspend fun getAllQuestions(): List<Question> {
            return questionDao.getAllQuestions()
        }

        suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers> {
            return questionDao.getQuestionsWithAnswers(quizId)
        }
    }

    inner class Cloud : AppRepository.Cloud {
        var usersRef = Firebase.database.getReference("Users")
        var firebaseAuth: FirebaseAuth? = null
        var firebaseUser: FirebaseUser? = null  // LiveData
        private var userLoggedIn = false // LiveData

        init {
            firebaseAuth = Firebase.auth

            if (Firebase.auth.currentUser != null) {
                firebaseUser = Firebase.auth.currentUser
                userLoggedIn = true
            }
        }

        suspend fun createAndSignInAnonymously() {
            firebaseAuth?.signInAnonymously()
                ?.addOnSuccessListener { result ->
                    Log.i(TAG, "signInAnonymously: SUCCESS")
                    firebaseUser = result.user
                    userLoggedIn = true // FOR TEST

                    val userId = result.user?.uid
                    val lastSessionStarted = getDateTime()

                    if (userId != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            setUserCreationDate(userId, lastSessionStarted)
                            updateUser(userId, lastSessionStarted)
                        }
                    }
                }
                ?.addOnFailureListener { e ->
                    Log.e(TAG, "signInAnonymously: FAILURE: $e")
                }
        }

        suspend fun addQuizResult(userId: String, quizResult: QuizResult) {
            val quizResultRef = Firebase.database.getReference("QuizResults").push()
            quizResultRef.setValue(quizResult)
            quizResultRef.child("userId").setValue(userId)
        }

        suspend fun updateUser(userId: String, lastLogInDate: String) {
            usersRef.child(userId).apply {
                child("name").setValue("Noname")
                child("email").setValue("Noname@mail.com")
                child("lastSessionStarted").setValue(lastLogInDate)
            }
        }

        suspend fun setUserCreationDate(userId: String, userCreationDate: String) =
            usersRef.child(userId).child("creationDate").setValue(userCreationDate)


        suspend fun setAnalyticsUserId(userId: String) = App.analytics.setUserId(userId)

        suspend fun setUserLangProperty(loadedLang: String) = App.analytics.setUserProperty(
            EVENT_PREFERRED_LANG, loadedLang)

        suspend fun logSessionStartEvent() = App.analytics.logEvent(EVENT_QUIZ_SESSION_START, null)

        suspend fun logAnonymLoginEvent(lastLogInDate: String) = App.analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, "anonymously")
            param(PARAM_LOGIN_DATE, lastLogInDate)
        }

        suspend fun logQuizBeginEvent() = App.analytics.logEvent(EVENT_QUIZ_BEGIN) {
            param(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
        }

        suspend fun logQuizCompleteEvent() = App.analytics.logEvent(EVENT_QUIZ_COMPLETE) {
            param(FirebaseAnalytics.Param.END_DATE, System.currentTimeMillis())
        }

        suspend fun logDetailedInfoEvent() = App.analytics.logEvent(EVENT_DETAILED_INFO, null)

        suspend fun logChangeQuizOptionEvent(quizOption: Int) {
            val bundle = Bundle().apply {
                putInt(FirebaseAnalytics.Param.CONTENT, quizOption)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "change_quiz_option")
            }
            App.analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        // TODO: Crashlytics disabled
        // fun logCrash(message: String) = App.crashlytics.log(message)
    }

    // TODO: 03/17/2021: crutch: get rid of Interface method from repo
    inner class UI : AppRepository.UI{
        fun showEndQuizDialogLambda(context: Context, onOkBlock: () -> Unit) {
            AlertDialog.Builder(context).create().apply {
                val dialogTitle = context.getString(R.string.all_dialog_do_you_want_to_end)
                setTitle(dialogTitle)
                setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.all_dialog_yes)) { _, _ ->
                    Local().setQuizIsActive(false)
//            quizIsActive = false // TODO: V - livedata? or get from Repo directly?
                    onOkBlock()
                }
                setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.all_dialog_no)) { _, _ ->
                    return@setButton
                }
                show()
            }
        }
    }

}