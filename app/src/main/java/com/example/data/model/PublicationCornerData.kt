package com.example.data.model

data class PublicationItem(
    val id: String,
    val title: String,
    val publisher: String,
    val category: String, // "General News", "Business & Finance", "Current Affairs", "Science & Tech", "Health & Lifestyle", etc.
    val description: String,
    val eEditionUrl: String,
    val badgeLabel: String = "E-EDITION",
    val accentColorHex: Long = 0xFF0F172A,
    val iconType: String = "newspaper" // "newspaper", "magazine", "business", "science", "health"
)

object PublicationCornerData {

    val NEWSPAPERS = listOf(
        PublicationItem(
            id = "the_hindu",
            title = "The Hindu",
            publisher = "THG Publishing Private Limited",
            category = "National Daily",
            description = "Renowned for comprehensive national reporting, unbiased editorial commentary, and deep investigative diplomacy and governance coverage.",
            eEditionUrl = "https://epaper.thehindu.com",
            badgeLabel = "DAILY E-PAPER",
            accentColorHex = 0xFF1E3A8A, // Deep Indigo
            iconType = "newspaper"
        ),
        PublicationItem(
            id = "indian_express",
            title = "The Indian Express",
            publisher = "The Indian Express Group",
            category = "National & Investigative",
            description = "Iconic journalistic integrity famous for investigative scoops, supreme court analyses, and the 'Explained' series.",
            eEditionUrl = "https://epaper.indianexpress.com",
            badgeLabel = "DAILY E-PAPER",
            accentColorHex = 0xFFB91C1C, // Deep Red
            iconType = "newspaper"
        ),
        PublicationItem(
            id = "times_of_india",
            title = "The Times of India",
            publisher = "Bennett, Coleman & Co. Ltd.",
            category = "National & Metro",
            description = "India's highest circulating English daily with comprehensive city bureaus, international wire coverage, sports, and lifestyle supplements.",
            eEditionUrl = "https://epaper.timesgroup.com",
            badgeLabel = "DAILY E-PAPER",
            accentColorHex = 0xFFC2410C, // Rust Orange
            iconType = "newspaper"
        ),
        PublicationItem(
            id = "hindustan_times",
            title = "Hindustan Times",
            publisher = "HT Media Ltd.",
            category = "National & Policy",
            description = "In-depth political reporting, parliamentary developments, strategic geopolitics, and metropolitan news coverage.",
            eEditionUrl = "https://epaper.hindustantimes.com",
            badgeLabel = "DAILY E-PAPER",
            accentColorHex = 0xFF0369A1, // Sky Blue / Cyan
            iconType = "newspaper"
        ),
        PublicationItem(
            id = "mint",
            title = "Mint (Livemint)",
            publisher = "HT Media / Wall Street Journal partner",
            category = "Business & Markets",
            description = "India's premier executive business daily specializing in macroeconomic trends, Dalal Street analysis, corporate earnings, and startup funding.",
            eEditionUrl = "https://epaper.livemint.com",
            badgeLabel = "BUSINESS E-PAPER",
            accentColorHex = 0xFFD97706, // Amber Gold
            iconType = "business"
        ),
        PublicationItem(
            id = "business_standard",
            title = "Business Standard",
            publisher = "Business Standard Private Limited",
            category = "Economy & Markets",
            description = "Authoritative financial daily covering fiscal policy, RBI regulations, banking health, commodities, and corporate governance.",
            eEditionUrl = "https://epaper.business-standard.com",
            badgeLabel = "FINANCIAL E-PAPER",
            accentColorHex = 0xFF991B1B, // Crimson
            iconType = "business"
        ),
        PublicationItem(
            id = "financial_express",
            title = "The Financial Express",
            publisher = "The Indian Express Group",
            category = "Finance & Conglomerates",
            description = "Premier financial and market daily covering Indian equities, corporate balance sheets, sectoral trends, and global macroeconomic policy.",
            eEditionUrl = "https://epaper.financialexpress.com",
            badgeLabel = "MARKETS E-PAPER",
            accentColorHex = 0xFF15803D, // Forest Green
            iconType = "business"
        ),
        PublicationItem(
            id = "telegraph",
            title = "The Telegraph",
            publisher = "ABP Group",
            category = "National & Regional",
            description = "Renowned for sharp front-page headlines, candid editorial stance, cultural columns, and extensive Eastern & Northeast India reportage.",
            eEditionUrl = "https://epaper.telegraphindia.com",
            badgeLabel = "DAILY E-PAPER",
            accentColorHex = 0xFF4338CA, // Slate Indigo
            iconType = "newspaper"
        )
    )

    val MAGAZINES = listOf(
        PublicationItem(
            id = "business_today",
            title = "Business Today",
            publisher = "Living Media India Limited",
            category = "Business & Markets",
            description = "India's highest-circulating business magazine delivering frontline reporting on corporate strategy, stock markets, startups, and leadership.",
            eEditionUrl = "https://www.businesstoday.in/magazine",
            badgeLabel = "BUSINESS FORTNIGHTLY",
            accentColorHex = 0xFFB91C1C, // Crimson Red
            iconType = "business"
        ),
        PublicationItem(
            id = "forbes_india",
            title = "Forbes India",
            publisher = "Network18 Media",
            category = "Wealth & Enterprise",
            description = "India's premier business and leadership authority featuring billionaire lists, venture capital deals, high-growth startups, and corporate game-changers.",
            eEditionUrl = "https://www.forbesindia.com/magazine/",
            badgeLabel = "WEALTH & STRATEGY",
            accentColorHex = 0xFF0F766E, // Teal
            iconType = "business"
        ),
        PublicationItem(
            id = "india_today",
            title = "India Today",
            publisher = "Living Media India Limited",
            category = "Current Affairs & Politics",
            description = "India's most influential news magazine offering deep cover-story investigations, election forecasts, defense analysis, and public opinion polls.",
            eEditionUrl = "https://www.indiatoday.in/magazine",
            badgeLabel = "WEEKLY MAGAZINE",
            accentColorHex = 0xFFDC2626, // Bright Red
            iconType = "magazine"
        ),
        PublicationItem(
            id = "the_week",
            title = "The Week",
            publisher = "Malayala Manorama Co. Ltd.",
            category = "General Interest & Society",
            description = "Comprehensive weekly newsmagazine highlighting geopolitical analysis, human interest stories, literary profiles, and scientific developments.",
            eEditionUrl = "https://www.theweek.in",
            badgeLabel = "WEEKLY MAGAZINE",
            accentColorHex = 0xFFEA580C, // Vibrant Orange
            iconType = "magazine"
        ),
        PublicationItem(
            id = "frontline",
            title = "Frontline",
            publisher = "THG Publishing Private Limited",
            category = "Policy & Investigative",
            description = "Fortnightly long-form intellectual magazine focused on agrarian economics, constitutional rights, civil liberties, and world politics.",
            eEditionUrl = "https://frontline.thehindu.com",
            badgeLabel = "FORTNIGHTLY",
            accentColorHex = 0xFF7C2D12, // Warm Brown/Maroon
            iconType = "magazine"
        ),
        PublicationItem(
            id = "down_to_earth",
            title = "Down To Earth",
            publisher = "Centre for Science and Environment (CSE)",
            category = "Environment & Climate",
            description = "Leading environmental, public health, biodiversity, and clean energy magazine providing frontline reporting on sustainable development.",
            eEditionUrl = "https://www.downtoearth.org.in",
            badgeLabel = "CLIMATE & NATURE",
            accentColorHex = 0xFF16A34A, // Green
            iconType = "science"
        ),
        PublicationItem(
            id = "outlook_business",
            title = "Outlook Business",
            publisher = "Outlook Publishing India",
            category = "Business & Strategy",
            description = "Enterprise insights, leadership profiles, disruptive unicorn strategies, market trends, and venture capital ecosystem analyses.",
            eEditionUrl = "https://www.outlookbusiness.com",
            badgeLabel = "BUSINESS MONTHLY",
            accentColorHex = 0xFF0284C7, // Cyan Blue
            iconType = "business"
        ),
        PublicationItem(
            id = "open_magazine",
            title = "Open Magazine",
            publisher = "Open Media Network",
            category = "Current Affairs & Culture",
            description = "Weekly current affairs and culture digest known for compelling long-form features, political commentary, and analytical reportage.",
            eEditionUrl = "https://openthemagazine.com",
            badgeLabel = "WEEKLY DIGEST",
            accentColorHex = 0xFF7C3AED, // Violet
            iconType = "magazine"
        ),
        PublicationItem(
            id = "digit",
            title = "Digit",
            publisher = "9.9 Group",
            category = "Technology & Gadgets",
            description = "India's foremost personal technology authority covering hardware teardowns, AI benchmarks, consumer gadgets, and cyber security.",
            eEditionUrl = "https://www.digit.in",
            badgeLabel = "TECH & AI",
            accentColorHex = 0xFF6D28D9, // Deep Purple
            iconType = "science"
        )
    )
}
