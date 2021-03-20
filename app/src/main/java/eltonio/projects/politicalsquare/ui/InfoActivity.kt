package eltonio.projects.politicalsquare.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.*
import eltonio.projects.politicalsquare.ui.viewmodel.InfoViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushLeft
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import kotlinx.android.synthetic.main.activity_info.*

@AndroidEntryPoint
class InfoActivity : AppCompatActivity() {
    private val viewModel: InfoViewModel by viewModels()
    private var oldIdeologyHover: ImageView? = null
    private var intentToViewInfo: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        title = getString(R.string.info_title_actionbar)

        // Set text for descriptions
        text_desc_compass_1.text = getString(R.string.text_desc_compass_1)
        text_desc_compass_2.text = getString(R.string.text_desc_compass_2)
        text_desc_compass_3.text = getString(R.string.text_desc_compass_3)

        // Set listeners
        frame_3.setOnTouchListener { v, event ->
            viewModel.getIdeology(event.x, event.y).observe(this, { ideology ->
                text_ideology_selected.text = ideology

                intentToViewInfo = Intent(this, ViewInfoActivity::class.java)
                intentToViewInfo?.putExtra(EXTRA_IDEOLOGY_TITLE, ideology)

                viewModel.getImageHoverId(ideology).observe(this, {
                    val imageHover = findViewById<ImageView>(it)
                    showThisIdeologyHover(imageHover)
                })
            })

            if (event.action == MotionEvent.ACTION_UP) {
                Handler().postDelayed({
                    v.performClick()
                    startActivity(intentToViewInfo)
                    pushLeft(this)
                }, 80)
            }
            return@setOnTouchListener true
        }
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

