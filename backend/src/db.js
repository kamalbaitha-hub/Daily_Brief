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
      purgeArticlesOlderThan(maxHours = 24) {
        try {
          const all = db.prepare(`SELECT id, published_at, created_at FROM articles`).all();
          const expiredIds = all.filter(row => {
            const pub = row.published_at ? new Date(row.published_at).getTime() : 0;
            const cre = row.created_at ? new Date(row.created_at).getTime() : 0;
            const t = pub || cre;
            return t && !isNaN(t) && (Date.now() - t > maxHours * 60 * 60 * 1000);
          }).map(r => r.id);

          if (expiredIds.length > 0) {
            const placeholders = expiredIds.map(() => '?').join(',');
            const delStmt = db.prepare(`DELETE FROM articles WHERE id IN (${placeholders})`);
            delStmt.run(...expiredIds);
            console.log(`[Database] Purged ${expiredIds.length} articles older than ${maxHours}h`);
            return expiredIds.length;
          }
          return 0;
        } catch (e) {
          console.error('[Database] Purge error:', e.message);
          return 0;
        }
      },
      getAllArticles() {
        this.purgeArticlesOlderThan(24);
        const stmt = db.prepare(`SELECT * FROM articles ORDER BY id DESC`);
        return stmt.all().filter(a => {
          const t = a.published_at ? new Date(a.published_at).getTime() : new Date(a.created_at).getTime();
          return !t || isNaN(t) || (Date.now() - t <= 24 * 60 * 60 * 1000);
        });
      },
      getArticlesByCategory(category) {
        this.purgeArticlesOlderThan(24);
        const stmt = db.prepare(`SELECT * FROM articles WHERE LOWER(category) = LOWER(?) ORDER BY id DESC`);
        return stmt.all(category).filter(a => {
          const t = a.published_at ? new Date(a.published_at).getTime() : new Date(a.created_at).getTime();
          return !t || isNaN(t) || (Date.now() - t <= 24 * 60 * 60 * 1000);
        });
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
        this.purgeArticlesOlderThan(24);
        return this.getAllArticles().length;
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
      purgeArticlesOlderThan(maxHours = 24) {
        const cutoff = Date.now() - maxHours * 60 * 60 * 1000;
        const initial = memoryArticles.length;
        memoryArticles = memoryArticles.filter(a => {
          const t = a.published_at ? new Date(a.published_at).getTime() : new Date(a.created_at).getTime();
          return !t || isNaN(t) || (t >= cutoff);
        });
        if (initial !== memoryArticles.length) {
          persist();
        }
        return initial - memoryArticles.length;
      },
      getAllArticles() {
        this.purgeArticlesOlderThan(24);
        return [...memoryArticles];
      },
      getArticlesByCategory(category) {
        this.purgeArticlesOlderThan(24);
        return memoryArticles.filter(a => a.category.toLowerCase() === category.toLowerCase());
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
        this.purgeArticlesOlderThan(24);
        return memoryArticles.length;
      }
    };
    return dbInstance;
  }
}

module.exports = {
  getDatabase
};
