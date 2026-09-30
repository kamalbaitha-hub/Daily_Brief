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
    val iconType: String = "newspaper", // "newspaper", "magazine", "business", "science", "health", "entertainment", "international"
    val region: String = "National" // "National", "International", "Hindi Entertainment"
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
        // === POPULAR INTERNATIONAL MAGAZINES ===
        PublicationItem(
            id = "time_magazine",
            title = "TIME Magazine",
            publisher = "TIME USA, LLC",
            category = "Global News & Geopolitics",
            description = "The iconic global newsweekly featuring historic cover stories, world leader profiles, TIME 100 changemakers, and world diplomacy reportage.",
            eEditionUrl = "https://time.com",
            badgeLabel = "GLOBAL WEEKLY",
            accentColorHex = 0xFFDC2626, // Iconic TIME Red
            iconType = "international",
            region = "International"
        ),
        PublicationItem(
            id = "the_economist",
            title = "The Economist",
            publisher = "The Economist Newspaper Ltd.",
            category = "World Economics & Finance",
            description = "Authoritative global weekly offering acute economic analysis, macroeconomic forecast models, international politics, and scientific advancements.",
            eEditionUrl = "https://www.economist.com",
            badgeLabel = "GLOBAL AFFAIRS",
            accentColorHex = 0xFFBE123C, // Crimson
            iconType = "international",
            region = "International"
        ),
        PublicationItem(
            id = "nat_geo",
            title = "National Geographic",
            publisher = "National Geographic Partners",
            category = "Exploration, Science & Nature",
            description = "World-famous publication showcasing breathtaking nature photography, archaeological discoveries, wildlife preservation, and environmental science.",
            eEditionUrl = "https://www.nationalgeographic.com",
            badgeLabel = "EXPLORATION & NATURE",
            accentColorHex = 0xFFD97706, // Iconic Gold
            iconType = "science",
            region = "International"
        ),
        PublicationItem(
            id = "harvard_biz_review",
            title = "Harvard Business Review",
            publisher = "Harvard Business Publishing",
            category = "Leadership & Strategy",
            description = "Premier international journal delivering peerless executive leadership paradigms, disruptive corporate strategies, and organizational behavioral insights.",
            eEditionUrl = "https://hbr.org",
            badgeLabel = "EXECUTIVE STRATEGY",
            accentColorHex = 0xFF0284C7, // Deep Sky Blue
            iconType = "business",
            region = "International"
        ),

        // === POPULAR NATIONAL MAGAZINES ===
        PublicationItem(
            id = "business_today",
            title = "Business Today",
            publisher = "Living Media India Limited",
            category = "Business & Markets",
            description = "India's highest-circulating business magazine delivering frontline reporting on corporate strategy, stock markets, startups, and leadership.",
            eEditionUrl = "https://www.businesstoday.in/magazine",
            badgeLabel = "BUSINESS FORTNIGHTLY",
            accentColorHex = 0xFFB91C1C, // Crimson Red
            iconType = "business",
            region = "National"
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
            iconType = "business",
            region = "National"
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
            iconType = "magazine",
            region = "National"
        ),
        PublicationItem(
            id = "outlook_india",
            title = "Outlook India",
            publisher = "Outlook Publishing India",
            category = "National Affairs & Culture",
            description = "Pioneering Indian newsweekly celebrated for independent political investigations, incisive opinion columns, social trends, and literary reviews.",
            eEditionUrl = "https://www.outlookindia.com/magazine",
            badgeLabel = "NEWS FORTNIGHTLY",
            accentColorHex = 0xFF4338CA, // Indigo
            iconType = "magazine",
            region = "National"
        ),
        PublicationItem(
            id = "the_caravan",
            title = "The Caravan",
            publisher = "Delhi Press",
            category = "Investigative Journalism",
            description = "India's premier long-form narrative journalism and politics journal, acclaimed for rigorous investigative exposés and deep policy analyses.",
            eEditionUrl = "https://caravanmagazine.in",
            badgeLabel = "INVESTIGATIVE JOURNAL",
            accentColorHex = 0xFF991B1B, // Dark Red
            iconType = "magazine",
            region = "National"
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
            iconType = "magazine",
            region = "National"
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
            iconType = "magazine",
            region = "National"
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
            iconType = "science",
            region = "National"
        ),
        PublicationItem(
            id = "readers_digest_india",
            title = "Reader's Digest India",
            publisher = "Living Media India Limited",
            category = "Life, Health & Inspiration",
            description = "Classic family monthly packed with inspiring true human stories, everyday wellness discoveries, wit, humor, and life advice.",
            eEditionUrl = "https://www.readersdigest.in",
            badgeLabel = "LIFE & INSPIRATION",
            accentColorHex = 0xFF2563EB, // Royal Blue
            iconType = "magazine",
            region = "National"
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
            iconType = "business",
            region = "National"
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
            iconType = "magazine",
            region = "National"
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
            iconType = "science",
            region = "National"
        ),

        // === HINDI ENTERTAINMENT & LIFESTYLE MAGAZINES ===
        PublicationItem(
            id = "mayapuri",
            title = "Mayapuri (मायापुरी)",
            publisher = "Mayapuri Group",
            category = "Hindi Bollywood & Cinema",
            description = "भारत की सबसे पुरानी व लोकप्रिय हिंदी फ़िल्मी पत्रिका — बॉलीवुड के ताज़ा गपशप, सितारों के इंटरव्यू, मूवी रिव्यूज़ और सिनेमा जगत की ख़ास ख़बरें।",
            eEditionUrl = "https://mayapuri.com",
            badgeLabel = "HINDI BOLLYWOOD",
            accentColorHex = 0xFFBE185D, // Hot Pink
            iconType = "entertainment",
            region = "Hindi Entertainment"
        ),
        PublicationItem(
            id = "filmfare",
            title = "Filmfare (फ़िल्मफ़ेयर)",
            publisher = "Worldwide Media (Times Group)",
            category = "Cinema & Entertainment",
            description = "भारतीय सिनेमा और ग्लैमर जगत का प्रतिष्ठित मंच — बॉलीवुड फ़ोटोशूट, फ़िल्मफ़ेयर पुरस्कार, सितारों की जीवनशैली और अनसुने किस्से।",
            eEditionUrl = "https://www.filmfare.com",
            badgeLabel = "CINEMA & STARS",
            accentColorHex = 0xFFE11D48, // Rose Red
            iconType = "entertainment",
            region = "Hindi Entertainment"
        ),
        PublicationItem(
            id = "grihshobha",
            title = "Grihshobha (गृहशोभा)",
            publisher = "Delhi Press",
            category = "Hindi Family & Lifestyle",
            description = "करोड़ों पाठकों की पसंदीदा हिंदी पत्रिका — पारिवारिक जीवन, मनोरंजन कहानियां, रिश्ते, फैशन, स्वास्थ्य, कुकिंग और गृह सज्जा।",
            eEditionUrl = "https://www.grihshobha.in",
            badgeLabel = "HINDI LIFESTYLE",
            accentColorHex = 0xFFD97706, // Amber
            iconType = "entertainment",
            region = "Hindi Entertainment"
        ),
        PublicationItem(
            id = "sarita",
            title = "Sarita (सरिता)",
            publisher = "Delhi Press",
            category = "Hindi Stories & Culture",
            description = "प्रसिद्ध सामाजिक व साहित्यिक हिंदी पत्रिका — विचारोत्तेजक कहानियां, सामाजिक सरोकार, पारिवारिक सलाह, जीवन शैली और साहित्य।",
            eEditionUrl = "https://www.sarita.in",
            badgeLabel = "HINDI STORIES",
            accentColorHex = 0xFF7C3AED, // Violet
            iconType = "magazine",
            region = "Hindi Entertainment"
        ),
        PublicationItem(
            id = "grehlakshmi",
            title = "Grehlakshmi (गृहलक्ष्मी)",
            publisher = "Diamond Magazine",
            category = "Hindi Entertainment & Women",
            description = "हिंदी की प्रमुख महिला व मनोरंजन पत्रिका — बॉलीवुड गपशप, ब्यूटी टिप्स, नारी सशक्तिकरण, रिश्ते और परिवार की लाइफस्टाइल।",
            eEditionUrl = "https://grehlakshmi.com",
            badgeLabel = "HINDI ENTERTAINMENT",
            accentColorHex = 0xFFC026D3, // Fuchsia
            iconType = "entertainment",
            region = "Hindi Entertainment"
        ),
        PublicationItem(
            id = "champak",
            title = "Champak (चंपक)",
            publisher = "Delhi Press",
            category = "Illustrated Stories & Fun",
            description = "भारत की सर्वप्रिय सचित्र हिंदी पत्रिका — चीकू और दोस्तों की कॉमिक्स, रोचक बाल कहानियां, पहेलियां और ज्ञानवर्धक मनोरंजन।",
            eEditionUrl = "https://www.champak.in",
            badgeLabel = "HINDI STORIES & FUN",
            accentColorHex = 0xFF059669, // Emerald Green
            iconType = "entertainment",
            region = "Hindi Entertainment"
        )
    )
}
