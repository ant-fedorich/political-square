package eltonio.projects.politicalcompassquiz.ui

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eltonio.projects.politicalcompassquiz.adapter.QuizRecycleAdapter
import eltonio.projects.politicalcompassquiz.R

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.*
import dagger.hilt.android.AndroidEntryPoint
import eltonio.projects.politicalcompassquiz.databinding.ActivitySavedResultsBinding
import eltonio.projects.politicalcompassquiz.util.Ideologies.Companion.resString
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.ui.viewmodel.SavedResultViewModel
import eltonio.projects.politicalcompassquiz.util.*
import eltonio.projects.politicalcompassquiz.util.AppUtil.getIdeologyResIdByStringId
import eltonio.projects.politicalcompassquiz.util.AppUtil.pushRight
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import org.jetbrains.annotations.TestOnly

@AndroidEntryPoint
class SavedResultsActivity : AppCompatActivity() {
    private val viewmodel by viewModels<SavedResultViewModel>()
    private val binding: ActivitySavedResultsBinding by lazy { ActivitySavedResultsBinding.inflate(layoutInflater)}

    private val quizAdapter: QuizRecycleAdapter by lazy { QuizRecycleAdapter(this) }
    private var resultList: List<QuizResult> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        subscribeToObservers()

        setupRecycler()

        title = getString(R.string.saved_title_actionbar)

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerResultsList)

        setContentView(binding.root)
    }

    private fun subscribeToObservers() {
        viewmodel.quizResultList.observe(this) {
            resultList = it
            quizAdapter.addQuizResultList(resultList)
        }
    }

    /****************************/

    private fun setupRecycler() {
        quizAdapter.onQuizItemClickListener = { position ->
            goToResultDetail(position)
        }

        binding.recyclerResultsList.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(this@SavedResultsActivity)
        }

    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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

                val ideologyResId = getIdeologyResIdByStringId(deletedResultItem.ideologyStringId)
                ideologyTitle = ideologyResId.resString(this@SavedResultsActivity)

                // Add Snackbar
                Snackbar.make(binding.recyclerResultsList, "$ideologyTitle удален", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        viewmodel.addQuizResult(deletedResultItem)
                    }.show()
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        pushRight(this) // info out
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pushRight(this) //info out
    }

    private fun goToResultDetail(position: Int) {
        val itemView = binding.recyclerResultsList.layoutManager?.findViewByPosition(position) as ConstraintLayout
        val itemContainerTransitionName = itemView.findViewById<ConstraintLayout>(R.id.layout_item_container).transitionName
        val titleTransitionName = itemView.findViewById<TextView>(R.id.text_saved_result_title).transitionName
        val dateTransitionName = itemView.findViewById<TextView>(R.id.text_saved_result_date).transitionName
        val imageTransitionName = itemView.findViewById<ImageView>(R.id.image_compass_saved_result).transitionName

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

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, itemView.findViewById<ConstraintLayout>(R.id.layout_item_container), itemContainerTransitionName)

        startActivity(intent, options.toBundle())
    }

    @TestOnly
    fun getResultList() = resultList
}

