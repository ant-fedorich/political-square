package eltonio.projects.politicalsquare.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.ScreenItem
import kotlinx.android.synthetic.main.layout_screen_item.view.*

class IntroViewPagerAdapter(var context: Context, var screenList: MutableList<ScreenItem>) : PagerAdapter(){

    var currentView: View? = null

    override fun getCount(): Int  = screenList.size

    override fun isViewFromObject(view: View, o: Any): Boolean = o == view

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) = container.removeView(o as View)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val screenLayout = inflater.inflate(R.layout.layout_screen_item, null)
        val title = screenLayout.text_intro_title
        val image = screenLayout.image_intro_animation
        val imageBackground = screenLayout.image_intro_background

        // Tags to have an access to views
        image.tag = "tag_image_intro_animation_$position"
        imageBackground.tag = "tag_image_intro_background_$position"

        title.text = screenList[position].title
        // Set a background image (the first image of GIF) for scrolling pager
        imageBackground.setImageResource(screenList[position].screenImage)

        // For the first screen, load animation immediately
        if (position == 0) {
            imageBackground.visibility = View.INVISIBLE
            playGif(screenList[position].screenImage, image)
        }

        container.addView(screenLayout)
        return  screenLayout
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, o: Any) {
        currentView = o as View
    }
}