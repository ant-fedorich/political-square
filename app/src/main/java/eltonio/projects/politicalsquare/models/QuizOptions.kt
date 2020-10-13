package eltonio.projects.politicalsquare.models

import android.content.Context
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.other.appContext

enum class QuizOptions(
    id: Int,
    title: String,
    owner: String,
    desc: String,
    image: Int
) {
    WORLD(
        1,
        appContext.getString(R.string.all_title_quiz_option_1),
        appContext.getString(R.string.all_owner_quiz_option_1),
        appContext.getString(R.string.all_desc_quiz_option_1),
        R.drawable.img_world_quiz
    ),
    UKRAINE(
        2,
        appContext.getString(R.string.all_title_quiz_option_2),
        appContext.getString(R.string.all_owner_quiz_option_2),
        appContext.getString(R.string.all_desc_quiz_option_2),
        R.drawable.img_ukraine_quiz
    );

    var id = id
        private set
    var title = title
        private set
    var owner = owner
        private set
    var desc = desc
        private set
    var image = image
        private set

    companion object {
        // We have to refresh all these parameters after changing a lang to get proper strings
        fun refreshAll(context: Context){
            WORLD.title = context.getString(R.string.all_title_quiz_option_1)
            WORLD.owner = context.getString(R.string.all_owner_quiz_option_1)
            WORLD.desc = context.getString(R.string.all_desc_quiz_option_1)
            //
            UKRAINE.title = context.getString(R.string.all_title_quiz_option_2)
            UKRAINE.owner = context.getString(R.string.all_owner_quiz_option_2)
            UKRAINE.desc = context.getString(R.string.all_desc_quiz_option_2)
        }
    }
}