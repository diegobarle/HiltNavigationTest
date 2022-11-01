package uk.co.diegobarle.hiltnavigationtest.ui.feature

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationData
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class FeatureSharedViewModel @Inject constructor(
    private val navigationManager: NavigationManager
): ViewModel() {

    var sharedValue = ""

    fun navigateToSecondScreen(value: String) {
        sharedValue = value
        navigationManager.navigate(
            NavigationData(
            navDirections = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
        )
        )
    }

    fun navigateToInitialScreen() {
        navigationManager.navigate(
            NavigationData(
                navDirections = SecondFragmentDirections.actionSecondFragmentToMainFragment()
            )
        )
    }
}