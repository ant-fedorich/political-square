package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.data.AppRepository
import eltonio.projects.politicalsquare.util.pushLeft
import eltonio.projects.politicalsquare.util.showEndQuizDialogLambda
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base.view.*

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_base, null)
        layoutInflater.inflate(layoutResID, fullView.activity_content, true)
        super.setContentView(fullView)

        fullView.nav_global_view.setNavigationItemSelectedListener(this)

        // Interface
        setSupportActionBar(toolbar_global)
        val toggle = ActionBarDrawerToggle(this, activity_container, toolbar_global,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()
        activity_container.addDrawerListener(toggle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {
                if (AppRepository.Local().getQuizIsActive()) {
                    activity_container.closeDrawer(GravityCompat.START)

                    showEndQuizDialogLambda(this) {
                        if (AppRepository.Local().getMainActivityIsInFront() == false) {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }

                } else {
                    if (AppRepository.Local().getMainActivityIsInFront() == false) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
            R.id.nav_saved -> {
                startActivity(Intent(this, SavedResultsActivity::class.java))
                pushLeft(this) // info in
            }

            R.id.nav_info -> {
                startActivity(Intent(this, InfoActivity::class.java))
                pushLeft(this) // info in
            }

            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                pushLeft(this) // info in
            }

            R.id.nav_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                pushLeft(this) // info in
            }
        }
        activity_container.closeDrawer(GravityCompat.START)

        return true
    }
}

