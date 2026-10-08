package com.himel.widgets.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Weather data from Open-Meteo (no API key required).
 * Same data source as the original web widget.
 */
object WeatherRepository {

    data class CurrentWeather(
        val tempC: Double,
        val weatherCode: Int,
        val isDay: Boolean,
        val humidity: Int,
        val windKmh: Double,
        val placeName: String
    )

    data class DailyForecast(
        val date: String,
        val weatherCode: Int,
        val tempMax: Double,
        val tempMin: Double
    )

    data class WeatherBundle(
        val current: CurrentWeather,
        val daily: List<DailyForecast>
    )

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    // Default: Dhaka (same as web widget)
    const val DEFAULT_LAT = 23.8103
    const val DEFAULT_LON = 90.4125
    const val DEFAULT_NAME = "Dhaka"

    suspend fun fetch(
        lat: Double = DEFAULT_LAT,
        lon: Double = DEFAULT_LON,
        placeName: String = DEFAULT_NAME
    ): WeatherBundle = withContext(Dispatchers.IO) {
        val url = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=$lat&longitude=$lon" +
                "&current=temperature_2m,relative_humidity_2m,weather_code,is_day,wind_speed_10m" +
                "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
                "&timezone=auto&forecast_days=5"

        val request = Request.Builder().url(url).build()
        val body = client.newCall(request).execute().use { it.body?.string() }
            ?: throw Exception("Empty response")

        val json = JSONObject(body)
        val current = json.getJSONObject("current")
        val daily = json.getJSONObject("daily")

        val times = daily.getJSONArray("time")
        val codes = daily.getJSONArray("weather_code")
        val maxs = daily.getJSONArray("temperature_2m_max")
        val mins = daily.getJSONArray("temperature_2m_min")

        val forecast = (0 until times.length()).map { i ->
            DailyForecast(
                date = times.getString(i),
                weatherCode = codes.getInt(i),
                tempMax = maxs.getDouble(i),
                tempMin = mins.getDouble(i)
            )
        }

        WeatherBundle(
            current = CurrentWeather(
                tempC = current.getDouble("temperature_2m"),
                weatherCode = current.getInt("weather_code"),
                isDay = current.getInt("is_day") == 1,
                humidity = current.getInt("relative_humidity_2m"),
                windKmh = current.getDouble("wind_speed_10m"),
                placeName = placeName
            ),
            daily = forecast
        )
    }

    /** WMO weather code → human label + emoji */
    fun describe(code: Int, isDay: Boolean = true): Pair<String, String> = when (code) {
        0 -> (if (isDay) "Clear" else "Clear night") to (if (isDay) "☀️" else "🌙")
        1, 2 -> "Partly cloudy" to "⛅"
        3 -> "Overcast" to "☁️"
        45, 48 -> "Fog" to "🌫️"
        51, 53, 55 -> "Drizzle" to "🌦️"
        61, 63, 65 -> "Rain" to "🌧️"
        66, 67 -> "Freezing rain" to "🌨️"
        71, 73, 75, 77 -> "Snow" to "❄️"
        80, 81, 82 -> "Showers" to "🌦️"
        85, 86 -> "Snow showers" to "🌨️"
        95 -> "Thunderstorm" to "⛈️"
        96, 99 -> "Thunderstorm + hail" to "⛈️"
        else -> "Unknown" to "🌡️"
    }
}
