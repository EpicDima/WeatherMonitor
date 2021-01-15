package by.resliv.weathermonitor.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.resliv.weathermonitor.data.storage.dao.CurrentForecastDao
import by.resliv.weathermonitor.data.storage.dao.DailyForecastDao
import by.resliv.weathermonitor.data.storage.dao.HourlyForecastDao
import by.resliv.weathermonitor.data.storage.dao.LocationDao
import by.resliv.weathermonitor.model.CurrentForecast
import by.resliv.weathermonitor.model.DailyForecast
import by.resliv.weathermonitor.model.HourlyForecast
import by.resliv.weathermonitor.model.Location

/**
 * @author EpicDima
 */
@Database(
    version = 1,
    exportSchema = false,
    entities = [Location::class, CurrentForecast::class, DailyForecast::class, HourlyForecast::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun currentForecastDao(): CurrentForecastDao
    abstract fun dailyForecastDao(): DailyForecastDao
    abstract fun hourlyForecastDao(): HourlyForecastDao

    companion object {
        private const val DATABASE_NAME = "app.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}