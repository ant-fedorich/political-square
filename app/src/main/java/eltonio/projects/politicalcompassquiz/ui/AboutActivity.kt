package eltonio.projects.politicalcompassquiz.ui

import android.content.Intent
import android.drm.DrmStore.Action
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.databinding.ActivityAboutBinding
import eltonio.projects.politicalcompassquiz.util.AppUtil.pushRight

@AndroidEntryPoint
class AboutActivity: AppCompatActivity() {
    private val binding: ActivityAboutBinding by lazy {ActivityAboutBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.about_title_actionbar)
//        setSupportActionBar(binding.toolbarViewInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.textPrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://sites.google.com/view/political-square".toUri())
            startActivity(intent)
        }

        setContentView(binding.root)
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