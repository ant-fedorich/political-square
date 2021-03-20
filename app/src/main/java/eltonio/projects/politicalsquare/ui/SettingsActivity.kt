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
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.QuizOptions
import eltonio.projects.politicalsquare.ui.viewmodel.SettingsViewModel
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_base.view.*
import kotlinx.android.synthetic.main.activity_settings.*

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), View.OnTouchListener {
    private val viewModel: SettingsViewModel by viewModels()
    private val appUtil = AppUtil(this)

    //private val localRepo = AppRepository.Local()

    private lateinit var langBorderShapeUkr: GradientDrawable
    private lateinit var langBorderShapeRus: GradientDrawable
    private lateinit var langBorderShapeEng: GradientDrawable
    private lateinit var langBorderShapeList: MutableList<GradientDrawable>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings_title_actionbar)

        initAllLangBorderShapes()

        // Reset all Lang Radio Borders (to prevent a bug)
        for (langBorder in langBorderShapeList) {
            langBorder.setColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        viewModel.getLang().observe(this, Observer {
            when(it) {
                "uk" -> loadLangForSettings(radio_ukr, langBorderShapeUkr)
                "ru" -> loadLangForSettings(radio_rus, langBorderShapeRus)
                "en" -> loadLangForSettings(radio_eng, langBorderShapeEng)
            }
        })

        viewModel.getQuizOption().observe(this, Observer {
            when(it) {
                QuizOptions.WORLD.id -> {
                    setQuizOptionToSelectedAnimation(layout_quiz_option_1)
                    title_quiz_option_1.setTypeface(null, Typeface.BOLD)
                    setQuizOptionToDefaultAnimation(layout_quiz_option_2)
                }
                QuizOptions.UKRAINE.id -> {
                    setQuizOptionToSelectedAnimation(layout_quiz_option_2)
                    title_quiz_option_2.setTypeface(null, Typeface.BOLD)
                    setQuizOptionToDefaultAnimation(layout_quiz_option_1)
                }
            }
        })

        // Init listeners
        radio_group_lang.setOnCheckedChangeListener { _, checkedId ->
            viewModel.getQuizIsActiveState().observe(this, Observer {
                if (it == true) {
                    val baseActivity = layoutInflater.inflate(R.layout.activity_base, null)
                    baseActivity.activity_container.closeDrawer(GravityCompat.START)

                    viewModel.showEndQuizDialogLambda(this) {
                        checkRadioButton(checkedId)
                    }
                } else {
                    checkRadioButton(checkedId)
                }
            })
        }
        // Use onTouch for visual effects
        radio_ukr.setOnTouchListener(this)
        radio_rus.setOnTouchListener(this)
        radio_eng.setOnTouchListener(this)

        image_ukr.setOnTouchListener(this)
        image_rus.setOnTouchListener(this)
        image_eng.setOnTouchListener(this)

        card_quiz_option_1.setOnTouchListener(this)
        card_quiz_option_2.setOnTouchListener(this)
    }

    private fun initAllLangBorderShapes() {
        langBorderShapeUkr = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeRus = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeEng = ContextCompat.getDrawable(this, R.drawable.shape_lang_radio_border) as GradientDrawable

        (image_ukr.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeUkr)
        (image_rus.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeRus)
        (image_eng.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeEng)

        langBorderShapeList = mutableListOf(
            langBorderShapeUkr,
            langBorderShapeRus,
            langBorderShapeEng
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            radio_ukr -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeUkr)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeUkr)
            }
            radio_rus -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeRus)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeRus)
            }
            radio_eng -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImageAnimation(langBorderShapeEng)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeEng)
            }

            // Delegate OnTouch Event
            image_ukr -> {
                radio_ukr.dispatchTouchEvent(event)
                radio_ukr.performClick()
            }
            image_rus -> {
                radio_rus.dispatchTouchEvent(event)
                radio_rus.performClick()
            }
            image_eng -> {
                radio_eng.dispatchTouchEvent(event)
                radio_eng.performClick()
            }

            card_quiz_option_1 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    setQuizOptionToSelectedAnimation(layout_quiz_option_1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    title_quiz_option_1.setTypeface(null, Typeface.BOLD)
                    title_quiz_option_2.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefaultAnimation(layout_quiz_option_2)

                    viewModel.saveQuizOption(QuizOptions.WORLD.id)
                    MainActivity.spinnerView.setSelection(0)
                }
                return true
            }
            card_quiz_option_2 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    setQuizOptionToSelectedAnimation(layout_quiz_option_2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    title_quiz_option_2.setTypeface(null, Typeface.BOLD)
                    title_quiz_option_1.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefaultAnimation(layout_quiz_option_1)

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
        appUtil.pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        appUtil.pushRight(this) // info out
    }

    /** CUSTOM METHODS */

    private fun checkRadioButton(checkedId: Int) {
        when (checkedId) {
            R.id.radio_ukr -> setLangAndStartMain(radio_ukr, "uk")
            R.id.radio_rus -> setLangAndStartMain(radio_rus, "ru")
            R.id.radio_eng -> setLangAndStartMain(radio_eng, "en")
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
