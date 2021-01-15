package by.resliv.weathermonitor.data.network.response

/**
 * @author EpicDima
 */
data class LocationResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
) {
    fun toDataModel(): by.resliv.weathermonitor.model.Location {
        return by.resliv.weathermonitor.model.Location(name = name, latitude = lat, longitude = lon)
    }
}