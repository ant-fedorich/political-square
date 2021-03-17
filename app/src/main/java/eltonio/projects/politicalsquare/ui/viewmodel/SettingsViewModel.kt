package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.data.MainAppRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    repository: MainAppRepository
) : ViewModel() {
    private val localRepo = repository.Local()
    private val interfaceRepo = repository.UI()


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

    fun showEndQuizDialogLambda(context: Context, onOkBlock: () -> Unit) {
        interfaceRepo.showEndQuizDialogLambda(context, onOkBlock)
    }
}