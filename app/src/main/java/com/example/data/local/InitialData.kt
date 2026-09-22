package com.example.data.local

object InitialData {
    val SEED_ARTICLES = listOf(
        ArticleEntity(
            id = 1,
            category = "Global markets & economy",
            headline = "Federal Reserve Holds Rates Steady as Global Inflation Pressures Ease",
            summary = "• Major global central banks signaled a cautious pivot toward monetary easing as annualized inflation rates stabilized near target bands.\n• Equities in Europe and Asia advanced moderately, led by manufacturing and renewable energy issues.\n• Currency markets saw the US Dollar consolidate against the Euro and Yen following quarterly trade adjustments.\n• Emerging markets reported increased sovereign bond inflows amidst resilient export balance sheets.",
            originalContent = "Global economic indices saw positive momentum this week as central banks signaled stabilizing monetary policies. Economists noted persistent supply chain recovery and lowering commodity price spikes across primary industrial sectors.",
            imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&auto=format&fit=crop&q=80",
            source = "Financial Wire",
            sourceUrl = "https://dailybrief.internal/news/global-markets-101",
            publishedAt = "2026-09-21T06:00:00Z",
            isCached = true
        ),
        ArticleEntity(
            id = 2,
            category = "Indian stock market & IPO",
            headline = "Nifty and Sensex Notch Fresh Weekly Highs Amid Robust Domestic Retail and DII Inflows",
            summary = "• Benchmark indices Sensex and Nifty closed up over 1.2% powered by banking and capital goods conglomerates.\n• Over four new SME and tech-enabled IPOs received blockbuster retail oversubscriptions above 35x.\n• Foreign institutional investors turned net buyers across high-growth domestic manufacturing corridors.\n• Broader indices outperformed benchmark metrics, reflecting strong quarterly balance sheet earnings.",
            originalContent = "The Indian stock markets witnessed an aggressive rally today, propelled by domestic institutional investors and optimistic macroeconomic indicators across key manufacturing states.",
            imageUrl = "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=800&auto=format&fit=crop&q=80",
            source = "Dalal Street Express",
            sourceUrl = "https://dailybrief.internal/news/indian-markets-201",
            publishedAt = "2026-09-21T06:15:00Z",
            isCached = true
        ),
        ArticleEntity(
            id = 3,
            category = "Railways & infrastructure",
            headline = "Indian Railways Launches 10 Next-Gen Vande Bharat Sleeper Routes and Upgrades Freight Corridors",
            summary = "• The Ministry of Railways unveiled upgraded aerodynamic Vande Bharat Sleeper trains equipped with automated anti-collision Kavach systems.\n• Key freight trunk corridors recorded a 24% boost in transit speeds, significantly reducing cross-country logistics costs.\n• Greenfield multi-modal logistics hubs received joint public-private investment commitments exceeding ₹18,000 crores.\n• Station modernization under Amrit Bharat initiative reached critical construction milestones at 120 regional hubs.",
            originalContent = "Indian Railways continues its rapid infrastructure transformation with the rollout of sleeper trainsets and modernization of multi-modal cargo terminals across high-density routes.",
            imageUrl = "https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=800&auto=format&fit=crop&q=80",
            source = "Infra Today",
            sourceUrl = "https://dailybrief.internal/news/railways-infra-301",
            publishedAt = "2026-09-21T06:30:00Z",
            isCached = true
        ),
        ArticleEntity(
            id = 4,
            category = "Technology & AI",
            headline = "OpenAI and DeepMind Announce Breakthrough Reasoning Models with Multimodal Verification",
            summary = "• Next-generation AI models demonstrate advanced scientific synthesis, formal theorem proving, and zero-shot reasoning capabilities.\n• Chipmakers showcased ultra-efficient edge tensor processors capable of running 70B parameter models directly on mobile devices.\n• Open-source AI development witnessed record adoption in enterprise automation and autonomous robotics pipelines.\n• Global AI safety frameworks announced coordinated benchmark standards for frontier model deployment.",
            originalContent = "Artificial Intelligence research reached a landmark milestone this month with models demonstrating verified step-by-step logic and real-time reasoning abilities across complex domains.",
            imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
            source = "Tech Horizon",
            sourceUrl = "https://dailybrief.internal/news/tech-ai-401",
            publishedAt = "2026-09-21T06:45:00Z",
            isCached = true
        ),
        ArticleEntity(
            id = 5,
            category = "Health & fitness",
            headline = "Groundbreaking Clinical Study Validates Zone-2 Cardio and Circadian Sleep for Metabolic Longevity",
            summary = "• Extensive peer-reviewed trials confirmed 150 minutes of moderate Zone-2 aerobic activity lowers cardiovascular risk markers by 38%.\n• Researchers demonstrated consistent circadian light exposure protocols enhance deep sleep cycles and cellular autophagy.\n• Wearable health telemetry algorithms received medical validation for early metabolic and cardiovascular stress detection.\n• Nutritionists emphasize whole-food micronutrient synergy over synthetic isolated supplements for sustained athletic vitality.",
            originalContent = "Health researchers published definitive data illustrating the profound health benefits of moderate endurance training, sleep hygiene, and metabolic biomarker tracking.",
            imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&auto=format&fit=crop&q=80",
            source = "Health & Longevity Journal",
            sourceUrl = "https://dailybrief.internal/news/health-fitness-501",
            publishedAt = "2026-09-21T07:00:00Z",
            isCached = true
        ),
        ArticleEntity(
            id = 6,
            category = "General news",
            headline = "Global Clean Energy Transition Reaches Milestone as Solar and Wind Surpass Coal Generation",
            summary = "• Renewable energy installations outpaced traditional fossil fuel additions worldwide for the third consecutive quarter.\n• International climate accords ratified streamlined financing mechanisms to accelerate green infrastructure in developing economies.\n• High-capacity grid-scale battery storage deployments doubled, resolving key intermittent distribution challenges.\n• Municipalities worldwide reported noticeable improvements in urban air quality metrics and localized power resilience.",
            originalContent = "Global renewable energy output reached an all-time peak, driven by massive utility-scale solar and offshore wind projects coming online across three continents.",
            imageUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800&auto=format&fit=crop&q=80",
            source = "World Wire",
            sourceUrl = "https://dailybrief.internal/news/general-news-601",
            publishedAt = "2026-09-21T07:10:00Z",
            isCached = true
        )
    )

    val ALL_CATEGORIES = listOf(
        "Global markets & economy",
        "Indian stock market & IPO",
        "Railways & infrastructure",
        "Technology & AI",
        "Health & fitness",
        "General news"
    )
}
