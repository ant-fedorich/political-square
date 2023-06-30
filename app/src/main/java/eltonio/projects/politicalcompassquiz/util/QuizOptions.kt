package eltonio.projects.politicalcompassquiz.util

import android.content.Context
import eltonio.projects.politicalcompassquiz.R

enum class QuizOptions(
    id: Int,
    titleResId: Int,
    ownerResId: Int,
    descResId: Int,
    quesAmount: Int,
    imageResId: Int
) {
    WORLD(
        1, //sqlite id starts with 1
        R.string.all_title_quiz_option_1,
        R.string.all_owner_quiz_option_1,
        R.string.all_desc_quiz_option_1,
        54,
        R.drawable.img_world_quiz
    ),
    UKRAINE(
        2, //sqlite id starts with 1
        R.string.all_title_quiz_option_2,
        R.string.all_owner_quiz_option_2,
        R.string.all_desc_quiz_option_2,
        40,
        R.drawable.img_ukraine_quiz
    );

    var id = id
        private set
    var titleResId = titleResId
        private set
    var ownerResId = ownerResId
        private set
    var descResId = descResId
        private set
    var quesAmount = quesAmount
        private set
    var imageResId = imageResId
        private set

    companion object {
        fun Int.resString(context: Context): String {
            return context.getString(this)
        }
    }
}