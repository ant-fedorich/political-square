package eltonio.projects.politicalsquare.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.models.QuizResult

class FakeAppRepository : AppRepository {

    private val quizResultList = mutableListOf<QuizResult>() // Imitation DB
    private val quizResultListLiveData = MutableLiveData<List<QuizResult>>(quizResultList)
    private var langInSettings = "en"

    private fun refreshLiveDataInDB() {
        quizResultListLiveData.postValue(quizResultList)
    }

    inner class Local : AppRepository.Local {
        override fun setLang(context: Context, lang: String) {
            langInSettings = lang
        }

        override fun getLang(): String {
            return langInSettings
        }

    }

    inner class DB : AppRepository.DB {
        override suspend fun addQuizResult(quizResult: QuizResult) {
            quizResultList.add(quizResult)
            refreshLiveDataInDB()
        }

        override suspend fun deleteQuizResult(quizResult: QuizResult) {
            quizResultList.remove(quizResult)
            refreshLiveDataInDB()
        }

        override fun getQuizResults(): LiveData<List<QuizResult>> {
            return quizResultListLiveData
        }

    }

    inner class Cloud : AppRepository.Cloud
    inner class UI : AppRepository.UI
}