package eltonio.projects.politicalcompassquiz.other

import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

class App : Application() {

    companion object {
        lateinit var appContext: Context
            private set
        lateinit var crashlytics: FirebaseCrashlytics
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        crashlytics = FirebaseCrashlytics.getInstance()

        // Reset splash appearance on starting the app
        val prefs = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE).edit()
        prefs.putBoolean(PREF_SPLASH_APPEARED, false)
        prefs.apply()

        // We have to set a lang before loading UI, cause it will take a lang by system default
        var loadedLang = LocaleHelper.loadLocate(this)
        LocaleHelper.setLocate(this, loadedLang)

    }
}