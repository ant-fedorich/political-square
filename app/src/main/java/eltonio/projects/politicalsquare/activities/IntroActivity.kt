package eltonio.projects.politicalsquare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.ScreenItem
import eltonio.projects.politicalsquare.other.IntroViewPagerAdapter
import eltonio.projects.politicalsquare.other.fadeIn
import eltonio.projects.politicalsquare.other.*
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var screenList: MutableList<ScreenItem>
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Skip Intro if it was opened before
        if (loadIsIntroOpened()) {
            Log.d(TAG, "Intro was already opened")
            // Load language
            var loadedLang = LocaleHelper.loadLang(this)
            LocaleHelper.setLang(this, loadedLang)

            // Set short animation without Intro
            splashAnimationTime = 600L

            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        } else {
            // Set long animation after Intro
            splashAnimationTime = 1200L
        }

        // Load language
        var loadedLang = LocaleHelper.loadLang(this)
        LocaleHelper.setLang(this, loadedLang)

        setContentView(R.layout.activity_intro)

        screenList = mutableListOf(
            ScreenItem(getString(R.string.intro_title_1), R.drawable.gif_intro_1),
            ScreenItem(getString(R.string.intro_title_2), R.drawable.gif_intro_2),
            ScreenItem(getString(R.string.intro_title_3), R.drawable.gif_intro_3)
        )

        // Set ViewPager
        introViewPagerAdapter = IntroViewPagerAdapter(this, screenList)
        pager_intro.adapter = introViewPagerAdapter
        pager_intro.offscreenPageLimit = 2
        tab_indicator.setupWithViewPager(pager_intro)

        // Listeners
        button_next.setOnClickListener {
            position = pager_intro.currentItem
            if (position < screenList.size) {
                position++
                pager_intro.currentItem = position
            }
            if (position == screenList.size-1) {
                button_next.visibility = View.INVISIBLE
                button_get_started.visibility = View.VISIBLE
            }
        }

        button_get_started.setOnClickListener {
            saveIsIntroOpened()
            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        }

        tab_indicator.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position!! < screenList.size) {
                    button_next.visibility = View.VISIBLE
                    button_get_started.visibility = View.INVISIBLE
                }
                if (tab.position == screenList.size-1) {
                    button_next.visibility = View.INVISIBLE
                    button_get_started.visibility = View.VISIBLE
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        pager_intro.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val currentImageItem = pager_intro.findViewWithTag<ImageView>("tag_image_intro_animation_$position")
                val currentBackgroundItem = pager_intro.findViewWithTag<ImageView>("tag_image_intro_background_$position")

                // We need animation to avoid jerking of an image
                currentBackgroundItem.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .withEndAction {
                        currentBackgroundItem.visibility = View.INVISIBLE
                    }
                    .start()
                playGif(screenList[position].screenImage, currentImageItem)
            }
        })
    }

    /** CUSTOM METHODS **/
}