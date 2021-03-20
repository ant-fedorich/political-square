package eltonio.projects.politicalsquare.models

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalsquare.data.MainAppRepository
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
    @Inject
    @ApplicationContext lateinit var context: Context
    private lateinit var localRepo: MainAppRepository.Local
    @Inject lateinit var mainRepo: MainAppRepository

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // TODO:  @MyRule Context

    @Before
    fun setup() {
        hiltRule.inject()
        localRepo = mainRepo.Local()
    }

    @Test
    fun testRefreshAll_ChangeLang_withRefreshing_returnsTrue() {
        // ACTION
        // First lang setup
        localRepo.setLang(context, enLocale)
        Ideologies.refreshAll(context) // TODO: crutch: include MockK for mocking Enum/Objects to isolate Ideologies
        val resultOne = Ideologies.ANARCHY.title

        // Lang changing
        localRepo.setLang(context, ruLocale)
        Ideologies.refreshAll(context)
        val resultTwo = Ideologies.ANARCHY.title

        // Lang changing
        localRepo.setLang(context, ukLocale)
        Ideologies.refreshAll(context)
        val resultThree = Ideologies.ANARCHY.title

        // VERIFY
        assertThat(resultOne).contains(enTitle)
        assertThat(resultTwo).contains(ruTitle)
       assertThat(resultThree).contains(ukTitle)
    }

    @Test
    fun testRefreshAll_ChangeLang_withoutRefreshing_returnsFalse() {
        // ACTION
        // First lang setup
        localRepo.setLang(context, enLocale)
        Ideologies.refreshAll(context) // TODO: crutch: include MockK for mocking Enum/Objects for isolating Ideologies
        val resultOne = Ideologies.ANARCHY.title

        // Lang changing
        localRepo.setLang(context, ruLocale)
        val resultTwo = Ideologies.ANARCHY.title

        // Lang changing
        localRepo.setLang(context, ukLocale)
        val resultThree = Ideologies.ANARCHY.title

        // VERIFY
        assertThat(resultOne).contains(enTitle)
        assertThat(resultTwo).doesNotContain(ruTitle)
        assertThat(resultThree).doesNotContain(ukTitle)
    }
}