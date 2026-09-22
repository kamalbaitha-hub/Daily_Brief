# Daily Brief - Backend Service

Node.js + Express backend service for **Daily Brief**, an automated AI-powered news briefing system.

## Features
- **News Ingestion**: Ingests breaking news from Google News RSS feeds and optional NewsAPI.
- **6 Core Categories**:
  - Global markets & economy
  - Indian stock market & IPO
  - Railways & infrastructure
  - Technology & AI
  - Health & fitness
  - General news
- **OpenAI Summarization**: Condenses news articles into 3–5 high-impact bullet lines using GPT-4o-mini (with built-in heuristic fallback if no key is provided).
- **SQLite Database**: Persistent SQLite storage with auto-migration (`data/daily_brief.sqlite`).
- **Automated Scheduler**: Daily cron job configured for **7:00 AM IST** (`Asia/Kolkata`) to fetch, summarize, and update briefs.
- **REST Endpoints**:
  - `GET /api/brief`: Returns today's categorized briefings.
  - `GET /api/brief/:category`: Returns briefings filtered by category.
  - `POST /api/brief/refresh`: Manually triggers fetch + AI summarization.
  - `GET /api/health`: Service status and article count.

---

## Quick Start / Setup

### 1. Prerequisites
- Node.js v18+ (Node.js 22 recommended)
- npm or yarn

### 2. Installation
```bash
cd backend
npm install
```

### 3. Environment Configuration
Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```
Edit `.env` to configure your keys:
```env
BACKEND_PORT=5000
OPENAI_API_KEY=sk-...your-openai-key...
NEWS_API_KEY=optional_newsapi_key
TZ=Asia/Kolkata
```
*(Note: If `OPENAI_API_KEY` is not provided, the backend seamlessly falls back to its intelligent heuristic summarizer, ensuring zero downtime and fully offline execution!)*

### 4. Running the Server
```bash
# Start the backend server
npm start

# Or with live reloading:
npm run dev
```

The server will be available at:
- **API Base**: `http://localhost:5000/api/brief`
- **Health Check**: `http://localhost:5000/api/health`

### 5. Manual Pipeline Trigger
To immediately run a full fetch + summarization cycle from command line:
```bash
npm run refresh
```
Or via HTTP:
```bash
curl -X POST http://localhost:5000/api/brief/refresh
```

---

## Connecting from Android App / Emulator
- When running in the Android Studio Emulator, use `http://10.0.2.2:5000/api/brief` to reach your host machine's port 5000.
- When running on a physical Android device on the same local Wi-Fi, use your machine's local IP address (e.g. `http://192.168.1.X:5000/api/brief`).
- The Android app also includes pre-bundled offline Room database caching and an in-app server settings dialog to easily point to any custom URL.
