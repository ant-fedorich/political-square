package eltonio.projects.politicalcompassquiz.activities

import android.content.Context
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
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.models.*
import eltonio.projects.politicalcompassquiz.other.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {

    val mainActivity = this

    private lateinit var database: FirebaseDatabase
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


        val prefs = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        val splashAppeared = prefs.getBoolean(PREF_SPLASH_APPEARED, false)
        Log.d(TAG, "splashAppeared: $splashAppeared")

        if (splashAppeared == false) {
            val splashActivityIntent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(splashActivityIntent)
        }

        Thread.sleep(300) // Needs to load Splash Activity

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


        //== Set Quiz Options Spinner ==
//        val quizOptionList = mutableListOf(
//            QuizOption(QUIZ_OPTION_WORLD, getString(R.string.settings_title_quiz_option_1), getString(R.string.settings_owner_quiz_option_1), R.drawable.img_world_quiz),
//            QuizOption(QUIZ_OPTION_UKRAINE, getString(R.string.settings_title_quiz_option_2), getString(R.string.settings_owner_quiz_option_2), R.drawable.img_ukraine_quiz)
//        )

//        val quizOptionList = mutableListOf(
//            QuizOptions.World,
//            QuizOptions.Ukraine
//        )

//        QuizContract.AnswersEntry.COLUMN_ANSWER_EN
//        Ideologies.LIBERTARIAN_SOCIALISM.title
//        QuizOptions_2.WORLD.title
//        val quizArray = QuizOptions_2.values()
//        quizArray[0].
        //enumValueOf<QuizOptions_2>
//        QuizOptions.World.title
//        QuizOptions.Ukraine.title
//        QuizOptions.option[1].stringId
//

        val spinnerAdapter = QuizOptionAdapter(this, QuizOptions.values())
        spinner_quiz_options.adapter = spinnerAdapter
        spinnerView = spinner_quiz_options

        when(QuizOptionHelper.loadQuizOption()) {
            QuizOptions.WORLD.id -> spinner_quiz_options.setSelection(0)
            QuizOptions.UKRAINE.id -> spinner_quiz_options.setSelection(1)
        }

        database = Firebase.database
        usersRef = database.getReference("Users")

        auth = Firebase.auth
        currentUser = auth.currentUser

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val uid = currentUser?.uid ?: "none"
        val uidRef = usersRef.child(uid)
        uidRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    userExists = false
                    toast("The user does not exist")
                    val currentDate = Date()
                    userCreationDate = formatter.format(currentDate)
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })

        usersRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(shapchot: DataSnapshot, p1: String?) {
                //Log.i(TAG, "Users: onChildAdded")
                //toast("Users: onChildAdded")
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

                    usersRef.child(userId).apply {
                        child("name").setValue("Name1")
                        child("email").setValue("User1@mail.com")
                        child("lastLogInDate").setValue(lastLogInDate)
                    }

                    getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                        .putString(PREF_USER_ID, userId).apply()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "signInAnonymously: FAILURE: $e")
            }

        // Set onClick listeners
        button_start.setOnClickListener(this)

        spinner_quiz_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                when (clickedItem.id) {
                    QuizOptions.WORLD.id -> {
                        QuizOptionHelper.saveQuizOption(QuizOptions.WORLD.id)
                        QuizOptionHelper.saveQuizOption(QuizOptions.WORLD.id)
                    }
                    QuizOptions.UKRAINE.id -> {
                        QuizOptionHelper.saveQuizOption(QuizOptions.UKRAINE.id)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        QuizDbHelper(this).initDB()
    }

    override fun onResume() {
        super.onResume()
        mainActivityIsInFront = true
    }

    override fun onPause() {
        super.onPause()
        mainActivityIsInFront = false
    }


    /** INTERFACE METHODS */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_start -> onStartClicked()
        }
    }

    private fun onStartClicked() {
        startActivity(Intent(this, ChooseViewActivity::class.java))
        slideLeft(this)
    }

    /** CUSTOM METHODS */
    fun setQuizOption(i: Int) {
        spinner_quiz_options.setSelection(i)
    }

}