package eltonio.projects.politicalsquare.other

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.data.*

class App : Application() {

    companion object {
        lateinit var appContext: Context
            private set
        lateinit var crashlytics: FirebaseCrashlytics
            private set
        lateinit var analytics: FirebaseAnalytics
            private set
        var appQuestions: List<Question> = emptyList()
        var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()
        var appQuestionAnswerDetail: List<QuestionAnswerDetail> = emptyList()
        var appQuizResults: List<QuizResult> = emptyList()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        crashlytics = FirebaseCrashlytics.getInstance()
        analytics = Firebase.analytics

        analytics.logEvent(EVENT_QUIZ_SESSION_START, null)

        // Reset splash appearance on starting the app
        val prefs = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE).edit()
        prefs.putBoolean(PREF_SPLASH_APPEARED, false)
        prefs.apply()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        var loadedLang = LocaleHelper.loadLang(this)
        LocaleHelper.setLang(this, loadedLang)


    }

}