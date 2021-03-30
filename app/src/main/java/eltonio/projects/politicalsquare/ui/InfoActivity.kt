package eltonio.projects.politicalsquare.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.*
import eltonio.projects.politicalsquare.databinding.ActivityInfoBinding
import eltonio.projects.politicalsquare.ui.viewmodel.InfoViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushLeft
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import eltonio.projects.politicalsquare.util.AppUtil.resString
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class InfoActivity : AppCompatActivity() {
    private val viewmodel: InfoViewModel by viewModels()
    private val binding: ActivityInfoBinding by lazy { ActivityInfoBinding.inflate(layoutInflater) }

    private val intentToViewInfo: Intent by lazy { Intent(this, IdeologyInfoActivity::class.java) }
    private var oldIdeologyHover: ImageView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupText()

        // Set listeners
        binding.frame3.setOnTouchListener { v, event ->
            val ideologyResId = viewmodel.getIdeologyResId(event.x, event.y)
            binding.textIdeologySelected.text = ideologyResId.resString(this)

            intentToViewInfo.putExtra(EXTRA_IDEOLOGY_TITLE_RES, ideologyResId)

            val imageHover = findViewById<ImageView>(viewmodel.getImageHoverId(ideologyResId))
            showThisIdeologyHover(imageHover)

            if (event.action == MotionEvent.ACTION_UP) {
                runBlocking { delay(80) }
                v.performClick()
                startActivity(intentToViewInfo)
                pushLeft(this@InfoActivity)
            }
            return@setOnTouchListener true
        }

        setContentView(binding.root)
    }

    private fun setupText() {
        title = getString(R.string.info_title_actionbar)

        // Set text for descriptions
        binding.textDescCompass1.text = getString(R.string.text_desc_compass_1)
        binding.textDescCompass2.text = getString(R.string.text_desc_compass_2)
        binding.textDescCompass3.text = getString(R.string.text_desc_compass_3)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this) // info out
    }

    /** CUSTOM METHODS */
    private fun showThisIdeologyHover(ideologyHover: ImageView?) {
        // Hide these ideologies
        // If Ideology same, break
        if (oldIdeologyHover == ideologyHover) {
            return
        }

        // Hide this ideology
        if (oldIdeologyHover != null) {
            oldIdeologyHover?.alpha = 0.5f
            oldIdeologyHover?.animate()?.apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                alpha(0f)
            }
        }
        ideologyHover?.alpha = 0f

        ideologyHover?.animate()?.apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            alpha(0.5f)
        }

        oldIdeologyHover = ideologyHover
    }

}

