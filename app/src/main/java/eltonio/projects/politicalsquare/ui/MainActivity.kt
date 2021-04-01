package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.graphics.Color.alpha
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.AdapterView
import android.widget.Button
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.model.*
import eltonio.projects.politicalsquare.adapter.QuizOptionAdapter
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.databinding.ActivityMainBinding
import eltonio.projects.politicalsquare.ui.viewmodel.MainViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.defaultOnItemSelectedListener
import eltonio.projects.politicalsquare.util.AppUtil.slideLeft
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity: BaseActivity() {
    private val viewmodel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        this.title = getString(R.string.main_title_actionbar)
        startContainerFadeAnimation()
        setupSpinner()

        binding.buttonStart.setOnClickListener{
            startActivity(Intent(this, ChooseIdeologyActivity::class.java))
            slideLeft(this)
        }

        binding.spinnerQuizOptions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener by defaultOnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val clickedItem = parent?.getItemAtPosition(position) as QuizOptions
                    viewmodel.clickSpinnerItem(clickedItem.id)
                }
            }

        startContainerFadeAnimation()

        setContentViewForBase(binding.root)
    }

    /***************************************/

    private fun subscribeToObservers() {
        lifecycle.addObserver(viewmodel)

        viewmodel.splashAppearedEvent.observe(this) {
            if (it == false) {
                startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            }
        }
        viewmodel.spinnerSelection.observe(this) {
            binding.spinnerQuizOptions.setSelection(it)
        }
    }

    private fun setupSpinner() {
        binding.spinnerQuizOptions.adapter = QuizOptionAdapter(this, QuizOptions.values())
    }
}