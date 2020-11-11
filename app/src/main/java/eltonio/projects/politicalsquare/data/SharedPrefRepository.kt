package eltonio.projects.politicalsquare.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.models.ScreenItem
import eltonio.projects.politicalsquare.util.*
import java.text.SimpleDateFormat
import java.util.*

class SharedPrefRepository {

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
    fun getUserId() = pref.getString(PREF_USER_ID, "").toString()

    fun getHorScore() = pref.getInt(PREF_HORIZONTAL_SCORE, -100)
    fun getVerScore() = pref.getInt(PREF_VERTICAL_SCORE, -100)
    fun getQuizId() = pref.getInt(PREF_QUIZ_ID, -1)

    fun putZeroAnswerCht(zeroAnswerCnt: Int) = pref.edit().putInt(PREF_ZERO_ANSWER_CNT, zeroAnswerCnt).apply()
    fun putUserId(userId: String) = pref.edit().putString(PREF_USER_ID, userId).apply()

    fun putHorScore(horizontalScore: Int) = pref.edit().putInt(PREF_HORIZONTAL_SCORE, horizontalScore).apply()
    fun putVerScore(verticalScore: Int) = pref.edit().putInt(PREF_VERTICAL_SCORE, verticalScore).apply()
    fun putQuizId(quizId: Int) = pref.edit().putInt(PREF_QUIZ_ID, quizId).apply()

    fun putSessionStarted() = pref.edit().putBoolean(PREF_SESSION_STARTED, true).apply()
    fun getSessionStarted(): Boolean = pref.getBoolean(PREF_SESSION_STARTED, false)

    fun putIntroOpened() = prefSettings.edit().putBoolean(PREF_IS_INTRO_OPENED, true).apply()
    fun getIntroOpened(): Boolean = prefSettings.getBoolean(PREF_IS_INTRO_OPENED, false)

    fun putUserLoggedIn() = prefSettings.edit().putBoolean(PREF_USER_LOGGED_IN, true).apply()
    fun getUserLoggedIn(): Boolean = prefSettings.getBoolean(PREF_USER_LOGGED_IN, false)



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

    fun loadLang(): String {
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

    fun getViewPagerScreenList(): MutableList<ScreenItem> {
        return mutableListOf(
            ScreenItem(appContext.getString(R.string.intro_title_1), R.drawable.gif_intro_1),
            ScreenItem(appContext.getString(R.string.intro_title_2), R.drawable.gif_intro_2),
            ScreenItem(appContext.getString(R.string.intro_title_3), R.drawable.gif_intro_3)
        )
    }

}