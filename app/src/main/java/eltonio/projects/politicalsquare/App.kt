package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.data.MainAppRepository
import eltonio.projects.politicalsquare.models.QuestionWithAnswers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    @ApplicationContext lateinit var _appContext: Context

    @Inject lateinit var repository: MainAppRepository
    private lateinit var localRepo: MainAppRepository.Local
    private lateinit var cloudRepo: MainAppRepository.Cloud

    companion object {
        lateinit var globalContext: Context
            private set

        // TODO: Crashlytics disabled
//        lateinit var crashlytics: FirebaseCrashlytics
//            private set
        lateinit var analytics: FirebaseAnalytics
            private set

        var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = _appContext
        localRepo = repository.Local()
        cloudRepo = repository.Cloud()

//        crashlytics = FirebaseCrashlytics.getInstance()
        analytics = Firebase.analytics

        CoroutineScope(Dispatchers.IO).launch {
            cloudRepo.logSessionStartEvent()
        }
        localRepo.clearPref()
        localRepo.setSplashIsNotAppeared()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        var loadedLang = repository.Local().getLang()
        localRepo.setLang(this, loadedLang)
    }




}