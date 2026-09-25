// Embedded data models matching Android InitialData, RecommendationsData, and PublicationCornerData

const INITIAL_ARTICLES = [
  {
    id: 1,
    category: "Global markets & economy",
    headline: "Global Central Banks Pivot Stance Amid Persistent Inflationary Signals & Shifting Capital Flows",
    summary: "• Major central banking authorities signal measured adjustments in policy benchmarks heading into the next fiscal quarter.\n• Asian and emerging equity markets register notable foreign institutional liquidity inflows.\n• Bond yields stabilize across benchmark sovereign notes as commodity volatility softens.\n• Global manufacturing and services PMI indices indicate resilient industrial sentiment across core export hubs.",
    source: "Bloomberg / Financial Times",
    source_url: "https://news.google.com",
    published_at: "Today, 07:00 AM",
    image_url: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&auto=format&fit=crop&q=80",
    reading_time: "3 min read"
  },
  {
    id: 2,
    category: "Indian stock market & IPO",
    headline: "Nifty & Sensex Sustain Bullish Momentum as Key Tech & Banking Heavyweights Drive Record Inflows",
    summary: "• Benchmark indices trade near record highs supported by strong quarterly corporate profitability.\n• Institutional participation via mutual fund SIPs reaches historic monthly milestones.\n• The primary IPO market sees oversubscribed retail and QIB subscription rates for upcoming infrastructure listings.\n• Broader mid-cap and small-cap indices reflect sustained domestic investor conviction.",
    source: "Mint / Economic Times",
    source_url: "https://news.google.com",
    published_at: "Today, 07:15 AM",
    image_url: "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=800&auto=format&fit=crop&q=80",
    reading_time: "3 min read"
  },
  {
    id: 3,
    category: "Railways & infrastructure",
    headline: "Indian Railways Accelerates Vande Bharat Expansions & Dedicated High-Speed Freight Corridors",
    summary: "• Multi-modal transit connectivity plans receive enhanced capital expenditure allocations across high-density corridors.\n• Next-generation Vande Bharat sleeper configurations undergo advanced trial runs on trunk routes.\n• Dedicated Freight Corridor segments achieve record container turnaround metrics and turnaround speed efficiency.\n• Modernization of key junction stations accelerates under the national Amrit Bharat development scheme.",
    source: "Press Information Bureau / RailPost",
    source_url: "https://news.google.com",
    published_at: "Today, 06:45 AM",
    image_url: "https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=800&auto=format&fit=crop&q=80",
    reading_time: "2 min read"
  },
  {
    id: 4,
    category: "Technology & AI",
    headline: "Breakthrough Enterprise AI Architectures & Semiconductor Innovations Reshape Digital Infrastructure",
    summary: "• Sovereign and private hyperscale computing centers deploy specialized hardware accelerators for generative inference.\n• Edge-computing silicon advances enable on-device contextual intelligence with lower latency and power consumption.\n• Cloud providers implement automated agent frameworks for enterprise analytics and autonomous workflow pipelines.\n• Open-source foundational weights reach state-of-the-art performance thresholds in multimodal evaluation suites.",
    source: "TechCrunch / MIT Technology Review",
    source_url: "https://news.google.com",
    published_at: "Today, 07:30 AM",
    image_url: "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
    reading_time: "3 min read"
  },
  {
    id: 5,
    category: "Health & fitness",
    headline: "Precision Cardiology & Circadian Metabolic Research Establish New Preventive Health Benchmarks",
    summary: "• Longitudinal clinical trials underscore the paramount role of consistent sleep architecture in metabolic balance.\n• Wearable health telemetry algorithms demonstrate clinical-grade early warning markers for cardiovascular variability.\n• Nutritional epidemiology studies emphasize polyphenolic whole-food diets for optimal cellular health and microbiome resilience.\n• Preventative health protocols gain mainstream adoption across workplace corporate wellness initiatives.",
    source: "The Lancet / Harvard Health",
    source_url: "https://news.google.com",
    published_at: "Today, 06:30 AM",
    image_url: "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&auto=format&fit=crop&q=80",
    reading_time: "2 min read"
  },
  {
    id: 6,
    category: "General news",
    headline: "Global Strategic Summits Focus on Energy Transition, Climate Resilience, and Bilateral Trade Treaties",
    summary: "• Delegations forge new economic partnership agreements emphasizing critical mineral supply-chain security.\n• Clean hydrogen initiatives and modular grid battery storage deployments secure substantial multilateral financing.\n• Diplomatic dialogues advance regional security stability accords and collaborative space exploration pacts.\n• International logistics corridors implement modern digital customs clearance standards to foster frictionless commerce.",
    source: "Reuters / Associated Press",
    source_url: "https://news.google.com",
    published_at: "Today, 07:45 AM",
    image_url: "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800&auto=format&fit=crop&q=80",
    reading_time: "2 min read"
  }
];

const STOCK_PICKS = [
  {
    symbol: "BEL",
    companyName: "Bharat Electronics Limited",
    sector: "Defence & Aerospace",
    currentPrice: "₹308.50",
    targetPrice: "₹365.00",
    potentialUpside: "+18.3%",
    stopLoss: "₹288.00",
    action: "STRONG BUY",
    timeHorizon: "Medium Term (3–6 Months)",
    rationale: "Robust ₹76,000+ Cr order book pipeline across indigenized radar suites, electronic warfare equipment, and naval communication systems. Operating margins expand sustainably on high-margin defense exports.",
    keyMetrics: [
      { label: "P/E Ratio", value: "41.2" },
      { label: "ROE", value: "25.8%" },
      { label: "Order Book", value: "₹76,200 Cr" },
      { label: "52W High/Low", value: "₹340 / ₹132" }
    ]
  },
  {
    symbol: "L&T",
    companyName: "Larsen & Toubro Ltd",
    sector: "Infrastructure & Heavy Engineering",
    currentPrice: "₹3,620.00",
    targetPrice: "₹4,150.00",
    potentialUpside: "+14.6%",
    stopLoss: "₹3,410.00",
    action: "BUY",
    timeHorizon: "Medium to Long Term (6–12 Months)",
    rationale: "Unmatched execution track record across domestic railways, metros, green hydrogen projects, and Middle-Eastern energy transition capex. Bidding pipeline remains robust above ₹8.5 Lakh Crore.",
    keyMetrics: [
      { label: "P/E Ratio", value: "33.5" },
      { label: "ROE", value: "16.4%" },
      { label: "Order Book", value: "₹4.75 Lakh Cr" },
      { label: "52W High/Low", value: "₹3,948 / ₹2,860" }
    ]
  },
  {
    symbol: "HDFCBANK",
    companyName: "HDFC Bank Limited",
    sector: "Banking & Financial Services",
    currentPrice: "₹1,645.00",
    targetPrice: "₹1,920.00",
    potentialUpside: "+16.7%",
    stopLoss: "₹1,530.00",
    action: "ACCUMULATE",
    timeHorizon: "Long Term (12 Months+)",
    rationale: "Post-merger loan-to-deposit ratio (LDR) normalizes steadily with robust branch mobilization. Asset quality remains industry benchmark with GNPA at pristine 1.24%. Valuation at multi-year historical discount.",
    keyMetrics: [
      { label: "P/B Ratio", value: "2.65" },
      { label: "ROE", value: "16.9%" },
      { label: "Gross NPA", value: "1.24%" },
      { label: "52W High/Low", value: "₹1,794 / ₹1,363" }
    ]
  },
  {
    symbol: "TATACHEM",
    companyName: "Tata Chemicals Ltd",
    sector: "Specialty Chemicals & EV Materials",
    currentPrice: "₹1,024.00",
    targetPrice: "₹1,240.00",
    potentialUpside: "+21.1%",
    stopLoss: "₹940.00",
    action: "BUY",
    timeHorizon: "Short to Medium Term (1–3 Months)",
    rationale: "Recovery in synthetic and natural soda ash realizations globally. Synergistic expansion in lithium-ion battery component recycling and premium silica portfolios under Tata ecosystem umbrella.",
    keyMetrics: [
      { label: "P/E Ratio", value: "24.8" },
      { label: "EV/EBITDA", value: "8.4" },
      { label: "Dividend Yield", value: "1.7%" },
      { label: "52W High/Low", value: "₹1,349 / ₹925" }
    ]
  }
];

const MUTUAL_FUNDS = [
  {
    fundName: "Parag Parikh Flexi Cap Fund",
    fundHouse: "PPFAS Mutual Fund",
    category: "Flexi Cap",
    riskLevel: "Very High",
    nav: "₹82.45",
    rating: 5,
    return1Yr: "+36.8%",
    return3Yr: "+23.4% CAGR",
    return5Yr: "+25.1% CAGR",
    expenseRatio: "0.62% (Direct)",
    aum: "₹72,400 Cr",
    verdictAndAnalysis: "Gold standard flexi-cap vehicle balancing large Indian blue-chips with value-accretive international tech giants (Alphabet, Meta, Microsoft) and debt arbitrage buffers for downside mitigation.",
    topHoldings: ["HDFC Bank", "Bajaj Holdings", "ITC Ltd", "Power Grid", "Alphabet Inc", "Coal India"]
  },
  {
    fundName: "Mirae Asset Large & Midcap Fund",
    fundHouse: "Mirae Asset Mutual Fund",
    category: "Large & Mid Cap",
    riskLevel: "Very High",
    nav: "₹154.20",
    rating: 5,
    return1Yr: "+41.2%",
    return3Yr: "+24.8% CAGR",
    return5Yr: "+22.6% CAGR",
    expenseRatio: "0.58% (Direct)",
    aum: "₹38,900 Cr",
    verdictAndAnalysis: "Top quartile performance with disciplined growth-at-reasonable-price (GARP) allocation across market leaders and rapidly expanding mid-market disruptors.",
    topHoldings: ["ICICI Bank", "L&T", "Reliance Industries", "Federal Bank", "TCS", "Bharat Electronics"]
  },
  {
    fundName: "Nippon India Small Cap Fund",
    fundHouse: "Nippon Life India AM",
    category: "Small Cap",
    riskLevel: "Very High",
    nav: "₹178.60",
    rating: 5,
    return1Yr: "+48.5%",
    return3Yr: "+31.2% CAGR",
    return5Yr: "+32.4% CAGR",
    expenseRatio: "0.68% (Direct)",
    aum: "₹56,200 Cr",
    verdictAndAnalysis: "Extensive diversification across 180+ niche manufacturing, capital goods, and domestic consumption small caps. Exceptional alpha generation across multiple market cycles.",
    topHoldings: ["Tube Investments", "HDFC Bank", "Apar Industries", "KPIT Tech", "Tejas Networks", "Voltamp Transformers"]
  },
  {
    fundName: "ICICI Prudential Balanced Advantage Fund",
    fundHouse: "ICICI Prudential AMC",
    category: "Dynamic Asset Allocation / Hybrid",
    riskLevel: "Moderate",
    nav: "₹68.90",
    rating: 4,
    return1Yr: "+19.4%",
    return3Yr: "+15.2% CAGR",
    return5Yr: "+14.8% CAGR",
    expenseRatio: "0.82% (Direct)",
    aum: "₹61,400 Cr",
    verdictAndAnalysis: "Automated in-house valuation model smoothly recalibrates equity exposure between 30% to 80% depending on Price-to-Book indices, delivering steady, low-volatility compound returns.",
    topHoldings: ["ICICI Bank", "Infosys", "Reliance Industries", "GOI 7.18% 2033 G-Sec", "Bharti Airtel", "L&T"]
  }
];

const NEWSPAPERS = [
  {
    id: "the_hindu",
    title: "The Hindu",
    publisher: "THG Publishing Private Limited",
    category: "National Daily",
    description: "Renowned for comprehensive national reporting, unbiased editorial commentary, and deep investigative diplomacy and governance coverage.",
    eEditionUrl: "https://epaper.thehindu.com",
    badgeLabel: "DAILY E-PAPER",
    accentColor: "#1e3a8a",
    icon: "newspaper"
  },
  {
    id: "indian_express",
    title: "The Indian Express",
    publisher: "The Indian Express Group",
    category: "National & Investigative",
    description: "Iconic journalistic integrity famous for investigative scoops, supreme court analyses, and the acclaimed 'Explained' series.",
    eEditionUrl: "https://epaper.indianexpress.com",
    badgeLabel: "DAILY E-PAPER",
    accentColor: "#b91c1c",
    icon: "newspaper"
  },
  {
    id: "times_of_india",
    title: "The Times of India",
    publisher: "Bennett, Coleman & Co. Ltd.",
    category: "National & Metro",
    description: "India's highest circulating English daily with comprehensive city bureaus, international wire coverage, sports, and lifestyle supplements.",
    eEditionUrl: "https://epaper.timesgroup.com",
    badgeLabel: "DAILY E-PAPER",
    accentColor: "#c2410c",
    icon: "newspaper"
  },
  {
    id: "hindustan_times",
    title: "Hindustan Times",
    publisher: "HT Media Ltd.",
    category: "National & Policy",
    description: "In-depth political reporting, parliamentary developments, strategic geopolitics, and metropolitan news coverage.",
    eEditionUrl: "https://epaper.hindustantimes.com",
    badgeLabel: "DAILY E-PAPER",
    accentColor: "#0369a1",
    icon: "newspaper"
  },
  {
    id: "mint",
    title: "Mint (Livemint)",
    publisher: "HT Media / Wall Street Journal partner",
    category: "Business & Markets",
    description: "India's premier executive business daily specializing in macroeconomic trends, Dalal Street analysis, corporate earnings, and startup funding.",
    eEditionUrl: "https://epaper.livemint.com",
    badgeLabel: "BUSINESS E-PAPER",
    accentColor: "#d97706",
    icon: "business"
  },
  {
    id: "business_standard",
    title: "Business Standard",
    publisher: "Business Standard Private Limited",
    category: "Economy & Markets",
    description: "Authoritative financial daily covering fiscal policy, RBI regulations, banking health, commodities, and corporate governance.",
    eEditionUrl: "https://epaper.business-standard.com",
    badgeLabel: "FINANCIAL E-PAPER",
    accentColor: "#991b1b",
    icon: "business"
  },
  {
    id: "economic_times",
    title: "The Economic Times",
    publisher: "Bennett, Coleman & Co. Ltd.",
    category: "Finance & Conglomerates",
    description: "The largest business daily covering Indian and global stock markets, IPO trackings, private equity deals, and tech leadership.",
    eEditionUrl: "https://epaper.indiatimes.com/the-economic-times",
    badgeLabel: "MARKETS E-PAPER",
    accentColor: "#15803d",
    icon: "business"
  },
  {
    id: "telegraph",
    title: "The Telegraph",
    publisher: "ABP Group",
    category: "National & Regional",
    description: "Renowned for sharp front-page headlines, candid editorial stance, cultural columns, and extensive Eastern & Northeast India reportage.",
    eEditionUrl: "https://epaper.telegraphindia.com",
    badgeLabel: "DAILY E-PAPER",
    accentColor: "#4338ca",
    icon: "newspaper"
  }
];

const MAGAZINES = [
  {
    id: "india_today",
    title: "India Today",
    publisher: "Living Media India Limited",
    category: "Current Affairs & Politics",
    description: "India's most influential news magazine offering deep cover-story investigations, election forecasts, defense analysis, and public opinion polls.",
    eEditionUrl: "https://www.indiatoday.in/magazine",
    badgeLabel: "WEEKLY MAGAZINE",
    accentColor: "#dc2626",
    icon: "magazine"
  },
  {
    id: "the_week",
    title: "The Week",
    publisher: "Malayala Manorama Co. Ltd.",
    category: "General Interest & Society",
    description: "Comprehensive weekly newsmagazine highlighting geopolitical analysis, human interest stories, literary profiles, and scientific developments.",
    eEditionUrl: "https://www.theweek.in/theweek.html",
    badgeLabel: "WEEKLY MAGAZINE",
    accentColor: "#ea580c",
    icon: "magazine"
  },
  {
    id: "frontline",
    title: "Frontline",
    publisher: "THG Publishing Private Limited",
    category: "Policy & Investigative",
    description: "Fortnightly long-form intellectual magazine focused on agrarian economics, constitutional rights, civil liberties, and world politics.",
    eEditionUrl: "https://frontline.thehindu.com",
    badgeLabel: "FORTNIGHTLY",
    accentColor: "#7c2d12",
    icon: "magazine"
  },
  {
    id: "science_reporter",
    title: "Science Reporter (CSIR)",
    publisher: "Council of Scientific & Industrial Research",
    category: "Science & Discovery",
    description: "India's premier popular science monthly covering space missions (ISRO), quantum technologies, biotech breakthroughs, and environmental science.",
    eEditionUrl: "https://niscpr.res.in/periodicals/popular-science-magazines/science-reporter",
    badgeLabel: "SCIENCE & TECH",
    accentColor: "#7c3aed",
    icon: "science"
  },
  {
    id: "down_to_earth",
    title: "Down To Earth",
    publisher: "Centre for Science and Environment (CSE)",
    category: "Environment & Climate",
    description: "Leading environmental, public health, biodiversity, and clean energy magazine providing frontline reporting on sustainable development.",
    eEditionUrl: "https://www.downtoearth.org.in",
    badgeLabel: "CLIMATE & NATURE",
    accentColor: "#16a34a",
    icon: "science"
  },
  {
    id: "outlook_business",
    title: "Outlook Business",
    publisher: "Outlook Publishing India",
    category: "Business & Strategy",
    description: "Enterprise insights, leadership profiles, disruptive unicorn strategies, market trends, and venture capital ecosystem analyses.",
    eEditionUrl: "https://www.outlookbusiness.com",
    badgeLabel: "BUSINESS MONTHLY",
    accentColor: "#0284c7",
    icon: "business"
  },
  {
    id: "health_and_nutrition",
    title: "Health & Nutrition",
    publisher: "Magna Publishing",
    category: "Health & Wellness",
    description: "India's pioneering wellness monthly featuring evidence-based clinical nutrition, preventative cardiology, fitness regimes, and mental well-being.",
    eEditionUrl: "https://healthandnutrition.in",
    badgeLabel: "HEALTH & FITNESS",
    accentColor: "#e11d48",
    icon: "health"
  },
  {
    id: "digit",
    title: "Digit",
    publisher: "9.9 Group",
    category: "Technology & Gadgets",
    description: "India's foremost personal technology authority covering hardware teardowns, AI benchmarks, consumer gadgets, and cybersecurity.",
    eEditionUrl: "https://www.digit.in/magazine",
    badgeLabel: "TECH & AI",
    accentColor: "#6d28d9",
    icon: "science"
  }
];
