package eltonio.projects.politicalsquare.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.AdapterView
import android.widget.Spinner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.App.Companion.analytics
import eltonio.projects.politicalsquare.App.Companion.crashlytics
import eltonio.projects.politicalsquare.adapter.QuizOptionAdapter
import eltonio.projects.politicalsquare.data.FirebaseRepository
import eltonio.projects.politicalsquare.data.SharedPrefRepository
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {

    // TEMP
    private val prefRepo = SharedPrefRepository()
    private val firebaseRepo = FirebaseRepository()

    val mainActivity = this

    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference
    private var currentUser: FirebaseUser? = null
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
        // MVVM Start
        var loadedLang = LocaleUtil.loadLang(this)
        // done: MVVM to firebase repo
        firebaseRepo.setUserLangProperty(loadedLang)
        Log.i(TAG, "Lang: $loadedLang")

        // done: MVVM to Reposytory
        val splashAppeared = prefRepo.getSplashAppeared()
        Log.d(TAG, "splashAppeared: $splashAppeared")

        if (splashAppeared == false) {
            val splashActivityIntent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(splashActivityIntent)
        }

        Thread.sleep(300) // Needs to load Splash Activity

        // todo: MVVM to Reposytory???
        Ideologies.refreshAll(this)
        QuizOptions.refreshAll(this)
        this.title = getString(R.string.main_title_actionbar)

        setContentView(R.layout.activity_main)

        // Add fade animation for Main Activity
        // is needed to load Splash Activity
        activity_container.alpha = 0f
        activity_container.animate().apply {
            duration = 500
            alpha(1f)
            interpolator = AccelerateInterpolator(3f)
        }.start()
        // MVVM End


        //== Set Quiz Options Spinner ==

        // TODO: mvvm, to vm
        val spinnerAdapter = QuizOptionAdapter(this, QuizOptions.values())
        spinner_quiz_options.adapter = spinnerAdapter
        spinnerView = spinner_quiz_options

        when(QuizOptionUtil.loadQuizOption(this)) {
            QuizOptions.WORLD.id -> spinner_quiz_options.setSelection(0)
            QuizOptions.UKRAINE.id -> spinner_quiz_options.setSelection(1)
        }

        // TODO: MVVM to FireStore Reposytory???
        usersRef = Firebase.database.getReference("Users")

        // TODO: MVVM to Auth Reposytory???
        auth = Firebase.auth
        currentUser = auth.currentUser

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val uid = currentUser?.uid ?: "none"
        val uidRef = usersRef.child(uid)

        uidRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // TODO: mvvm, to vm
                // MVVM Start
                if (!snapshot.exists()) {
                    userExists = false
                    firebaseRepo.logCrash("FirebaseDatabase: OnDataChange: User does not exist")
                    val currentDate = Date()
                    userCreationDate = formatter.format(currentDate)
                }
                // MVVM End
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // TODO: mvvm, to vm?
        usersRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(shapchot: DataSnapshot, p1: String?) {
                //Log.i(TAG, "Users: onChildAdded")
            }
            override fun onCancelled(e: DatabaseError) {
                //Log.e(TAG, "Users: onCancelled: $e")
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //toast("Users: onChildChanged")
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })

        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                Log.i(TAG, "signInAnonymously: SUCCESS")

                val userId = result.user?.uid
                val currentDate = Date()
                val lastLogInDate = formatter.format(currentDate)

                if (userId != null) {
                    if (!userExists) {
                        usersRef.child(userId).child("creationDate").setValue(userCreationDate)
                    }

                    // done: MVVM to Analytic Repo
                    firebaseRepo.setUserId(userId)
                    firebaseRepo.logAnonymLoginEvent(lastLogInDate)
                    prefRepo.putUserId(userId)

                    // TODO: MVVM to Reposytory???
                    usersRef.child(userId).apply {
                        child("name").setValue("Noname")
                        child("email").setValue("Noname@mail.com")
                        child("lastLogInDate").setValue(lastLogInDate)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "signInAnonymously: FAILURE: $e")
            }
        // end mvvm

        // Set onClick listeners
        button_start.setOnClickListener(this)

        spinner_quiz_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // TODO: mvvm, to vm
                val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                when (clickedItem.id) {
                    QuizOptions.WORLD.id -> {
                        QuizOptionUtil.saveQuizOption(QuizOptions.WORLD.id)
                        // done: MVVM to analytic Repo
                        firebaseRepo.logChangeQuizOptionEvent(QuizOptions.WORLD.id)
                    }
                    QuizOptions.UKRAINE.id -> {
                        QuizOptionUtil.saveQuizOption(QuizOptions.UKRAINE.id)
                        // done: MVVM to analytic Repo
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
        // TODO: mvvm, to vm
        mainActivityIsInFront = true
    }

    override fun onPause() {
        super.onPause()
        // TODO: mvvm, to vm
        mainActivityIsInFront = false
    }


    /** INTERFACE METHODS */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_start -> {
                onStartClicked()
            }
        }
    }

    private fun onStartClicked() {
        startActivity(Intent(this, ChooseViewActivity::class.java))
        slideLeft(this)
    }
}