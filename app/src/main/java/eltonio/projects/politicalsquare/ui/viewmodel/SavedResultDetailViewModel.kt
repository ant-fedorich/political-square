package eltonio.projects.politicalsquare.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.util.QuizOptions
import javax.inject.Inject

@HiltViewModel
class SavedResultDetailViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
): ViewModel() {
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