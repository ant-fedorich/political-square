package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.repository.LocalRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepo: LocalRepository
) : ViewModel() {
    private var lang = MutableLiveData<String>()
    private var quizOption = MutableLiveData<Int>()
    private var quizIsActiveState = MutableLiveData<Boolean>()

    fun getLang(): LiveData<String> {
        lang.value = localRepo.getLang()
        return lang
    }

    fun setLang(context: Context, lang: String) {
        localRepo.setLang(context, lang)
    }

    fun getQuizOption(): LiveData<Int> {
        localRepo.getLang()
        quizOption.value = localRepo.loadQuizOption()
        return quizOption
    }

    fun saveQuizOption(quizOptionId: Int) {
        localRepo.saveQuizOption(quizOptionId)
    }

    fun getQuizIsActiveState(): LiveData<Boolean> {
        quizIsActiveState.value = localRepo.getQuizIsActive()
        return quizIsActiveState
    }
}