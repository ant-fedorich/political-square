package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.AdapterView
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.adapter.QuizOptionAdapter
import eltonio.projects.politicalsquare.data.FirebaseRepository
import eltonio.projects.politicalsquare.data.SharedPrefRepository
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    // TEMP
    private val prefRepo = SharedPrefRepository()
    private val firebaseRepo = FirebaseRepository()

    val mainActivity = this

    private lateinit var usersRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var userId: String
    private lateinit var lastSessionStarted: String

    private var userExists = true
    private var userCreationDate = ""


    companion object {
        lateinit var spinnerView: Spinner // To use it in Settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*        // FOR TEST - Load language (without intro)
        var loadedLang = LocaleHelper.loadLocate(this)
        LocaleHelper.setLocate(this, loadedLang)
        */

        // TODO: mvvm, to vm
        var loadedLang = prefRepo.loadLang()
        firebaseRepo.setUserLangProperty(loadedLang)

        if (prefRepo.getSplashAppeared() == false) {
            val splashActivityIntent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(splashActivityIntent)
        }

        Thread.sleep(300) // Needs to load Splash Activity

        refreshAllСatalogs(this)
        this.title = getString(R.string.main_title_actionbar)

        setContentView(R.layout.activity_main)

        startContainerFadeAnimation()
        // MVVM End

        //== Set Quiz Options Spinner ==
        // TODO: mvvm, to vm
        val spinnerAdapter = QuizOptionAdapter(this, QuizOptions.values())
        spinner_quiz_options.adapter = spinnerAdapter
        spinnerView = spinner_quiz_options

        when(prefRepo.loadQuizOption()) {
            QuizOptions.WORLD.id -> spinner_quiz_options.setSelection(0)
            QuizOptions.UKRAINE.id -> spinner_quiz_options.setSelection(1)
        }

        usersRef = firebaseRepo.usersRef
        currentUser = firebaseRepo.firebaseUser // TODO: Check nullable
        lastSessionStarted = getDateTime()

        if (currentUser == null) {
            firebaseRepo.createAndSignInAnonymously()
            prefRepo.putSessionStarted()
            userId = currentUser?.uid ?: "none"
        } else {
            userId = currentUser?.uid ?: "none"
            if (!prefRepo.getSessionStarted()) {
                firebaseRepo.updateUser(userId, lastSessionStarted)
                prefRepo.putSessionStarted()
            }
            firebaseRepo.setAnalyticsUserId(userId)
            firebaseRepo.logAnonymLoginEvent(lastSessionStarted)
        }

        button_start.setOnClickListener{ onStartClicked() }

        spinner_quiz_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // TODO: mvvm, to vm
                val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                when (clickedItem.id) {
                    QuizOptions.WORLD.id -> {
                        prefRepo.saveQuizOption(QuizOptions.WORLD.id)
                        firebaseRepo.logChangeQuizOptionEvent(QuizOptions.WORLD.id)
                    }
                    QuizOptions.UKRAINE.id -> {
                        prefRepo.saveQuizOption(QuizOptions.UKRAINE.id)
                        firebaseRepo.logChangeQuizOptionEvent(QuizOptions.UKRAINE.id)
                    }
                }
                // end mvvm
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivityIsInFront = true  // TODO: mvvm, to vm
    }

    override fun onPause() {
        super.onPause()
        mainActivityIsInFront = false // TODO: mvvm, to vm
    }


    /** INTERFACE METHODS */
    private fun onStartClicked() {
        startActivity(Intent(this, ChooseViewActivity::class.java))
        slideLeft(this)
    }

    /** CUSTOM METHODS */
    private fun startContainerFadeAnimation() { // Is needed to load Splash Activity
        activity_container.alpha = 0f
        activity_container.animate().apply {
            duration = 500
            alpha(1f)
            interpolator = AccelerateInterpolator(3f)
        }.start()
    }
}