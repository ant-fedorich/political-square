package eltonio.projects.politicalsquare.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import eltonio.projects.politicalsquare.other.*

class SharedPrefRepository {

    private val prefSettings =
        appContext.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)

    private val pref =
        appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setSplashAppeared() {
        prefSettings.edit()
            .putBoolean(PREF_SPLASH_APPEARED, true)
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


    fun getChosenViewX(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getChosenViewY(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getHorStartScore(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getVerStartScore(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getChosenIdeology(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getStartedAt(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    fun getStartedAt(): Float = pref.getFloat(PREF_CHOSEN_VIEW_X, 0f)

    zeroAnswerCnt

    val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    chosenViewX = sharedPref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
    chosenViewY = sharedPref.getFloat(PREF_CHOSEN_VIEW_Y, 0f)
    horStartScore = sharedPref.getInt(PREF_HORIZONTAL_START_SCORE, 0)
    verStartScore = sharedPref.getInt(PREF_VERTICAL_START_SCORE, 0)
    chosenIdeology = sharedPref.getString(PREF_CHOSEN_IDEOLOGY, "").toString()
    startedAt = sharedPref.getString(PREF_STARTED_AT, "").toString()
    zeroAnswerCnt = sharedPref.getInt(PREF_ZERO_ANSWER_CNT, -1)
}