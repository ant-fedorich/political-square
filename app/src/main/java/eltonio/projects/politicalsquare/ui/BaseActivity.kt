package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.AppUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private val baseBinding: ActivityBaseBinding by lazy { ActivityBaseBinding.inflate(layoutInflater)}

    @Inject lateinit var localRepo: LocalRepository

    fun setContentViewForBase(childView: View) {
        super.setContentView(baseBinding.root)
        baseBinding.activityContent.addView(childView)

        baseBinding.navGlobalView.setNavigationItemSelectedListener { i ->  onNavigationItemSelected(i) }
        // Interface
        setSupportActionBar(baseBinding.toolbarGlobal)
        val toggle = ActionBarDrawerToggle(this, baseBinding.baseContainer, baseBinding.toolbarGlobal,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()
        baseBinding.baseContainer.addDrawerListener(toggle)
    }

    override fun onBackPressed() {
        if (baseBinding.baseContainer.isDrawerOpen(baseBinding.navGlobalView)) {
            baseBinding.baseContainer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> MainScope().launch {
                    if (localRepo.getQuizIsActive()) {
                        baseBinding.baseContainer.closeDrawer(GravityCompat.START)

                        AppUtil.showEndQuizDialogLambda(this@BaseActivity) {
                            MainScope().launch{
                                if (localRepo.getMainActivityIsInFront() == false) {
                                    val intent = Intent(this@BaseActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                    } else {
                        if (localRepo.getMainActivityIsInFront() == false) {
                            startActivity(Intent(this@BaseActivity, MainActivity::class.java))
                            finish()
                        }
                    }
            }
            R.id.nav_saved -> {
                startActivity(Intent(this, SavedResultsActivity::class.java))
                AppUtil.pushLeft(this) // info in
            }

            R.id.nav_info -> {
                startActivity(Intent(this, InfoActivity::class.java))
                AppUtil.pushLeft(this) // info in
            }

            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                AppUtil.pushLeft(this) // info in
            }

            R.id.nav_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                AppUtil.pushLeft(this) // info in
            }
        }
        baseBinding.baseContainer.closeDrawer(GravityCompat.START)

        return true
    }

    fun startContainerFadeAnimation() { // Is needed to load Splash Activity
        baseBinding.baseContainer.alpha = 0f
        baseBinding.baseContainer.apply {
            animate().apply {
                duration = 700
                alpha(1f)
                interpolator = AccelerateInterpolator(3f)
            }.start()
        }
    }
}

