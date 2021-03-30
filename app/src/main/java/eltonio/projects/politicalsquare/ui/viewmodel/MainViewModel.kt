package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.repository.CloudRepository
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.util.AppUtil.getDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepo: LocalRepository,
    private val cloudRepo: CloudRepository
) : ViewModel(), LifecycleObserver {
    private lateinit var usersRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var userId: String
    private lateinit var lastSessionStarted: String

    private var _splashAppearedEvent = MutableLiveData<Boolean>()
    val splashAppearedEvent: LiveData<Boolean> = _splashAppearedEvent
    private var _spinnerSelection = MutableLiveData<Int>()
    val spinnerSelection: LiveData<Int> = _spinnerSelection

    init {
        runBlocking {
            cloudRepo.setUserLangProperty(localRepo.getLang())
            if (localRepo.getSplashAppeared() == false) _splashAppearedEvent.value = false
            _spinnerSelection.value = if (localRepo.loadQuizOption() == QuizOptions.WORLD.id) 0 else 1
            val test = localRepo.getSplashAppeared()
            test
        }
        initUser()
    }

    private fun initUser() {
        usersRef = cloudRepo.usersRef
        currentUser = cloudRepo.firebaseUser
        lastSessionStarted = getDateTime()

        runBlocking {
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

    fun clickSpinnerItem(id: Int) = runBlocking {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = runBlocking {
        localRepo.setMainActivityIsInFront(true)
        _spinnerSelection.value = if (localRepo.loadQuizOption() == QuizOptions.WORLD.id) 0 else 1
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() = runBlocking {
        localRepo.setMainActivityIsInFront(false)
    }

}