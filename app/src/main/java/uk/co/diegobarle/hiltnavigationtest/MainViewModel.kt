package uk.co.diegobarle.hiltnavigationtest

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigationManager: NavigationManager
) : ViewModel() {

    fun prepareNavigationManager(activity: AppCompatActivity) {
        navigationManager.setNavigationController(
            Navigation.findNavController(activity, R.id.nav_host_fragment)
        )
    }

}