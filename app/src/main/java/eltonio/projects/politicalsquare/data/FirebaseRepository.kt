package eltonio.projects.politicalsquare.data

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.App.Companion.analytics
import eltonio.projects.politicalsquare.App.Companion.crashlytics
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.util.*

class FirebaseRepository {

    var usersRef = Firebase.database.getReference("Users")
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null  // LiveData
    private var userLoggedIn = false // LiveData
    private var userExists = true // FOR TEST
    private val prefRepo = SharedPrefRepository()

    init {
        firebaseAuth = Firebase.auth

        if (Firebase.auth.currentUser != null) {
            firebaseUser = Firebase.auth.currentUser
            userLoggedIn = true
        }
    }

    fun createAndSignInAnonymously() {
        firebaseAuth?.signInAnonymously()
            ?.addOnSuccessListener { result ->
                Log.i(TAG, "signInAnonymously: SUCCESS")
                firebaseUser = result.user
                userLoggedIn = true // FOR TEST

                val userId = result.user?.uid
                val lastSessionStarted = getDateTime()

                if (userId != null) {
                    setUserCreationDate(userId, lastSessionStarted) //TODO: Does not work
                    updateUser(userId, lastSessionStarted)
                }
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "signInAnonymously: FAILURE: $e")
            }
    }

    fun getUserUidRef(uid: String): DatabaseReference? {
        return usersRef?.child(uid)
    }

    fun addQuizResult(userId: String, quizResult: QuizResult) {
        val quizResultRef = Firebase.database.getReference("QuizResults").push()
        quizResultRef.setValue(quizResult)
        quizResultRef.child("userId").setValue(userId)
    }

    fun updateUser(userId: String, lastLogInDate: String) {
        usersRef?.child(userId)?.apply {
            child("name").setValue("Noname")
            child("email").setValue("Noname@mail.com")
            child("lastSessionStarted").setValue(lastLogInDate)
        }
    }

    fun setUserCreationDate(userId: String, userCreationDate: String) =
        usersRef?.child(userId)?.child("creationDate")?.setValue(userCreationDate)


    fun setAnalyticsUserId(userId: String) = analytics.setUserId(userId)

    fun setUserLangProperty(loadedLang: String) = analytics.setUserProperty(EVENT_PREFERRED_LANG, loadedLang)

    fun logSessionStartEvent() = analytics.logEvent(EVENT_QUIZ_SESSION_START, null)

    fun logAnonymLoginEvent(lastLogInDate: String) = analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
        param(FirebaseAnalytics.Param.METHOD, "anonymously")
        param(PARAM_LOGIN_DATE, lastLogInDate)
    }

    fun logQuizBeginEvent() = analytics.logEvent(EVENT_QUIZ_BEGIN) {
        param(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
    }

    fun logQuizCompleteEvent() = analytics.logEvent(EVENT_QUIZ_COMPLETE) {
        param(FirebaseAnalytics.Param.END_DATE, System.currentTimeMillis())
    }

    fun logDetailedInfoEvent() = analytics.logEvent(EVENT_DETAILED_INFO, null)

    fun logChangeQuizOptionEvent(quizOption: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.CONTENT, quizOption)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "change_quiz_option")
        }
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun logCrash(message: String) = crashlytics.log(message)
}