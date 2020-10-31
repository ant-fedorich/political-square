package eltonio.projects.politicalsquare.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import eltonio.projects.politicalsquare.*
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.other.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.image_anar_hover
import kotlinx.android.synthetic.main.activity_info.image_autho_left_hover
import kotlinx.android.synthetic.main.activity_info.image_autho_right_hover
import kotlinx.android.synthetic.main.activity_info.image_cons_hover
import kotlinx.android.synthetic.main.activity_info.image_gov_hover
import kotlinx.android.synthetic.main.activity_info.image_left_anar_hover
import kotlinx.android.synthetic.main.activity_info.image_lib_hover
import kotlinx.android.synthetic.main.activity_info.image_lib_soc
import kotlinx.android.synthetic.main.activity_info.image_libertar_hover
import kotlinx.android.synthetic.main.activity_info.image_nation_hover
import kotlinx.android.synthetic.main.activity_info.image_prog_hover
import kotlinx.android.synthetic.main.activity_info.image_radical_cap_hover
import kotlinx.android.synthetic.main.activity_info.image_right_anar_hover
import kotlinx.android.synthetic.main.activity_info.image_soc_demo_hover
import kotlinx.android.synthetic.main.activity_info.image_soc_hover

class InfoActivity : AppCompatActivity() {

    private var horScore = 0
    private var verScore = 0
    private var ideology = ""
    private var oldIdeologyHover: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        title = getString(R.string.info_title_actionbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set text for descriptions
        text_desc_compass_1.text = getString(R.string.text_desc_compass_1)
        text_desc_compass_2.text = getString(R.string.text_desc_compass_2)
        text_desc_compass_3.text = getString(R.string.text_desc_compass_3)

        // Set listeners
        frame_3.setOnTouchListener { v, event ->
            var ideologyForInfo = ""
            ideologyForInfo = showIdeologyHover(event.x, event.y)
            text_ideology_selected.text = ideologyForInfo

            if (event.action == MotionEvent.ACTION_UP) {
                Handler().postDelayed({
                    v.performClick()
                    val intent = Intent(this, ViewInfoActivity::class.java)
                    intent.putExtra(EXTRA_IDEOLOGY_TITLE, ideologyForInfo)
                    startActivity(intent)
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
    private fun showIdeologyHover(x: Float, y: Float): String {
        var step = convertDpToPx(4f)
        horScore = (x/step - 40).toInt()
        verScore = (y/step -40).toInt()

        ideology = getIdeology(horScore, verScore)

        when (ideology) {
            Ideologies.AUTHORITARIAN_LEFT.title -> showThisIdeologyHover(image_autho_left_hover)
            Ideologies.RADICAL_NATIONALISM.title  -> showThisIdeologyHover(image_nation_hover)
            Ideologies.POWER_CENTRISM.title  -> showThisIdeologyHover(image_gov_hover)
            Ideologies.SOCIAL_DEMOCRACY.title  -> showThisIdeologyHover(image_soc_demo_hover)
            Ideologies.SOCIALISM.title  -> showThisIdeologyHover(image_soc_hover)

            Ideologies.AUTHORITARIAN_RIGHT.title  -> showThisIdeologyHover(image_autho_right_hover)
            Ideologies.RADICAL_CAPITALISM.title  -> showThisIdeologyHover(image_radical_cap_hover)
            Ideologies.CONSERVATISM.title  -> showThisIdeologyHover(image_cons_hover)
            Ideologies.PROGRESSIVISM.title  -> showThisIdeologyHover(image_prog_hover)

            Ideologies.RIGHT_ANARCHY.title  -> showThisIdeologyHover(image_right_anar_hover)
            Ideologies.ANARCHY.title  -> showThisIdeologyHover(image_anar_hover)
            Ideologies.LIBERALISM.title  -> showThisIdeologyHover(image_lib_hover)
            Ideologies.LIBERTARIANISM.title  -> showThisIdeologyHover(image_libertar_hover)

            Ideologies.LEFT_ANARCHY.title  -> showThisIdeologyHover(image_left_anar_hover)
            Ideologies.LIBERTARIAN_SOCIALISM.title  -> showThisIdeologyHover(image_lib_soc)

            else -> showThisIdeologyHover(null)
        }
        return ideology
    }

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
/*   old style
 val listOfIdeologyHover = listOf<ImageView>(
            image_autho_left_hover,
            image_nation_hover,
            image_gov_hover,
            image_soc_demo_hover,
            image_soc_hover,
            image_autho_right_hover,
            image_radical_cap_hover,
            image_cons_hover,
            image_prog_hover,
            image_right_anar_hover,
            image_soc_hover,
            image_anar_hover,
            image_lib_hover,
            image_libertar_hover,
            image_left_anar_hover,
            image_lib_soc
        )
        listOfIdeologyHover.forEach {
            it.alpha = 0.5f
            it.animate().alpha(0f).setDuration(1000)
//            it.visibility = View.INVISIBLE
        }*/
        // show this ideology
//        ideologyHover?.visibility = View.VISIBLE
        ideologyHover?.alpha = 0f

        ideologyHover?.animate()?.apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            alpha(0.5f)
        }

        oldIdeologyHover = ideologyHover
    }

}

