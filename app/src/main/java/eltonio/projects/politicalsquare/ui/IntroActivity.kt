package eltonio.projects.politicalsquare.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.model.ScreenItem
import eltonio.projects.politicalsquare.adapter.IntroViewPagerAdapter
import eltonio.projects.politicalsquare.databinding.ActivityIntroBinding
import eltonio.projects.politicalsquare.ui.viewmodel.IntroViewModel
import eltonio.projects.politicalsquare.util.AppUtil.fadeIn
import eltonio.projects.politicalsquare.util.AppUtil.playGif

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private val viewmodel: IntroViewModel by viewModels()
    private val binding: ActivityIntroBinding by lazy { ActivityIntroBinding.inflate(layoutInflater) }


//    var appQuestionsWithAnswers: List<QuestionWithAnswers> = emptyList()


    private lateinit var screenList: MutableList<ScreenItem>
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We have to set a lang before loading UI, cause it will take a lang by system default
        val loadedLang = viewmodel.loadLang().value as String
        viewmodel.setLang(this, loadedLang)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_intro)

        // TODO: 03/17/2021 : Get rid of this. Make it in viewmodel  with Livedata
        viewmodel.loadLang().observe(this, Observer {
            viewmodel.setLang(this, it)
        })
        viewmodel.checkIntroOpened()
        viewmodel.getIntroOpened().observe(this, Observer {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                fadeIn(this)
                finish()
            }
        })

        // TODO: 03/17/2021 : Get rid of this. Make it in viewmodel with Livedata
        viewmodel.getSplashAnimationTime().observe(this, Observer {
            viewmodel.setSplashAnimationTime(it)

        })
        viewmodel.getScreenList().observe(this@IntroActivity, Observer {
            screenList = it
            initViewPager()
        })

        // Listeners
        binding.buttonNext.setOnClickListener {
            showNextPage()
        }

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            fadeIn(this)
            finish()
        }

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeButtonsIfLastPage(tab?.position!!)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
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
    /** CUSTOM METHODS **/
    private fun showNextPage() {
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

    private fun initViewPager() {
        introViewPagerAdapter = IntroViewPagerAdapter(this, screenList)
        binding.pagerIntro.adapter = introViewPagerAdapter
        binding.pagerIntro.offscreenPageLimit = 2
        binding.tabIndicator.setupWithViewPager(binding.pagerIntro)
    }

}