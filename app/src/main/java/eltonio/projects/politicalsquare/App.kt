package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.model.QuestionWithAnswers
import eltonio.projects.politicalsquare.repository.ProdCloudRepository
import eltonio.projects.politicalsquare.repository.ProdLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var localRepo: ProdLocalRepository
    @Inject
    lateinit var cloudRepo: ProdCloudRepository
    // TODO: Get rid runBlocking
    // FIXME: fix backstack everywhere

    override fun onCreate() {
        super.onCreate()
        System.setProperty("kotlinx.coroutines.debug", "on")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        CoroutineScope(Dispatchers.IO).launch {
            cloudRepo.logSessionStartEvent()
            localRepo.clearPref()
            localRepo.setSplashIsNOTAppeared()
        }
    }



}