package eltonio.projects.politicalsquare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.util.QuizOptions.Companion.resString

class QuizOptionAdapter(context: Context, quizOptionList: Array<QuizOptions>) : ArrayAdapter<QuizOptions>(context, 0, quizOptionList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_spinner_item, parent, false)
        }
        val title = itemView?.findViewById<TextView>(R.id.title_quiz_option)
        val desc = itemView?.findViewById<TextView>(R.id.text_quiz_option)
        val image = itemView?.findViewById<ImageView>(R.id.image_quiz_option)

        val currentItem = getItem(position)
        if (currentItem != null) {
            title?.text = currentItem.titleRes.resString(context)
            desc?.text = currentItem.descRes.resString(context)
            image?.setImageResource(currentItem.image)
        }
        return itemView!! // only that way, with non-null assertion
    }
}