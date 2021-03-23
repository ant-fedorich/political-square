package eltonio.projects.politicalsquare.ui.viewmodel


import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import javax.inject.Inject

@HiltViewModel
class ViewInfoViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {
    private var ideology: MutableLiveData<String> = MutableLiveData()
    private var imageId: MutableLiveData<Int> = MutableLiveData()
    private var descriptionId: MutableLiveData<Int> = MutableLiveData()
    private var styleId: MutableLiveData<Int> = MutableLiveData()

    fun updateData(ideologyTitleRes: Int) {
        when (ideologyTitleRes) {
            Ideologies.AUTHORITARIAN_LEFT.titleRes -> {
                ideology.value = Ideologies.AUTHORITARIAN_LEFT.titleRes.resString(context)
                imageId.value = R.drawable.img_info_1_autho_left
                descriptionId.value = R.string.desc_authoritarian_left
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_NATIONALISM.titleRes -> {
                ideology.value = Ideologies.RADICAL_NATIONALISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_2_radical_nation
                descriptionId.value = R.string.desc_nationalism
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.POWER_CENTRISM.titleRes -> {
                ideology.value = Ideologies.POWER_CENTRISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_3_gov3
                descriptionId.value = R.string.desc_powercentrism
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIAL_DEMOCRACY.titleRes -> {
                ideology.value = Ideologies.SOCIAL_DEMOCRACY.titleRes.resString(context)
                imageId.value = R.drawable.img_info_4_soc_dem
                descriptionId.value = R.string.desc_social_democracy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIALISM.titleRes -> {
                ideology.value = Ideologies.SOCIALISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_5_soc
                descriptionId.value = R.string.desc_socialism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.AUTHORITARIAN_RIGHT.titleRes -> {
                ideology.value = Ideologies.AUTHORITARIAN_RIGHT.titleRes.resString(context)
                imageId.value = R.drawable.img_info_6_autho_right
                descriptionId.value = R.string.desc_authoritarian_right
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_CAPITALISM.titleRes -> {
                ideology.value = Ideologies.RADICAL_CAPITALISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_7_radical_cap
                descriptionId.value = R.string.desc_radical_capitalism
                styleId.value = R.style.TextAppearance_MyApp_Title_SmallExpanded
            }
            Ideologies.CONSERVATISM.titleRes -> {
                ideology.value = Ideologies.CONSERVATISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_8_cons
                descriptionId.value = R.string.desc_conservatism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.PROGRESSIVISM.titleRes -> {
                ideology.value = Ideologies.PROGRESSIVISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_9_prog
                descriptionId.value = R.string.desc_progressivism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.RIGHT_ANARCHY.titleRes -> {
                ideology.value = Ideologies.RIGHT_ANARCHY.titleRes.resString(context)
                imageId.value = R.drawable.img_info_10_right_anar3
                descriptionId.value = R.string.desc_right_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.ANARCHY.titleRes -> {
                ideology.value = Ideologies.ANARCHY.titleRes.resString(context)
                imageId.value = R.drawable.img_info_11_anar
                descriptionId.value = R.string.desc_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERALISM.titleRes -> {
                ideology.value = Ideologies.LIBERALISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_12_lib
                descriptionId.value = R.string.desc_liberalism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERTARIANISM.titleRes -> {
                ideology.value = Ideologies.LIBERTARIANISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_13_libertar
                descriptionId.value = R.string.desc_libertarianism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.LEFT_ANARCHY.titleRes -> {
                ideology.value = Ideologies.LEFT_ANARCHY.titleRes.resString(context)
                imageId.value = R.drawable.img_info_14_left_anar
                descriptionId.value = R.string.desc_left_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.LIBERTARIAN_SOCIALISM.titleRes -> {
                ideology.value = Ideologies.LIBERTARIAN_SOCIALISM.titleRes.resString(context)
                imageId.value = R.drawable.img_info_15_lib_soc
                descriptionId.value = R.string.desc_liberal_socialism
                styleId.value = R.style.TextAppearance_MyApp_Title_SmallExpanded
            }
        }
    }

    fun getIdeology(): LiveData<String> = ideology
    fun getImageId(): LiveData<Int> = imageId
    fun getDescriptionId(): LiveData<Int> = descriptionId
    fun getStyleId(): LiveData<Int> = styleId


}