package uk.co.diegobarle.hiltnavigationtest.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationData
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val navigationManager: NavigationManager
): ViewModel() {

    fun navigateToFeature() {
        navigationManager.navigate(NavigationData(
            navDirections = InitialFragmentDirections.actionInitialFragmentToFirstFragment()
        ))
    }
}