package eltonio.projects.politicalsquare.activities

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.transition.ChangeBounds
//import android.transition.Fade
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eltonio.projects.politicalsquare.other.*
import eltonio.projects.politicalsquare.other.QuizDbHelper
import eltonio.projects.politicalsquare.other.QuizRecycleAdapter
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.R
import kotlinx.android.synthetic.main.activity_saved_results.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_result_item.view.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.*
import eltonio.projects.politicalsquare.models.Ideologies
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

//import com.google.android.material


class SavedResultsActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDbHelper
    private var resultList = mutableListOf<QuizResult>()
    private lateinit var quizAdapter: QuizRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_results)

        title = getString(R.string.saved_title_actionbar)

        dbHelper = QuizDbHelper(this)
        resultList = dbHelper.getQuizResults()

        // For debugging
        for (item in resultList) Log.d(TAG, "Item: $item")

        quizAdapter = QuizRecycleAdapter(resultList)
        recycler_results_list.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
        quizAdapter.setOnItemClickListener(object:
            QuizRecycleAdapter.OnQuizItemClickListener {
            override fun onItemClick(position: Int) = goToResultDetail(position)
            override fun onDeleteClick(position: Int) = removeItem(position)
        })

        // Add swiping actions
        var swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedResultItem: QuizResult
                var ideologyTitle = "NONE"

                if (direction == ItemTouchHelper.LEFT) {
                    deletedResultItem = resultList[position]

                    for (ideo in Ideologies.values()) {
                        if (ideo.stringId == deletedResultItem.ideologyId) {
                            ideologyTitle = ideo.title
                        }
                    }
                    resultList.removeAt(position)

                    // Add Snackbar
                    quizAdapter.notifyItemRemoved(position)
                    Snackbar.make(recycler_results_list, "$ideologyTitle удален", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            resultList.add(position, deletedResultItem)
                            quizAdapter.notifyItemInserted(position)
                        }
                        .show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@SavedResultsActivity, R.color.deleting))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recycler_results_list)
    }

    override fun onSupportNavigateUp(): Boolean {
//        super.onBackPressed()
        finish()
        pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finish()
        pushRight(this) //info out
    }

    /** CUSTOM METHODS */

    private fun goToResultDetail(position: Int) {
        val itemView = recycler_results_list.layoutManager?.findViewByPosition(position) as ConstraintLayout
        val itemContainerTransitionName = itemView.layout_item_container.transitionName
        val titleTransitionName = itemView.text_saved_result_title.transitionName
        val dateTransitionName = itemView.text_saved_result_date.transitionName
        val imageTransitionName = itemView.image_compass_saved_result.transitionName

//        val id = itemView.title_result_detail as TextView

        var intent = Intent(this, SavedResultDetailActivity::class.java).apply {
            putExtra(EXTRA_IDEOLOGY_ID, resultList[position].ideologyId)
            putExtra(EXTRA_QUIZ_ID, resultList[position].quizId)
            putExtra(EXTRA_ENDED_AT, resultList[position].endedAt)
            putExtra(EXTRA_HOR_START_SCORE, resultList[position].horStartScore)
            putExtra(EXTRA_VER_START_SCORE, resultList[position].verStartScore)
            putExtra(EXTRA_HOR_RESULT_SCORE, resultList[position].horResultScore)
            putExtra(EXTRA_VER_RESULT_SCORE, resultList[position].verResultScore)
            putExtra(EXTRA_TITLE_TRANSITION_NAME, titleTransitionName)
            putExtra(EXTRA_DATE_TRANSITION_NAME, dateTransitionName)
            putExtra(EXTRA_IMAGE_TRANSITION_NAME, imageTransitionName)
            putExtra(EXTRA_ITEM_CONTAINER_TRANSITION_NAME, itemContainerTransitionName)
        }
//        val pair1: Pair<View, String> = Pair.create(itemView.text_saved_result_title, titleTransitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, itemView.layout_item_container, itemContainerTransitionName)

        startActivity(intent, options.toBundle())
        //pushLeft(this) // info in //
    }

    private fun removeItem(position: Int) {
        dbHelper.deleteQuizResult(resultList[position].id)
        resultList.removeAt(position)
        quizAdapter.notifyItemRemoved(position)
    }

}

