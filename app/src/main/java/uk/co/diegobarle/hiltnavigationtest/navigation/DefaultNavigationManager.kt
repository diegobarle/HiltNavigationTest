package uk.co.diegobarle.hiltnavigationtest.navigation

import androidx.navigation.NavController
import javax.inject.Inject

class DefaultNavigationManager @Inject constructor(): NavigationManager {

    private var navController: NavController? = null

    private fun requireNavController(): NavController {
        return navController
            ?: throw NullPointerException("navController is null, navController must be set before class use")
    }

    override fun setNavigationController(nav: NavController) {
        navController = nav
    }

    override fun navigate(navigationData: NavigationData) {

        try {
            requireNavController().navigate(
                navigationData.navDirections.actionId,
                navigationData.navDirections.arguments,
                navigationData.navOptions,
                navigationData.navigatorExtras
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }
}