package by.resliv.weathermonitor.data.network

import by.resliv.weathermonitor.BuildConfig
import by.resliv.weathermonitor.data.network.response.ForecastResponse
import by.resliv.weathermonitor.data.network.response.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author EpicDima
 */
interface RetrofitApiService {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        private const val FORECAST_URL = "forecast.json"
        private const val LOCATIONS_URL = "search.json"
        private const val API_KEY = BuildConfig.API_KEY
        private const val DAYS = 5
    }

    @GET(FORECAST_URL)
    suspend fun getForecast(
        @Query("q") location: String,
        @Query("days") days: Int = DAYS,
        @Query("key") key: String = API_KEY
    ): ForecastResponse

    @GET(LOCATIONS_URL)
    suspend fun getLocations(
        @Query("q") location: String,
        @Query("key") key: String = API_KEY
    ): List<LocationResponse>
}