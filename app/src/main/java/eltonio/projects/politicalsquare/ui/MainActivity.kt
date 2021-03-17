package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.*
import eltonio.projects.politicalsquare.adapter.QuizOptionAdapter
import eltonio.projects.politicalsquare.ui.viewmodel.MainViewModel
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity: BaseActivity() {
    private val viewModel: MainViewModel by viewModels()

    companion object {
        lateinit var spinnerView: Spinner // To use it in Settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)

        viewModel.splashAppearedEvent.observe(this, {
            if (it == false) {
                val splashActivityIntent = Intent(this@MainActivity, SplashActivity::class.java)
                startActivity(splashActivityIntent)
            }
        })

        Thread.sleep(300) // Needs to load Splash Activity Correctly

        refreshAllСatalogs(this)
        this.title = getString(R.string.main_title_actionbar)

        setContentView(R.layout.activity_main)

        startContainerFadeAnimation()

        initSpinner()
        viewModel.spinnerSelection.observe(this, Observer<Int> {
            spinner_quiz_options.setSelection(it)
        })

        button_start.setOnClickListener{ onStartClicked() }

        spinner_quiz_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                viewModel.clickSpinnerItem(clickedItem.id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    private fun startContainerFadeAnimation() { // Is needed to load Splash Activity
        activity_container.alpha = 0f
        activity_container.animate().apply {
            duration = 500
            alpha(1f)
            interpolator = AccelerateInterpolator(3f)
        }.start()
    }
}