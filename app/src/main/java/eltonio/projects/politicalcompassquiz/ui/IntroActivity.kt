package eltonio.projects.politicalcompassquiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.model.ScreenItem
import eltonio.projects.politicalcompassquiz.adapter.IntroViewPagerAdapter
import eltonio.projects.politicalcompassquiz.databinding.ActivityIntroBinding
import eltonio.projects.politicalcompassquiz.ui.viewmodel.IntroViewModel
import eltonio.projects.politicalcompassquiz.util.AppUtil.defaultOnTabSelectedListener
import eltonio.projects.politicalcompassquiz.util.AppUtil.fadeIn
import eltonio.projects.politicalcompassquiz.util.AppUtil.playGif

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private val viewmodel: IntroViewModel by viewModels()
    private val binding: ActivityIntroBinding by lazy { ActivityIntroBinding.inflate(layoutInflater) }

    private lateinit var screenList: MutableList<ScreenItem>
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUISettings()
        subscribeToObservers()

        viewmodel.checkIntroOpened()


        // Listeners
        binding.buttonNext.setOnClickListener {
            var position = binding.pagerIntro.currentItem
            if (position < screenList.size) {
                position++
                binding.pagerIntro.currentItem = position
            }
            if (position == screenList.size-1) {
                binding.buttonNext.visibility = View.INVISIBLE
                binding.buttonGetStarted.visibility = View.VISIBLE
            }
        }

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        }

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener by defaultOnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeButtonsIfLastPage(tab?.position!!)
            }
        })

        binding.pagerIntro.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val currentImageItem =
                    binding.pagerIntro.findViewWithTag<ImageView>("tag_image_intro_animation_$position")
                val currentBackgroundItem =
                    binding.pagerIntro.findViewWithTag<ImageView>("tag_image_intro_background_$position")

                startPlayingPager(position, currentImageItem, currentBackgroundItem)
            }
        })

        setContentView(binding.root)
    }

    /*****************************/

    private fun subscribeToObservers() {
        viewmodel.lang.observe(this) {
            viewmodel.setupAndSaveLang(this, it)
        }
        viewmodel.introOpenedEvent.observe(this) {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                fadeIn(this)
                finish()
            }
        }
        viewmodel.splashAnimationTime.observe(this) {
            viewmodel.setSplashAnimationTime(it)
        }
        viewmodel.screenList.observe(this) {
            screenList = it
            setupViewPager()
        }
    }

    private fun setupUISettings() {
        // We have to set a savedLang before loading UI, cause it will take a savedLang by system default
        viewmodel.loadLang()
        viewmodel.loadScreenList()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun changeButtonsIfLastPage(tabPosition: Int) {
        if (tabPosition < screenList.size) {
            binding.buttonNext.visibility = View.VISIBLE
            binding.buttonGetStarted.visibility = View.INVISIBLE
        }
        if (tabPosition == screenList.size - 1) {
            binding.buttonNext.visibility = View.INVISIBLE
            binding.buttonGetStarted.visibility = View.VISIBLE
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
        playGif(this, screenList[position].screenImage, currentImageItem)
    }

    private fun setupViewPager() {
        introViewPagerAdapter = IntroViewPagerAdapter(this, screenList)
        binding.pagerIntro.adapter = introViewPagerAdapter
        binding.pagerIntro.offscreenPageLimit = 2
        binding.tabIndicator.setupWithViewPager(binding.pagerIntro)
    }

}