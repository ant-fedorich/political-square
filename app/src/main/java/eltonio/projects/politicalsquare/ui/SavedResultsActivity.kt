package eltonio.projects.politicalsquare.ui

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eltonio.projects.politicalsquare.adapter.QuizRecycleAdapter
import eltonio.projects.politicalsquare.R

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.*
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalsquare.databinding.ActivitySavedResultsBinding
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.ui.viewmodel.SaveResultViewModel
import eltonio.projects.politicalsquare.util.*
import eltonio.projects.politicalsquare.util.AppUtil.pushRight
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

// FIXME: how to get rid of sintetic
import kotlinx.android.synthetic.main.layout_result_item.view.*

@AndroidEntryPoint
class SavedResultsActivity : AppCompatActivity() {
    private val viewmodel: SaveResultViewModel by viewModels()
    private val binding: ActivitySavedResultsBinding by lazy { ActivitySavedResultsBinding.inflate(layoutInflater)}

    private lateinit var resultList: List<QuizResult>
    private lateinit var quizAdapter: QuizRecycleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        super.onCreate(savedInstanceState)

        title = getString(R.string.saved_title_actionbar)

        resultList = emptyList()
        viewmodel.getResultList().observe(this, Observer {
            // TODO: How to get resultList imidiatly? Not in Observer or onResume
            resultList = it
            initRecycler()
            quizAdapter.setOnItemClickListener(object :
                QuizRecycleAdapter.OnQuizItemClickListener {
                override fun onItemClick(position: Int) = goToResultDetail(position)
            })
        })

        val itemTouchHelper = ItemTouchHelper(getSwipeCallback(this))
        itemTouchHelper.attachToRecyclerView(binding.recyclerResultsList)

        setContentView(binding.root)
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
    private fun getSwipeCallback(context: Context): ItemTouchHelper.SimpleCallback {
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
                    viewmodel.deleteQuizResult(deletedResultItem)
                    // TODO: VM
                    viewmodel.getResultListWithDelay().observe(this@SavedResultsActivity, Observer {
                        resultList = it // TODO: How to get resultList immediately?
                        quizAdapter.setQuizResults(resultList)
                    })

                    for (ideo in Ideologies.values()) {
                        if (ideo.stringId == deletedResultItem.ideologyStringId) {
                            ideologyTitle = ideo.titleRes.resString(context)
                        }
                    }

                    // Add Snackbar
                    Snackbar.make(binding.recyclerResultsList, "$ideologyTitle удален", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewmodel.addQuizResult(deletedResultItem)
                            viewmodel.getResultListWithDelay().observe(this@SavedResultsActivity, Observer {
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
        quizAdapter = QuizRecycleAdapter(this)
        binding.recyclerResultsList.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
        quizAdapter.setQuizResults(resultList)
    }

    private fun goToResultDetail(position: Int) {
        val itemView = binding.recyclerResultsList.layoutManager?.findViewByPosition(position) as ConstraintLayout
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

