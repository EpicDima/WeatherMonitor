package by.resliv.weathermonitor.di

import android.app.Application
import by.resliv.weathermonitor.data.storage.AppDatabase
import by.resliv.weathermonitor.data.storage.dao.CurrentForecastDao
import by.resliv.weathermonitor.data.storage.dao.DailyForecastDao
import by.resliv.weathermonitor.data.storage.dao.HourlyForecastDao
import by.resliv.weathermonitor.data.storage.dao.LocationDao
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
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Singleton
    @Provides
    fun provideCurrentForecastDao(database: AppDatabase): CurrentForecastDao {
        return database.currentForecastDao()
    }

    @Singleton
    @Provides
    fun provideDailyForecastDao(database: AppDatabase): DailyForecastDao {
        return database.dailyForecastDao()
    }

    @Singleton
    @Provides
    fun provideHourlyForecastDao(database: AppDatabase): HourlyForecastDao {
        return database.hourlyForecastDao()
    }
}