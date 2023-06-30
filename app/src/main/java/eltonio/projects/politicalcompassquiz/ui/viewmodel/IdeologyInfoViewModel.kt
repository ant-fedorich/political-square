package eltonio.projects.politicalcompassquiz.ui.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eltonio.projects.politicalcompassquiz.R
import eltonio.projects.politicalcompassquiz.util.Ideologies
import javax.inject.Inject

@HiltViewModel
class IdeologyInfoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var _ideologyResId: MutableLiveData<String> = MutableLiveData()
    val ideologyResId: LiveData<String> = _ideologyResId

    private var _imageId: MutableLiveData<Int> = MutableLiveData()
    val imageId: LiveData<Int> = _imageId

    private var _descriptionId: MutableLiveData<Int> = MutableLiveData()
    val descriptionId: LiveData<Int> = _descriptionId

    private var _styleId: MutableLiveData<Int> = MutableLiveData()
    val styleId: LiveData<Int> = _styleId

    fun updateData(ideologyResId: Int) {
        when (ideologyResId) {
            Ideologies.AUTHORITARIAN_LEFT.resId -> {
                _imageId.value = R.drawable.img_info_1_autho_left
                _descriptionId.value = R.string.desc_authoritarian_left
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_NATIONALISM.resId -> {
                _imageId.value = R.drawable.img_info_2_radical_nation
                _descriptionId.value = R.string.desc_nationalism
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.POWER_CENTRISM.resId -> {
                _imageId.value = R.drawable.img_info_3_gov3
                _descriptionId.value = R.string.desc_powercentrism
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIAL_DEMOCRACY.resId -> {
                _imageId.value = R.drawable.img_info_4_soc_dem
                _descriptionId.value = R.string.desc_social_democracy
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.SOCIALISM.resId -> {
                _imageId.value = R.drawable.img_info_5_soc
                _descriptionId.value = R.string.desc_socialism
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.AUTHORITARIAN_RIGHT.resId -> {
                _imageId.value = R.drawable.img_info_6_autho_right
                _descriptionId.value = R.string.desc_authoritarian_right
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.RADICAL_CAPITALISM.resId -> {
                _imageId.value = R.drawable.img_info_7_radical_cap
                _descriptionId.value = R.string.desc_radical_capitalism
                _styleId.value = R.style.TextAppearance_MyApp_Title_SmallExpanded
            }
            Ideologies.CONSERVATISM.resId -> {
                _imageId.value = R.drawable.img_info_8_cons
                _descriptionId.value = R.string.desc_conservatism
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.PROGRESSIVISM.resId -> {
                _imageId.value = R.drawable.img_info_9_prog
                _descriptionId.value = R.string.desc_progressivism
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.RIGHT_ANARCHY.resId -> {
                _imageId.value = R.drawable.img_info_10_right_anar3
                _descriptionId.value = R.string.desc_right_anarchy
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.ANARCHY.resId -> {
                _imageId.value = R.drawable.img_info_11_anar
                _descriptionId.value = R.string.desc_anarchy
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERALISM.resId -> {
                _imageId.value = R.drawable.img_info_12_lib
                _descriptionId.value = R.string.desc_liberalism
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            Ideologies.LIBERTARIANISM.resId -> {
                _imageId.value = R.drawable.img_info_13_libertar
                _descriptionId.value = R.string.desc_libertarianism
                _styleId.value = R.style.TextAppearance_MyApp_Title_LargeExpanded
            }
            //
            Ideologies.LEFT_ANARCHY.resId -> {
                _imageId.value = R.drawable.img_info_14_left_anar
                _descriptionId.value = R.string.desc_left_anarchy
                _styleId.value = R.style.TextAppearance_MyApp_Title_MediumExpanded
            }
            Ideologies.LIBERTARIAN_SOCIALISM.resId -> {
                _imageId.value = R.drawable.img_info_15_lib_soc
                _descriptionId.value = R.string.desc_liberal_socialism
                _styleId.value = R.style.TextAppearance_MyApp_Title_SmallExpanded
            }
        }
    }




}