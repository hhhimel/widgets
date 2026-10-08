# Himel Widgets — Native Android Home-Screen Widgets

Three native Android home-screen widgets ported from the interactive widgets on [your portfolio site](https://github.com/…).

| Widget | What it shows |
|--------|---------------|
| **World Clock** | Local time + Dhaka, Mecca, London, New York, Tokyo |
| **BD Calendar** | Today’s Gregorian + Bangla + Hijri date + next Bangladesh holiday |
| **Weather** | Current conditions + 3-day forecast (Open-Meteo, defaults to Dhaka) |

Built with **Jetpack Glance** (modern declarative widgets) + Kotlin.

---

## Requirements

- Android Studio Ladybug (2024.2) or newer
- JDK 17
- minSdk 26 (Android 8.0)

---

## How to open & run

1. Unzip / open the `HimelWidgets` folder in **Android Studio**.
2. Let Gradle sync (it will download dependencies automatically).
3. Connect a device or start an emulator (API 26+).
4. Press **Run** ▶.

After the app is installed:

1. Long-press the home screen → **Widgets**
2. Find **Himel Widgets**
3. Drag **World Clock**, **BD Calendar** or **Weather** onto the home screen

---

## Project structure

```
app/src/main/java/com/himel/widgets/
├── MainActivity.kt                 # Simple “how to add widgets” screen
├── data/
│   ├── BanglaCalendar.kt           # Bangla + Hijri conversion (ported from JS)
│   ├── Holidays.kt                 # 2026 Bangladesh government holidays
│   └── WeatherRepository.kt        # Open-Meteo API client
└── widgets/
    ├── ClockWidget.kt              # World Clock Glance widget
    ├── CalendarWidget.kt           # BD Calendar Glance widget
    └── WeatherWidget.kt            # Weather Glance widget
```

---

## Customisation ideas

### Clock
- Edit the city list in `ClockWidget.kt` (`cities` list).
- Change update frequency in `res/xml/clock_widget_info.xml` (`updatePeriodMillis`).

### Calendar
- Add more years of holidays in `Holidays.kt`.
- Adjust Hijri offset (`offsetDays`) in `CalendarWidget.kt` if Bangladesh moon-sighting differs.

### Weather
- Change default location in `WeatherRepository.kt` (`DEFAULT_LAT`, `DEFAULT_LON`, `DEFAULT_NAME`).
- Later you can add a configuration activity so the user can pick any city.

---

## Notes

- Weather uses the same free Open-Meteo API as your web widget (no key needed).
- Bangla calendar logic is a direct port of the revised Bangladesh calendar used on the website.
- Widgets refresh automatically according to the periods set in the `*_widget_info.xml` files (Clock every 1 min, Calendar & Weather every 30 min). Android may batch updates to save battery.

---

## Next steps you might want

1. Add a **widget configuration screen** (city picker for weather, timezone picker for clock).
2. Support dark mode properly with `GlanceTheme`.
3. Publish to Play Store as a small companion app for your portfolio.

Enjoy the widgets!
