package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import dagger.android.support.DaggerAppCompatActivity
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.ui.viewmodel.ViewInfoViewModel
import eltonio.projects.politicalsquare.ui.viewmodel.ViewModelProviderFactory
import eltonio.projects.politicalsquare.util.EXTRA_IDEOLOGY_TITLE
import eltonio.projects.politicalsquare.util.pushRight
import kotlinx.android.synthetic.main.activity_view_info.*
import javax.inject.Inject

class ViewInfoActivity : DaggerAppCompatActivity() {
    private lateinit var viewModel: ViewInfoViewModel
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_info)
        viewModel = ViewModelProviders.of(this, providerFactory)[ViewInfoViewModel::class.java]

        setSupportActionBar(toolbar_collapsing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set color, because is transparent by style
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)

        layout_collapsing_toolbar.apply {
            setContentScrimColor(resources.getColor(R.color.collapsing_image_сollapsed))
            setCollapsedTitleTextColor(resources.getColor(R.color.on_primary_bright))
            setExpandedTitleColor(resources.getColor(R.color.on_primary_bright))
        }

        text_ideology_description.movementMethod = ScrollingMovementMethod()

        val ideology = intent.getStringExtra(EXTRA_IDEOLOGY_TITLE)

        viewModel.updateData(ideology)
        viewModel.getIdeology().observe(this, Observer {
            toolbar_collapsing.title = it
        })
        viewModel.getImageId().observe(this, Observer {
            image_ideology_info.setImageResource(it)
        })
        viewModel.getDescriptionId().observe(this, Observer {
            text_ideology_description.text = getString(it)
        })
        viewModel.getStyleId().observe(this, Observer {
            layout_collapsing_toolbar.setExpandedTitleTextAppearance(it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        pushRight(this)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this)
    }

}