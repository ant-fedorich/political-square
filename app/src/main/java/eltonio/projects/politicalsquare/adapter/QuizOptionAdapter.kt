package eltonio.projects.politicalsquare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.LayoutSpinnerItemBinding
import eltonio.projects.politicalsquare.util.QuizOptions
import eltonio.projects.politicalsquare.util.QuizOptions.Companion.resString
import kotlinx.android.synthetic.main.layout_spinner_item.view.*

class QuizOptionAdapter(context: Context, quizOptionList: Array<QuizOptions>) : ArrayAdapter<QuizOptions>(context, 0, quizOptionList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View = initView(position, convertView, parent)
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View = initView(position, convertView, parent)

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = LayoutSpinnerItemBinding.inflate(LayoutInflater.from(context))
        val item = getItem(position)
        item?.let {
            binding.titleQuizOption.text = it.titleRes.resString(context)
            binding.textQuizOption.text = it.descRes.resString(context)
            binding.imageQuizOption.setImageResource(it.image)
        }
        return binding.root
    }
}