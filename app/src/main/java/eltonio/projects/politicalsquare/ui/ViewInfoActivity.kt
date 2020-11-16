package eltonio.projects.politicalsquare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.core.content.ContextCompat
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.EXTRA_IDEOLOGY_TITLE
import eltonio.projects.politicalsquare.util.pushRight
import kotlinx.android.synthetic.main.activity_view_info.*

class ViewInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_info)

        // TODO: V
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
        // end

        // TODO: MVVM Extra to Repository
        // TODO: MVVM - to vm???
        var ideology = intent.getStringExtra(EXTRA_IDEOLOGY_TITLE)

        // TODO: VM - to vm
        when (ideology) {
            Ideologies.AUTHORITARIAN_LEFT.title -> {
                toolbar_collapsing.title = Ideologies.AUTHORITARIAN_LEFT.title
                image_ideology_info.setImageResource(R.drawable.img_info_1_autho_left)
                text_ideology_description.text = getString(R.string.desc_authoritarian_left)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.RADICAL_NATIONALISM.title -> {
                toolbar_collapsing.title = Ideologies.RADICAL_NATIONALISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_2_radical_nation)
                text_ideology_description.text = getString(R.string.desc_nationalism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.POWER_CENTRISM.title -> {
                toolbar_collapsing.title = Ideologies.POWER_CENTRISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_3_gov3)
                text_ideology_description.text = getString(R.string.desc_powercentrism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.SOCIAL_DEMOCRACY.title -> {
                toolbar_collapsing.title = Ideologies.SOCIAL_DEMOCRACY.title
                image_ideology_info.setImageResource(R.drawable.img_info_4_soc_dem)
                text_ideology_description.text = getString(R.string.desc_social_democracy)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.SOCIALISM.title -> {
                toolbar_collapsing.title = Ideologies.SOCIALISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_5_soc)
                text_ideology_description.text = getString(R.string.desc_socialism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            //
            Ideologies.AUTHORITARIAN_RIGHT.title -> {
                toolbar_collapsing.title = Ideologies.AUTHORITARIAN_RIGHT.title
                image_ideology_info.setImageResource(R.drawable.img_info_6_autho_right)
                text_ideology_description.text = getString(R.string.desc_authoritarian_right)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.RADICAL_CAPITALISM.title -> {
                toolbar_collapsing.title = Ideologies.RADICAL_CAPITALISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_7_radical_cap)
                text_ideology_description.text = getString(R.string.desc_radical_capitalism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_SmallExpanded)
            }
            Ideologies.CONSERVATISM.title -> {
                toolbar_collapsing.title = Ideologies.CONSERVATISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_8_cons)
                text_ideology_description.text = getString(R.string.desc_conservatism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            Ideologies.PROGRESSIVISM.title -> {
                toolbar_collapsing.title = Ideologies.PROGRESSIVISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_9_prog)
                text_ideology_description.text = getString(R.string.desc_progressivism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            //
            Ideologies.RIGHT_ANARCHY.title -> {
                toolbar_collapsing.title = Ideologies.RIGHT_ANARCHY.title
                image_ideology_info.setImageResource(R.drawable.img_info_10_right_anar3)
                text_ideology_description.text = getString(R.string.desc_right_anarchy)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.ANARCHY.title -> {
                toolbar_collapsing.title = Ideologies.ANARCHY.title
                image_ideology_info.setImageResource(R.drawable.img_info_11_anar)
                text_ideology_description.text = getString(R.string.desc_anarchy)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            Ideologies.LIBERALISM.title -> {
                toolbar_collapsing.title = Ideologies.LIBERALISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_12_lib)
                text_ideology_description.text = getString(R.string.desc_liberalism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            Ideologies.LIBERTARIANISM.title -> {
                toolbar_collapsing.title = Ideologies.LIBERTARIANISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_13_libertar)
                text_ideology_description.text = getString(R.string.desc_libertarianism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_LargeExpanded)
            }
            //
            Ideologies.LEFT_ANARCHY.title -> {
                toolbar_collapsing.title = Ideologies.LEFT_ANARCHY.title
                image_ideology_info.setImageResource(R.drawable.img_info_14_left_anar)
                text_ideology_description.text = getString(R.string.desc_left_anarchy)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_MediumExpanded)
            }
            Ideologies.LIBERTARIAN_SOCIALISM.title -> {
                toolbar_collapsing.title = Ideologies.LIBERTARIAN_SOCIALISM.title
                image_ideology_info.setImageResource(R.drawable.img_info_15_lib_soc)
                text_ideology_description.text = getString(R.string.desc_liberal_socialism)
                layout_collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_SmallExpanded)
            }
        }
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