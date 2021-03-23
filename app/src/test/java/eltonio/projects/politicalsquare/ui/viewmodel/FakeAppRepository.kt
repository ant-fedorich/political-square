package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.AppRepository
import eltonio.projects.politicalsquare.repository.QuestionDao
import eltonio.projects.politicalsquare.repository.QuizResultDao
import eltonio.projects.politicalsquare.util.AppUtil
import javax.inject.Inject

class FakeAppRepository (
    private val questionDao: QuestionDao,
    private val quizResultDao: QuizResultDao,
    private val appUtil: AppUtil
) : AppRepository {
    val context = appUtil.context

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