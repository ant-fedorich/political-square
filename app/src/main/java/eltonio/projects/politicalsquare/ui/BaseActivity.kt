package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.ActivityBaseBinding
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.AppUtil
import javax.inject.Inject

// TODO: DI: Get rid of transfer repo here
@AndroidEntryPoint
open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val baseBinding: ActivityBaseBinding by lazy { ActivityBaseBinding.inflate(layoutInflater)}

    @Inject lateinit var localRepo: LocalRepository

    // FIXME: fix backstack everywhere

    fun setContentViewForBase(childView: View) {
        super.setContentView(baseBinding.root)
        baseBinding.activityContent.addView(childView)

        baseBinding.navGlobalView.setNavigationItemSelectedListener(this)

        // Interface
        setSupportActionBar(baseBinding.toolbarGlobal)
        val toggle = ActionBarDrawerToggle(this, baseBinding.activityContainer, baseBinding.toolbarGlobal,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()
        baseBinding.activityContainer.addDrawerListener(toggle)
    }

    override fun onBackPressed() {
        if (baseBinding.activityContainer.isDrawerOpen(baseBinding.navGlobalView)) {
            baseBinding.activityContainer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {
                if (localRepo.getQuizIsActive()) {
                    baseBinding.activityContainer.closeDrawer(GravityCompat.START)

                    AppUtil.showEndQuizDialogLambda(this) {
                        if (localRepo.getMainActivityIsInFront() == false) {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }

                } else {
                    if (localRepo.getMainActivityIsInFront() == false) {
                        startActivity(Intent(this, MainActivity::class.java))
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
        baseBinding.activityContainer.closeDrawer(GravityCompat.START)

        return true
    }
}

