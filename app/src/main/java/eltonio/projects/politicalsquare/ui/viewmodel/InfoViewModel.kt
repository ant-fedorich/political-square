package eltonio.projects.politicalsquare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.util.AppUtil
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    val appUtil: AppUtil
): ViewModel() {
    private val context = appUtil.context
    private var imageId: MutableLiveData<Int> = MutableLiveData()
    private var ideology: MutableLiveData<String> = MutableLiveData()

    private var horScore = 0
    private var verScore = 0

    fun getIdeology(x: Float, y: Float): LiveData<String> {
        var step = appUtil.convertDpToPx(4f)
        horScore = (x/step - 40).toInt()
        verScore = (y/step - 40).toInt()
        ideology.value = appUtil.getIdeologyFromScore(horScore, verScore)
        return ideology
    }
    
    fun getImageHoverId(ideology: String): LiveData<Int> {
        imageId.value = when(ideology) {
            // TODO: Change to StringId
            Ideologies.AUTHORITARIAN_LEFT.titleRes.resString(context) -> R.id.image_autho_left_hover
            Ideologies.RADICAL_NATIONALISM.titleRes.resString(context)  -> R.id.image_nation_hover
            Ideologies.POWER_CENTRISM.titleRes.resString(context)   -> R.id.image_gov_hover
            Ideologies.SOCIAL_DEMOCRACY.titleRes.resString(context)   -> R.id.image_soc_demo_hover
            Ideologies.SOCIALISM.titleRes.resString(context)   -> R.id.image_soc_hover

            Ideologies.AUTHORITARIAN_RIGHT.titleRes.resString(context)   -> R.id.image_autho_right_hover
            Ideologies.RADICAL_CAPITALISM.titleRes.resString(context)   -> R.id.image_radical_cap_hover
            Ideologies.CONSERVATISM.titleRes.resString(context)   -> R.id.image_cons_hover
            Ideologies.PROGRESSIVISM.titleRes.resString(context)   -> R.id.image_prog_hover

            Ideologies.RIGHT_ANARCHY.titleRes.resString(context)   -> R.id.image_right_anar_hover
            Ideologies.ANARCHY.titleRes.resString(context)   -> R.id.image_anar_hover
            Ideologies.LIBERALISM.titleRes.resString(context)   -> R.id.image_lib_hover
            Ideologies.LIBERTARIANISM.titleRes.resString(context)   -> R.id.image_libertar_hover

            Ideologies.LEFT_ANARCHY.titleRes.resString(context)   -> R.id.image_left_anar_hover
            Ideologies.LIBERTARIAN_SOCIALISM.titleRes.resString(context)   -> R.id.image_lib_soc
            else -> null
        } 
        return imageId
    }
}