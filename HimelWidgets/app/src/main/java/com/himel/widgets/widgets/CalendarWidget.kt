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
import com.himel.widgets.data.BanglaCalendar
import com.himel.widgets.data.Holidays
import java.util.Calendar
import java.util.TimeZone

/**
 * Bangladesh Calendar home-screen widget.
 * Shows today with Gregorian + Bangla + Hijri dates and the next holiday.
 */
class CalendarWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                CalendarContent()
            }
        }
    }

    @Composable
    private fun CalendarContent() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"))
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)          // 0-based
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dow = cal.get(Calendar.DAY_OF_WEEK) - 1  // 0 = Sunday

        val bn = BanglaCalendar.toBangla(cal)
        val hj = BanglaCalendar.toHijri(cal, offsetDays = 0)

        val holiday = Holidays.get(year, month + 1, day)
        val nextHoliday = findNextHoliday(cal)

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFFF8FAF8)))
                .padding(14.dp)
        ) {
            // Big day number
            Text(
                text = day.toString(),
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B5E20)),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "${BanglaCalendar.dowShort(dow)}, ${BanglaCalendar.monthNameEn(month)} $year",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B1B1B)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(GlanceModifier.height(6.dp))

            // Bangla
            Text(
                text = "${BanglaCalendar.toBnDigits(bn.day)} ${bn.monthNameBn} ${BanglaCalendar.toBnDigits(bn.year)}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF2E7D32)),
                    fontSize = 13.sp
                )
            )

            // Hijri
            Text(
                text = "${BanglaCalendar.toBnDigits(hj.day)} ${hj.monthNameBn} ${BanglaCalendar.toBnDigits(hj.year)}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF5A5A5A)),
                    fontSize = 12.sp
                )
            )

            Spacer(GlanceModifier.height(8.dp))

            // Today holiday or next holiday
            if (holiday != null) {
                Text(
                    text = "🎉 ${holiday.englishName}",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFC62828)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            } else if (nextHoliday != null) {
                Text(
                    text = "Next: ${nextHoliday.second.englishName} (${nextHoliday.first})",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF5A5A5A)),
                        fontSize = 11.sp
                    )
                )
            }
        }
    }

    private fun findNextHoliday(from: Calendar): Pair<String, Holidays.Holiday>? {
        val year = from.get(Calendar.YEAR)
        val map = Holidays.forYear(year)
        if (map.isEmpty()) return null

        val todayKey = "%02d-%02d".format(
            from.get(Calendar.MONTH) + 1,
            from.get(Calendar.DAY_OF_MONTH)
        )

        val sorted = map.keys.sorted()
        for (key in sorted) {
            if (key > todayKey) {
                return key to map[key]!!
            }
        }
        // wrap to next year if needed – for now just return null
        return null
    }
}

class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CalendarWidget()
}
