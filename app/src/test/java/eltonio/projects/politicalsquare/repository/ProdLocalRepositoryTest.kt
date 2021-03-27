package eltonio.projects.politicalsquare.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.util.*
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ProdLocalRepositoryTest {
    private lateinit var context: Context
    private lateinit var testSharedPref: SharedPreferences
    private lateinit var localRepo: LocalRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        testSharedPref = context.getSharedPreferences(PREF_QUIZ_FOR_TEST, Context.MODE_PRIVATE)
        localRepo = ProdLocalRepository(context)
        localRepo.clearPref() // PREF_NAME_FOR_TEST used
    }

    @Test
    fun `put data for ResultActivity to SharedPref and get it`() {
        //given - setup
        testSharedPref.edit().apply{
            putInt(PREF_QUIZ_OPTION, 1)
            putFloat(PREF_CHOSEN_VIEW_X, 2.0f)
            putFloat(PREF_CHOSEN_VIEW_Y, 2.0f)
            putInt(PREF_HORIZONTAL_START_SCORE, 1)
            putInt(PREF_VERTICAL_START_SCORE, 1)
            putString(PREF_CHOSEN_IDEOLOGY, "www")
            putString(PREF_STARTED_AT, "10.10.10")
            putInt(PREF_HORIZONTAL_SCORE, 1)
            putInt(PREF_VERTICAL_SCORE, 1)
        }.apply()
        //then - verify
        assertThat(localRepo.loadQuizOption()).isEqualTo(1)
        assertThat(localRepo.getChosenViewX()).isEqualTo(2.0f)
        assertThat(localRepo.getChosenViewY()).isEqualTo(2.0f)
        assertThat(localRepo.getHorStartScore()).isEqualTo(1)
        assertThat(localRepo.getVerStartScore()).isEqualTo(1)
        assertThat(localRepo.getChosenIdeology()).contains("www")
        assertThat(localRepo.getStartedAt()).contains("10.10.10")
        assertThat(localRepo.getHorScore()).isEqualTo(1)
        assertThat(localRepo.getVerScore()).isEqualTo(1)
    }
}