/**
 * Daily Brief Express & HTTP Backend Server
 * Serves GET /api/brief, handles manual refresh, and coordinates the 7 AM scheduler.
 */

const http = require('http');
const url = require('url');
const path = require('path');
const fs = require('fs');

// Load environment variables if .env exists
const envPath = path.resolve(__dirname, '../.env');
if (fs.existsSync(envPath)) {
  const envContent = fs.readFileSync(envPath, 'utf8');
  envContent.split('\n').forEach(line => {
    const trimmed = line.trim();
    if (trimmed && !trimmed.startsWith('#') && trimmed.includes('=')) {
      const [k, ...v] = trimmed.split('=');
      process.env[k.trim()] = v.join('=').trim();
    }
  });
}

const { getDatabase } = require('./db');
const { CATEGORIES, seedDatabaseIfEmpty, runDailyUpdate } = require('./fetcher');
const { initScheduler } = require('./scheduler');

const PORT = parseInt(process.env.DEFAULT_APP_PORT || process.env.BACKEND_PORT || '3000', 10);
const PUBLIC_DIR = path.resolve(__dirname, '../../public');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.ico': 'image/x-icon',
  '.svg': 'image/svg+xml'
};

// Helper to set standard CORS & JSON headers
function setStandardHeaders(res) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
}

// Static file server helper for PWA
function serveStaticFile(reqPath, res) {
  let relativePath = reqPath === '/' ? '/index.html' : reqPath;
  const filePath = path.join(PUBLIC_DIR, relativePath);

  // Security check to prevent directory traversal
  if (!filePath.startsWith(PUBLIC_DIR)) {
    res.statusCode = 403;
    res.end('Access Denied');
    return true;
  }

  if (fs.existsSync(filePath) && fs.statSync(filePath).isFile()) {
    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';
    res.setHeader('Content-Type', contentType);
    if (ext === '.html' || ext === '.json' || ext === '.js') {
      res.setHeader('Cache-Control', 'no-cache');
    } else {
      res.setHeader('Cache-Control', 'public, max-age=86400');
    }
    res.statusCode = 200;
    fs.createReadStream(filePath).pipe(res);
    return true;
  }

  return false;
}

// Request handler supporting standard Node HTTP & Express compatibility
async function handleRequest(req, res) {
  setStandardHeaders(res);

  if (req.method === 'OPTIONS') {
    res.statusCode = 204;
    res.end();
    return;
  }

  const parsedUrl = url.parse(req.url, true);
  const pathname = parsedUrl.pathname;

  // 1. Health check
  if (pathname === '/api/health') {
    const db = getDatabase();
    res.setHeader('Content-Type', 'application/json');
    res.statusCode = 200;
    res.end(JSON.stringify({
      status: 'ok',
      service: 'Daily Brief Backend & PWA API',
      timestamp: new Date().toISOString(),
      articlesCount: db.getCount(),
      categories: CATEGORIES.map(c => c.name)
    }, null, 2));
    return;
  }

  // 2. Primary Endpoint: GET /api/brief
  if (pathname === '/api/brief' && req.method === 'GET') {
    const db = getDatabase();
    const allArticles = db.getAllArticles();
    const grouped = {};
    for (const cat of CATEGORIES) {
      grouped[cat.name] = [];
    }
    for (const art of allArticles) {
      if (!grouped[art.category]) {
        grouped[art.category] = [];
      }
      grouped[art.category].push(art);
    }

    res.setHeader('Content-Type', 'application/json');
    res.statusCode = 200;
    res.end(JSON.stringify({
      success: true,
      lastUpdated: new Date().toISOString(),
      count: allArticles.length,
      categories: CATEGORIES,
      articles: allArticles,
      grouped: grouped
    }, null, 2));
    return;
  }

  // 3. Category Endpoint: GET /api/brief/:category
  if (pathname.startsWith('/api/brief/') && req.method === 'GET') {
    const rawCategory = decodeURIComponent(pathname.replace('/api/brief/', ''));
    if (rawCategory && rawCategory !== 'refresh') {
      const db = getDatabase();
      const articles = db.getArticlesByCategory(rawCategory);
      res.setHeader('Content-Type', 'application/json');
      res.statusCode = 200;
      res.end(JSON.stringify({
        success: true,
        category: rawCategory,
        count: articles.length,
        articles: articles
      }, null, 2));
      return;
    }
  }

  // 4. Manual Refresh: POST /api/brief/refresh or GET /api/brief/refresh
  if (pathname === '/api/brief/refresh' && (req.method === 'POST' || req.method === 'GET')) {
    console.log('[Server] Manual refresh triggered via API.');
    try {
      const result = await runDailyUpdate();
      const db = getDatabase();
      const allArticles = db.getAllArticles();

      res.setHeader('Content-Type', 'application/json');
      res.statusCode = 200;
      res.end(JSON.stringify({
        success: true,
        message: 'Daily brief refreshed successfully',
        updated: result.updatedCount,
        count: allArticles.length,
        articles: allArticles
      }, null, 2));
    } catch (err) {
      res.setHeader('Content-Type', 'application/json');
      res.statusCode = 500;
      res.end(JSON.stringify({
        success: false,
        error: err.message
      }));
    }
    return;
  }

  // 5. Static PWA File Serving (index.html, styles.css, app.js, manifest.json, sw.js, assets)
  if (!pathname.startsWith('/api/')) {
    const served = serveStaticFile(pathname, res);
    if (served) return;

    // Fallback to /index.html for client-side routing
    const fallbackServed = serveStaticFile('/index.html', res);
    if (fallbackServed) return;
  }

  // 404 Not Found
  res.setHeader('Content-Type', 'application/json');
  res.statusCode = 404;
  res.end(JSON.stringify({ error: 'Endpoint not found', path: pathname }));
}

// Start Server
async function startServer() {
  await seedDatabaseIfEmpty();
  initScheduler();

  const server = http.createServer(handleRequest);

  server.listen(PORT, '0.0.0.0', () => {
    console.log(`====================================================`);
    console.log(` Daily Brief Backend Service is running!`);
    console.log(` Port: ${PORT}`);
    console.log(` Health: http://localhost:${PORT}/api/health`);
    console.log(` API Endpoint: http://localhost:${PORT}/api/brief`);
    console.log(` Refresh Trigger: POST http://localhost:${PORT}/api/brief/refresh`);
    console.log(` Scheduled: Daily at 7:00 AM IST`);
    console.log(`====================================================`);
  });
}

startServer().catch(err => {
  console.error('[Server] Fatal startup error:', err);
});
