package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.di.AppComponent
import eltonio.projects.politicalsquare.di.DaggerAppComponent
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import eltonio.projects.politicalsquare.models.QuizResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : DaggerApplication() {

    companion object {
        lateinit var appContext: Context
            private set

        // TODO: Crashlytics disabled
//        lateinit var crashlytics: FirebaseCrashlytics
//            private set
        lateinit var analytics: FirebaseAnalytics
            private set

        var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerAppComponent.builder().application(this).build()
    }
    // components = services, Activities = clients

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

//        crashlytics = FirebaseCrashlytics.getInstance()
        analytics = Firebase.analytics

        CoroutineScope(Dispatchers.IO).launch {
            AppRepository.Cloud().logSessionStartEvent()
        }
        AppRepository.Local().clearPref()
        AppRepository.Local().setSplashIsNotAppeared()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        var loadedLang = AppRepository.Local().getLang()
        AppRepository.Local().setLang(this, loadedLang)
    }




}