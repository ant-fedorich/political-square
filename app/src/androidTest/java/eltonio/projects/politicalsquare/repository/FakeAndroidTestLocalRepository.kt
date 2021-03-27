package eltonio.projects.politicalsquare.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.model.ScreenItem
import eltonio.projects.politicalsquare.repository.LocalRepository

class FakeAndroidTestLocalRepository (
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
        return 0
    }

    override fun setQuizIsActive(active: Boolean) {}

    override fun saveChosenView(
        x: Float,
        y: Float,
        horStartScore: Int,
        verStartScore: Int,
        ideologyId: String,
        quizId: Int,
        startedAt: String
    ) {

    }

    override fun getIntroOpened(): Boolean {
        return false
    }

    override fun setIntroOpened() {
    }

    override fun getViewPagerScreenList(): MutableList<ScreenItem>? {
        return null
    }

    override fun setSplashAnimationTime(time: Long) {

    }

    override fun getSplashAppeared(): Boolean {
        return false
    }

    override fun setSessionStarted() {

    }

    override fun getSessionStarted(): Boolean {
        return false
    }

    override fun saveQuizOption(id: Int) {

    }

    override fun setMainActivityIsInFront(b: Boolean) {

    }

    override fun setHorScore(toInt: Int) {

    }

    override fun setVerScore(toInt: Int) {

    }

    override fun getChosenViewX(): Float {
        return 0f
    }

    override fun getChosenViewY(): Float {
        return 0f
    }

    override fun getHorStartScore(): Int {
        return -1
    }

    override fun getVerStartScore(): Int {
        return -1
    }

    override fun getChosenIdeology(): String {
        return ""
    }

    override fun getStartedAt(): String {
        return ""
    }

    override fun getHorScore(): Int {
        return -1
    }

    override fun getVerScore(): Int {
        return -1
    }

    override fun getQuizIsActive(): Boolean {
        return false
    }

    override fun getSplashAnimationTime(): Long {
        return -1
    }

    override fun getMainActivityIsInFront(): Boolean {
        return false
    }

    override fun setSplashIsAppeared() {

    }

    override fun clearPref() {

    }
}