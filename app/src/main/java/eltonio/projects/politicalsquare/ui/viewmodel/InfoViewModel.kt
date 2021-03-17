package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.Ideologies

import eltonio.projects.politicalsquare.util.convertDpToPx
import eltonio.projects.politicalsquare.util.getIdeology

class InfoViewModel : ViewModel() {
    private var imageId: MutableLiveData<Int> = MutableLiveData()
    private var ideology: MutableLiveData<String> = MutableLiveData()

    private var horScore = 0
    private var verScore = 0

    fun getIdeology(x: Float, y: Float): LiveData<String> {
        var step = convertDpToPx(4f)
        horScore = (x/step - 40).toInt()
        verScore = (y/step - 40).toInt()
        ideology.value = getIdeology(horScore, verScore)
        return ideology
    }
    
    fun getImageHoverId(ideology: String): LiveData<Int> {
        imageId.value = when(ideology) {
            Ideologies.AUTHORITARIAN_LEFT.title -> R.id.image_autho_left_hover
            Ideologies.RADICAL_NATIONALISM.title -> R.id.image_nation_hover
            Ideologies.POWER_CENTRISM.title  -> R.id.image_gov_hover
            Ideologies.SOCIAL_DEMOCRACY.title  -> R.id.image_soc_demo_hover
            Ideologies.SOCIALISM.title  -> R.id.image_soc_hover

            Ideologies.AUTHORITARIAN_RIGHT.title  -> R.id.image_autho_right_hover
            Ideologies.RADICAL_CAPITALISM.title  -> R.id.image_radical_cap_hover
            Ideologies.CONSERVATISM.title  -> R.id.image_cons_hover
            Ideologies.PROGRESSIVISM.title  -> R.id.image_prog_hover

            Ideologies.RIGHT_ANARCHY.title  -> R.id.image_right_anar_hover
            Ideologies.ANARCHY.title  -> R.id.image_anar_hover
            Ideologies.LIBERALISM.title  -> R.id.image_lib_hover
            Ideologies.LIBERTARIANISM.title  -> R.id.image_libertar_hover

            Ideologies.LEFT_ANARCHY.title  -> R.id.image_left_anar_hover
            Ideologies.LIBERTARIAN_SOCIALISM.title  -> R.id.image_lib_soc
            else -> null
        } 
        return imageId
    }
}