package eltonio.projects.politicalsquare.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityViewInfoBinding
import eltonio.projects.politicalsquare.ui.viewmodel.ViewInfoViewModel
import eltonio.projects.politicalsquare.util.EXTRA_IDEOLOGY_TITLE

import eltonio.projects.politicalsquare.util.AppUtil.EmptyTransitionListener
import eltonio.projects.politicalsquare.util.AppUtil.pushRight

@AndroidEntryPoint
class ViewInfoActivity : AppCompatActivity() {
    private val viewModel: ViewInfoViewModel by viewModels()
    private val binding: ActivityViewInfoBinding by lazy { ActivityViewInfoBinding.inflate(layoutInflater) }

    // TODO: Use only context for AppUtil, not method?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setSupportActionBar(toolbar_collapsing)
        setSupportActionBar(binding.toolbarViewInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set color, because is transparent by style
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        title = ""

//        layout_collapsing_toolbar.apply {
//            setContentScrimColor(resources.getColor(R.color.collapsing_image_сollapsed))
//            setCollapsedTitleTextColor(resources.getColor(R.color.on_primary_bright))
//            setExpandedTitleColor(resources.getColor(R.color.on_primary_bright))
//        }

        binding.textIdeologyDescription.movementMethod = ScrollingMovementMethod()

        val ideology = intent.getStringExtra(EXTRA_IDEOLOGY_TITLE)

        viewModel.updateData(ideology.length) //fixme ONLY TEST ideology.length
        viewModel.getIdeology().observe(this, {
            binding.titleViewInfo.text = it
        })
        viewModel.getImageId().observe(this, {
            binding.imageIdeologyInfo.setImageResource(it)
        })
        viewModel.getDescriptionId().observe(this, {
            binding.textIdeologyDescription.text = getString(it)
        })
//        viewModel.getStyleId().observe(this, Observer {
//            layout_collapsing_toolbar.setExpandedTitleTextAppearance(it)
//        })

        binding.motionViewinfo.setTransitionListener(object : MotionLayout.TransitionListener by EmptyTransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {
                if (progress >= 0.95f) binding.titleViewInfo.typeface = Typeface.createFromAsset(assets, "font/roboto_medium.ttf")
                if (progress < 0.95f) binding.titleViewInfo.typeface = Typeface.createFromAsset(assets, "font/roboto_regular.ttf")
            }
        })

        setContentView(binding.root)
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