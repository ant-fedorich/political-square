package eltonio.projects.politicalcompassquiz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.other.pushRight

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

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