# Daily Brief — Full-Stack Android Application

**Daily Brief** is an executive news briefing platform engineered with a **Node.js + Express backend** and a **Jetpack Compose Android app**. It automatically aggregates breaking news across six key verticals, summarizes each story into 3–5 high-impact bullet lines using the OpenAI API, stores briefings in SQLite, and synchronizes with an offline-first Android client with daily 8:00 AM push notifications.

---

## Architecture & Folder Structure

```
.
├── backend/                             # Backend Service (Node.js + Express)
│   ├── src/
│   │   ├── server.js                    # HTTP server (GET /api/brief, POST /api/brief/refresh)
│   │   ├── fetcher.js                   # Ingests Google News RSS feeds & seeds across 6 categories
│   │   ├── summarizer.js                # OpenAI GPT-4o-mini 3-5 line summarizer (with heuristic fallback)
│   │   ├── db.js                        # SQLite database integration (table: articles)
│   │   └── scheduler.js                 # Daily 7:00 AM IST cron job
│   ├── .env.example                     # Sample backend environment variables
│   ├── package.json                     # Node.js dependencies and run scripts
│   └── README.md                        # Dedicated backend guide
│
├── app/                                 # Android Client (Kotlin + Jetpack Compose)
│   ├── src/main/java/com/example/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── ArticleEntity.kt     # Room database entity
│   │   │   │   ├── ArticleDao.kt        # Reactive DAO with Flow
│   │   │   │   ├── AppDatabase.kt       # Room Database singleton
│   │   │   │   └── InitialData.kt       # Pre-bundled offline starter dataset
│   │   │   ├── remote/
│   │   │   │   └── DailyBriefApiService.kt # Retrofit & Moshi network client
│   │   │   └── repository/
│   │   │       └── ArticleRepository.kt # Repository coordinating Room & Remote sync
│   │   ├── notification/
│   │   │   ├── DailyNotificationReceiver.kt # BroadcastReceiver for 8:00 AM notification
│   │   │   └── NotificationHelper.kt    # AlarmManager & NotificationChannel scheduler
│   │   ├── ui/
│   │   │   ├── DailyBriefViewModel.kt   # MVVM StateFlow manager
│   │   │   ├── components/
│   │   │   │   ├── CategoryCard.kt      # Headline, image, 3-5 line summary, source
│   │   │   │   ├── CategoryChipRow.kt   # Category filter pills
│   │   │   │   ├── SkeletonLoadingCard.kt # Animated shimmer placeholder UI
│   │   │   │   └── ServerConfigDialog.kt # In-app backend URL & test triggers
│   │   │   ├── screens/
│   │   │   │   ├── HomeScreen.kt        # Category grid, pull-to-refresh, manual refresh
│   │   │   │   └── CategoryDetailScreen.kt # Full briefing, context, share, and link
│   │   │   └── theme/
│   │   │       ├── Color.kt             # Dark & Light editorial palettes
│   │   │       ├── Theme.kt             # Material 3 dynamic theme
│   │   │       └── Type.kt
│   │   └── MainActivity.kt              # Entry point with edge-to-edge & permissions
│   ├── src/main/res/
│   │   ├── drawable/
│   │   │   ├── ic_launcher_background.xml # Midnight-to-blue gradient
│   │   │   └── ic_launcher_foreground.xml # Folded newspaper & morning sun vector
│   │   └── values/strings.xml
│   └── build.gradle.kts
└── README.md
```

---

## 1. Backend Setup & Run Instructions

### Step 1: Install Dependencies
```bash
cd backend
npm install
```

### Step 2: Configure Environment
Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```
*(Optional: Add your `OPENAI_API_KEY` for GPT-4o-mini summarization. If omitted, the server uses its built-in heuristic summarizer to extract 3–5 crisp bullet lines without requiring an API key).*

### Step 3: Run the Backend
```bash
npm start
```
The server starts on port `5000`:
- **API Base**: `http://localhost:5000/api/brief`
- **Health Check**: `http://localhost:5000/api/health`
- **Manual Refresh**: `POST http://localhost:5000/api/brief/refresh`

### Step 4: Scheduled Cron
- Configured to run automatically every morning at **7:00 AM IST** (`Asia/Kolkata`).
- Fetches new articles from the 6 categories, generates AI summaries, and updates SQLite.

---

## 2. Android App Setup & Run Instructions

### Step 1: Open in Android Studio
1. Launch Android Studio.
2. Select **Open** and choose the project root folder.
3. Gradle will automatically sync dependencies (Room, Retrofit, Coil, Compose M3).

### Step 2: Run the App
- Select your target device or emulator (Pixel 8 / Android 14+ recommended) and press **Run (Shift + F10)**.
- **Offline First**: The app immediately boots with pre-seeded briefings across all 6 categories stored in Room DB.
- **Connecting to Local Backend**:
  - In the Android Emulator, `http://10.0.2.2:5000/` automatically routes to your computer's `localhost:5000`.
  - On a physical device on the same Wi-Fi, tap the **Settings icon** in the top bar and enter `http://<YOUR_COMPUTER_IP>:5000/`.

---

## 3. GitHub Push & In-App Over-The-Air (OTA) Updates

The application is built with a built-in **GitHub Over-The-Air Update Engine**. Once installed on your phone, the app queries your GitHub repository releases, alerts you with a dialog and notification when a new version is available, downloads the new APK with a live progress bar upon your consent, and triggers Android's PackageInstaller.

### Step 1: Push this Project to GitHub
Initialize your Git repository and push the project to your GitHub account:

```bash
git init
git add .
git commit -m "feat: Daily Brief initial release with OTA auto-updater"
git branch -M main
git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/daily-brief.git
git push -u origin main
```

### Step 2: Download & Install the Initial APK on Your Mobile Phone
The project has already compiled the signed debug APK ready for installation:
1. Locate the built APK at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```
2. Transfer `app-debug.apk` to your phone via USB cable, Google Drive, WhatsApp, or email, and tap to install it.
3. On first launch, open **Settings** (gear icon) in the app top bar and confirm your **GitHub Owner** and **Repository name** (e.g., `kamalbaitha` / `daily-brief`).

### Step 3: Publishing Further Updates to GitHub
Whenever you make updates to the app and want to deliver them to your phone:
1. Bump the `versionCode` (e.g., from `1` to `2`) and `versionName` (e.g., from `"1.0"` to `"1.1"`) in `app/build.gradle.kts`.
2. Push a git tag to GitHub:
   ```bash
   git tag v1.1
   git push origin v1.1
   ```
3. The included GitHub Actions workflow (`.github/workflows/release-apk.yml`) will automatically:
   - Build `DailyBrief-v1.1.apk`
   - Publish a new GitHub Release with the attached APK asset.
   *(Alternatively, build locally using `./gradlew assembleDebug` and attach the APK manually to a GitHub Release under Releases > Draft a new release).*

### Step 4: In-App Detection, Consent & 1-Tap Update
- Every time the app opens, it checks GitHub's Releases API for newer tags.
- You can also tap the **Update icon** in the top bar or inside **Settings > Check for GitHub Update Now**.
- When an update is detected, the app displays an **Update Available Dialog** with release notes and prompts:
  > *"v1.0 ➔ v1.1 — Do you want to download and install this update now?"*
- Once you tap **"Download & Install"**, the app streams the APK, shows a live progress percentage, and uses Android's `FileProvider` to prompt the system installer to update the app seamlessly.

---

## Features Implemented

1. **6 Required News Categories**:
   - Global markets & economy
   - Indian stock market & IPO
   - Railways & infrastructure
   - Technology & AI
   - Health & fitness
   - General news
2. **OpenAI Summarization**: 3–5 bullet points summarizing each story's key figures, events, and impacts.
3. **SQLite Database**: Persistent server storage with schema indexing.
4. **Daily 7:00 AM IST Scheduler**: Automated daily news aggregation pipeline.
5. **Room Database Offline Caching**: Full offline browsing capability via Room with reactive `Flow`.
6. **Pull-to-Refresh & Manual Refresh**: Pull-to-refresh swipe gesture and dedicated manual refresh buttons in the top bar and floating action button.
7. **Loading Skeleton UI**: Shimmer card placeholders displayed during data loads.
8. **Daily 8:00 AM Push Notification**: Notification scheduled with AlarmManager displaying *"Your Daily Brief is ready"* (plus in-app instant test trigger).
9. **Material 3 Design**: Dark & Light mode toggle, custom adaptive icon, and editorial typography.
