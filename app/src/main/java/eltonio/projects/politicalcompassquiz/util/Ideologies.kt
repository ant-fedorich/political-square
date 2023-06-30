package eltonio.projects.politicalcompassquiz.util

import android.content.Context
import eltonio.projects.politicalcompassquiz.R


enum class Ideologies(resId: Int, stringId: String) {
    AUTHORITARIAN_LEFT(R.string.title_authoritarian_left, "authoritarian_left"),
    RADICAL_NATIONALISM(R.string.title_nationalism, "radical_nationalism"),
    POWER_CENTRISM(R.string.title_powercentrism, "power_centrism"),
    SOCIAL_DEMOCRACY(R.string.title_social_democracy, "social_democracy"),
    SOCIALISM(R.string.title_socialism, "socialism"),
    //
    AUTHORITARIAN_RIGHT(R.string.title_authoritarian_right, "authoritarian_right"),
    RADICAL_CAPITALISM(R.string.title_radical_capitalism, "radical_capitalism"),
    CONSERVATISM(R.string.title_conservatism, "conservatism"),
    PROGRESSIVISM(R.string.title_progressivism, "progressivism"),
    //
    RIGHT_ANARCHY(R.string.title_right_anarchy, "right_anarchy"),
    ANARCHY(R.string.title_anarchy, "anarchy"),
    LIBERALISM(R.string.title_liberalism, "liberalism"),
    LIBERTARIANISM(R.string.title_libertarianism, "libertarianism"),
    //
    LEFT_ANARCHY(R.string.title_left_anarchy, "left_anarchy"),
    LIBERTARIAN_SOCIALISM(R.string.title_liberal_socialism, "libertarian_socialism");

    var resId = resId
        private set

    var stringId = stringId
        private set

    companion object {
        fun Int.resString(context: Context): String {
            return context.getString(this)
        }
    }
}


