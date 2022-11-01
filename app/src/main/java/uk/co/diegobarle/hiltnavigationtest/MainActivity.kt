package uk.co.diegobarle.hiltnavigationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewStub
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    override fun onStart() {
        super.onStart()
        prepareNavigationManager()
    }

    protected open fun prepareNavigationManager() {
        viewModel.prepareNavigationManager(this)
    }

    protected open fun setupNavigation() {
        findViewById<ViewStub>(R.id.nav_host_fragment_stub).apply {
            layoutResource = R.layout.nav_host_fragment_activity_main
            inflate()
        }
    }
}