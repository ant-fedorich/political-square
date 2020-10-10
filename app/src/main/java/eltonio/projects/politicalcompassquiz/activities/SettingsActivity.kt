package eltonio.projects.politicalcompassquiz.activities

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.constraintlayout.widget.ConstraintLayout
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.models.Ideologies
import eltonio.projects.politicalcompassquiz.models.QuizOptions
import eltonio.projects.politicalcompassquiz.other.*
import kotlinx.android.synthetic.main.activity_base.view.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var langBorderShapeUkr: GradientDrawable
    private lateinit var langBorderShapeRus: GradientDrawable
    private lateinit var langBorderShapeEng: GradientDrawable
    private lateinit var langBorderShapeList: MutableList<GradientDrawable>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings_title_actionbar)

        // Get all language border shapes
        langBorderShapeUkr = getDrawable(R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeRus = getDrawable(R.drawable.shape_lang_radio_border) as GradientDrawable
        langBorderShapeEng = getDrawable(R.drawable.shape_lang_radio_border) as GradientDrawable

        (image_ukr.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeUkr)
        (image_rus.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeRus)
        (image_eng.background as LayerDrawable).setDrawableByLayerId(R.id.shape_lang_radio_border, langBorderShapeEng)

        langBorderShapeList = mutableListOf(
            langBorderShapeUkr,
            langBorderShapeRus,
            langBorderShapeEng
        )

        // Reset all Lang Radio Borders (to prevent a bug)
        for (langBorder in langBorderShapeList) {
            langBorder.setColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        // Load language for Settings
        when(LocaleHelper.loadLang(this)) {
            "uk" -> {
                radio_ukr.isChecked = true
                changeLangRadioImage(langBorderShapeUkr)
                changeLangRadioTitle(radio_ukr)
            }
            "ru" -> {
                radio_rus.isChecked = true
                changeLangRadioImage(langBorderShapeRus)
                changeLangRadioTitle(radio_rus)
            }
            "en" -> {
                radio_eng.isChecked = true
                changeLangRadioImage(langBorderShapeEng)
                changeLangRadioTitle(radio_eng)
            }
        }

        // Load Quiz option
        when(QuizOptionHelper.loadQuizOption(this)) {
            QuizOptions.WORLD.id -> {
                setQuizOptionToSelected(layout_quiz_option_1)
                title_quiz_option_1.setTypeface(null, Typeface.BOLD)
                setQuizOptionToDefault(layout_quiz_option_2)
            }
            QuizOptions.UKRAINE.id -> {
                setQuizOptionToSelected(layout_quiz_option_2)
                title_quiz_option_2.setTypeface(null, Typeface.BOLD)
                setQuizOptionToDefault(layout_quiz_option_1)
            }
        }

        // Do actions when changing Radio
        radio_group_lang.setOnCheckedChangeListener { _, checkedId ->
            if (quizIsActive) {
                val baseActivity = layoutInflater.inflate(R.layout.activity_base, null)
                baseActivity.activity_container.closeDrawer(GravityCompat.START)

                showEndQuizDialogLambda(this) {
                    checkRadioButton(checkedId)
                }
            } else {
                checkRadioButton(checkedId)
            }
        }

        // Init listeners
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            radio_ukr -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImage(langBorderShapeUkr)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeUkr)
            }
            radio_rus -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImage(langBorderShapeRus)
                if (event?.action == MotionEvent.ACTION_UP)
                    resetAllLangRadioImageExcept(langBorderShapeRus)
            }
            radio_eng -> {
                if (event?.action == MotionEvent.ACTION_DOWN)
                    changeLangRadioImage(langBorderShapeEng)
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
                    setQuizOptionToSelected(layout_quiz_option_1)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    title_quiz_option_1.setTypeface(null, Typeface.BOLD)
                    title_quiz_option_2.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefault(layout_quiz_option_2)

                    QuizOptionHelper.saveQuizOption(QuizOptions.WORLD.id)
                    MainActivity.spinnerView.setSelection(0)
                }
                return true
            }
            card_quiz_option_2 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    setQuizOptionToSelected(layout_quiz_option_2)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    title_quiz_option_2.setTypeface(null, Typeface.BOLD)
                    title_quiz_option_1.setTypeface(null, Typeface.NORMAL)
                    setQuizOptionToDefault(layout_quiz_option_1)

                    QuizOptionHelper.saveQuizOption(QuizOptions.UKRAINE.id)
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
            R.id.radio_ukr -> {
                radio_ukr.isChecked
                LocaleHelper.setLang(this, "uk")
                Ideologies.refreshAll(this)
                QuizOptions.refreshAll(this)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.radio_rus -> {
                radio_rus.isChecked
                LocaleHelper.setLang(this,"ru")
                Ideologies.refreshAll(this)
                QuizOptions.refreshAll(this)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.radio_eng -> {
                radio_eng.isChecked
                LocaleHelper.setLang(this,"en")
                Ideologies.refreshAll(this)
                QuizOptions.refreshAll(this)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
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

    private fun changeLangRadioImage(langBorderShape: GradientDrawable) {
        langBorderShape.alpha = 0
        langBorderShape?.setColor(ContextCompat.getColor(this, R.color.primary))
        ValueAnimator.ofInt(0, 255).apply {
            duration = 200
            addUpdateListener { langBorderShape.alpha = animatedValue as Int }
            start()
        }
    }

    private fun setQuizOptionToSelected(layout: ConstraintLayout) {
        ValueAnimator.ofInt(3, 9).apply {
            duration = 200
            addUpdateListener {
                (layout.background as GradientDrawable)
                    .setStroke(animatedValue as Int, ContextCompat.getColor(this@SettingsActivity, R.color.primary))
            }
        }.start()
    }

    private fun setQuizOptionToDefault(layout: ConstraintLayout) {
        ValueAnimator.ofInt(9, 3).apply {
            duration = 200
            addUpdateListener {
                (layout.background as GradientDrawable)
                    .setStroke(animatedValue as Int, ContextCompat.getColor(this@SettingsActivity, R.color.on_surface))
            }
        }.start()
    }

}
