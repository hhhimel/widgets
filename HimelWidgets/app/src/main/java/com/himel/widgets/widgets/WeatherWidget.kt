package com.himel.widgets.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.himel.widgets.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Weather home-screen widget powered by Open-Meteo.
 * Defaults to Dhaka (same as the original web widget).
 */
class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Fetch on background thread before composing
        val weather = try {
            withContext(Dispatchers.IO) {
                WeatherRepository.fetch()
            }
        } catch (e: Exception) {
            null
        }

        provideContent {
            GlanceTheme {
                WeatherContent(weather)
            }
        }
    }

    @Composable
    private fun WeatherContent(bundle: WeatherRepository.WeatherBundle?) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFFF8FAF8)))
                .padding(14.dp)
        ) {
            if (bundle == null) {
                Text(
                    text = "Weather unavailable",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF5A5A5A)),
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "Tap to retry later",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF9E9E9E)),
                        fontSize = 12.sp
                    )
                )
                return@Column
            }

            val cur = bundle.current
            val (label, emoji) = WeatherRepository.describe(cur.weatherCode, cur.isDay)

            // Location
            Text(
                text = "📍 ${cur.placeName}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF5A5A5A)),
                    fontSize = 12.sp
                )
            )

            Spacer(GlanceModifier.height(4.dp))

            // Big temperature + icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.fillMaxWidth()
            ) {
                Text(
                    text = emoji,
                    style = TextStyle(fontSize = 32.sp)
                )
                Spacer(GlanceModifier.defaultWeight())
                Text(
                    text = "${cur.tempC.toInt()}°",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF1B5E20)),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = label,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B1B1B)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(GlanceModifier.height(6.dp))

            // Extra info
            Text(
                text = "Humidity ${cur.humidity}%  ·  Wind ${cur.windKmh.toInt()} km/h",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF5A5A5A)),
                    fontSize = 11.sp
                )
            )

            // Mini 3-day forecast
            if (bundle.daily.size >= 2) {
                Spacer(GlanceModifier.height(8.dp))
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    bundle.daily.take(3).forEach { day ->
                        val (_, e) = WeatherRepository.describe(day.weatherCode)
                        val shortDate = day.date.takeLast(5) // MM-DD
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = GlanceModifier.defaultWeight()
                        ) {
                            Text(
                                text = shortDate,
                                style = TextStyle(
                                    color = ColorProvider(Color(0xFF9E9E9E)),
                                    fontSize = 10.sp
                                )
                            )
                            Text(text = e, style = TextStyle(fontSize = 16.sp))
                            Text(
                                text = "${day.tempMax.toInt()}°",
                                style = TextStyle(
                                    color = ColorProvider(Color(0xFF1B1B1B)),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}
