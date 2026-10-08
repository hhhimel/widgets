# Install Himel Widgets on your Android phone (no Android Studio needed)

You only need:
- A free GitHub account
- Your Android phone
- ~5 minutes

---

## Step 1 — Put the project on GitHub

1. Go to https://github.com/new
2. Repository name: `HimelWidgets` (or any name)
3. Keep it **Public**
4. Click **Create repository**
5. On the next page click **uploading an existing file**
6. Drag the entire contents of the `HimelWidgets` folder (or the zip) into the browser and commit

---

## Step 2 — Let GitHub build the APK for you

1. In your new repository click the **Actions** tab
2. Click the workflow **Build APK**
3. Click **Run workflow** → **Run workflow**
4. Wait 3–6 minutes until the green checkmark appears
5. Click the completed run
6. At the bottom under **Artifacts** click **HimelWidgets-APK**
7. Download the zip → extract → you get `app-debug.apk`

---

## Step 3 — Install on your phone

1. Copy `app-debug.apk` to your phone (USB, Google Drive, Telegram to yourself, etc.)
2. On the phone open the APK file
3. If Android says “Install blocked”, tap **Settings** and allow install from that source
4. Tap **Install**

---

## Step 4 — Add the widgets to the home screen

1. Long-press on an empty area of the home screen
2. Tap **Widgets**
3. Scroll to **Himel Widgets**
4. Drag these three onto the home screen:
   - **World Clock**
   - **BD Calendar**
   - **Weather**

Done. They will update automatically.

---

### Notes
- This is a debug APK (fine for personal use).
- Weather defaults to Dhaka (same as your website).
- Calendar shows Bangla + Hijri + 2026 Bangladesh holidays.
- Clock shows Dhaka, Mecca, London, New York, Tokyo.
