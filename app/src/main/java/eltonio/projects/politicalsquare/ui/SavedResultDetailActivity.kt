package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.ui.viewmodel.SavedResultDetailViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.views.ResultDetailPointView
import kotlinx.android.synthetic.main.activity_saved_result_detail.*
import kotlinx.android.synthetic.main.activity_saved_result_detail.view.*

@AndroidEntryPoint
class SavedResultDetailActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: SavedResultDetailViewModel by viewModels()

    private var resultIdeology = ""
    private var ideologyId = ""
    private var quizId = -1
    private var quizOwner = ""
    private var endedAt = ""

    private var horStartScore = 0
    private var verStartScore = 0
    private var horResultScore = 0
    private var verResultScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        setWindowSharedElementTransitions()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_result_detail)

        title = getString(R.string.savedresultdetail_title_actionbar)

        intent.extras?.let {
            getAllDataFromIntent(it)
            setTransitionNamesFromIntent(it)
        }
        // Init listeners
        button_compass_info_3.setOnClickListener(this)

        viewModel.getIdeology(ideologyId).observe(this, Observer {
                title_result_detail.text = it
                resultIdeology = it
        })

        viewModel.getOwner(quizId).observe(this, Observer {
            quizOwner = it
            text_result_owner.text = getString(R.string.savedresultdetail_title_quiz) + ": " + quizOwner
            text_result_date.text = endedAt
        })

        addPointsOnCompass()
    }

    /** INTERFACE METHODS */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_compass_info_3 -> {
                val intent = Intent(this, ViewInfoActivity::class.java)
                intent.putExtra(EXTRA_IDEOLOGY_TITLE, resultIdeology)
                startActivity(intent)
                pushLeft(this) // info in
            }
        }
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

    /** CUSTOM METHODS */
    private fun setTransitionNamesFromIntent(extras: Bundle) {
        extras.let {
            title_result_detail.transitionName = it.getString(EXTRA_TITLE_TRANSITION_NAME, "NONE")
            text_result_date.transitionName  = it.getString(EXTRA_DATE_TRANSITION_NAME,"NONE")
            image_main_compass_2.transitionName  = it.getString(EXTRA_IMAGE_TRANSITION_NAME, "NONE")
            layout_saved_result_detail.transitionName = it.getString(
                EXTRA_ITEM_CONTAINER_TRANSITION_NAME, "NONE")
        }
    }

    private fun getAllDataFromIntent(extras: Bundle) {
        extras.let {
            ideologyId = it.getString(EXTRA_IDEOLOGY_ID, "")
            quizId = it.getInt(EXTRA_QUIZ_ID, -1)
            endedAt = it.getString(EXTRA_ENDED_AT, "")
            horStartScore = it.getInt (EXTRA_HOR_START_SCORE, -100)
            verStartScore = it.getInt (EXTRA_VER_START_SCORE, -100)
            horResultScore = it.getInt (EXTRA_HOR_RESULT_SCORE, -100)
            verResultScore = it.getInt (EXTRA_VER_RESULT_SCORE, -100)
        }
    }

    private fun setWindowSharedElementTransitions() {
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
    }

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
}






