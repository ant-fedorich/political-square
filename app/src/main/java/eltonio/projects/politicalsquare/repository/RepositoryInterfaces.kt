package eltonio.projects.politicalsquare.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import eltonio.projects.politicalsquare.model.*


interface LocalRepository {
    fun setLang(context: Context, lang: String)
    fun getLang(): String
    fun loadQuizOption(): Int
    fun setQuizIsActive(active: Boolean)
    fun saveChosenView(
        x: Float,
        y: Float,
        horStartScore: Int,
        verStartScore: Int,
        ideologyId: String,
        quizId: Int,
        startedAt: String
    )

    fun getIntroOpened(): Boolean
    fun setIntroOpened()
    fun getViewPagerScreenList(): MutableList<ScreenItem>?
    fun setSplashAnimationTime(time: Long)
    fun getSplashAppeared(): Boolean
    fun setSessionStarted()
    fun getSessionStarted(): Boolean
    fun saveQuizOption(id: Int)
    fun setMainActivityIsInFront(b: Boolean)
    fun setHorScore(toInt: Int)
    fun setVerScore(toInt: Int)
    fun getChosenViewX(): Float
    fun getChosenViewY(): Float
    fun getHorStartScore(): Int
    fun getVerStartScore(): Int
    fun getChosenIdeology(): String
    fun getStartedAt(): String
    fun getHorScore(): Int
    fun getVerScore(): Int
    fun getQuizIsActive(): Boolean
    fun getSplashAnimationTime(): Long
    fun getMainActivityIsInFront(): Boolean
    fun setSplashIsAppeared()
}

interface DBRepository
{
    suspend fun addQuizResult(quizResult: QuizResult)
    suspend fun deleteQuizResult(quizResult: QuizResult)
    fun getQuizResults(): LiveData<List<QuizResult>>
    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers>
}

interface CloudRepository {
    val firebaseUser: FirebaseUser?
    val usersRef: DatabaseReference

    suspend fun setUserLangProperty(lang: String)
    suspend fun createAndSignInAnonymously()
    suspend fun updateUser(userId: String, lastSessionStarted: String)
    suspend fun setAnalyticsUserId(userId: String)
    suspend fun logAnonymLoginEvent(lastSessionStarted: String)
    suspend fun logChangeQuizOptionEvent(id: Int)
    suspend fun logQuizBeginEvent()
    suspend fun logQuizCompleteEvent()
    suspend fun addQuizResult(userId: String, quizResult: QuizResult)
    suspend fun logDetailedInfoEvent()

}
