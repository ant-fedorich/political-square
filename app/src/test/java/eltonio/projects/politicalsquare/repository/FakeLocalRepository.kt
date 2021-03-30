package eltonio.projects.politicalsquare.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.model.ChosenIdeologyData
import eltonio.projects.politicalsquare.model.ScreenItem

class FakeLocalRepository (
    private val context: Context
) : LocalRepository {

    private val quizResultList = mutableListOf<QuizResult>() // Imitation DB
    private val quizResultListLiveData = MutableLiveData<List<QuizResult>>(quizResultList)
    private var langInSettings = "en"

    private fun refreshLiveDataInDB() {
        quizResultListLiveData.postValue(quizResultList)
    }

    override suspend fun setLang(context: Context, lang: String) {
        langInSettings = lang
    }

    override suspend fun getLang(): String {
        return langInSettings
    }

    override suspend fun loadQuizOption(): Int {
        return -1
    }

    override suspend fun setQuizIsActive(active: Boolean) {}


    override suspend fun getIntroOpened(): Boolean {
        return false
    }

    override suspend fun setIntroOpened() {}

    override fun getViewPagerScreenList(): MutableList<ScreenItem>? {
        return null
    }

    override suspend fun setSplashAnimationTime(time: Long) {}

    override suspend fun getSplashAppeared(): Boolean {
        return false
    }

    override suspend fun setSessionStarted() {
    }

    override suspend fun getSessionStarted(): Boolean {
        return false
    }

    override suspend fun saveQuizOption(id: Int) {}

    override suspend fun setMainActivityIsInFront(b: Boolean) {}

    override suspend fun getQuizIsActive(): Boolean {
        return false
    }

    override suspend fun getSplashAnimationTime(): Long {
        return -1
    }

    override suspend fun getMainActivityIsInFront(): Boolean {
        return false
    }

    override suspend fun setSplashIsAppeared() {}

    override fun clearPref() {}
    override suspend fun loadChosenView(): ChosenIdeologyData {
        return ChosenIdeologyData()
    }

    override suspend fun saveChosenIdeology(
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
        TODO("Not yet implemented")
    }
}