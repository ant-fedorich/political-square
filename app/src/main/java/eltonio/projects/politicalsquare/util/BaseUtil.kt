package eltonio.projects.politicalsquare.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class BaseUtil {
    @Inject
    @ApplicationContext
    lateinit var baseContext: Context
}