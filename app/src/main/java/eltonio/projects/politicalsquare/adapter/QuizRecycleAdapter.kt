package eltonio.projects.politicalsquare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.views.ResultListPointView
import kotlinx.android.synthetic.main.layout_result_item.view.*

class QuizRecycleAdapter(val context: Context) : RecyclerView.Adapter<QuizRecycleAdapter.QuizRecycleViewHolder>() {

    private var resultList: List<QuizResult> = emptyList()
    private lateinit var itemClickListener: OnQuizItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizRecycleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_result_item, parent, false)

        return QuizRecycleViewHolder(
            itemView,
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: QuizRecycleViewHolder, position: Int) {
        val currentItem = resultList[position]

        holder.textSavedResultDate.text = currentItem.endedAt

        holder.itemView.layout_item_container.transitionName = "transition_item_containter_$position"

        for (ideology in Ideologies.values()) {
            if (ideology.stringId == currentItem.ideologyStringId) {
                holder.textSavedResultTitle.text = ideology.titleRes.resString(context)
            }
        }

        holder.textSavedResultNumber.text = (position+1).toString()

        horStartScore = currentItem.horStartScore
        verStarScore = currentItem.verStartScore
        horResultScore = currentItem.horResultScore
        verResultScore = currentItem.verResultScore

        val myView = ResultListPointView(
            context,
            horResultScore,
            verResultScore
        )
        holder.frameQuizResultImage.addView(myView)
    }

    override fun getItemCount() = resultList.size

    fun setQuizResults(resultList: List<QuizResult>) {
        this.resultList = resultList
        notifyDataSetChanged()
    }

    fun getQuizResultAt(position: Int) = resultList[position]

    class QuizRecycleViewHolder(itemView: View, listener: OnQuizItemClickListener): RecyclerView.ViewHolder(itemView) {
        val textSavedResultDate: TextView = itemView.text_saved_result_date
        val textSavedResultTitle: TextView = itemView.text_saved_result_title
        val textSavedResultNumber: TextView = itemView.text_saved_result_number
//        val imageDeleteItem: ImageView = itemView.image_delete_item
        val frameQuizResultImage = itemView.frame_quiz_result_image

        init {
            itemView.setOnClickListener {
                if (listener != null) {
                    val position: Int = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

    interface OnQuizItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnQuizItemClickListener) { itemClickListener = listener  }

    companion object {
        var horStartScore = 0
        var verStarScore = 0
        var horResultScore = 0
        var verResultScore = 0
    }

}
