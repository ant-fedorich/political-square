package eltonio.projects.politicalcompassquiz.repository

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import eltonio.projects.politicalcompassquiz.model.*
import eltonio.projects.politicalcompassquiz.repository.entity.QuestionWithAnswers
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import kotlinx.coroutines.flow.Flow


interface LocalRepository {
    suspend fun setupAndSaveLang(context: Context, lang: String)
    suspend fun getSavedLang(): String
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
    suspend fun clearPref()
    suspend fun loadChosenIdeologyData(): ChosenIdeologyData?
    suspend fun saveChosenIdeologyData(
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
    fun getQuizResults(): Flow<List<QuizResult>>
    suspend fun getQuestionsWithAnswers(quizId: Int): List<QuestionWithAnswers>
}

interface CloudRepository {
    val firebaseUser: FirebaseUser?
    val usersRef: DatabaseReference

    suspend fun setUserLangPropertyEvent(lang: String)
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
