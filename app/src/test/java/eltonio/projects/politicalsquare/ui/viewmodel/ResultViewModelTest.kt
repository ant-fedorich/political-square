package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import eltonio.projects.politicalsquare.repository.*
import org.junit.Before
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import eltonio.projects.politicalsquare.getOrAwaitForUnitTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ResultViewModelTest {
    private lateinit var context: Context
    private lateinit var localRepo: LocalRepository
    private lateinit var dbRepo: DBRepository
    private lateinit var cloudRepo: CloudRepository
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var resultViewModel: ResultViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // For coroutines, livedata

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        localRepo = mockk<ProdLocalRepository>()
        dbRepo = FakeDBRepository()
        cloudRepo = mockk<ProdCloudRepository>(relaxed = true)
        firebaseUser = mockk()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get data from localRepo and put a new result to dbRepo, returns DB is not empty`() = runBlockingTest {
        // setup - given
        every { localRepo.loadQuizOption() } returns 1

        every { localRepo.getChosenViewX() } returns 10.10f
        every { localRepo.getChosenViewY() } returns 10.10f
        every { localRepo.getHorStartScore() } returns 1
        every { localRepo.getVerStartScore() } returns 1
        every { localRepo.getChosenIdeology() } returns "socialist"
        every { localRepo.getStartedAt() } returns "2020-10-10 10:10:10"
        every { localRepo.getHorScore() } returns 5
        every { localRepo.getVerScore() } returns 5

        every { cloudRepo.firebaseUser } returns firebaseUser
        every { cloudRepo.firebaseUser?.uid.toString() } returns "12345"

        // action - when
        resultViewModel = ResultViewModel(context, localRepo, dbRepo, cloudRepo) // init trigger of getting data from localRepo and put a new getQuizResults to dbRepo
        val result = dbRepo.getQuizResults().getOrAwaitForUnitTest()

        // verify - then
        verify (atLeast = 1) {
            localRepo.loadQuizOption()
            cloudRepo.firebaseUser
        }

        assertThat(result).isNotEmpty()
        assertThat(result.last().startedAt).contains("2020-10-10 10:10:10")
    }
}
