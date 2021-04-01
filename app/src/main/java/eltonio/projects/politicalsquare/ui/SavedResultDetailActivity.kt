package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivitySavedResultDetailBinding
import eltonio.projects.politicalsquare.ui.viewmodel.SavedResultDetailViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyResIdByStringId
import eltonio.projects.politicalsquare.util.AppUtil.pushLeft
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import eltonio.projects.politicalsquare.views.ResultDetailPointView

@AndroidEntryPoint
class SavedResultDetailActivity(): AppCompatActivity() {
    private val viewmodel: SavedResultDetailViewModel by viewModels()
    private val binding: ActivitySavedResultDetailBinding by lazy { ActivitySavedResultDetailBinding.inflate(layoutInflater) }

    private var ideologyStringId = ""
    private var ideologyResId = 0
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

        super.onCreate(savedInstanceState) // TODO: onCreate before or after
        subscribeToObservers()

        title = getString(R.string.savedresultdetail_title_actionbar)

        intent.extras?.let {
            getAllDataFromIntent(it)
            setTransitionNamesFromIntent(it)
        }

        addPointsOnCompass()

        // Setup listeners
        binding.buttonCompassInfo3.setOnClickListener {
            val intent = Intent(this, IdeologyInfoActivity::class.java)
            intent.putExtra(EXTRA_IDEOLOGY_TITLE_RES, ideologyResId)
            startActivity(intent)
            pushLeft(this) // info in
        }

        setContentView(binding.root)
    }

    /*****************************************************/


    private fun subscribeToObservers() {

        viewmodel.ideology.observe(this) {
            binding.titleResultDetail.text = it
        }

        viewmodel.owner.observe(this) {
            quizOwner = it
            binding.textResultOwner.text =
                getString(R.string.savedresultdetail_title_quiz) + ": " + quizOwner
            binding.textResultDate.text = endedAt
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
    private fun getAllDataFromIntent(extras: Bundle) {
        extras.let {
            ideologyStringId = it.getString(EXTRA_IDEOLOGY_ID, "")
            quizId = it.getInt(EXTRA_QUIZ_ID, -1)
            endedAt = it.getString(EXTRA_ENDED_AT, "")
            horStartScore = it.getInt (EXTRA_HOR_START_SCORE, -100)
            verStartScore = it.getInt (EXTRA_VER_START_SCORE, -100)
            horResultScore = it.getInt (EXTRA_HOR_RESULT_SCORE, -100)
            verResultScore = it.getInt (EXTRA_VER_RESULT_SCORE, -100)
        }
        viewmodel.setOwner(quizId, this)
        viewmodel.setIdeologyTitle(ideologyStringId, this)
        ideologyResId = getIdeologyResIdByStringId(ideologyStringId)
    }

    private fun setTransitionNamesFromIntent(extras: Bundle) {
        extras.let {
            binding.titleResultDetail.transitionName = it.getString(EXTRA_TITLE_TRANSITION_NAME, "NONE")
            binding.textResultDate.transitionName  = it.getString(EXTRA_DATE_TRANSITION_NAME,"NONE")
            binding.imageMainCompass2.transitionName  = it.getString(EXTRA_IMAGE_TRANSITION_NAME, "NONE")
            binding.layoutSavedResultDetail.transitionName = it.getString(
                EXTRA_ITEM_CONTAINER_TRANSITION_NAME, "NONE")
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
                this,
                horStartScore,
                verStartScore,
                horResultScore,
                verResultScore
            )
        binding.frameSavedResultDetail.addView(resultDetailPoint)
    }
}






