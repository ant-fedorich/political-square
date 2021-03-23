package eltonio.projects.politicalsquare.ui.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import eltonio.projects.politicalsquare.repository.AppRepository
import eltonio.projects.politicalsquare.repository.QuestionDao
import eltonio.projects.politicalsquare.repository.QuizResultDao
import eltonio.projects.politicalsquare.util.AppUtil
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class SettingsViewModelTest {
    private lateinit var context: Context
    private lateinit var questionDaoMock: QuestionDao
    private lateinit var quizResultDaoMock: QuizResultDao
    private lateinit var appUtil: AppUtil
    private lateinit var localRepo: AppRepository.Local
    private lateinit var viewmodel: SettingsViewModel


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        quizResultDaoMock = mock(QuizResultDao::class.java)
        questionDaoMock = mock(QuestionDao::class.java)
        appUtil = AppUtil(context)
        val repository = FakeAppRepository(questionDaoMock, quizResultDaoMock, appUtil)
        localRepo = repository.Local()
        viewmodel = SettingsViewModel(repository)
    }

    @Test
    fun `get Lang from settings, returns en`() {
        val langDefault = "en"
        val result = localRepo.getLang()

        assertThat(result).contains(langDefault)
    }

    @Test
    fun `change Lang in settings, returns uk`() {
        val langDefault = "en"
        val resultOne =
    }

}