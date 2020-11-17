package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skydoves.balloon.createBalloon
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.QuizOptions
import kotlinx.android.synthetic.main.activity_saved_result_detail.*

class SavedResultDetailViewModel : ViewModel() {

    private var owner =  MutableLiveData<String>()
    private var ideology = MutableLiveData<String>()

    fun getOwner(quizId: Int): LiveData<String> {
        owner.value = when(quizId) {
            QuizOptions.UKRAINE.id -> QuizOptions.UKRAINE.owner
            QuizOptions.WORLD.id -> QuizOptions.WORLD.owner
            else -> "none"
        }
        return owner
    }

    fun getIdeology(ideologyId: String): LiveData<String> {
        for (ideo in Ideologies.values()) {
            if (ideo.stringId == ideologyId) {
                 ideology.value = ideo.title
            }
        }
        return ideology
    }
}