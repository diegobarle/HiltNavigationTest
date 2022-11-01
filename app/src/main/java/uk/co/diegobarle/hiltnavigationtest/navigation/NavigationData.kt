package uk.co.diegobarle.hiltnavigationtest.navigation

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

data class NavigationData(
    val navDirections: NavDirections,
    val navOptions: NavOptions? = null,
    val navigatorExtras: Navigator.Extras? = null,
    val isInclusive: Boolean = false
)
