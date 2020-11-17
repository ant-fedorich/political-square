package eltonio.projects.politicalsquare.ui

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eltonio.projects.politicalsquare.adapter.QuizRecycleAdapter
import eltonio.projects.politicalsquare.R
import kotlinx.android.synthetic.main.activity_saved_results.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_result_item.view.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.*
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizResult
import eltonio.projects.politicalsquare.ui.viewmodel.SaveResultViewModel
import eltonio.projects.politicalsquare.util.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class SavedResultsActivity : AppCompatActivity() {
    private lateinit var viewModel: SaveResultViewModel

    private lateinit var resultList: List<QuizResult>
    private lateinit var quizAdapter: QuizRecycleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_results)
        viewModel = ViewModelProvider(this).get(SaveResultViewModel::class.java)

        title = getString(R.string.saved_title_actionbar)

        resultList = emptyList()
        viewModel.getResultList().observe(this, Observer {
            // TODO: How to get resultList imidiatly? Not in Observer or onResume
            resultList = it
            initRecycler()
            quizAdapter.setOnItemClickListener(object :
                QuizRecycleAdapter.OnQuizItemClickListener {
                override fun onItemClick(position: Int) = goToResultDetail(position)
            })
        })

        val itemTouchHelper = ItemTouchHelper(getSwipeCallback())
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
    private fun getSwipeCallback(): ItemTouchHelper.SimpleCallback {
        // Add swiping actions
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedResultItem: QuizResult
                var ideologyTitle = "NONE"

                if (direction == ItemTouchHelper.LEFT) {
                    deletedResultItem = quizAdapter.getQuizResultAt(position)
                    viewModel.deleteQuizResult(deletedResultItem)
                    // TODO: VM
                    viewModel.getResultListWithDelay().observe(this@SavedResultsActivity, Observer {
                        resultList = it // TODO: How to get resultList immediately?
                        quizAdapter.setQuizResults(resultList)
                    })

                    for (ideo in Ideologies.values()) {
                        if (ideo.stringId == deletedResultItem.ideologyStringId) {
                            ideologyTitle = ideo.title
                        }
                    }

                    // Add Snackbar
                    Snackbar.make(recycler_results_list, "$ideologyTitle удален", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.addQuizResult(deletedResultItem)
                            viewModel.getResultListWithDelay().observe(this@SavedResultsActivity, Observer {
                                resultList = it
                                quizAdapter.setQuizResults(resultList)
                            })
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
    }

    private fun initRecycler() {
        quizAdapter = QuizRecycleAdapter()
        recycler_results_list.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
        quizAdapter.setQuizResults(resultList)
    }

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

}

