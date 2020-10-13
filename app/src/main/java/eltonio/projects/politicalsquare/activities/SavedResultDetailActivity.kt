package eltonio.projects.politicalsquare.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.other.*
import eltonio.projects.politicalsquare.views.ResultDetailPointView
import kotlinx.android.synthetic.main.activity_result.button_compass_info_2
import kotlinx.android.synthetic.main.activity_saved_result_detail.*
import kotlinx.android.synthetic.main.activity_saved_result_detail.view.*


class SavedResultDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var resultIdeology = ""
    private var ideologyId = ""
    private var quizId = -1
    private var quizOwner = ""
    private var endedAt = ""

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        val enterTrans = MaterialContainerTransform().apply {
            duration = 600
            scrimColor = Color.BLACK
            setAllContainerColors(Color.WHITE)
            addTarget(R.id.layout_saved_result_detail)
        }

        val returnTrans = MaterialContainerTransform().apply {
            duration = 400
            scrimColor = Color.BLACK
            setAllContainerColors(Color.WHITE)
            addTarget(R.id.layout_saved_result_detail)
        }

        window.apply {
            sharedElementEnterTransition = enterTrans
            sharedElementReturnTransition = returnTrans
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_result_detail)

        title = getString(R.string.savedresultdetail_title_actionbar)

        database = Firebase.database

        val extras = intent.extras

        extras?.let {
            ideologyId = it.getString(EXTRA_IDEOLOGY_ID, "")
            quizId = it.getInt(EXTRA_QUIZ_ID, -1)
            endedAt = it.getString(EXTRA_ENDED_AT, "")
            horStartScore = it.getInt (EXTRA_HOR_START_SCORE, -100)
            verStartScore = it.getInt (EXTRA_VER_START_SCORE, -100)
            horResultScore = it.getInt (EXTRA_HOR_RESULT_SCORE, -100)
            verResultScore = it.getInt (EXTRA_VER_RESULT_SCORE, -100)
            title_result_detail.transitionName = it.getString(EXTRA_TITLE_TRANSITION_NAME, "NONE")
            text_result_date.transitionName  = it.getString(EXTRA_DATE_TRANSITION_NAME,"NONE")
            image_main_compass_2.transitionName  = it.getString(EXTRA_IMAGE_TRANSITION_NAME, "NONE")
            layout_saved_result_detail.transitionName = it.getString(EXTRA_ITEM_CONTAINER_TRANSITION_NAME, "NONE")
        }

        when(quizId) {
            QuizOptions.UKRAINE.id -> quizOwner = QuizOptions.UKRAINE.owner
            QuizOptions.WORLD.id -> quizOwner = QuizOptions.WORLD.owner
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

        for (ideology in Ideologies.values()) {
            if (ideology.stringId == ideologyId) {
                title_result_detail.text = ideology.title
                resultIdeology = ideology.title
            }
        }

        text_result_date.text = endedAt
        text_result_owner.text = getString(R.string.savedresultdetail_title_quiz) + ": " + quizOwner

        addPointsOnCompass()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this) // info out
    }

    /** INTERFACE METHODS */

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_compass_info_2 -> {
                val intent = Intent(this, ViewInfoActivity::class.java)
                intent.putExtra(EXTRA_IDEOLOGY_TITLE, resultIdeology)
                startActivity(intent)
                pushLeft(this) // info in
            }
        }
    }

    /** CUSTOM METHODS */

    private fun addPointsOnCompass() {
        val resultDetailPoint =
            ResultDetailPointView(
                applicationContext,
                horStartScore,
                verStartScore,
                horResultScore,
                verResultScore
            )
        layout_saved_result_detail.frame_saved_result_detail.addView(resultDetailPoint)
    }

    companion object {
        var horStartScore = 0
        var verStartScore = 0
        var horResultScore = 0
        var verResultScore = 0
    }
}






