package eltonio.projects.politicalcompassquiz.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalcompassquiz.repository.LocalRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepo: LocalRepository
) : ViewModel() {
    private var _savedLang = MutableLiveData<String>()
    val savedLang: LiveData<String> = _savedLang

    private var _quizOption = MutableLiveData<Int>()
    val quizOption: LiveData<Int> = _quizOption

    private var _quizIsActiveState = MutableLiveData<Boolean>()
    val quizIsActiveState: LiveData<Boolean> = _quizIsActiveState

    fun loadSavedLang() = viewModelScope.launch{
        _savedLang.value = localRepo.getSavedLang()
    }

    fun setupAndSaveLang(context: Context, lang: String) = viewModelScope.launch {
        localRepo.setupAndSaveLang(context, lang)
    }

    fun loadQuizOption() = viewModelScope.launch {
        localRepo.getSavedLang()
        _quizOption.value = localRepo.loadQuizOption()
    }

    fun saveQuizOption(quizOptionId: Int) = viewModelScope.launch {
        localRepo.saveQuizOption(quizOptionId)
    }

    fun loadQuizIsActiveState() = viewModelScope.launch {
        _quizIsActiveState.value = localRepo.getQuizIsActive()
    }
}