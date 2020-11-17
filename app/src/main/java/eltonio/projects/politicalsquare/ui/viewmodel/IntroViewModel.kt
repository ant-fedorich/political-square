package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.ScreenItem

class IntroViewModel : ViewModel() {
    private val localRepo = AppRepository.Local()

    private var splashAnimationTime: MutableLiveData<Long> = MutableLiveData()
    private var introOpenEvent: MutableLiveData<Boolean> = MutableLiveData()
    private var lang: MutableLiveData<String> = MutableLiveData()
    private var screenList: MutableLiveData<MutableList<ScreenItem>> = MutableLiveData()
    private var screenLisUpdatedState: MutableLiveData<Boolean> = MutableLiveData()

    fun checkIntroOpened() {
        if (localRepo.getIntroOpened()) {
            splashAnimationTime.value = 600L
            introOpenEvent.value = true
        } else {
            // Set long animation after Intro
            splashAnimationTime.value = 1200L
            introOpenEvent.value = false
            localRepo.setIntroOpened()
        }
    }

    fun loadLang(): LiveData<String> {
        lang.value = localRepo.getLang()
        return lang
    }

    fun getScreenList(): LiveData<MutableList<ScreenItem>> {
        screenList.value = localRepo.getViewPagerScreenList()
        screenLisUpdatedState.value = true
        return screenList
    }

    fun getIntroOpened(): LiveData<Boolean> {
        return introOpenEvent
    }

    fun getSplashAnimationTime(): LiveData<Long> {
        return splashAnimationTime
    }
}