package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.AppUtil
import javax.inject.Inject

@AndroidEntryPoint
class AboutActivity: AppCompatActivity() {
    private val appUtil = AppUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        title = getString(R.string.about_title_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        appUtil.pushRight(this)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        appUtil.pushRight(this)
    }
}