package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.data.MainAppRepository
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: MainAppRepository,
    val appUtil: AppUtil
) : ViewModel(), LifecycleObserver {
    private val localRepo = repository.Local()
    private val cloudRepo = repository.Cloud()

    var splashAppearedEvent = MutableLiveData<Boolean>()
    var spinnerSelection = MutableLiveData<Int>()

    private lateinit var usersRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var userId: String
    private lateinit var lastSessionStarted: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            cloudRepo.setUserLangProperty(localRepo.getLang())
        }

        if (localRepo.getSplashAppeared() == false) splashAppearedEvent.value = false

        spinnerSelection.value = if (localRepo.loadQuizOption() == QuizOptions.WORLD.id) 0 else 1

        initUser()
    }

    private fun initUser() {
        usersRef = cloudRepo.usersRef
        currentUser = cloudRepo.firebaseUser
        lastSessionStarted = appUtil.getDateTime()

        viewModelScope.launch {
            if (currentUser == null) {
                withContext(Dispatchers.IO) {
                    cloudRepo.createAndSignInAnonymously()
                }
                localRepo.setSessionStarted()
                userId = currentUser?.uid ?: "none"
            } else {
                userId = currentUser?.uid ?: "none"
                if (localRepo.getSessionStarted() == false) {
                    withContext(Dispatchers.IO) {
                        cloudRepo.updateUser(userId, lastSessionStarted)
                    }
                    localRepo.setSessionStarted()
                }
                withContext(Dispatchers.IO) {
                    cloudRepo.setAnalyticsUserId(userId)
                    cloudRepo.logAnonymLoginEvent(lastSessionStarted)
                }
            }
        }
    }

    fun clickSpinnerItem(id: Int) {
        viewModelScope.launch {
            when (id) {
                QuizOptions.WORLD.id -> {
                    localRepo.saveQuizOption(QuizOptions.WORLD.id)
                    withContext(Dispatchers.IO) {
                        cloudRepo.logChangeQuizOptionEvent(QuizOptions.WORLD.id) }
                }
                QuizOptions.UKRAINE.id -> {
                    withContext(Dispatchers.IO) {
                        localRepo.saveQuizOption(QuizOptions.UKRAINE.id) }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        localRepo.setMainActivityIsInFront(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        localRepo.setMainActivityIsInFront(false)
    }

}