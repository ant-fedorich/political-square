package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx
import eltonio.projects.politicalsquare.util.AppUtil.getIdeologyResIdFromScore
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor (
    @ApplicationContext private val context: Context
): ViewModel() {
    private var horScore = 0
    private var verScore = 0

    fun getIdeologyResId(x: Float, y: Float): Int {
        var step = convertDpToPx(context, 4f)
        horScore = (x/step - 40).toInt()
        verScore = (y/step - 40).toInt()
        return getIdeologyResIdFromScore(horScore, verScore)
    }

    fun getImageHoverId(ideologyResId: Int): Int {
        return when(ideologyResId) {
            // TODO: Change to StringId
            Ideologies.AUTHORITARIAN_LEFT.resId -> R.id.image_autho_left_hover
            Ideologies.RADICAL_NATIONALISM.resId  -> R.id.image_nation_hover
            Ideologies.POWER_CENTRISM.resId   -> R.id.image_gov_hover
            Ideologies.SOCIAL_DEMOCRACY.resId   -> R.id.image_soc_demo_hover
            Ideologies.SOCIALISM.resId   -> R.id.image_soc_hover

            Ideologies.AUTHORITARIAN_RIGHT.resId   -> R.id.image_autho_right_hover
            Ideologies.RADICAL_CAPITALISM.resId   -> R.id.image_radical_cap_hover
            Ideologies.CONSERVATISM.resId   -> R.id.image_cons_hover
            Ideologies.PROGRESSIVISM.resId   -> R.id.image_prog_hover

            Ideologies.RIGHT_ANARCHY.resId   -> R.id.image_right_anar_hover
            Ideologies.ANARCHY.resId   -> R.id.image_anar_hover
            Ideologies.LIBERALISM.resId   -> R.id.image_lib_hover
            Ideologies.LIBERTARIANISM.resId   -> R.id.image_libertar_hover

            Ideologies.LEFT_ANARCHY.resId   -> R.id.image_left_anar_hover
            Ideologies.LIBERTARIAN_SOCIALISM.resId   -> R.id.image_lib_soc
            else -> R.id.image_lib_soc
        }
    }
    
    fun getImageHoverIdViaString(ideology: String): Int {
        return when(ideology) {
            // TODO: Change to StringId
            Ideologies.AUTHORITARIAN_LEFT.resId.resString(context) -> R.id.image_autho_left_hover
            Ideologies.RADICAL_NATIONALISM.resId.resString(context)  -> R.id.image_nation_hover
            Ideologies.POWER_CENTRISM.resId.resString(context)   -> R.id.image_gov_hover
            Ideologies.SOCIAL_DEMOCRACY.resId.resString(context)   -> R.id.image_soc_demo_hover
            Ideologies.SOCIALISM.resId.resString(context)   -> R.id.image_soc_hover

            Ideologies.AUTHORITARIAN_RIGHT.resId.resString(context)   -> R.id.image_autho_right_hover
            Ideologies.RADICAL_CAPITALISM.resId.resString(context)   -> R.id.image_radical_cap_hover
            Ideologies.CONSERVATISM.resId.resString(context)   -> R.id.image_cons_hover
            Ideologies.PROGRESSIVISM.resId.resString(context)   -> R.id.image_prog_hover

            Ideologies.RIGHT_ANARCHY.resId.resString(context)   -> R.id.image_right_anar_hover
            Ideologies.ANARCHY.resId.resString(context)   -> R.id.image_anar_hover
            Ideologies.LIBERALISM.resId.resString(context)   -> R.id.image_lib_hover
            Ideologies.LIBERTARIANISM.resId.resString(context)   -> R.id.image_libertar_hover

            Ideologies.LEFT_ANARCHY.resId.resString(context)   -> R.id.image_left_anar_hover
            Ideologies.LIBERTARIAN_SOCIALISM.resId.resString(context)   -> R.id.image_lib_soc
            else -> R.id.image_lib_soc
        }
    }
}