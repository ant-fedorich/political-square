package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.pushRight
import javax.inject.Inject
import eltonio.projects.politicalsquare.util.TAG

class AboutActivity : DaggerAppCompatActivity() {


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