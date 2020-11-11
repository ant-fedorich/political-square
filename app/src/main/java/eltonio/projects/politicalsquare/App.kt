package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.data.FirebaseRepository
import eltonio.projects.politicalsquare.data.SharedPrefRepository
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizResult

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

        FirebaseRepository().logSessionStartEvent()
        SharedPrefRepository().clearPref()
        SharedPrefRepository().setSplashIsNotAppeared()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        var loadedLang = SharedPrefRepository().loadLang()
        SharedPrefRepository().setLang(this, loadedLang)
    }




}