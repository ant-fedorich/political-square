package eltonio.projects.politicalsquare

import android.content.Context
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.model.ScreenItem
import eltonio.projects.politicalsquare.repository.LocalRepository

class FakeLocalRepository (
    private val context: Context
) : LocalRepository {

    private val quizResultList = mutableListOf<QuizResult>() // Imitation DB
    private val quizResultListLiveData = MutableLiveData<List<QuizResult>>(quizResultList)
    private var langInSettings = "en"

    private fun refreshLiveDataInDB() {
        quizResultListLiveData.postValue(quizResultList)
    }

    override fun setLang(context: Context, lang: String) {
        langInSettings = lang
    }

    override fun getLang(): String {
        return langInSettings
    }



    override fun loadQuizOption(): Int {
        TODO("Not yet implemented")
    }

    override fun setQuizIsActive(active: Boolean) {
        TODO("Not yet implemented")
    }

    override fun saveChosenView(
        x: Float,
        y: Float,
        horStartScore: Int,
        verStartScore: Int,
        ideologyId: String,
        quizId: Int,
        startedAt: String
    ) {
        TODO("Not yet implemented")
    }

    override fun getIntroOpened(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setIntroOpened() {
        TODO("Not yet implemented")
    }

    override fun getViewPagerScreenList(): MutableList<ScreenItem>? {
        TODO("Not yet implemented")
    }

    override fun setSplashAnimationTime(time: Long) {
        TODO("Not yet implemented")
    }

    override fun getSplashAppeared(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setSessionStarted() {
        TODO("Not yet implemented")
    }

    override fun getSessionStarted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveQuizOption(id: Int) {
        TODO("Not yet implemented")
    }

    override fun setMainActivityIsInFront(b: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setHorScore(toInt: Int) {
        TODO("Not yet implemented")
    }

    override fun setVerScore(toInt: Int) {
        TODO("Not yet implemented")
    }

    override fun getChosenViewX(): Float {
        TODO("Not yet implemented")
    }

    override fun getChosenViewY(): Float {
        TODO("Not yet implemented")
    }

    override fun getHorStartScore(): Int {
        TODO("Not yet implemented")
    }

    override fun getVerStartScore(): Int {
        TODO("Not yet implemented")
    }

    override fun getChosenIdeology(): String {
        TODO("Not yet implemented")
    }

    override fun getStartedAt(): String {
        TODO("Not yet implemented")
    }

    override fun getHorScore(): Int {
        TODO("Not yet implemented")
    }

    override fun getVerScore(): Int {
        TODO("Not yet implemented")
    }

    override fun getQuizIsActive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSplashAnimationTime(): Long {
        TODO("Not yet implemented")
    }

    override fun getMainActivityIsInFront(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setSplashIsAppeared() {
        TODO("Not yet implemented")
    }
}