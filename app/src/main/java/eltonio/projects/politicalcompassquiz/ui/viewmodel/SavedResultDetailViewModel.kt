package eltonio.projects.politicalcompassquiz.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalcompassquiz.util.Ideologies
import eltonio.projects.politicalcompassquiz.util.Ideologies.Companion.resString
import eltonio.projects.politicalcompassquiz.util.QuizOptions
import javax.inject.Inject

@HiltViewModel
class SavedResultDetailViewModel @Inject constructor(): ViewModel() {
    private var _owner =  MutableLiveData<String>()
    val owner: LiveData<String> = _owner

    private var _ideology = MutableLiveData<String>()
    val ideology: LiveData<String> = _ideology

    fun setOwner(quizId: Int, activityContext: Context) {
        _owner.value = when(quizId) {
            QuizOptions.UKRAINE.id -> QuizOptions.UKRAINE.ownerResId.resString(activityContext)
            QuizOptions.WORLD.id -> QuizOptions.WORLD.ownerResId.resString(activityContext)
            else -> "none"
        }
    }
    fun setIdeologyTitle(ideologyStringId: String, activityContext: Context) {
        for (ideo in Ideologies.values()) {
            if (ideo.stringId == ideologyStringId) {
                 _ideology.value = ideo.resId.resString(activityContext)
            }
        }
    }
}