package eltonio.projects.politicalsquare.ui

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
import eltonio.projects.politicalsquare.adapter.IntroViewPagerAdapter
import eltonio.projects.politicalsquare.data.FirebaseRepository
import eltonio.projects.politicalsquare.data.SharedPrefRepository
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    // TEMP
    private val prefRepo = SharedPrefRepository()

    // TODO: MVVM to VM
    private lateinit var screenList: MutableList<ScreenItem>
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private var position = 0
    // end MVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // TODO: MVVM to VM
        // Skip Intro if it was opened before
        if (prefRepo.getIntroOpened()) {
            Log.d(TAG, "Intro was already opened")
            var loadedLang = prefRepo.loadLang()
            prefRepo.setLang(this, loadedLang)

            // Set short animation without Intro
            splashAnimationTime = 600L

            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        } else {
            // Set long animation after Intro
            splashAnimationTime = 1200L
        }
        // End MVVM
        var loadedLang = prefRepo.loadLang()
        prefRepo.setLang(this, loadedLang)

        setContentView(R.layout.activity_intro)

        screenList = prefRepo.getViewPagerScreenList()

        // TODO: MVVM to VM
        // Set ViewPager
        introViewPagerAdapter = IntroViewPagerAdapter(this, screenList)
        pager_intro.adapter = introViewPagerAdapter
        pager_intro.offscreenPageLimit = 2
        tab_indicator.setupWithViewPager(pager_intro)
        // end MVVM

        // Listeners
        button_next.setOnClickListener {
            // TODO: MVVM to VM
            position = pager_intro.currentItem
            if (position < screenList.size) {
                position++
                pager_intro.currentItem = position
            }
            if (position == screenList.size-1) {
                button_next.visibility = View.INVISIBLE
                button_get_started.visibility = View.VISIBLE
            }
            // end MVVM
        }

        button_get_started.setOnClickListener {
            prefRepo.putIntroOpened()
            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        }

        tab_indicator.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // TODO: MVVM to VM
                if (tab?.position!! < screenList.size) {

                    button_next.visibility = View.VISIBLE
                    button_get_started.visibility = View.INVISIBLE
                }
                if (tab.position == screenList.size-1) {
                    button_next.visibility = View.INVISIBLE
                    button_get_started.visibility = View.VISIBLE
                }
                // end MVVM
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        pager_intro.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            // TODO: MVVM to VM
            override fun onPageSelected(position: Int) {
                val currentImageItem =
                    pager_intro.findViewWithTag<ImageView>("tag_image_intro_animation_$position")
                val currentBackgroundItem =
                    pager_intro.findViewWithTag<ImageView>("tag_image_intro_background_$position")

                // We need animation to avoid jerking of an image
                // TODO: create method
                currentBackgroundItem.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .withEndAction {
                        currentBackgroundItem.visibility = View.INVISIBLE
                    }
                    .start()
                playGif(screenList[position].screenImage, currentImageItem)
            }
            // end MVVM
        })
    }

    /** CUSTOM METHODS **/
}