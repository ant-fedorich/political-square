package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.util.getDateTime
import eltonio.projects.politicalsquare.util.mainActivityIsInFront


class MainViewModel : ViewModel(), LifecycleObserver {

    private val localRepo = AppRepository.Local()
    private val cloudRepo = AppRepository.Cloud()

    private lateinit var usersRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var userId: String
    private lateinit var lastSessionStarted: String

    var splashAppearedEvent = MutableLiveData<Boolean>()
    var spinnerSelection = MutableLiveData<Int>()

    init {
        cloudRepo.setUserLangProperty(localRepo.getLang())

        if (localRepo.getSplashAppeared() == false) splashAppearedEvent.value = false

        spinnerSelection.value = if (localRepo.loadQuizOption() == QuizOptions.WORLD.id) 0 else 1

        initUser()
    }

    private fun initUser() {
        usersRef = cloudRepo.usersRef
        currentUser = cloudRepo.firebaseUser
        lastSessionStarted = getDateTime()

        if (currentUser == null) {
            cloudRepo.createAndSignInAnonymously()
            localRepo.setSessionStarted()
            userId = currentUser?.uid ?: "none"
        } else {
            userId = currentUser?.uid ?: "none"
            if (localRepo.getSessionStarted() == false) {
                cloudRepo.updateUser(userId, lastSessionStarted)
                localRepo.setSessionStarted()
            }
            cloudRepo.setAnalyticsUserId(userId)
            cloudRepo.logAnonymLoginEvent(lastSessionStarted)
        }
    }

    fun clickSpinnerItem(id: Int) {
        when (id) {
            QuizOptions.WORLD.id -> {
                localRepo.saveQuizOption(QuizOptions.WORLD.id)
                cloudRepo.logChangeQuizOptionEvent(QuizOptions.WORLD.id)
            }
            QuizOptions.UKRAINE.id -> {
                localRepo.saveQuizOption(QuizOptions.UKRAINE.id)
                cloudRepo.logChangeQuizOptionEvent(QuizOptions.UKRAINE.id)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() { mainActivityIsInFront = true }// TODO: Refactor - to Repo

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() { mainActivityIsInFront = false }// TODO: Refactor - to Repo

}