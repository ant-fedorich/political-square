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
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.databinding.ActivitySettingsBinding
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.ui.viewmodel.SettingsViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import eltonio.projects.politicalsquare.util.AppUtil.showEndQuizDialogLambda

@SuppressLint("ClickableViewAccessibility")

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val viewmodel: SettingsViewModel by viewModels()
    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private lateinit var langBorderShapeUkr: GradientDrawable
    private lateinit var langBorderShapeRus: GradientDrawable
    private lateinit var langBorderShapeEng: GradientDrawable
    private lateinit var langBorderShapeList: MutableList<GradientDrawable>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        title = getString(R.string.settings_title_actionbar)

        setupAllLangBorderShapes()

        // Setup listeners
        viewmodel.loadQuizIsActiveState()
        binding.radioGroupLang.setOnCheckedChangeListener { _, checkedId ->
            viewmodel.quizIsActiveState.observe(this) {
                if (it == true) {
                    val baseBinding = ActivityBaseBinding.inflate(layoutInflater)
                    baseBinding.baseContainer.closeDrawer(GravityCompat.START)

                    showEndQuizDialogLambda(this) {
                        checkRadioButton(checkedId)
                    }
                } else {
                    checkRadioButton(checkedId)
                }
            }
        }
        binding.radioUkr.setOnTouchListener { v, e -> onTouchItem(v, e) }
        binding.radioRus.setOnTouchListener { v, e -> onTouchItem(v, e) }
        binding.radioEng.setOnTouchListener { v, e -> onTouchItem(v, e) }

        binding.imageUkr.setOnTouchListener { v, e -> onTouchItem(v, e) }
        binding.imageRus.setOnTouchListener { v, e -> onTouchItem(v, e) }
        binding.imageEng.setOnTouchListener { v, e -> onTouchItem(v, e) }

        binding.cardQuizOption1.setOnTouchListener { v, e -> onTouchItem(v, e) }
        binding.cardQuizOption2.setOnTouchListener { v, e -> onTouchItem(v, e) }

        setContentView(binding.root)
    }

    private fun subscribeToObservers() {
        viewmodel.loadSavedLang()
        viewmodel.savedLang.observe(this) {
            when (it) {
                LANG_UK -> loadLangForSettings(binding.radioUkr, langBorderShapeUkr)
                LANG_RU -> loadLangForSettings(binding.radioRus, langBorderShapeRus)
                LANG_EN -> loadLangForSettings(binding.radioEng, langBorderShapeEng)
            }
        }

        viewmodel.loadQuizOption()
        viewmodel.quizOption.observe(this) {
            when (it) {
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
        }
    }

    private fun setupAllLangBorderShapes() {
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

        // Reset all Lang Radio Borders (to prevent a bug)
        for (langBorder in langBorderShapeList) {
            langBorder.setColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
    }

    private fun onTouchItem(v: View?, event: MotionEvent?): Boolean {
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

                    viewmodel.saveQuizOption(QuizOptions.WORLD.id)
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

                    viewmodel.saveQuizOption(QuizOptions.UKRAINE.id)
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
            R.id.radio_ukr -> setLangAndStartMain(binding.radioUkr, LANG_UK)
            R.id.radio_rus -> setLangAndStartMain(binding.radioRus, LANG_RU)
            R.id.radio_eng -> setLangAndStartMain(binding.radioEng, LANG_EN)
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
        viewmodel.setupAndSaveLang(this, lang)
        //refreshAllСatalogs(this)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loadLangForSettings(radioButton: RadioButton, langBorderShape: GradientDrawable) {
        changeLangRadioImageAnimation(langBorderShape)
        changeLangRadioTitle(radioButton)
    }

}
