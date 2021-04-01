package eltonio.projects.politicalsquare

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
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
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        CoroutineScope(Dispatchers.IO).launch {
            cloudRepo.logSessionStartEvent()
            localRepo.setSplashIsNOTAppeared()
        }
    }



}