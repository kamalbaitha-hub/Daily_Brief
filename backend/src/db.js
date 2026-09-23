const path = require('path');
const fs = require('fs');

const DB_DIR = path.resolve(__dirname, '../data');
if (!fs.existsSync(DB_DIR)) {
  fs.mkdirSync(DB_DIR, { recursive: true });
}
const DB_FILE = path.join(DB_DIR, 'daily_brief.sqlite');

let dbInstance = null;

function getDatabase() {
  if (dbInstance) return dbInstance;

  try {
    // 1. Try built-in Node 22+ SQLite
    const { DatabaseSync } = require('node:sqlite');
    const db = new DatabaseSync(DB_FILE);

    // Initialize Schema
    db.exec(`
      CREATE TABLE IF NOT EXISTS articles (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category TEXT NOT NULL,
        headline TEXT NOT NULL,
        summary TEXT NOT NULL,
        original_content TEXT,
        image_url TEXT,
        source TEXT,
        source_url TEXT UNIQUE,
        published_at TEXT,
        created_at TEXT DEFAULT CURRENT_TIMESTAMP
      );
      CREATE INDEX IF NOT EXISTS idx_articles_category ON articles(category);
      CREATE INDEX IF NOT EXISTS idx_articles_created ON articles(created_at DESC);
    `);

    dbInstance = {
      type: 'node:sqlite',
      insertOrUpdateArticle(article) {
        const stmt = db.prepare(`
          INSERT INTO articles (category, headline, summary, original_content, image_url, source, source_url, published_at)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?)
          ON CONFLICT(source_url) DO UPDATE SET
            category = excluded.category,
            headline = excluded.headline,
            summary = excluded.summary,
            original_content = excluded.original_content,
            image_url = excluded.image_url,
            source = excluded.source,
            published_at = excluded.published_at,
            created_at = CURRENT_TIMESTAMP
        `);
        return stmt.run(
          article.category,
          article.headline,
          article.summary,
          article.original_content || '',
          article.image_url || '',
          article.source || '',
          article.source_url || '',
          article.published_at || new Date().toISOString()
        );
      },
      getAllArticles() {
        const stmt = db.prepare(`SELECT * FROM articles ORDER BY id DESC`);
        return stmt.all();
      },
      getArticlesByCategory(category) {
        const stmt = db.prepare(`SELECT * FROM articles WHERE LOWER(category) = LOWER(?) ORDER BY id DESC`);
        return stmt.all(category);
      },
      getBriefGrouped() {
        const all = this.getAllArticles();
        const grouped = {};
        for (const item of all) {
          if (!grouped[item.category]) {
            grouped[item.category] = [];
          }
          grouped[item.category].push(item);
        }
        return grouped;
      },
      getCount() {
        const stmt = db.prepare(`SELECT count(*) as count FROM articles`);
        const res = stmt.get();
        return res ? res.count : 0;
      }
    };
    return dbInstance;
  } catch (err) {
    console.warn('Falling back to SQLite file driver:', err.message);
    // Lightweight JSON/SQLite compatibility layer if native SQLite is not available
    const JSON_FILE = path.join(DB_DIR, 'daily_brief.json');
    let memoryArticles = [];
    if (fs.existsSync(JSON_FILE)) {
      try {
        memoryArticles = JSON.parse(fs.readFileSync(JSON_FILE, 'utf8'));
      } catch (e) {
        memoryArticles = [];
      }
    }

    const persist = () => {
      fs.writeFileSync(JSON_FILE, JSON.stringify(memoryArticles, null, 2), 'utf8');
    };

    dbInstance = {
      type: 'json-fallback',
      insertOrUpdateArticle(article) {
        const existingIdx = memoryArticles.findIndex(a => a.source_url && a.source_url === article.source_url);
        const entry = {
          id: existingIdx >= 0 ? memoryArticles[existingIdx].id : memoryArticles.length + 1,
          category: article.category,
          headline: article.headline,
          summary: article.summary,
          original_content: article.original_content || '',
          image_url: article.image_url || '',
          source: article.source || '',
          source_url: article.source_url || '',
          published_at: article.published_at || new Date().toISOString(),
          created_at: new Date().toISOString()
        };
        if (existingIdx >= 0) {
          memoryArticles[existingIdx] = entry;
        } else {
          memoryArticles.unshift(entry);
        }
        persist();
        return { changes: 1 };
      },
      getAllArticles() {
        return [...memoryArticles];
      },
      getArticlesByCategory(category) {
        return memoryArticles.filter(a => a.category.toLowerCase() === category.toLowerCase());
      },
      getBriefGrouped() {
        const grouped = {};
        for (const item of memoryArticles) {
          if (!grouped[item.category]) {
            grouped[item.category] = [];
          }
          grouped[item.category].push(item);
        }
        return grouped;
      },
      getCount() {
        return memoryArticles.length;
      }
    };
    return dbInstance;
  }
}

module.exports = {
  getDatabase
};
