package eltonio.projects.politicalcompassquiz.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.model.*
import eltonio.projects.politicalcompassquiz.adapter.QuizOptionAdapter
import eltonio.projects.politicalcompassquiz.databinding.ActivityMainBinding
import eltonio.projects.politicalcompassquiz.ui.viewmodel.MainViewModel
import eltonio.projects.politicalcompassquiz.util.*
import eltonio.projects.politicalcompassquiz.util.AppUtil.defaultOnItemSelectedListener
import eltonio.projects.politicalcompassquiz.util.AppUtil.slideLeft

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