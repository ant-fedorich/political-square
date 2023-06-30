package eltonio.projects.politicalcompassquiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eltonio.projects.politicalcompassquiz.databinding.LayoutSpinnerItemBinding
import eltonio.projects.politicalcompassquiz.util.QuizOptions
import eltonio.projects.politicalcompassquiz.util.QuizOptions.Companion.resString

class QuizOptionAdapter(context: Context, quizOptionList: Array<QuizOptions>) : ArrayAdapter<QuizOptions>(context, 0, quizOptionList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View = initView(position, convertView, parent)
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View = initView(position, convertView, parent)

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = LayoutSpinnerItemBinding.inflate(LayoutInflater.from(context))
        val item = getItem(position)
        item?.let {
            binding.titleQuizOption.text = it.titleResId.resString(context)
            binding.textQuizOption.text = it.descResId.resString(context)
            binding.imageQuizOption.setImageResource(it.imageResId)
        }
        return binding.root
    }
}