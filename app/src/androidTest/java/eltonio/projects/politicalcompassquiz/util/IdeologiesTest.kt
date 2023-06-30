package eltonio.projects.politicalcompassquiz.util

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eltonio.projects.politicalcompassquiz.repository.LocalRepository
import eltonio.projects.politicalcompassquiz.util.Ideologies.Companion.resString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
    @Inject lateinit var localRepo: LocalRepository

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun changeLang_getIdeologyTitle_returnsTitlesChanged() = runBlocking {
        // action
        // First savedLang setup
        localRepo.setupAndSaveLang(context, enLocale)
        val resultOne = Ideologies.ANARCHY.resId.resString(context)

        // Lang changing
        localRepo.setupAndSaveLang(context, ruLocale)
        val resultTwo = Ideologies.ANARCHY.resId.resString(context)

        // Lang changing
        localRepo.setupAndSaveLang(context, ukLocale)
        val resultThree = Ideologies.ANARCHY.resId.resString(context)

        // verify
        assertThat(resultOne).contains(enTitle)
        assertThat(resultTwo).contains(ruTitle)
        assertThat(resultThree).contains(ukTitle)
    }

    @Test
    fun changeLang_getLocale_returnsChangedLocaleSettings() = runBlocking {
        // action
        // First savedLang setup
        localRepo.setupAndSaveLang(context, enLocale)
        val resultOne = localRepo.getSavedLang()

        // Lang changing
        localRepo.setupAndSaveLang(context, ruLocale)
        val resultTwo = localRepo.getSavedLang()
        // Lang changing
        localRepo.setupAndSaveLang(context, ukLocale)
        val resultThree = localRepo.getSavedLang()

        // verify
        assertThat(resultOne).contains(enLocale)
        assertThat(resultTwo).contains(ruLocale)
        assertThat(resultThree).contains(ukLocale)
    }
}