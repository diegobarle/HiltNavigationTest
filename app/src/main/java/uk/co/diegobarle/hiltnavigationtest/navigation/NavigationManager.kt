package uk.co.diegobarle.hiltnavigationtest.navigation

import androidx.navigation.NavController

interface NavigationManager {

    fun setNavigationController(nav: NavController)

    fun navigate(navigationData: NavigationData)
}