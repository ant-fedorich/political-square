package eltonio.projects.politicalsquare.other

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.core.os.ConfigurationCompat
import java.util.*

object LocaleHelper {

    fun setLang(context: Context, lang: String) {
        val locale = Locale(lang)
        Log.d(TAG, "Lang: $lang, Default Lang: ${Locale.getDefault().language}")

        //Locale.setDefault(locale) // Default location is reset to the system one every launching the app

        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        var sharedPref = context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE).edit()
        sharedPref.putString(PREF_LANG, lang).apply()

        defaultLang = lang
    }

    fun loadLang(context: Context): String {
        val defLang = Locale.getDefault().language
        val sharedPref = context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)
        val language = sharedPref.getString(PREF_LANG, defLang)
        Log.d(TAG, "Saved lang: $language")

        return language ?: defLang
    }

    fun loadCountry(context: Context): String {
        return ConfigurationCompat.getLocales(context.resources.configuration)[0].toString()
    }

}