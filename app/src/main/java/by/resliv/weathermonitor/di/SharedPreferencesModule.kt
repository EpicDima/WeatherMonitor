package by.resliv.weathermonitor.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import by.resliv.weathermonitor.settings.APP_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author EpicDima
 */
@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }
}