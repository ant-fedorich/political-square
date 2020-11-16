package eltonio.projects.politicalsquare.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.ScreenItem
import eltonio.projects.politicalsquare.adapter.IntroViewPagerAdapter
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.util.*
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    private lateinit var viewModel: IntroViewModel
    private var localRepo = AppRepository.Local()

    private lateinit var screenList: MutableList<ScreenItem>
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_intro)
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)

        viewModel.loadLang().observe(this, Observer {
            localRepo.setLang(this, it)
        })
        viewModel.checkIntroOpened()
        viewModel.getIntroOpened().observe(this, Observer {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                fadeIn(this)
                finish()
            }
        })
        viewModel.getSplashAnimationTime().observe(this, Observer {
            splashAnimationTime = it
        })
        viewModel.getScreenList().observe(this@IntroActivity, Observer {
            screenList = it
            initViewPager()
        })

        // Listeners
        button_next.setOnClickListener {
            showNextPage()
        }

        button_get_started.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        }

        tab_indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeButtonsIfLastPage(tab?.position!!)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        pager_intro.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val currentImageItem =
                    pager_intro.findViewWithTag<ImageView>("tag_image_intro_animation_$position")
                val currentBackgroundItem =
                    pager_intro.findViewWithTag<ImageView>("tag_image_intro_background_$position")

                startPlayingPager(position, currentImageItem, currentBackgroundItem)
            }
        })
    }
    /** CUSTOM METHODS **/
    private fun showNextPage() {
        var position = pager_intro.currentItem
        if (position < screenList.size) {
            position++
            pager_intro.currentItem = position
        }
        if (position == screenList.size-1) {
            button_next.visibility = View.INVISIBLE
            button_get_started.visibility = View.VISIBLE
        }
    }

    private fun changeButtonsIfLastPage(tabPosition: Int) {
        if (tabPosition < screenList.size) {
            button_next.visibility = View.VISIBLE
            button_get_started.visibility = View.INVISIBLE
        }
        if (tabPosition == screenList.size - 1) {
            button_next.visibility = View.INVISIBLE
            button_get_started.visibility = View.VISIBLE
        }
    }

    private fun startPlayingPager(
        position: Int,
        currentImageItem: ImageView,
        currentBackgroundItem: ImageView
    ) {
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

    private fun initViewPager() {
        introViewPagerAdapter = IntroViewPagerAdapter(this, screenList)
        pager_intro.adapter = introViewPagerAdapter
        pager_intro.offscreenPageLimit = 2
        tab_indicator.setupWithViewPager(pager_intro)
    }

}