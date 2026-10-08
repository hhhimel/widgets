package com.himel.widgets.data

/**
 * Bangladesh government holidays for 2026 (from the official বর্ষপঞ্জি).
 * Key format: "MM-DD"
 * Triple: (Bangla name, English name, moonDependent)
 */
object Holidays {

    data class Holiday(
        val banglaName: String,
        val englishName: String,
        val moonDependent: Boolean
    )

    private val map2026 = mapOf(
        "02-04" to Holiday("শবেবরাত", "Shab-e-Barat", false),
        "02-21" to Holiday("শহিদ দিবস ও আন্তর্জাতিক মাতৃভাষা দিবস", "Shaheed Day & Intl. Mother Language Day", false),
        "03-18" to Holiday("লাইলাতুল কদর", "Laylatul Qadr", false),
        "03-20" to Holiday("জুমাতুল বিদা", "Jumatul Bida", false),
        "03-21" to Holiday("ঈদ-উল-ফিতর", "Eid-ul-Fitr", true),
        "03-26" to Holiday("স্বাধীনতা ও জাতীয় দিবস", "Independence & National Day", false),
        "04-14" to Holiday("বাংলা নববর্ষ", "Bangla New Year", false),
        "05-01" to Holiday("মে দিবস", "May Day", false),
        "05-22" to Holiday("বুদ্ধ পূর্ণিমা (বৈশাখী পূর্ণিমা)", "Buddha Purnima", false),
        "05-27" to Holiday("ঈদ-উল-আজহা", "Eid-ul-Adha", true),
        "06-26" to Holiday("মহরম / আশুরা", "Muharram / Ashura", false),
        "08-05" to Holiday("গণ-অভ্যুত্থান দিবস", "Mass Uprising Day", false),
        "08-25" to Holiday("ঈদ-ই-মিলাদুন্নবী (সা:)", "Eid-e-Miladunnabi", true),
        "09-04" to Holiday("জন্মাষ্টমী", "Janmashtami", false),
        "10-21" to Holiday("দুর্গাপূজা (বিজয়া দশমী)", "Durga Puja (Bijoya Dashami)", false),
        "12-16" to Holiday("বিজয় দিবস", "Victory Day", false),
        "12-25" to Holiday("যিশুখ্রিস্টের জন্মদিন (বড়দিন)", "Christmas Day", false)
    )

    fun forYear(year: Int): Map<String, Holiday> = when (year) {
        2026 -> map2026
        else -> emptyMap() // extend later
    }

    fun get(year: Int, month: Int, day: Int): Holiday? {
        val key = "%02d-%02d".format(month, day)
        return forYear(year)[key]
    }
}
