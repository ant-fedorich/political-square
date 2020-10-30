package eltonio.projects.politicalsquare.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.*
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppViewModel
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.other.*
import eltonio.projects.politicalsquare.other.App.Companion.analytics
import eltonio.projects.politicalsquare.other.App.Companion.appQuizResults
import eltonio.projects.politicalsquare.views.ResultPointView

import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ResultActivity : BaseActivity(), View.OnClickListener {

    private var doubleBackPressed: Long = 0

    private var chosenIdeology = ""
    private var resultIdeology = ""
    private var startedAt = ""
    private var endedAt = ""
    private var duration = 0
    private var avgAnswerTime = 0.0
    private var zeroAnswerCnt = 0

    private var quizOwner = ""

    private lateinit var database: FirebaseDatabase
    private var userId = ""
    private var quizId = -1

    private lateinit var appViewModel: AppViewModel
    private lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        title = getString(R.string.result_title_actionbar)
        analytics.logEvent(EVENT_QUIZ_COMPLETE) {
            param(FirebaseAnalytics.Param.END_DATE, System.currentTimeMillis())
        }

        database = Firebase.database

        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        chosenViewX = sharedPref.getFloat(PREF_CHOSEN_VIEW_X, 0f)
        chosenViewY = sharedPref.getFloat(PREF_CHOSEN_VIEW_Y, 0f)
        horStartScore = sharedPref.getInt(PREF_HORIZONTAL_START_SCORE, 0)
        verStartScore = sharedPref.getInt(PREF_VERTICAL_START_SCORE, 0)
        chosenIdeology = sharedPref.getString(PREF_CHOSEN_IDEOLOGY, "").toString()
        startedAt = sharedPref.getString(PREF_STARTED_AT, "").toString()
        zeroAnswerCnt = sharedPref.getInt(PREF_ZERO_ANSWER_CNT, -1)

        userId = sharedPref.getString(PREF_USER_ID, "").toString()

        val youThoughtText = getString(R.string.result_subtitle_you_thought)
        title_2_2.text = "($youThoughtText: $chosenIdeology)"


        val intent = intent.extras
        if (intent != null) {
            horScore = intent.getInt(EXTRA_HORIZONTAL_SCORE, -100)
            verScore = intent.getInt(EXTRA_VERTICAL_SCORE, -100)
            quizId = intent.getInt(EXTRA_QUIZ_ID, -1)
        }
        // For debug
        Log.d(TAG, "zeroAnswerCnt Total: $zeroAnswerCnt")

        if (horScore != null && verScore != null) {
            resultIdeology = getIdeology(horScore, verScore)
        }

        // Init listeners
        button_compass_info_2.setOnClickListener(this)
        database.getReference("QuizResults").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(shapchot: DataSnapshot, p1: String?) {
                Log.i(TAG, "QuizResults: onChildAdded")
            }
            override fun onCancelled(e: DatabaseError) {
                Log.e(TAG, "QuizResults: onCancelled: $e")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })

        title_2.text = resultIdeology

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val endDate = Date()
        val startedAtParsed = formatter.parse(startedAt)
        val diffInMillies = endDate.time - startedAtParsed.time
        duration = TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.DAYS).toInt()
        endedAt = formatter.format(endDate)

        /* Add Result Points with animations */
    /* ValueAnimator variant
       ValueAnimator.ofFloat(4f, 100f).apply {
           duration = 3000
           addUpdateListener {
               val resultPotins = ResultAnimatedPointView(this@ResultActivity, it.animatedValue as Float)
               frame_result_points.addView(resultPotins)
           }
           start()
       }*/

        // Add start points
        val resultPoints = ResultPointView(this, 0f, 0f)
        resultPoints.alpha = 0f
        frame_result_points.addView(resultPoints)

        // Fading animation
        val animateFade = ObjectAnimator.ofFloat(resultPoints, View.ALPHA, 1f).apply {
            duration = 200
            addUpdateListener {
                frame_result_points.removeView(resultPoints)
                frame_result_points.addView(resultPoints)
            }
        }

        // Chosen point animation 1
        val animateChosenPoint1 = ObjectAnimator.ofFloat(resultPoints, "radiusInDp", 14f).apply {
            duration = 200
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                frame_result_points.removeView(resultPoints)
                frame_result_points.addView(resultPoints)
            }
        }

        // Chosen point animation 2
        val animateChosenPoint2 = ObjectAnimator.ofFloat(resultPoints, "radiusInDp", 10f).apply {
            duration = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                frame_result_points.removeView(resultPoints)
                frame_result_points.addView(resultPoints)
            }
        }

        // Result point animation 1
        val animateResultPoint1 = ObjectAnimator.ofFloat(resultPoints, "radiusResultInDp", 14f).apply {
            duration = 200
            startDelay = 200
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                frame_result_points.removeView(resultPoints)
                frame_result_points.addView(resultPoints)
            }
        }

        // Result point animation 2
        val animateResultPoint2 = ObjectAnimator.ofFloat(resultPoints, "radiusResultInDp", 12f).apply {
            duration = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                frame_result_points.removeView(resultPoints)
                frame_result_points.addView(resultPoints)
            }
        }

        // Animate it all
        AnimatorSet().apply {
            startDelay = 400
            play(animateFade)
            playSequentially(animateChosenPoint1, animateChosenPoint2)
            playSequentially(animateResultPoint1, animateResultPoint2)
            start()
        }



        // For debug
        Log.d(TAG, "Datetime duration: $duration, endedAt: $endedAt")

        avgAnswerTime = (duration/40.0)

        Log.d(TAG, "Date avgAnswerTime: $avgAnswerTime")

        val ideologyId = getIdeologyStringId(resultIdeology)

        // Adding data to SQL
        val dbHelper = QuizDbHelper(this)
        val quizResult = QuizResult(
            userId = userId,
            quizId = quizId,
            ideologyId = ideologyId,
            horStartScore = horStartScore,
            verStartScore = verStartScore,
            horResultScore = horScore,
            verResultScore = verScore,
            startedAt = startedAt,
            endedAt = endedAt,
            duration = duration,
            zeroAnswerCnt = zeroAnswerCnt,
            avgAnswerTime = avgAnswerTime
        )
        // Adding data to SQL
        dbHelper.addQuizResult(quizResult)
        // Adding data to Firebase
        database.getReference("QuizResults").push().setValue(quizResult)

        // Adding data to Room DB
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        scope = CoroutineScope(Dispatchers.IO)

        val quizResultRoom = eltonio.projects.politicalsquare.data.QuizResult(
            id = 0, //id is autoincrement
            quizId = chosenQuizId,
            ideologyStringId = ideologyId,
            horStartScore = horStartScore,
            verStartScore = verStartScore,
            horResultScore = horScore,
            verResultScore = verScore,
            startedAt = startedAt,
            endedAt = endedAt,
            duration = duration,
            avgAnswerTime = avgAnswerTime
        )
        appViewModel.addQuizResult(quizResultRoom)
        scope.launch {
            appQuizResults = appViewModel.getQuizResults()
            Log.w(TAG, "QuizResults in ResultActivity inside Coroutine:")
            for (item in appQuizResults) Log.w(TAG, "Item: $item")
        }

//        appViewModel.getAllQuestions().observe(this, androidx.lifecycle.Observer {
//
//        })
        Log.i(TAG, "------------------------")
/*        val quizResultAll = appViewModel.getQuizResults
        quizResultAll.forEach {
            Log.i(TAG, "Result: ${it.id}, ${it.ideologyStringId}, ${it.startedAt}")
        }
        Log.i(TAG, "------------------------")*/

        compassX = horScore.plus(40)
        compassY = verScore.plus(40)

    }

    override fun onBackPressed() {
        showEndQuizDialogLambda(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    /** INTERFACE METHODS */

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_compass_info_2 -> {
                analytics.logEvent(EVENT_DETAILED_INFO, null)

                val intent = Intent(this, ViewInfoActivity::class.java)
                intent.putExtra(EXTRA_IDEOLOGY_TITLE, resultIdeology)
                startActivity(intent)
                pushLeft(this) // info in
            }
        }
    }

    /** CUSTOM METHODS */

    companion object {
        var chosenViewX = 0f
        var chosenViewY = 0f
        var compassX: Int? = 0
        var compassY: Int? = 0
        var verScore: Int = 0
        var horScore: Int = 0
        var horStartScore = 0
        var verStartScore = 0
    }
}


