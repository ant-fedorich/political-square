package eltonio.projects.politicalsquare

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.model.QuestionWithAnswers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    // TODO: get rid or appQuestionsWithAnswers in APP
    companion object {
        var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()
    }
}