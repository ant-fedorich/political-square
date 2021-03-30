package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivitySavedResultDetailBinding
import eltonio.projects.politicalsquare.ui.viewmodel.SavedResultDetailViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushLeft
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import eltonio.projects.politicalsquare.views.ResultDetailPointView

@AndroidEntryPoint
class SavedResultDetailActivity : AppCompatActivity(), View.OnClickListener {
    private val viewmodel: SavedResultDetailViewModel by viewModels()
    private val binding: ActivitySavedResultDetailBinding by lazy { ActivitySavedResultDetailBinding.inflate(layoutInflater) }

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

        title = getString(R.string.savedresultdetail_title_actionbar)

        intent.extras?.let {
            getAllDataFromIntent(it)
            setTransitionNamesFromIntent(it)
        }
        // Init listeners
        binding.buttonCompassInfo3.setOnClickListener(this)

        viewmodel.getIdeology(ideologyId).observe(this, Observer {
                binding.titleResultDetail.text = it
                resultIdeology = it
        })

        viewmodel.getOwner(quizId).observe(this, Observer {
            quizOwner = it
            binding.textResultOwner.text = getString(R.string.savedresultdetail_title_quiz) + ": " + quizOwner
            binding.textResultDate.text = endedAt
        })

        addPointsOnCompass()

        setContentView(binding.root)
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
            binding.titleResultDetail.transitionName = it.getString(EXTRA_TITLE_TRANSITION_NAME, "NONE")
            binding.textResultDate.transitionName  = it.getString(EXTRA_DATE_TRANSITION_NAME,"NONE")
            binding.imageMainCompass2.transitionName  = it.getString(EXTRA_IMAGE_TRANSITION_NAME, "NONE")
            binding.layoutSavedResultDetail.transitionName = it.getString(
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

    // todo use applicationContext or From Hilt Context
    private fun addPointsOnCompass() {
        val resultDetailPoint =
            ResultDetailPointView(
                applicationContext,
                horStartScore,
                verStartScore,
                horResultScore,
                verResultScore
            )
        // TODO: with layout or without?
        //binding.layoutSavedResultDetail.frame_saved_result_detail.addView(resultDetailPoint)
        binding.frameSavedResultDetail.addView(resultDetailPoint)
    }
}






