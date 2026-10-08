package com.himel.widgets.data

import java.util.Calendar
import java.util.TimeZone

/**
 * Bangladesh revised Bangla calendar + simple Umm al-Qura style Hijri approximation.
 * Ported from the web calendar-modal.js logic.
 */
object BanglaCalendar {

    data class BanglaDate(val day: Int, val month: Int, val year: Int, val monthNameBn: String, val monthNameEn: String)
    data class HijriDate(val day: Int, val month: Int, val year: Int, val monthNameBn: String)

    // [Bangla name, translit, [jsMonth (0-based), startDay]]
    private val BN_MONTHS = listOf(
        Triple("বৈশাখ", "Boishakh", 3 to 14),
        Triple("জ্যৈষ্ঠ", "Joishtha", 4 to 15),
        Triple("আষাঢ়", "Ashar", 5 to 15),
        Triple("শ্রাবণ", "Srabon", 6 to 16),
        Triple("ভাদ্র", "Bhadro", 7 to 16),
        Triple("আশ্বিন", "Ashwin", 8 to 16),
        Triple("কার্তিক", "Kartik", 9 to 17),
        Triple("অগ্রহায়ণ", "Ogrohayon", 10 to 16),
        Triple("পৌষ", "Poush", 11 to 16),
        Triple("মাঘ", "Magh", 0 to 15),
        Triple("ফাল্গুন", "Falgun", 1 to 14),
        Triple("চৈত্র", "Choitro", 2 to 15)
    )

    private val HIJRI_BN = listOf(
        "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    )

    private val BN_DIG = "০১২৩৪৫৬৭৮৯"

    fun toBnDigits(value: Any): String =
        value.toString().map { c -> if (c.isDigit()) BN_DIG[c - '0'] else c }.joinToString("")

    /**
     * Convert Gregorian date → Bangla date (Bangladesh revised calendar).
     */
    fun toBangla(cal: Calendar): BanglaDate {
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)          // 0-based
        val d = cal.get(Calendar.DAY_OF_MONTH)

        // Find which Bangla month this day falls into
        for (i in BN_MONTHS.indices) {
            val (bn, en, start) = BN_MONTHS[i]
            val (startM, startD) = start
            val nextIdx = (i + 1) % 12
            val (nextStartM, nextStartD) = BN_MONTHS[nextIdx].third

            val startOfThis = dayOfYear(y, startM, startD)
            val startOfNext = if (nextIdx == 0) {
                // next year Boishakh
                dayOfYear(y + 1, nextStartM, nextStartD) + daysInYear(y)
            } else {
                dayOfYear(y, nextStartM, nextStartD)
            }
            val today = dayOfYear(y, m, d)

            if (today >= startOfThis && today < startOfNext) {
                val bnDay = today - startOfThis + 1
                // Bangla year: starts mid-April
                val bnYear = if (m > 3 || (m == 3 && d >= 14)) y - 593 else y - 594
                return BanglaDate(bnDay, i, bnYear, bn, en)
            }
        }
        // fallback
        return BanglaDate(d, m, y - 593, BN_MONTHS[0].first, BN_MONTHS[0].second)
    }

    /**
     * Approximate Hijri date (Umm al-Qura style).
     * HIJRI_OFFSET_DAYS can be adjusted for local moon sighting (-1 is common for BD).
     */
    fun toHijri(cal: Calendar, offsetDays: Int = 0): HijriDate {
        val c = cal.clone() as Calendar
        c.add(Calendar.DAY_OF_MONTH, offsetDays)

        // Julian Day Number
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH) + 1
        val d = c.get(Calendar.DAY_OF_MONTH)
        var jy = y
        var jm = m
        if (m <= 2) {
            jy -= 1
            jm += 12
        }
        val a = jy / 100
        val b = 2 - a + a / 4
        val jd = (365.25 * (jy + 4716)).toInt() + (30.6001 * (jm + 1)).toInt() + d + b - 1524.5

        // Islamic calendar conversion (approx)
        val l = (jd + 0.5).toInt() - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val l2 = l - 10631 * n + 354
        val j = ((10985 - l2) / 5316).toInt() * ((50 * l2) / 17719).toInt() +
                (l2 / 5670).toInt() * ((43 * l2) / 15238).toInt()
        val l3 = l2 - ((30 - j) / 15).toInt() * ((17719 * j) / 50).toInt() -
                (j / 16).toInt() * ((15238 * j) / 43).toInt() + 29
        val hm = ((24 * l3) / 709).toInt()
        val hd = l3 - ((709 * hm) / 24).toInt()
        val hy = 30 * n + j - 30

        val monthIdx = (hm - 1).coerceIn(0, 11)
        return HijriDate(hd, hm, hy, HIJRI_BN[monthIdx])
    }

    private fun dayOfYear(year: Int, month0: Int, day: Int): Int {
        val c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"))
        c.set(year, month0, day, 12, 0, 0)
        return c.get(Calendar.DAY_OF_YEAR)
    }

    private fun daysInYear(year: Int): Int {
        val c = Calendar.getInstance()
        c.set(year, 11, 31)
        return c.get(Calendar.DAY_OF_YEAR)
    }

    fun monthNameEn(month0: Int): String =
        listOf("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")[month0]

    fun dowShort(dayOfWeek: Int): String =
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")[dayOfWeek]
}
