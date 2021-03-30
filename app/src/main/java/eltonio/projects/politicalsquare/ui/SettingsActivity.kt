package eltonio.projects.politicalsquare.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.databinding.ActivitySettingsBinding
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.ui.viewmodel.SettingsViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import eltonio.projects.politicalsquare.util.AppUtil.showEndQuizDialogLambda


@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), View.OnTouchListener {
    private val viewModel: SettingsViewModel by viewModels()
    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    //private val localRepo = AppRepository.Local()

    private lateinit var langBorderShapeUkr: GradientDrawable
    private lateinit var langBorderShapeRus: GradientDrawable
    private lateinit var langBorderShapeEng: GradientDrawable
    private lateinit var langBorderShapeList: MutableList<GradientDrawable>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.settings_title_actionbar)

        initAllLangBorderShapes()

        // Reset all Lang Radio Borders (to prevent a bug)
        for (langBorder in langBorderShapeList) {
            langBorder.setColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        viewModel.getLang().observe(this, Observer {
            when(it) {
                "uk" -> loadLangForSettings(binding.radioUkr, langBorderShapeUkr)
                "ru" -> loadLangForSettings(binding.radioRus, langBorderShapeRus)
                "en" -> loadLangForSettings(binding.radioEng, langBorderShapeEng)
            }
        })

        viewModel.getQuizOption().observe(this, Observer {
            when(it) {
                QuizOptions.WORLD.id -> {
                    setQuizOptionToSelectedAnimation(binding.layoutQuizOption1)
                    binding.titleQuizOption1.setTypeface(null, Typeface.BOLD)
                    setQuizOptionToDefaultAnimation(binding.layoutQuizOption2)
                }
                QuizOptions.UKRAINE.id -> {
                    setQuizOptionToSelectedAnimation(binding.layoutQuizOption2)
                    binding.titleQuizOption2.setTypeface(null, Typeface.BOLD)
                    setQuizOptionToDefaultAnimation(binding.layoutQuizOption1)
                }
            }
        })

        // Init listeners
        binding.radioGroupLang.setOnCheckedChangeListener { _, checkedId ->
            viewModel.getQuizIsActiveState().observe(this, Observer {
                if (it == true) {
                    val baseBinding = ActivityBaseBinding.inflate(layoutInflater)
                    baseBinding.activityContainer.closeDrawer(GravityCompat.START)

                    showEndQuizDialogLambda(this) {
                        checkRadioButton(checkedId)
                    }
                } else {
                    checkRadioButton(checkedId)
                }
            })
        }
        // Use onTouch for visual effects
        binding.radioUkr.setOnTouchListener(this)
        binding.radioRus.setOnTouchListener(this)
        binding.radioEng.setOnTouchListener(this)

        binding.imageUkr.setOnTouchListener(this)
        binding.imageRus.setOnTouchListener(this)
        binding.imageEng.setOnTouchListener(this)

        binding.cardQuizOption1.setOnTouchListener(this)
        binding.cardQuizOption2.setOnTouchListener(this)

        setContentView(binding.root)
    }

    private fun initAllLangBorderShapes() {
        langBorderShapeUkr = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeRus = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeEng = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable

        (binding.imageUkr.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeUkr)
        (binding.imageRus.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeRus)
        (binding.imageEng.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeEng)

        langBorderShapeList = mutableListOf(
            langBorderShapeUkr,
            langBorderShapeRus,
            langBorderShapeEng
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            binding.radioUkr -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeUkr)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeUkr)
            }
            binding.radioRus -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeRus)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeRus)
            }
            binding.radioEng -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeEng)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeEng)
            }

            // Delegate OnTouch Event
            binding.imageUkr -> {
                binding.radioUkr.dispatchTouchEvent(event)
                binding.radioUkr.performClick()
            }
            binding.imageRus -> {
                binding.radioRus.dispatchTouchEvent(event)
                binding.radioRus.performClick()
            }
            binding.imageEng -> {
                binding.radioEng.dispatchTouchEvent(event)
                binding.radioEng.performClick()
            }

            binding.cardQuizOption1 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    setQuizOptionToSelectedAnimation(binding.layoutQuizOption1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.titleQuizOption1.setTypeface(null, Typeface.BOLD)
                    binding.titleQuizOption2.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefaultAnimation(binding.layoutQuizOption2)

                    viewModel.saveQuizOption(QuizOptions.WORLD.id)
                    MainActivity.spinnerView.setSelection(0)
                }
                return true
            }
            binding.cardQuizOption2 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    setQuizOptionToSelectedAnimation(binding.layoutQuizOption2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    binding.titleQuizOption2.setTypeface(null, Typeface.BOLD)
                    binding.titleQuizOption1.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefaultAnimation(binding.layoutQuizOption1)

                    viewModel.saveQuizOption(QuizOptions.WORLD.id)
                    MainActivity.spinnerView.setSelection(1)
                }
                return true
            }
        }
        return false
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

    private fun checkRadioButton(checkedId: Int) {
        when (checkedId) {
            R.id.radio_ukr -> setLangAndStartMain(binding.radioUkr, "uk")
            R.id.radio_rus -> setLangAndStartMain(binding.radioRus, "ru")
            R.id.radio_eng -> setLangAndStartMain(binding.radioEng, "en")
        }
    }

    private fun resetAllLangRadioImageExcept(langBorderShapeException: GradientDrawable) {
        langBorderShapeList.remove(langBorderShapeException)
        for (langBorder in langBorderShapeList) {
            ValueAnimator.ofInt(255, 0).apply {
                duration = 300
                addUpdateListener {
                    langBorder.alpha = animatedValue as Int
                }
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        langBorder.setColor(ContextCompat.getColor(this@SettingsActivity, android.R.color.transparent))
                    }
                })
                start()
            }
        }
    }

    private fun changeLangRadioTitle(langRadio: RadioButton) {
        langRadio.setTypeface(null, Typeface.BOLD)
    }

    private fun changeLangRadioImageAnimation(langBorderShape: GradientDrawable) {
        langBorderShape.alpha = 0
        langBorderShape.setColor(ContextCompat.getColor(this, R.color.primary))
        ValueAnimator.ofInt(0, 255).apply {
            duration = 200
            addUpdateListener { langBorderShape.alpha = animatedValue as Int }
            start()
        }
    }

    private fun setQuizOptionToSelectedAnimation(layout: ConstraintLayout) {
        ValueAnimator.ofInt(3, 9).apply {
            duration = 200
            addUpdateListener {
                (layout.background as GradientDrawable)
                    .setStroke(animatedValue as Int, ContextCompat.getColor(this@SettingsActivity, R.color.primary))
            }
        }.start()
    }

    private fun setQuizOptionToDefaultAnimation(layout: ConstraintLayout) {
        ValueAnimator.ofInt(9, 3).apply {
            duration = 200
            addUpdateListener {
                (layout.background as GradientDrawable)
                    .setStroke(animatedValue as Int, ContextCompat.getColor(this@SettingsActivity, R.color.on_surface))
            }
        }.start()
    }

    private fun setLangAndStartMain(radioButton: RadioButton, lang: String) {
        radioButton.isChecked
        viewModel.setLang(this, lang)
        //refreshAllСatalogs(this)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loadLangForSettings(radioButton: RadioButton, langBorderShape: GradientDrawable) {
        changeLangRadioImageAnimation(langBorderShape)
        changeLangRadioTitle(radioButton)
    }

}
