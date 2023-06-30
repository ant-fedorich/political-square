package eltonio.projects.politicalcompassquiz.repository

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.util.*
import eltonio.projects.politicalcompassquiz.util.AppUtil.getDateTime
import kotlinx.coroutines.*
import javax.inject.Inject


class ProdCloudRepository @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private var firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): CloudRepository {
    override var usersRef = firebaseDatabase.getReference("Users")
    override var firebaseUser: FirebaseUser? = null  // LiveData
    private var userLoggedIn = false // LiveData

    init {
        if (firebaseAuth.currentUser != null) {
            firebaseUser = Firebase.auth.currentUser
            userLoggedIn = true
        }
    }

    override suspend fun createAndSignInAnonymously() {
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener { result ->
//                CoroutineScope(Dispatchers.IO).launch {
                    //delay(3000)
                    Log.i(TAG, "signInAnonymously: SUCCESS")
                    firebaseUser = result.user
                    userLoggedIn = true // FOR TEST

                    val userId = result.user?.uid
                    val lastSessionStarted = getDateTime()

                    if (userId != null) {
                        runBlocking {
                            setUserCreationDate(userId, lastSessionStarted)
                            updateUser(userId, lastSessionStarted)
                        }
                    }
                }
//            }
            .addOnFailureListener { e ->
                Log.e(TAG, "signInAnonymously: FAILURE: $e")
            }
    }

    override suspend fun addQuizResult(userId: String, quizResult: QuizResult) {
        val quizResultRef = Firebase.database.getReference("QuizResults").push()
        quizResultRef.setValue(quizResult)
        quizResultRef.child("userId").setValue(userId)
    }

    override suspend fun updateUser(userId: String, lastLogInDate: String) {
        usersRef.child(userId).apply {
            child("getResString").setValue("Noname")
            child("email").setValue("Noname@mail.com")
            child("lastSessionStarted").setValue(lastLogInDate)
        }
    }

    suspend fun setUserCreationDate(userId: String, userCreationDate: String) =
        usersRef.child(userId).child("creationDate").setValue(userCreationDate)


    override suspend fun setAnalyticsUserId(userId: String) = analytics.setUserId(userId)

    override suspend fun setUserLangPropertyEvent(loadedLang: String) = analytics.setUserProperty(
        EVENT_PREFERRED_LANG, loadedLang)

    suspend fun logSessionStartEvent() = analytics.logEvent(EVENT_QUIZ_SESSION_START, null)

    override suspend fun logAnonymLoginEvent(lastLogInDate: String) = analytics.logEvent(
        FirebaseAnalytics.Event.LOGIN) {
        param(FirebaseAnalytics.Param.METHOD, "anonymously")
        param(PARAM_LOGIN_DATE, lastLogInDate)
    }

    override suspend fun logQuizBeginEvent() = analytics.logEvent(EVENT_QUIZ_BEGIN) {
        param(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
    }

    override suspend fun logQuizCompleteEvent() = analytics.logEvent(EVENT_QUIZ_COMPLETE) {
        param(FirebaseAnalytics.Param.END_DATE, System.currentTimeMillis())
    }

    override suspend fun logDetailedInfoEvent() = analytics.logEvent(EVENT_DETAILED_INFO, null)

    override suspend fun logChangeQuizOptionEvent(quizOption: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.CONTENT, quizOption)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "change_quiz_option")
        }
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
    // Crashlytics disabled
    // fun logCrash(message: String) = App.crashlytics.log(message)
}