package uk.co.diegobarle.hiltnavigationtest

import android.view.ViewStub

class TestActivity: MainActivity() {

    override fun setupNavigation() {
        findViewById<ViewStub>(R.id.nav_host_fragment_stub).apply {
            layoutResource = R.layout.nav_host_fragment_activity_test
            inflate()
        }
    }

    override fun prepareNavigationManager() {

    }
}