package eltonio.projects.politicalsquare.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalsquare.repository.LocalRepository
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class IdeologiesTest {
    private val enLocale = "en"
    private val ruLocale = "ru"
    private val ukLocale = "uk"
    private val enTitle = "Anarchy"
    private val ruTitle = "Анархизм"
    private val ukTitle = "Анархізм"
    lateinit var context: Context
    @Inject lateinit var localRepo: LocalRepository

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // TODO:  @MyRule Context

    @Before
    fun setup() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testChangeLang_andGetIdeologyTitle_returnsTrue() {
        // ACTION
        // First lang setup
        localRepo.setLang(context, enLocale)
        val resultOne = Ideologies.ANARCHY.titleRes.resString(context)

        // Lang changing
        localRepo.setLang(context, ruLocale)
        val resultTwo = Ideologies.ANARCHY.titleRes.resString(context)

        // Lang changing
        localRepo.setLang(context, ukLocale)
        val resultThree = Ideologies.ANARCHY.titleRes.resString(context)

        // VERIFY
        assertThat(resultOne).contains(enTitle)
        assertThat(resultTwo).contains(ruTitle)
        assertThat(resultThree).contains(ukTitle)
    }

    @Test
    fun testChangeLang_andGetLocale_returnsTrue() {
        // ACTION
        // First lang setup
        localRepo.setLang(context, enLocale)
        val resultOne = localRepo.getLang()

        // Lang changing
        localRepo.setLang(context, ruLocale)
        val resultTwo = localRepo.getLang()
        // Lang changing
        localRepo.setLang(context, ukLocale)
        val resultThree = localRepo.getLang()

        // VERIFY
        assertThat(resultOne).contains(enLocale)
        assertThat(resultTwo).contains(ruLocale)
        assertThat(resultThree).contains(ukLocale)
    }
}