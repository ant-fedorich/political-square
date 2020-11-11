package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.data.FirebaseRepository
import eltonio.projects.politicalsquare.data.SharedPrefRepository
import eltonio.projects.politicalsquare.models.Question
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.util.EVENT_QUIZ_SESSION_START
import eltonio.projects.politicalsquare.util.LocaleUtil
import eltonio.projects.politicalsquare.util.PREF_SETTINGS
import eltonio.projects.politicalsquare.util.PREF_SPLASH_APPEARED

class App : Application() {

    companion object {
        lateinit var appContext: Context
            private set
        lateinit var crashlytics: FirebaseCrashlytics
            private set
        lateinit var analytics: FirebaseAnalytics
            private set

        // TODO: Get rid of storing data in App
        var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()
        var appQuizResults: List<QuizResult> = emptyList()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        crashlytics = FirebaseCrashlytics.getInstance()
        analytics = Firebase.analytics

        // done: MVVM to Analytic Repo
        FirebaseRepository().logSessionStartEvent()
        // Clear SharedPreference QuizData
        SharedPrefRepository().clearPref()

        // Reset splash appearance on starting the app
        // done: MVVM to Repository
        SharedPrefRepository().setSplashIsNotAppeared()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        // TODO: MVVM to Settings Repo
        var loadedLang = LocaleUtil.loadLang(this)
        LocaleUtil.setLang(this, loadedLang)
    }

}