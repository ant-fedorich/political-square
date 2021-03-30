package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.repository.LocalRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepo: LocalRepository
) : ViewModel() {
    private var _lang = MutableLiveData<String>()
    val lang: LiveData<String> = _lang

    private var _quizOption = MutableLiveData<Int>()
    val quizOption: LiveData<Int> = _quizOption

    private var _quizIsActiveState = MutableLiveData<Boolean>()
    val quizIsActiveState: LiveData<Boolean> = _quizIsActiveState

    fun loadLang() = runBlocking{
        _lang.value = localRepo.getLang()
    }

    fun setLang(context: Context, lang: String) = runBlocking {
        localRepo.setLang(context, lang)
    }

    fun loadQuizOption() = runBlocking {
        localRepo.getLang()
        _quizOption.value = localRepo.loadQuizOption()
    }

    fun saveQuizOption(quizOptionId: Int) = runBlocking {
        localRepo.saveQuizOption(quizOptionId)
    }

    fun loadQuizIsActiveState() = runBlocking {
        _quizIsActiveState.value = localRepo.getQuizIsActive()
    }
}