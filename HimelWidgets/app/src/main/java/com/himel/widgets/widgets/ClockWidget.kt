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
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * World Clock home-screen widget.
 * Shows local time + several preset cities (same defaults as the web widget).
 */
class ClockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                ClockContent()
            }
        }
    }

    @Composable
    private fun ClockContent() {
        val cities = listOf(
            City("Dhaka", "Asia/Dhaka", "🇧🇩"),
            City("Mecca", "Asia/Riyadh", "🇸🇦"),
            City("London", "Europe/London", "🇬🇧"),
            City("New York", "America/New_York", "🇺🇸"),
            City("Tokyo", "Asia/Tokyo", "🇯🇵")
        )

        val localTz = TimeZone.getDefault().id
        val now = Date()

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFFF8FAF8)))
                .padding(12.dp)
        ) {
            // Header – local time
            val localTime = formatTime(now, localTz)
            val localDate = formatDate(now, localTz)

            Text(
                text = "🕐  $localTime",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B5E20)),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = localDate,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF5A5A5A)),
                    fontSize = 12.sp
                )
            )

            Spacer(GlanceModifier.height(8.dp))

            // City rows
            cities.forEach { city ->
                val t = formatTime(now, city.tz)
                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${city.flag} ${city.name}",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF1B1B1B)),
                            fontSize = 13.sp
                        ),
                        modifier = GlanceModifier.defaultWeight()
                    )
                    Text(
                        text = t,
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF1B5E20)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }

    private data class City(val name: String, val tz: String, val flag: String)

    private fun formatTime(date: Date, tzId: String): String {
        val fmt = SimpleDateFormat("HH:mm", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone(tzId)
        return fmt.format(date)
    }

    private fun formatDate(date: Date, tzId: String): String {
        val fmt = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone(tzId)
        return fmt.format(date)
    }
}

class ClockWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ClockWidget()
}
