package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.model.*
import eltonio.projects.politicalsquare.adapter.QuizOptionAdapter
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.databinding.ActivityMainBinding
import eltonio.projects.politicalsquare.ui.viewmodel.MainViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.slideLeft
import eltonio.projects.politicalsquare.util.AppUtil.toast
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity: BaseActivity() {
    private val viewmodel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    companion object {
        lateinit var spinnerView: Spinner // To use it in Settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(viewmodel)

        viewmodel.splashAppearedEvent.observe(this, {
            if (it == false) {
                val splashActivityIntent = Intent(this@MainActivity, SplashActivity::class.java)
                startActivity(splashActivityIntent)
            }
        })

        Thread.sleep(300) // Needs to load Splash Activity Correctly

        //todo refreshAllСatalogs(this)
        this.title = getString(R.string.main_title_actionbar)



        startContainerFadeAnimation()

        initSpinner()
        viewmodel.spinnerSelection.observe(this, {
            binding.spinnerQuizOptions.setSelection(it)
        })

        button_start.setOnClickListener{ onStartClicked() }
//        binding.buttonStart.setOnClickListener {
//            toast(this, "Hello")
//        }

        spinner_quiz_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                viewmodel.clickSpinnerItem(clickedItem.id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        setContentView(binding.root)
    }

    /** INTERFACE METHODS */
    private fun onStartClicked() {
        startActivity(Intent(this, ChooseViewActivity::class.java))
        slideLeft(this)
    }

    /** CUSTOM METHODS */
    private fun initSpinner() {
        val spinnerAdapter = QuizOptionAdapter(this, QuizOptions.values())
        spinner_quiz_options.adapter = spinnerAdapter
        spinnerView = spinner_quiz_options
    }

    private fun startContainerFadeAnimation() { // Is needed to load Splash Activity .alpha = 0f
//        val baseBinding = ActivityBaseBinding.inflate(LayoutInflater.from(this), this, true)
//
//        baseBinding.activityContainer.animate().apply {
//            duration = 500
//            alpha(1f)
//            interpolator = AccelerateInterpolator(3f)
//        }.start()


        this.findViewById<DrawerLayout>(R.id.activity_container).animate().apply {
            duration = 500
            alpha(1f)
            interpolator = AccelerateInterpolator(3f)
        }.start()
    }
}