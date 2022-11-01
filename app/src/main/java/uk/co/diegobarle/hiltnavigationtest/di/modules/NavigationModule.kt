package uk.co.diegobarle.hiltnavigationtest.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.co.diegobarle.hiltnavigationtest.navigation.DefaultNavigationManager
import uk.co.diegobarle.hiltnavigationtest.navigation.NavigationManager
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
class NavigationModule {

    @Provides
    @Singleton
    internal fun provideNavigationManager(): NavigationManager {
        return DefaultNavigationManager()
    }

}