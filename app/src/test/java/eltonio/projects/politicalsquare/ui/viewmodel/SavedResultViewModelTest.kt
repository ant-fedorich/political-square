package eltonio.projects.politicalsquare.ui.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.getOrAwaitForUnitTest
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.repository.DBRepository
import eltonio.projects.politicalsquare.repository.FakeDBRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SavedResultViewModelTest {
    private lateinit var dbRepo: DBRepository
    private lateinit var saveResultViewModel: SavedResultViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dbRepo = FakeDBRepository()
        saveResultViewModel = SavedResultViewModel(dbRepo)
    }

    @Test
    fun `put a result to DB and get it`() = runBlockingTest {
        val quizResult = QuizResult(2, 2, "ideology", 2, 2, 2, 2, "10.10.10", "11.11.11", 2, 10.0)
        dbRepo.addQuizResult(quizResult)

        val result = saveResultViewModel.getQuizResultList().getOrAwaitForUnitTest()

        assertThat(result).contains(quizResult)
    }
}