package eltonio.projects.politicalsquare.util

import android.content.Context
import eltonio.projects.politicalsquare.R

enum class QuizOptions(
    id: Int,
    titleRes: Int,
    ownerRes: Int,
    descRes: Int,
    quesAmount: Int,
    image: Int
) {
    WORLD(
        1,
        R.string.all_title_quiz_option_1,
        R.string.all_owner_quiz_option_1,
        R.string.all_desc_quiz_option_1,
        54,
        R.drawable.img_world_quiz
    ),
    UKRAINE(
        2,
        R.string.all_title_quiz_option_2,
        R.string.all_owner_quiz_option_2,
        R.string.all_desc_quiz_option_2,
        40,
        R.drawable.img_ukraine_quiz
    );

    var id = id
        private set
    var titleRes = titleRes
        private set
    var ownerRes = ownerRes
        private set
    var descRes = descRes
        private set
    var quesAmount = quesAmount
        private set
    var image = image
        private set

    val context: Context? = null

    companion object {
        fun Int.resString(context: Context): String {
            return context.getString(this)
        }
    }
}