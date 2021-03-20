package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.data.MainAppRepository
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.models.QuizOptions
import kotlinx.android.synthetic.main.activity_saved_result_detail.*
import javax.inject.Inject

@HiltViewModel
class SavedResultDetailViewModel @Inject constructor(
    val repository: MainAppRepository
): ViewModel() {
    private val context = repository.context
    private var owner =  MutableLiveData<String>()
    private var ideology = MutableLiveData<String>()


    // TODO: Do local unit test with LD
    fun getOwner(quizId: Int): LiveData<String> {
        owner.value = when(quizId) {
            QuizOptions.UKRAINE.id -> QuizOptions.UKRAINE.ownerRes.resString(context)
            QuizOptions.WORLD.id -> QuizOptions.WORLD.ownerRes.resString(context)
            else -> "none"
        }
        return owner
    }


    // TODO: Do local unit test with LD
    fun getIdeology(ideologyId: String): LiveData<String> {
        for (ideo in Ideologies.values()) {
            if (ideo.stringId == ideologyId) {
                 ideology.value = ideo.titleRes.resString(context)
            }
        }
        return ideology
    }
}