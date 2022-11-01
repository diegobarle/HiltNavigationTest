package uk.co.diegobarle.hiltnavigationtest.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import uk.co.diegobarle.hiltnavigationtest.R
import uk.co.diegobarle.hiltnavigationtest.di.modules.NavigationModule
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationManager
import uk.co.diegobarle.hiltnavigationtest.testUtils.NavigationParamsMatcher
import uk.co.diegobarle.hiltnavigationtest.testUtils.launchFragmentInTestContainer
import uk.co.diegobarle.hiltnavigationtest.testUtils.waitId
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(
    NavigationModule::class
)
class InitialFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var navigationManager: NavigationManager

    companion object {
        private const val WAIT_TIMEOUT = 10000L
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()
    }

    /**
     * Test doesn't have the issue with NavController set when fragment injects using viewModels()
     */
    @Test
    fun test_initial_ui_and_click_on_button() = runBlockingTest {
        launchFragmentInTestContainer<InitialFragment>()

        Espresso.onView(ViewMatchers.isRoot())
            .perform(waitId(R.id.main, WAIT_TIMEOUT))

        Espresso.onView(ViewMatchers.withId(R.id.mainTitle))
            .check(ViewAssertions.matches(CoreMatchers.allOf(
                ViewMatchers.withText("Initial Fragment"),
                ViewMatchers.isDisplayed()
            )))

        Espresso.onView(ViewMatchers.withId(R.id.btn0))
            .check(ViewAssertions.matches(CoreMatchers.allOf(
                ViewMatchers.withText("Go to Feature"),
                ViewMatchers.isDisplayed()
            )))

        Espresso.onView(ViewMatchers.withText("Go to Feature"))
            .perform(ViewActions.click())

        verify(navigationManager, timeout(WAIT_TIMEOUT)).navigate(
            argThat(NavigationParamsMatcher(R.id.action_initialFragment_to_firstFragment))
        )
    }

    // Test helpers

    @Module
    @InstallIn(SingletonComponent::class)
    class TestNavigationModule {

        @Provides
        @Singleton
        internal fun provideNavigationManager(): NavigationManager = mock()
    }

}