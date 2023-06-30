package eltonio.projects.politicalcompassquiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import eltonio.projects.politicalcompassquiz.databinding.LayoutScreenItemBinding
import eltonio.projects.politicalcompassquiz.model.ScreenItem
import eltonio.projects.politicalcompassquiz.util.AppUtil.playGif

class IntroViewPagerAdapter(var context: Context, var screenList: MutableList<ScreenItem>) : PagerAdapter(){
    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val binding = LayoutScreenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.apply {
            imageIntroAnimation.tag = "tag_image_intro_animation_$position"
            imageIntroBackground.tag = "tag_image_intro_background_$position"

            textIntroTitle.text = screenList[position].title

            // Set a background image (the first image of GIF) for scrolling pager
            imageIntroBackground.setImageResource(screenList[position].screenImage)

            // For the first screen, load animation immediately
            if (position == 0) {
                imageIntroBackground.visibility = View.INVISIBLE
                playGif(context, screenList[position].screenImage, imageIntroAnimation)
            }
        }
        parent.addView(binding.root)
        return  binding.root
    }

    override fun destroyItem(parent: ViewGroup, position: Int, o: Any) = parent.removeView(o as View)
    override fun getCount(): Int  = screenList.size
    override fun isViewFromObject(view: View, o: Any): Boolean = o == view
}