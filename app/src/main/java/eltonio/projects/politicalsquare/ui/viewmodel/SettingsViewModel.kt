package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eltonio.projects.politicalsquare.data.AppRepository

class SettingsViewModel : ViewModel() {
    private val localRepo = AppRepository.Local()

    private var lang = MutableLiveData<String>()
    private var quizOption = MutableLiveData<Int>()
    private var quizIsActiveState = MutableLiveData<Boolean>()

    fun getLang(): LiveData<String> {
        lang.value = localRepo.getLang()
        return lang
    }

    fun getQuizOption(): LiveData<Int> {
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