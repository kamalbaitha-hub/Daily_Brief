// Daily Brief PWA Application Script

let currentCategory = 'all'; // 'all', 'newspapers', 'magazines', 'recommendations', or specific category
let recSubTab = 'stocks'; // 'stocks' or 'funds'
let deferredPrompt = null;
let articlesList = [...INITIAL_ARTICLES];

document.addEventListener('DOMContentLoaded', () => {
  initPwaInstall();
  initTheme();
  setupEventListeners();
  loadArticlesFromBackend();
  renderApp();
});

// PWA Service Worker & Install Flow
function initPwaInstall() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/sw.js')
      .then((reg) => console.log('PWA ServiceWorker registered with scope:', reg.scope))
      .catch((err) => console.log('PWA ServiceWorker registration failed:', err));
  }

  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    const installBtn = document.getElementById('btn-install-header');
    if (installBtn) installBtn.style.display = 'inline-flex';
    const toast = document.getElementById('pwa-install-toast');
    if (toast && !localStorage.getItem('pwa_dismissed')) {
      toast.style.display = 'block';
    }
  });

  window.addEventListener('appinstalled', () => {
    console.log('Daily Brief PWA was installed successfully!');
    deferredPrompt = null;
    const installBtn = document.getElementById('btn-install-header');
    if (installBtn) installBtn.style.display = 'none';
    const toast = document.getElementById('pwa-install-toast');
    if (toast) toast.style.display = 'none';
  });
}

function promptInstall() {
  if (deferredPrompt) {
    deferredPrompt.prompt();
    deferredPrompt.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('User accepted the PWA install prompt');
      }
      deferredPrompt = null;
    });
  } else {
    alert('To install Daily Brief on your Laptop/Desktop:\n1. On Chrome/Edge: Click the (Install) icon in the address bar.\n2. Or click the 3-dots browser menu -> "Install Daily Brief" or "Create Shortcut".');
  }
}

// Dark / Light Theme Toggle
function initTheme() {
  const savedTheme = localStorage.getItem('daily_brief_theme') || 'dark';
  document.documentElement.setAttribute('data-theme', savedTheme);
  updateThemeIcon(savedTheme);
}

function toggleTheme() {
  const current = document.documentElement.getAttribute('data-theme') || 'dark';
  const next = current === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', next);
  localStorage.setItem('daily_brief_theme', next);
  updateThemeIcon(next);
}

function updateThemeIcon(theme) {
  const icon = document.getElementById('theme-icon');
  if (icon) {
    icon.innerHTML = theme === 'dark' ? '☀️' : '🌙';
  }
}

// Data fetching from backend if running
async function loadArticlesFromBackend() {
  try {
    const res = await fetch('/api/brief');
    if (res.ok) {
      const data = await res.json();
      if (data.articles && data.articles.length > 0) {
        articlesList = data.articles;
        if (currentCategory === 'all' || !isSpecialCategory(currentCategory)) {
          renderApp();
        }
      }
    }
  } catch (err) {
    console.log('Using pre-cached client initial data');
  }
}

function setupEventListeners() {
  // Check URL params for initial tab
  const urlParams = new URLSearchParams(window.location.search);
  const tabParam = urlParams.get('tab');
  if (tabParam) {
    currentCategory = tabParam;
  }

  // Header buttons
  document.getElementById('btn-theme-toggle')?.addEventListener('click', toggleTheme);
  document.getElementById('btn-install-header')?.addEventListener('click', promptInstall);
  document.getElementById('btn-pwa-install-now')?.addEventListener('click', promptInstall);
  document.getElementById('btn-pwa-dismiss')?.addEventListener('click', () => {
    const toast = document.getElementById('pwa-install-toast');
    if (toast) toast.style.display = 'none';
    localStorage.setItem('pwa_dismissed', 'true');
  });

  // Category filter chips
  document.querySelectorAll('.filter-chip').forEach((chip) => {
    chip.addEventListener('click', (e) => {
      const cat = chip.getAttribute('data-category');
      selectCategory(cat);
    });
  });

  // Refresh
  document.getElementById('btn-refresh')?.addEventListener('click', () => {
    refreshFeed();
  });
}

function isSpecialCategory(cat) {
  return ['newspapers', 'magazines', 'recommendations'].includes(cat);
}

function selectCategory(cat) {
  currentCategory = cat;

  // Update chip active states
  document.querySelectorAll('.filter-chip').forEach((chip) => {
    if (chip.getAttribute('data-category') === cat) {
      chip.classList.add('active');
    } else {
      chip.classList.remove('active');
    }
  });

  renderApp();
}

function renderApp() {
  const mainContent = document.getElementById('main-content');
  if (!mainContent) return;

  if (currentCategory === 'newspapers') {
    renderNewspaperCorner(mainContent);
  } else if (currentCategory === 'magazines') {
    renderMagazineCorner(mainContent);
  } else if (currentCategory === 'recommendations') {
    renderRecommendationsSection(mainContent);
  } else {
    renderNewsFeed(mainContent);
  }
}

// 1. Newspaper Corner View
function renderNewspaperCorner(container) {
  container.innerHTML = `
    <div class="section-banner" style="border-color: rgba(30, 58, 138, 0.4); background: linear-gradient(135deg, rgba(30, 58, 138, 0.2), rgba(37, 99, 235, 0.1));">
      <div class="section-banner-text">
        <span style="font-size: 0.75rem; font-weight: 800; color: #60a5fa; letter-spacing: 0.05em; display: block; margin-bottom: 4px;">📰 NEWSPAPER CORNER</span>
        <h2>Daily English Newspaper E-Editions</h2>
        <p>Instant digital access to front pages, editorial sections, and daily epapers of India's leading publications.</p>
      </div>
      <div style="font-size: 0.85rem; color: #94a3b8; font-weight: 600;">${NEWSPAPERS.length} E-Papers Available</div>
    </div>

    <div class="tiles-grid">
      ${NEWSPAPERS.map((item) => `
        <div class="pub-tile" onclick="window.open('${item.eEditionUrl}', '_blank', 'noopener,noreferrer')">
          <div>
            <div class="pub-tile-top">
              <div class="pub-icon-box" style="background: ${item.accentColor}22; color: ${item.accentColor}">
                📰
              </div>
              <div class="pub-external-icon">↗</div>
            </div>
            <span class="pub-badge" style="background: ${item.accentColor}22; color: ${item.accentColor}">${item.badgeLabel}</span>
            <div class="pub-title">${item.title}</div>
            <div class="pub-category" style="color: ${item.accentColor}">${item.category}</div>
            <div class="pub-desc">${item.description}</div>
          </div>
          <div class="pub-action" style="color: ${item.accentColor}">
            <span>Tap to read e-edition</span>
            <span>&rarr;</span>
          </div>
        </div>
      `).join('')}
    </div>
  `;
}

// 2. Magazine Corner View
function renderMagazineCorner(container) {
  container.innerHTML = `
    <div class="section-banner" style="border-color: rgba(220, 38, 38, 0.4); background: linear-gradient(135deg, rgba(220, 38, 38, 0.15), rgba(234, 88, 12, 0.1));">
      <div class="section-banner-text">
        <span style="font-size: 0.75rem; font-weight: 800; color: #f87171; letter-spacing: 0.05em; display: block; margin-bottom: 4px;">📖 MAGAZINE CORNER</span>
        <h2>Popular E-Magazines & Periodicals</h2>
        <p>Curated monthly and fortnightly digests covering Politics, Science, Business, and Health & Nutrition.</p>
      </div>
      <div style="font-size: 0.85rem; color: #94a3b8; font-weight: 600;">${MAGAZINES.length} Magazines Available</div>
    </div>

    <div class="tiles-grid">
      ${MAGAZINES.map((item) => `
        <div class="pub-tile" onclick="window.open('${item.eEditionUrl}', '_blank', 'noopener,noreferrer')">
          <div>
            <div class="pub-tile-top">
              <div class="pub-icon-box" style="background: ${item.accentColor}22; color: ${item.accentColor}">
                ${item.icon === 'science' ? '🔬' : item.icon === 'business' ? '💼' : item.icon === 'health' ? '❤️' : '📖'}
              </div>
              <div class="pub-external-icon">↗</div>
            </div>
            <span class="pub-badge" style="background: ${item.accentColor}22; color: ${item.accentColor}">${item.badgeLabel}</span>
            <div class="pub-title">${item.title}</div>
            <div class="pub-category" style="color: ${item.accentColor}">${item.category}</div>
            <div class="pub-desc">${item.description}</div>
          </div>
          <div class="pub-action" style="color: ${item.accentColor}">
            <span>Tap to read magazine</span>
            <span>&rarr;</span>
          </div>
        </div>
      `).join('')}
    </div>
  `;
}

// 3. Recommendations Section (Stocks & Mutual Funds)
function renderRecommendationsSection(container) {
  container.innerHTML = `
    <div class="section-banner" style="border-color: rgba(16, 185, 129, 0.4); background: linear-gradient(135deg, rgba(16, 185, 129, 0.15), rgba(5, 150, 105, 0.1));">
      <div class="section-banner-text">
        <span style="font-size: 0.75rem; font-weight: 800; color: #34d399; letter-spacing: 0.05em; display: block; margin-bottom: 4px;">📈 MARKET RECOMMENDATIONS</span>
        <h2>Executive Stock Picks & Mutual Funds</h2>
        <p>Research-backed high-conviction ideas, valuation multiples, and multi-year CAGR mutual fund analysis.</p>
      </div>
    </div>

    <div class="sub-tabs-row">
      <button class="sub-tab-btn ${recSubTab === 'stocks' ? 'active' : ''}" onclick="switchRecTab('stocks')">
        📊 Indian Stock Picks (${STOCK_PICKS.length})
      </button>
      <button class="sub-tab-btn ${recSubTab === 'funds' ? 'active' : ''}" onclick="switchRecTab('funds')">
        🏛️ Mutual Funds (${MUTUAL_FUNDS.length})
      </button>
    </div>

    ${recSubTab === 'stocks' ? renderStocksList() : renderFundsList()}
  `;
}

function switchRecTab(tab) {
  recSubTab = tab;
  renderApp();
}

function renderStocksList() {
  return `
    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px;">
      ${STOCK_PICKS.map((stock) => `
        <div class="article-card">
          <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">
            <div>
              <div style="font-size: 1.15rem; font-weight: 800;">${stock.companyName}</div>
              <div style="font-size: 0.8rem; color: #94a3b8; font-weight: 600;">NSE/BSE: ${stock.symbol} • ${stock.sector}</div>
            </div>
            <span style="background: rgba(16, 185, 129, 0.15); color: #10b981; font-size: 0.75rem; font-weight: 800; padding: 4px 8px; border-radius: 6px;">
              ${stock.action}
            </span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(3, 1fr); background: var(--bg-surface); padding: 12px; border-radius: 10px; margin: 12px 0; text-align: center; border: 1px solid var(--border-color);">
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">CMP</div>
              <div style="font-weight: 700; font-size: 0.95rem;">${stock.currentPrice}</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">Target</div>
              <div style="font-weight: 700; font-size: 0.95rem; color: #10b981;">${stock.targetPrice}</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">Upside</div>
              <div style="font-weight: 800; font-size: 0.95rem; color: #10b981;">${stock.potentialUpside}</div>
            </div>
          </div>

          <p style="font-size: 0.85rem; color: var(--text-sub); line-height: 1.45; margin-bottom: 12px;">
            ${stock.rationale}
          </p>

          <div style="display: flex; flex-wrap: wrap; gap: 8px; border-top: 1px solid var(--border-color); padding-top: 10px;">
            ${stock.keyMetrics.map(m => `
              <span style="background: rgba(255,255,255,0.05); padding: 3px 8px; border-radius: 6px; font-size: 0.72rem; color: #cbd5e1;">
                <strong>${m.label}:</strong> ${m.value}
              </span>
            `).join('')}
          </div>
        </div>
      `).join('')}
    </div>
  `;
}

function renderFundsList() {
  return `
    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px;">
      ${MUTUAL_FUNDS.map((fund) => `
        <div class="article-card">
          <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">
            <div>
              <div style="font-size: 1.15rem; font-weight: 800;">${fund.fundName}</div>
              <div style="font-size: 0.8rem; color: #94a3b8; font-weight: 600;">${fund.fundHouse} • ${fund.category}</div>
            </div>
            <span style="background: rgba(245, 158, 11, 0.15); color: #f59e0b; font-size: 0.75rem; font-weight: 800; padding: 4px 8px; border-radius: 6px;">
              ${'★'.repeat(fund.rating)} Rating
            </span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(3, 1fr); background: var(--bg-surface); padding: 12px; border-radius: 10px; margin: 12px 0; text-align: center; border: 1px solid var(--border-color);">
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">1Y Return</div>
              <div style="font-weight: 700; font-size: 0.95rem; color: #10b981;">${fund.return1Yr}</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">3Y CAGR</div>
              <div style="font-weight: 700; font-size: 0.95rem; color: #10b981;">${fund.return3Yr}</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: #94a3b8;">5Y CAGR</div>
              <div style="font-weight: 800; font-size: 0.95rem; color: #10b981;">${fund.return5Yr}</div>
            </div>
          </div>

          <p style="font-size: 0.85rem; color: var(--text-sub); line-height: 1.45; margin-bottom: 12px;">
            ${fund.verdictAndAnalysis}
          </p>

          <div style="font-size: 0.75rem; color: #94a3b8; border-top: 1px solid var(--border-color); padding-top: 10px;">
            <strong>Top Holdings:</strong> ${fund.topHoldings.join(', ')}
          </div>
        </div>
      `).join('')}
    </div>
  `;
}

// 4. Standard Articles Feed
function renderNewsFeed(container) {
  let filtered = articlesList;
  if (currentCategory !== 'all') {
    filtered = articlesList.filter(a => a.category.toLowerCase().includes(currentCategory.toLowerCase()));
  }

  if (filtered.length === 0) {
    container.innerHTML = `
      <div style="text-align: center; padding: 60px 20px;">
        <div style="font-size: 2.5rem; margin-bottom: 12px;">📰</div>
        <h3>No briefs found for this category</h3>
        <p style="color: #94a3b8; margin: 8px 0 16px;">Try switching back to All Categories or refresh the news feed.</p>
        <button class="filter-chip active" onclick="selectCategory('all')">Show All Categories</button>
      </div>
    `;
    return;
  }

  const [lead, ...concise] = filtered;

  container.innerHTML = `
    <div class="articles-feed">
      <!-- Concise Top Article Hero Banner -->
      <div class="lead-article-card">
        <img class="lead-article-image" src="${lead.image_url || 'https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800'}" alt="${lead.headline}">
        <div class="lead-article-content">
          <div class="article-meta">
            <span class="article-category-badge">${lead.category}</span>
            <span>⏱️ ${lead.reading_time || '3 min read'}</span>
          </div>

          <!-- News Heading -->
          <h2 class="article-headline" style="font-size: 1.35rem; margin-bottom: 14px;">${lead.headline}</h2>

          <!-- Below heading: Few new points from contents -->
          <div class="brief-points-container">
            <div class="brief-section-title">Key Points from Contents:</div>
            <ul class="article-summary-bullets">
              ${formatBullets(lead.summary, lead.headline)}
            </ul>
          </div>

          <!-- Below this place: Background and full story -->
          <div class="brief-background-container">
            <div class="brief-section-title">Background & Full Story</div>
            <p class="brief-background-text">${lead.original_content || 'Comprehensive contextual background and sector analysis reported by Daily Brief newsroom.'}</p>
          </div>

          <!-- With link Read original publication -->
          <div class="brief-action-row">
            ${lead.source_url ? `
              <a href="${lead.source_url}" target="_blank" rel="noopener noreferrer" class="btn-read-pub">
                <span>Read Original Publication</span>
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path><polyline points="15 3 21 3 21 9"></polyline><line x1="10" y1="14" x2="21" y2="3"></line></svg>
              </a>
            ` : ''}
            <span class="brief-source-meta">📰 ${lead.source || 'Daily Brief Wire'}</span>
          </div>
        </div>
      </div>

      <!-- Concise Subsequent Cards -->
      ${concise.length > 0 ? `
        <div style="margin-top: 16px;">
          <div style="font-size: 0.85rem; font-weight: 800; color: #60a5fa; letter-spacing: 0.05em; margin-bottom: 16px;">
            MORE CATEGORY BRIEFS (${concise.length})
          </div>
          <div class="concise-cards-grid">
            ${concise.map(art => `
              <div class="article-card">
                <div class="article-meta">
                  <span class="article-category-badge">${art.category}</span>
                  <span>⏱️ ${art.reading_time || '2 min read'}</span>
                </div>

                <!-- News Heading -->
                <h3 class="article-headline" style="font-size: 1.15rem; margin-bottom: 12px;">${art.headline}</h3>

                <!-- Below heading: Few new points from contents -->
                <div class="brief-points-container">
                  <div class="brief-section-title">Key Points from Contents:</div>
                  <ul class="article-summary-bullets">
                    ${formatBullets(art.summary, art.headline)}
                  </ul>
                </div>

                <!-- Below this place: Background and full story -->
                <div class="brief-background-container">
                  <div class="brief-section-title">Background & Full Story</div>
                  <p class="brief-background-text">${art.original_content || 'Comprehensive background and ongoing developments tracked by Daily Brief.'}</p>
                </div>

                <!-- With link Read original publication -->
                <div class="brief-action-row">
                  ${art.source_url ? `
                    <a href="${art.source_url}" target="_blank" rel="noopener noreferrer" class="btn-read-pub">
                      <span>Read Original Publication</span>
                      <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path><polyline points="15 3 21 3 21 9"></polyline><line x1="10" y1="14" x2="21" y2="3"></line></svg>
                    </a>
                  ` : ''}
                  <span class="brief-source-meta">📰 ${art.source || 'Daily Wire'}</span>
                </div>
              </div>
            `).join('')}
          </div>
        </div>
      ` : ''}
    </div>
  `;
}

function formatBullets(summary, headline = '') {
  if (!summary) return '<li>Latest industry and market updates curated by Daily Brief.</li>';
  const normHeadline = headline.toLowerCase().replace(/[^a-z0-9]/g, ' ').trim();
  const boilerplatePatterns = [
    'verified real-time reporting',
    'verified real time reporting',
    'fast executive summary',
    'compiled directly from live news feeds',
    'real-time news wire',
    'live coverage:'
  ];
  const prefixRegex = /^(Key\s*context|Details?|Context|Live\s*coverage|Executive\s*summary|Summary|Update|Analysis|Overview|Background|Highlights?)\s*[:\-]\s*/i;

  const lines = summary.split('\n')
    .map(l => l.trim())
    .filter(l => l.length > 0)
    .map(line => line.replace(/^[•\-\*\d\.]+\s*/, '').trim())
    .map(line => line.replace(/<[^>]*>/g, ' ').trim())
    .filter(clean => {
      const lower = clean.toLowerCase();
      for (const bp of boilerplatePatterns) {
        if (lower.includes(bp)) return false;
      }
      return true;
    })
    .map(line => line.replace(prefixRegex, '').trim())
    .filter(clean => {
      // Filter out any bullet that just repeats the headline
      const normClean = clean.toLowerCase().replace(/[^a-z0-9]/g, ' ').trim();
      if (normHeadline.length > 15 && (normClean.includes(normHeadline) || normHeadline.includes(normClean))) {
        return false;
      }
      return clean.length > 12;
    })
    .map(line => line.charAt(0).toUpperCase() + line.slice(1));

  if (lines.length === 0) {
    return '<li>Comprehensive developments and market analysis from the Daily Brief wire.</li>';
  }

  return lines.slice(0, 4).map(line => `<li>${line}</li>`).join('');
}

async function refreshFeed() {
  const btn = document.getElementById('btn-refresh');
  if (btn) btn.style.transform = 'rotate(180deg)';
  try {
    const res = await fetch('/api/brief/refresh');
    if (res.ok) {
      await loadArticlesFromBackend();
    }
  } catch (e) {
    console.log('Refresh fallback');
  } finally {
    if (btn) btn.style.transform = 'none';
  }
}
