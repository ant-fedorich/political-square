package eltonio.projects.politicalsquare.repository

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.model.ScreenItem
import eltonio.projects.politicalsquare.util.*
import java.util.*
import javax.inject.Inject

// TODO: 03/24/2021 Get rif open, change to Mockk
open
class ProdLocalRepository @Inject constructor(
        @ApplicationContext private val context: Context
    ) : LocalRepository {

    // TODO: DI: Inject appContext
    private val prefSettings =
        context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)

    private val pref =
        context.getSharedPreferences(PREF_QUIZ_FOR_TEST, Context.MODE_PRIVATE)  // FIXME:

    override fun clearPref() = pref.edit().clear().apply()

    override fun setSplashIsAppeared() {
        prefSettings.edit()
            .putBoolean(PREF_SPLASH_APPEARED, true)
            .apply()
    }

    fun setSplashIsNotAppeared() {
        prefSettings.edit()
            .putBoolean(PREF_SPLASH_APPEARED, false)
            .apply()
    }

    override fun getSplashAppeared(): Boolean {
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
    }


    override fun getLang(): String {
        val defLang = Locale.getDefault().language
        val language = prefSettings.getString(PREF_LANG, defLang)
        return language ?: defLang
    }

    override fun saveQuizOption(quizOptionId: Int) =
        prefSettings.edit().putInt(PREF_QUIZ_OPTION, quizOptionId).apply()

    override fun loadQuizOption(): Int {
        val defQuizOption: Int = QuizOptions.WORLD.id
        return prefSettings.getInt(PREF_QUIZ_OPTION, defQuizOption)
    }

    override fun saveChosenView(
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

    override fun getChosenViewX() = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    override fun getChosenViewY() = pref.getFloat(PREF_CHOSEN_VIEW_Y, 0f)
    override fun getHorStartScore() = pref.getInt(PREF_HORIZONTAL_START_SCORE, 0)
    override fun getVerStartScore() = pref.getInt(PREF_VERTICAL_START_SCORE, 0)
    override fun getChosenIdeology() = pref.getString(PREF_CHOSEN_IDEOLOGY, "").toString()
    override fun getStartedAt() = pref.getString(PREF_STARTED_AT, "").toString()

    override fun getHorScore() = pref.getInt(PREF_HORIZONTAL_SCORE, -100)
    override fun getVerScore() = pref.getInt(PREF_VERTICAL_SCORE, -100)

    override fun setHorScore(horizontalScore: Int) = pref.edit().putInt(PREF_HORIZONTAL_SCORE, horizontalScore).apply()
    override fun setVerScore(verticalScore: Int) = pref.edit().putInt(PREF_VERTICAL_SCORE, verticalScore).apply()

    override fun setSessionStarted() = pref.edit().putBoolean(PREF_SESSION_STARTED, true).apply()
    override fun getSessionStarted(): Boolean = pref.getBoolean(PREF_SESSION_STARTED, false)

    override fun setIntroOpened() = prefSettings.edit().putBoolean(PREF_IS_INTRO_OPENED, true).apply()
    override fun getIntroOpened(): Boolean = prefSettings.getBoolean(PREF_IS_INTRO_OPENED, false)

    override fun setSplashAnimationTime(animationTime: Long) = pref.edit().putLong(PREF_SPLASH_ANIMATION_TIME, animationTime).apply()
    override fun getSplashAnimationTime() = pref.getLong(PREF_SPLASH_ANIMATION_TIME, 600L)

    override fun setQuizIsActive(isActive: Boolean) = pref.edit().putBoolean(PREF_QUIZ_IS_ACTIVE, isActive).apply()
    override fun getQuizIsActive() = pref.getBoolean(PREF_QUIZ_IS_ACTIVE, false)

    override fun setMainActivityIsInFront(isInFront: Boolean) = pref.edit().putBoolean(
        PREF_MAIN_ACTIVITY_IS_IN_FRONT, isInFront).apply()
    override fun getMainActivityIsInFront() = pref.getBoolean(PREF_MAIN_ACTIVITY_IS_IN_FRONT, false)


    override fun getViewPagerScreenList(): MutableList<ScreenItem> {
        return mutableListOf(
            ScreenItem(context.getString(R.string.intro_title_1), R.drawable.gif_intro_1),
            ScreenItem(context.getString(R.string.intro_title_2), R.drawable.gif_intro_2),
            ScreenItem(context.getString(R.string.intro_title_3), R.drawable.gif_intro_3)
        )
    }
}