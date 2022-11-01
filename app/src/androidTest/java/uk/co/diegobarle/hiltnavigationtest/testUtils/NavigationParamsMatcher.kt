package uk.co.diegobarle.hiltnavigationtest.testUtils

import org.mockito.ArgumentMatcher
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationData

/**
 * ArgumentMatcher to check that a NavigationParams has the expected actionId.
 * If $check function is not null, the matcher will pass if the return is true.
 */
class NavigationParamsMatcher(
    private val expectedActionId: Int
) :
    ArgumentMatcher<NavigationData> {
    override fun matches(data: NavigationData): Boolean {
        return data.navDirections.actionId == expectedActionId
    }
}