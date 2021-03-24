package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.arraySetOf
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.whenever
import eltonio.projects.politicalsquare.FakeDBRepository
import eltonio.projects.politicalsquare.repository.*
import org.junit.Before
import org.mockito.Mockito.mock
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockitokotlin2.verify
import eltonio.projects.politicalsquare.getOrAwaitForUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.not
import org.mockito.stubbing.OngoingStubbing
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@LooperMode(LooperMode.Mode.PAUSED)
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

        localRepo = mock(ProdLocalRepository::class.java)
        dbRepo = FakeDBRepository()
        cloudRepo = mock(ProdCloudRepository::class.java)

        firebaseUser = mock(FirebaseUser::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get data from localRepo and put a new result to dbRepo`() = runBlockingTest {
        // setup - given
        whenever(localRepo.loadQuizOption()).thenReturn(1)

        whenever(localRepo.getChosenViewX()).thenReturn(10.10f)
        whenever(localRepo.getChosenViewY()).thenReturn(10.10f)
        whenever(localRepo.getHorStartScore()).thenReturn(1)
        whenever(localRepo.getVerStartScore()).thenReturn(1)
        whenever(localRepo.getChosenIdeology()).thenReturn("socialist")
        whenever(localRepo.getStartedAt()).thenReturn("2020-10-10 10:10:10")
        whenever(localRepo.getHorScore()).thenReturn(5)
        whenever(localRepo.getVerScore()).thenReturn(5)

        whenever(cloudRepo.firebaseUser).thenReturn(firebaseUser)
        whenever(cloudRepo.firebaseUser?.uid.toString()).thenReturn("12345")

        // action - when
        resultViewModel = ResultViewModel(context, localRepo, dbRepo, cloudRepo) // init trigger of getting data from localRepo and put a new getQuizResults to dbRepo
        val result = dbRepo.getQuizResults().getOrAwaitForUnitTest()

        // verify - then
        assertThat(result).isNotEmpty()
        assertThat(result.last().startedAt).contains("2020-10-10 10:10:10")
    }
}
