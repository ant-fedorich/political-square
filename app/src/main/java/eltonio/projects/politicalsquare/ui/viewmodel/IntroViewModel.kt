package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.model.ScreenItem
import eltonio.projects.politicalsquare.repository.LocalRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val localRepo: LocalRepository
) : ViewModel() {
    private var _splashAnimationTime: MutableLiveData<Long> = MutableLiveData()
    val splashAnimationTime: LiveData<Long> = _splashAnimationTime

    private var _introOpenEvent: MutableLiveData<Boolean> = MutableLiveData()
    val introOpenedEvent: LiveData<Boolean> = _introOpenEvent

    private var _lang: MutableLiveData<String> = MutableLiveData()
    val lang: LiveData<String> = _lang

    private var _screenList: MutableLiveData<MutableList<ScreenItem>> = MutableLiveData()
    val screenList: LiveData<MutableList<ScreenItem>> = _screenList


    fun setSplashAnimationTime(time: Long) = viewModelScope.launch {
        localRepo.setSplashAnimationTime(time)
    }

    fun checkIntroOpened() = viewModelScope.launch {
        if (localRepo.getIntroOpened()) {
            _splashAnimationTime.value = 600L
            _introOpenEvent.value = true
        } else {
            // Set long animation after Intro
            _splashAnimationTime.value = 1200L
            _introOpenEvent.value = false
            localRepo.setIntroOpened()
        }
    }

    fun loadLang() = viewModelScope.launch {
        _lang.value = localRepo.getSavedLang()
    }

    fun setupAndSaveLang(context: Context, lang: String) = viewModelScope.launch {
        localRepo.setupAndSaveLang(context, lang)
    }

    fun loadScreenList() {
        _screenList.value = localRepo.getViewPagerScreenList()
    }
}