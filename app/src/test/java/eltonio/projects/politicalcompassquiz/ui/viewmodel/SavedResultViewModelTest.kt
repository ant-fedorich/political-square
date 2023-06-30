package eltonio.projects.politicalcompassquiz.ui.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalcompassquiz.getOrAwaitForUnitTest
import eltonio.projects.politicalcompassquiz.repository.entity.QuizResult
import eltonio.projects.politicalcompassquiz.repository.DBRepository
import eltonio.projects.politicalcompassquiz.repository.FakeDBRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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

        val result = saveResultViewModel.quizResultList.getOrAwaitForUnitTest()

        assertThat(result).contains(quizResult)
    }
}