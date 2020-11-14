package eltonio.projects.politicalsquare.ui

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
import eltonio.projects.politicalsquare.adapter.QuizRecycleAdapter
import eltonio.projects.politicalsquare.R
import kotlinx.android.synthetic.main.activity_saved_results.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_result_item.view.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.*
import eltonio.projects.politicalsquare.data.AppViewModel
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.util.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.*

//import com.google.android.material


class SavedResultsActivity : AppCompatActivity() {

    private lateinit var resultList: List<QuizResult>
    private lateinit var quizAdapter: QuizRecycleAdapter

    private lateinit var appViewModel: AppViewModel
    private lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {

        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_results)

        title = getString(R.string.saved_title_actionbar)

        resultList = emptyList()
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            resultList = appViewModel.getQuizResults()
            Log.w(TAG, "QuizResults in Coroutine:")
            for (item in resultList) Log.i(TAG, "Item: $item")
        }

        // For debugging
        Log.w(TAG, "QuizResults out of Coroutine:")
        for (item in resultList) Log.i(TAG, "Item: $item")

        quizAdapter = QuizRecycleAdapter()
        recycler_results_list.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
        quizAdapter.setQuizResults(resultList)
        quizAdapter.setOnItemClickListener(object :
            QuizRecycleAdapter.OnQuizItemClickListener {
            override fun onItemClick(position: Int) = goToResultDetail(position)
            // TODO: Delete onDelete
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
                    deletedResultItem = quizAdapter.getQuizResultAt(position)
                    appViewModel.deleteQuizResult(deletedResultItem)
                    runBlocking{
                        delay(200)
                        resultList = appViewModel.getQuizResults()
                    }
                    quizAdapter.setQuizResults(resultList)

                    for (ideo in Ideologies.values()) {
                        if (ideo.stringId == deletedResultItem.ideologyStringId) {
                            ideologyTitle = ideo.title
                        }
                    }

                    // Add Snackbar
                    Snackbar.make(recycler_results_list, "$ideologyTitle удален", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            appViewModel.addQuizResult(deletedResultItem)
                            runBlocking {
                                delay(200)
                                resultList = appViewModel.getQuizResults()
                            }
                            quizAdapter.setQuizResults(resultList)
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
        finish()
        pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this) //info out
    }

    /** CUSTOM METHODS */

    private fun goToResultDetail(position: Int) {
        val itemView = recycler_results_list.layoutManager?.findViewByPosition(position) as ConstraintLayout
        val itemContainerTransitionName = itemView.layout_item_container.transitionName
        val titleTransitionName = itemView.text_saved_result_title.transitionName
        val dateTransitionName = itemView.text_saved_result_date.transitionName
        val imageTransitionName = itemView.image_compass_saved_result.transitionName

        // TODO: MVVM Extra to Repository
        var intent = Intent(this, SavedResultDetailActivity::class.java).apply {
            putExtra(EXTRA_IDEOLOGY_ID, resultList[position].ideologyStringId)
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

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, itemView.layout_item_container, itemContainerTransitionName)

        startActivity(intent, options.toBundle())
    }

    private fun removeItem(position: Int) {
        appViewModel.deleteQuizResult(resultList[position])
        runBlocking {
            resultList = appViewModel.getQuizResults()
        }
        quizAdapter.notifyItemRemoved(position)
    }

}

