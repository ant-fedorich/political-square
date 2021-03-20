package eltonio.projects.politicalsquare.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.models.Ideologies
import eltonio.projects.politicalsquare.models.IdeologiesWithHilt
import eltonio.projects.politicalsquare.models.IdeologiesWithHilt.Companion.getTit

class ViewInfoViewModel : ViewModel() {

    private var ideology: MutableLiveData<String> = MutableLiveData()
    private var imageId: MutableLiveData<Int> = MutableLiveData()
    private var descriptionId: MutableLiveData<Int> = MutableLiveData()
    private var styleId: MutableLiveData<Int> = MutableLiveData()

    fun updateData(ideologyTitle: String) {
        when (ideologyTitle) {
            Ideologies.AUTHORITARIAN_LEFT.title -> {
                IdeologiesWithHilt.ANARCHY.titleRes.getTit()
                ideology.value = Ideologies.AUTHORITARIAN_LEFT.title
                imageId.value = R.drawable.img_info_1_autho_left
                descriptionId.value = R.string.desc_authoritarian_left
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_NATIONALISM.title -> {
                ideology.value = Ideologies.RADICAL_NATIONALISM.title
                imageId.value = R.drawable.img_info_2_radical_nation
                descriptionId.value = R.string.desc_nationalism
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.POWER_CENTRISM.title -> {
                ideology.value = Ideologies.POWER_CENTRISM.title
                imageId.value = R.drawable.img_info_3_gov3
                descriptionId.value = R.string.desc_powercentrism
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIAL_DEMOCRACY.title -> {
                ideology.value = Ideologies.SOCIAL_DEMOCRACY.title
                imageId.value = R.drawable.img_info_4_soc_dem
                descriptionId.value = R.string.desc_social_democracy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIALISM.title -> {
                ideology.value = Ideologies.SOCIALISM.title
                imageId.value = R.drawable.img_info_5_soc
                descriptionId.value = R.string.desc_socialism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.AUTHORITARIAN_RIGHT.title -> {
                ideology.value = Ideologies.AUTHORITARIAN_RIGHT.title
                imageId.value = R.drawable.img_info_6_autho_right
                descriptionId.value = R.string.desc_authoritarian_right
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_CAPITALISM.title -> {
                ideology.value = Ideologies.RADICAL_CAPITALISM.title
                imageId.value = R.drawable.img_info_7_radical_cap
                descriptionId.value = R.string.desc_radical_capitalism
                styleId.value = R.style.TextAppearance_MyApp_Title_SmallExpanded
            }
            Ideologies.CONSERVATISM.title -> {
                ideology.value = Ideologies.CONSERVATISM.title
                imageId.value = R.drawable.img_info_8_cons
                descriptionId.value = R.string.desc_conservatism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.PROGRESSIVISM.title -> {
                ideology.value = Ideologies.PROGRESSIVISM.title
                imageId.value = R.drawable.img_info_9_prog
                descriptionId.value = R.string.desc_progressivism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.RIGHT_ANARCHY.title -> {
                ideology.value = Ideologies.RIGHT_ANARCHY.title
                imageId.value = R.drawable.img_info_10_right_anar3
                descriptionId.value = R.string.desc_right_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.ANARCHY.title -> {
                ideology.value = Ideologies.ANARCHY.title
                imageId.value = R.drawable.img_info_11_anar
                descriptionId.value = R.string.desc_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERALISM.title -> {
                ideology.value = Ideologies.LIBERALISM.title
                imageId.value = R.drawable.img_info_12_lib
                descriptionId.value = R.string.desc_liberalism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERTARIANISM.title -> {
                ideology.value = Ideologies.LIBERTARIANISM.title
                imageId.value = R.drawable.img_info_13_libertar
                descriptionId.value = R.string.desc_libertarianism
                styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.LEFT_ANARCHY.title -> {
                ideology.value = Ideologies.LEFT_ANARCHY.title
                imageId.value = R.drawable.img_info_14_left_anar
                descriptionId.value = R.string.desc_left_anarchy
                styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.LIBERTARIAN_SOCIALISM.title -> {
                ideology.value = Ideologies.LIBERTARIAN_SOCIALISM.title
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