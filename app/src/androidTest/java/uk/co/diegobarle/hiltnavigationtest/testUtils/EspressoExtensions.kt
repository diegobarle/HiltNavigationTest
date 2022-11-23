package uk.co.diegobarle.hiltnavigationtest.testUtils

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.testing.R
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import uk.co.diegobarle.hiltnavigationtest.TestActivity
import java.util.concurrent.TimeoutException

/**
 * Launch a Fragment of type T with Bundle fragmentArgs inside our TestActivity. It also gets the
 * ViewModel of type VM from the viewmodels factory, storing it in the ViewModelWrapper.
 */
inline fun <reified T : Fragment> launchFragmentInTestContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    navigationGraphResId: Int? = null,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<TestActivity> {
    return launchFragmentInSpecificContainer<T, TestActivity>(
        fragmentArgs,
        themeResId,
        navigationGraphResId,
        action
    )
}

/**
 * Launch a Fragment of type T with Bundle fragmentArgs inside an Activity of type U which requires
 * to be a Hilt Activity (Activity that is marked with the tag @AndroidEntryPoint).
 * If U is not a Hilt Activity, the test will fail with the error "Unable to resolve activity for: Intent".
 * It also gets the ViewModel of type VM from the viewmodels factory, storing it in the ViewModelWrapper.
 */
inline fun <reified T : Fragment, reified U : AppCompatActivity> launchFragmentInSpecificContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    navigationGraphResId: Int? = null,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<U> {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            U::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    return ActivityScenario.launch<U>(startActivityIntent).onActivity { activity ->
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setViewModelStore(ViewModelStore())

        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        ).also { fragment ->
            if (navigationGraphResId != null) {
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(navigationGraphResId)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()

    }
}

fun waitId(viewId: Int, millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher: Matcher<View> = ViewMatchers.withId(viewId)
            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}