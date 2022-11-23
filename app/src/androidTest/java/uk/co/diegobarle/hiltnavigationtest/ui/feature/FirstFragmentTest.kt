package uk.co.diegobarle.hiltnavigationtest.ui.feature

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
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
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
class FirstFragmentTest {

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
     * TODO Test failing due to issue with hiltNavGraphViewModels throwing '<View> does not have a NavController set'
     */
    @Test
    fun test_feature_ui_and_click_on_button() = runBlockingTest {
        launchFragmentInTestContainer<FirstFragment>(
            navigationGraphResId = R.navigation.feature_nav_graph
        )

        Espresso.onView(ViewMatchers.isRoot())
            .perform(waitId(R.id.main, WAIT_TIMEOUT))

        Espresso.onView(ViewMatchers.withId(R.id.title1))
            .check(
                ViewAssertions.matches(
                    CoreMatchers.allOf(
                        ViewMatchers.withText("First Fragment"),
                        ViewMatchers.isDisplayed()
                    )
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.et1))
            .perform(ViewActions.replaceText("Hello world"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.btn1))
            .check(
                ViewAssertions.matches(
                    CoreMatchers.allOf(
                        ViewMatchers.withText("Go to Fragment 2"),
                        ViewMatchers.isDisplayed()
                    )
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btn1))
            .perform(ViewActions.click())

        verify(navigationManager, timeout(WAIT_TIMEOUT)).navigate(
            argThat(NavigationParamsMatcher(R.id.action_firstFragment_to_secondFragment))
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