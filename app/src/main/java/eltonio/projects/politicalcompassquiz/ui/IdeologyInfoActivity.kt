package eltonio.projects.politicalcompassquiz.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.databinding.ActivityIdeologyInfoBinding
import eltonio.projects.politicalcompassquiz.ui.viewmodel.IdeologyInfoViewModel
import eltonio.projects.politicalcompassquiz.util.EXTRA_IDEOLOGY_TITLE_RES

import eltonio.projects.politicalcompassquiz.util.AppUtil.DefaultTransitionListener
import eltonio.projects.politicalcompassquiz.util.AppUtil.pushRight
import eltonio.projects.politicalcompassquiz.util.AppUtil.resString

@AndroidEntryPoint
class IdeologyInfoActivity : AppCompatActivity() {
    private val viewmodel: IdeologyInfoViewModel by viewModels()
    private val binding: ActivityIdeologyInfoBinding by lazy { ActivityIdeologyInfoBinding.inflate(layoutInflater) }

    private var ideologyResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        setSupportActionBar(binding.toolbarViewInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set color, because is transparent by style
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        title = ""
        binding.textIdeologyDescription.movementMethod = ScrollingMovementMethod()

        ideologyResId = intent.getIntExtra(EXTRA_IDEOLOGY_TITLE_RES, 0)
        binding.titleViewInfo.text = ideologyResId.resString(this)

        viewmodel.updateData(ideologyResId)

        binding.motionViewinfo.setTransitionListener(object : MotionLayout.TransitionListener by DefaultTransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {
                if (progress >= 0.95f) binding.titleViewInfo.typeface = Typeface.createFromAsset(assets, "font/roboto_medium.ttf")
                if (progress < 0.95f) binding.titleViewInfo.typeface = Typeface.createFromAsset(assets, "font/roboto_regular.ttf")
            }
        })

        setContentView(binding.root)
    }

//    on

    private fun subscribeToObservers() {
        viewmodel.imageId.observe(this) {
            binding.imageIdeologyInfo.setImageResource(it)
        }
        viewmodel.descriptionId.observe(this) {
            binding.textIdeologyDescription.text = getString(it)
        }
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