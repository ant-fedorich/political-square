package eltonio.projects.politicalsquare.data

import android.content.Context
import androidx.lifecycle.LiveData
import eltonio.projects.politicalsquare.models.*


interface AppRepository {
    interface Local {
        fun setLang(context: Context, lang: String)
        fun getLang(): String
    }

    interface DB
    {
        suspend fun addQuizResult(quizResult: QuizResult)
        suspend fun deleteQuizResult(quizResult: QuizResult)
        fun getQuizResults(): LiveData<List<QuizResult>>
    }

    interface Cloud

    interface UI
}