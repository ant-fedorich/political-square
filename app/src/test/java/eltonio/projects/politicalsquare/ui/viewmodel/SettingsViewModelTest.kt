package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.repository.FakeLocalRepository
import eltonio.projects.politicalsquare.repository.LocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SettingsViewModelTest {
    private lateinit var context: Context
    private lateinit var localRepo: LocalRepository
    private lateinit var viewmodel: SettingsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        localRepo = FakeLocalRepository(context)
        viewmodel = SettingsViewModel(localRepo)
    }


    @Test
    fun `get Lang from settings, returns en`() = runBlockingTest {
        val langDefault = "en"
        val result = localRepo.getLang()

        assertThat(result).contains(langDefault)
    }

    @Test
    fun `change Lang in settings, returns uk`() = runBlockingTest {
        val langDefault = "en"

        localRepo.setLang(context, "uk")
        val result = localRepo.getLang()

        assertThat(result).isEqualTo("uk")
    }
}