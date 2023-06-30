package eltonio.projects.politicalcompassquiz.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.util.AppUtil.pushRight

@AndroidEntryPoint
class AboutActivity: AppCompatActivity(R.layout.activity_about) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.about_title_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        pushRight(this)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        pushRight(this)
    }
}