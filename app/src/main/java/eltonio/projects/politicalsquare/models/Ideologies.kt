package eltonio.projects.politicalsquare.models

import android.content.Context
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.util.appContext

enum class Ideologies(title: String, stringId: String) {
    AUTHORITARIAN_LEFT(
        appContext.getString(R.string.title_authoritarian_left), "authoritarian_left"),
    RADICAL_NATIONALISM(
        appContext.getString(R.string.title_nationalism), "radical_nationalism"),
    POWER_CENTRISM(
        appContext.getString(R.string.title_powercentrism), "power_centrism"),
    SOCIAL_DEMOCRACY(
        appContext.getString(R.string.title_social_democracy), "social_democracy"),
    SOCIALISM(
        appContext.getString(R.string.title_socialism), "socialism"),
    //
    AUTHORITARIAN_RIGHT(
        appContext.getString(R.string.title_authoritarian_right), "authoritarian_right"),
    RADICAL_CAPITALISM(
        appContext.getString(R.string.title_radical_capitalism), "radical_capitalism"),
    CONSERVATISM(
        appContext.getString(R.string.title_conservatism), "conservatism"),
    PROGRESSIVISM(
        appContext.getString(R.string.title_progressivism), "progressivism"),
    //
    RIGHT_ANARCHY(
        appContext.getString(R.string.title_right_anarchy), "right_anarchy"),
    ANARCHY(
        appContext.getString(R.string.title_anarchy), "anarchy"),
    LIBERALISM(
        appContext.getString(R.string.title_liberalism), "liberalism"),
    LIBERTARIANISM(
        appContext.getString(R.string.title_libertarianism), "libertarianism"),
    //
    LEFT_ANARCHY(
        appContext.getString(R.string.title_left_anarchy), "left_anarchy"),
    LIBERTARIAN_SOCIALISM(
        appContext.getString(R.string.title_liberal_socialism), "libertarian_socialism");

    var title = title
        private set
    var stringId = stringId
        private set

    companion object {

        // We have to refresh all these parameters after changing a lang to get proper strings
        fun refreshAll(context: Context) {
            AUTHORITARIAN_LEFT.title = context.getString(
                R.string.title_authoritarian_left
            )
            RADICAL_NATIONALISM.title = context.getString(
                R.string.title_nationalism
            )
            POWER_CENTRISM.title = context.getString(
                R.string.title_powercentrism
            )
            SOCIAL_DEMOCRACY.title = context.getString(
                R.string.title_social_democracy
            )
            SOCIALISM.title = context.getString(
                R.string.title_socialism
            )
            //
            AUTHORITARIAN_RIGHT.title = context.getString(
                R.string.title_authoritarian_right
            )
            RADICAL_CAPITALISM.title = context.getString(
                R.string.title_radical_capitalism
            )
            CONSERVATISM.title = context.getString(
                R.string.title_conservatism
            )
            PROGRESSIVISM.title = context.getString(
                R.string.title_progressivism
            )
            //
            RIGHT_ANARCHY.title = context.getString(
                R.string.title_right_anarchy
            )
            ANARCHY.title = context.getString(
                R.string.title_anarchy
            )
            LIBERALISM.title = context.getString(
                R.string.title_liberalism
            )
            LIBERTARIANISM.title = context.getString(
                R.string.title_libertarianism
            )
            //
            LEFT_ANARCHY.title = context.getString(
                R.string.title_left_anarchy
            )
            LIBERTARIAN_SOCIALISM.title = context.getString(
                R.string.title_liberal_socialism
            )
        }
    }

}


