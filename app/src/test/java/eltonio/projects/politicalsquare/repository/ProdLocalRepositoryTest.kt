package eltonio.projects.politicalsquare.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ProdLocalRepositoryTest {
    private lateinit var context: Context
    private lateinit var testSharedPref: SharedPreferences
    private lateinit var localRepo: LocalRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() = runBlocking{
        context = ApplicationProvider.getApplicationContext()
        testSharedPref = context.getSharedPreferences(PREF_QUIZ_FOR_TEST, Context.MODE_PRIVATE)
        localRepo = ProdLocalRepository(context)
        localRepo.clearPref() // PREF_NAME_FOR_TEST used
    }

    @Test
    fun `put data for ResultActivity to SharedPref and get it`() = runBlocking {
        //given - setup
        localRepo.saveChosenIdeologyData(
            x = 2.0f,
            y = 2.0f,
            horStartScore = 1,
            verStartScore = 1,
            ideology = "ideology",
            startedAt = "10.10.10",
            verEndScore = 1,
            horEndScore = 1,
            quizId = 1
        )

        val result = localRepo.loadChosenIdeologyData()

        //then - verify
        assertThat(result?.chosenQuizId).isEqualTo(1)
        assertThat(result?.chosenIdeologyStringId).isEqualTo("ideology")
        assertThat(result?.chosenViewX).isEqualTo(2.0f)
        assertThat(result?.horEndScore).isEqualTo(1)

    }
}