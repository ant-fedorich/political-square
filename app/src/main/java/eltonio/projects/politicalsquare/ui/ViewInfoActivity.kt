package eltonio.projects.politicalsquare.ui

import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.ui.viewmodel.ViewInfoViewModel
import eltonio.projects.politicalsquare.util.EXTRA_IDEOLOGY_TITLE
import kotlinx.android.synthetic.main.activity_view_info.*
import eltonio.projects.politicalsquare.util.AppUtil.EmptyTransitionListener
import eltonio.projects.politicalsquare.util.AppUtil.pushRight

@AndroidEntryPoint
class ViewInfoActivity : AppCompatActivity() {
    private val viewModel: ViewInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_info)

//        setSupportActionBar(toolbar_collapsing)
        setSupportActionBar(toolbar_view_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set color, because is transparent by style
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        title = ""

//        layout_collapsing_toolbar.apply {
//            setContentScrimColor(resources.getColor(R.color.collapsing_image_сollapsed))
//            setCollapsedTitleTextColor(resources.getColor(R.color.on_primary_bright))
//            setExpandedTitleColor(resources.getColor(R.color.on_primary_bright))
//        }

        text_ideology_description.movementMethod = ScrollingMovementMethod()

        val ideology = intent.getStringExtra(EXTRA_IDEOLOGY_TITLE)

        viewModel.updateData(ideology)
        viewModel.getIdeology().observe(this, Observer {
            title_view_info.text = it
        })
        viewModel.getImageId().observe(this, Observer {
            image_ideology_info.setImageResource(it)
        })
        viewModel.getDescriptionId().observe(this, Observer {
            text_ideology_description.text = getString(it)
        })
//        viewModel.getStyleId().observe(this, Observer {
//            layout_collapsing_toolbar.setExpandedTitleTextAppearance(it)
//        })

        motion_viewinfo.setTransitionListener(object : MotionLayout.TransitionListener by EmptyTransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {
                if (progress >= 0.95f) title_view_info.typeface = Typeface.createFromAsset(assets, "font/roboto_medium.ttf")
                if (progress < 0.95f) title_view_info.typeface = Typeface.createFromAsset(assets, "font/roboto_regular.ttf")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        pushRight(this)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this)
    }

}