package eltonio.projects.politicalsquare.data

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import eltonio.projects.politicalsquare.App.Companion.analytics
import eltonio.projects.politicalsquare.App.Companion.crashlytics
import eltonio.projects.politicalsquare.util.*

class FirebaseRepository {

    fun setUserId(userId: String) = analytics.setUserId(userId)
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