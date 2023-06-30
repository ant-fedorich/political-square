package eltonio.projects.politicalcompassquiz.repository

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.model.ChosenIdeologyData
import eltonio.projects.politicalcompassquiz.model.ScreenItem
import eltonio.projects.politicalcompassquiz.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREF_SETTINGS)

class ProdLocalRepository @Inject constructor(
        @ApplicationContext private val context: Context
    ) : LocalRepository {
    private val prefDataStore = context.dataStore

    object PrefKeys {
        val KEY_SPLASH_APPEARED = booleanPreferencesKey(PREF_SPLASH_APPEARED)
        val KEY_LANG = stringPreferencesKey(PREF_LANG)
        val KEY_QUIZ_OPTION = intPreferencesKey(PREF_QUIZ_OPTION)
        val KEY_IS_INTRO_OPENED = booleanPreferencesKey(PREF_IS_INTRO_OPENED)
        val KEY_SESSION_STARTED = booleanPreferencesKey(PREF_SESSION_STARTED)
        val KEY_QUIZ_IS_ACTIVE = booleanPreferencesKey(PREF_QUIZ_IS_ACTIVE)
        val KEY_MAIN_ACTIVITY_IS_IN_FRONT = booleanPreferencesKey(PREF_MAIN_ACTIVITY_IS_IN_FRONT)
        val KEY_SPLASH_ANIMATION_TIME = longPreferencesKey(PREF_SPLASH_ANIMATION_TIME)
        val KEY_CHOSEN_IDEOLOGY_DATA = stringPreferencesKey(PREF_CHOSEN_IDEOLOGY_DATA)
    }

    override suspend fun clearPref() {
        prefDataStore.edit { it.clear() }
    }

    override suspend fun setSplashIsAppeared() {
        prefDataStore.edit { pref ->
            pref[PrefKeys.KEY_SPLASH_APPEARED] = true
        }
    }

    override suspend fun setSplashIsNOTAppeared() {
        prefDataStore.edit { pref ->
            pref[PrefKeys.KEY_SPLASH_APPEARED] = false
        }
    }

    override suspend fun getSplashAppeared(): Boolean {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_SPLASH_APPEARED] ?: false
        }.first()
    }

    override suspend fun setupAndSaveLang(context: Context, lang: String) {
        val locale = Locale(lang)
        Log.d(TAG, "Lang: $lang, Default Lang: ${Locale.getDefault().language}")
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, null)

        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_LANG] = lang
        }
    }

    override suspend fun getSavedLang(): String {
        val defLang = Locale.getDefault().language

        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_LANG] ?: defLang
        }.first()
    }

    override suspend fun saveQuizOption(quizOptionId: Int) {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_QUIZ_OPTION] = quizOptionId
        }
    }

    override suspend fun loadQuizOption(): Int {
        val defQuizOption = QuizOptions.WORLD.id

        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_QUIZ_OPTION] ?: defQuizOption
        }.first()
    }

    override suspend fun setIntroOpened() {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_IS_INTRO_OPENED] = true
        }
    }

    override suspend fun getIntroOpened(): Boolean {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_IS_INTRO_OPENED] ?: false
        }.first()
    }

    override suspend fun setSessionStarted() {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_SESSION_STARTED] = true
        }
    }

    override suspend fun getSessionStarted(): Boolean {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_SESSION_STARTED] ?: false
        }.first()
    }
    override suspend fun setSplashAnimationTime(animationTime: Long) {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_SPLASH_ANIMATION_TIME] = animationTime
        }
    }

    override suspend fun getSplashAnimationTime(): Long {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_SPLASH_ANIMATION_TIME] ?: 600L
        }.first()
    }


    override suspend fun setQuizIsActive(isActive: Boolean) {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_QUIZ_IS_ACTIVE] = isActive
        }
    }


    override suspend fun getQuizIsActive(): Boolean {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_QUIZ_IS_ACTIVE] ?: false
        }.first()
    }

    override suspend fun setMainActivityIsInFront(isInFront: Boolean) {
        prefDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_MAIN_ACTIVITY_IS_IN_FRONT] = isInFront
        }
    }

    override suspend fun getMainActivityIsInFront(): Boolean {
        return prefDataStore.data.map { prefs ->
            prefs[PrefKeys.KEY_MAIN_ACTIVITY_IS_IN_FRONT] ?: false
        }.first()
    }

    override suspend fun saveChosenIdeologyData(
        x: Float,
        y: Float,
        horStartScore: Int,
        verStartScore: Int,
        ideology: String,
        quizId: Int,
        startedAt: String,
        horEndScore: Int,
        verEndScore: Int
    ) {
        try {
            val chosenIdeologyData = ChosenIdeologyData(
                x,
                y,
                horStartScore,
                verStartScore,
                ideology,
                quizId,
                startedAt,
                horEndScore,
                verEndScore
            )

            val jsonString = Gson().toJson(chosenIdeologyData)

            prefDataStore.edit { prefs ->
                prefs[PrefKeys.KEY_CHOSEN_IDEOLOGY_DATA] = jsonString
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveChosenView: $e")
        }
    }

    override suspend fun loadChosenIdeologyData(): ChosenIdeologyData? {
        try {
            val jsonString = prefDataStore.data.map { prefs ->
                prefs[PrefKeys.KEY_CHOSEN_IDEOLOGY_DATA] ?: ""
            }.first()
            return Gson().fromJson(jsonString, ChosenIdeologyData::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "loadChosenView: $e")
        }
        return null
    }

    override fun getViewPagerScreenList(): MutableList<ScreenItem> {
        return mutableListOf(
            ScreenItem(context.getString(R.string.intro_title_1), R.drawable.gif_intro_1),
            ScreenItem(context.getString(R.string.intro_title_2), R.drawable.gif_intro_2),
            ScreenItem(context.getString(R.string.intro_title_3), R.drawable.gif_intro_3)
        )
    }
}