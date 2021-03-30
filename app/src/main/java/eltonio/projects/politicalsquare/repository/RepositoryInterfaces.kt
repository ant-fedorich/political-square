package eltonio.projects.politicalsquare.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import eltonio.projects.politicalsquare.model.*


interface LocalRepository {
    suspend fun setLang(context: Context, lang: String)
    suspend fun getLang(): String
    suspend fun loadQuizOption(): Int
    suspend fun setQuizIsActive(active: Boolean)
    suspend fun getIntroOpened(): Boolean
    suspend fun setIntroOpened()
    fun getViewPagerScreenList(): MutableList<ScreenItem>?
    suspend fun setSplashAnimationTime(time: Long)
    suspend fun getSplashAppeared(): Boolean
    suspend fun setSessionStarted()
    suspend fun getSessionStarted(): Boolean
    suspend fun saveQuizOption(id: Int)
    suspend fun setMainActivityIsInFront(b: Boolean)
    suspend fun getQuizIsActive(): Boolean
    suspend fun getSplashAnimationTime(): Long
    suspend fun getMainActivityIsInFront(): Boolean
    suspend fun setSplashIsAppeared()
    suspend fun setSplashIsNOTAppeared()
    fun clearPref()
    suspend fun loadChosenView(): ChosenIdeologyData?
    suspend fun saveChosenIdeology(
        x: Float,
        y: Float,
        horStartScore: Int,
        verStartScore: Int,
        ideology: String,
        quizId: Int,
        startedAt: String,
        horEndScore: Int,
        verEndScore: Int
    )
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
