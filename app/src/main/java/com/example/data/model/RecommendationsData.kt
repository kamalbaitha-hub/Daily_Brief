package com.example.data.model

data class StockPick(
    val symbol: String,
    val companyName: String,
    val sector: String,
    val currentPrice: String,
    val targetPrice: String,
    val potentialUpside: String,
    val stopLoss: String,
    val action: String = "BUY", // BUY, STRONG BUY, ACCUMULATE, OUTPERFORM, OVERWEIGHT
    val timeHorizon: String, // Short Term (1-3 weeks), Medium Term (3-6 months), Long Term (1 yr+)
    val researchSource: String, // e.g. Motilal Oswal, Goldman Sachs, Morgan Stanley, Jefferies, Kotak, ICICI Sec, Nomura, JPMorgan
    val sourceType: String = "National Research", // "National Research" or "International Institution"
    val isInternational: Boolean = false,
    val recommendationDate: String = "Latest (Sep 2026)",
    val rationale: String,
    val keyMetrics: List<Pair<String, String>>
)

data class MutualFundRecommendation(
    val fundName: String,
    val fundHouse: String,
    val category: String, // Large & Mid Cap, Flexi Cap, Small Cap, Sectoral/Thematic, Balanced Advantage, Global ETF
    val riskLevel: String, // Very High, Moderately High, Moderate, Low to Moderate
    val nav: String,
    val rating: Int, // 1 to 5 Stars
    val return1Yr: String,
    val return3Yr: String,
    val return5Yr: String,
    val expenseRatio: String,
    val aum: String,
    val researchSource: String = "Morningstar & Value Research Top Pick",
    val sourceType: String = "National AMC", // "National AMC" or "Global Fund House / ETF"
    val isInternational: Boolean = false,
    val recommendationDate: String = "Latest (Sep 2026)",
    val verdictAndAnalysis: String,
    val topHoldings: List<String>
)

object RecommendationsData {

    val ALL_STOCK_PICKS: List<StockPick> = listOf(
        // === INDIAN NATIONAL PICKS ===
        StockPick(
            symbol = "BEL",
            companyName = "Bharat Electronics Limited",
            sector = "Defence & Aerospace",
            currentPrice = "₹308.50",
            targetPrice = "₹380.00",
            potentialUpside = "+23.2%",
            stopLoss = "₹285.00",
            action = "STRONG BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Motilal Oswal",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Robust ₹76,000+ Cr order book pipeline across indigenized radar suites, electronic warfare equipment, and naval communication systems. Operating margins expand sustainably on high-margin defense exports.",
            keyMetrics = listOf(
                "P/E Ratio" to "41.2",
                "ROE" to "25.8%",
                "Order Book" to "₹76,200 Cr",
                "52W High/Low" to "₹340 / ₹132"
            )
        ),
        StockPick(
            symbol = "LT",
            companyName = "Larsen & Toubro Ltd",
            sector = "Infrastructure & Heavy Engineering",
            currentPrice = "₹3,620.00",
            targetPrice = "₹4,250.00",
            potentialUpside = "+17.4%",
            stopLoss = "₹3,420.00",
            action = "BUY",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "Goldman Sachs",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Massive infrastructure capex execution across Indian high-speed rail, renewables, and Middle East EPC contracts. Record execution speed driving double-digit revenue visibility with steady balance sheet deleveraging.",
            keyMetrics = listOf(
                "P/E Ratio" to "34.1",
                "ROE" to "16.4%",
                "Order Inflow" to "+22% YoY",
                "52W High/Low" to "₹3,948 / ₹2,860"
            )
        ),
        StockPick(
            symbol = "HDFCBANK",
            companyName = "HDFC Bank Limited",
            sector = "Banking & Financial Services",
            currentPrice = "₹1,675.00",
            targetPrice = "₹1,980.00",
            potentialUpside = "+18.2%",
            stopLoss = "₹1,560.00",
            action = "OVERWEIGHT",
            timeHorizon = "Medium to Long Term",
            researchSource = "Morgan Stanley",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Credit-deposit ratio improving rapidly post-merger integration. Asset quality remains pristine with Net NPA under 0.35%. Valuation at 2.4x FY26E ABV offers attractive risk-reward for long-term compounders.",
            keyMetrics = listOf(
                "P/E Ratio" to "18.8",
                "P/BV" to "2.4x",
                "Net NPA" to "0.33%",
                "52W High/Low" to "₹1,794 / ₹1,363"
            )
        ),
        StockPick(
            symbol = "TATAPOWER",
            companyName = "Tata Power Company Limited",
            sector = "Clean Energy & Transmission",
            currentPrice = "₹412.00",
            targetPrice = "₹495.00",
            potentialUpside = "+20.1%",
            stopLoss = "₹385.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Jefferies",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Aggressive 20GW renewable capacity pipeline by 2030, surging rooftop solar deployments under PM Surya Ghar Muft Bijli Yojana, and expanding EV public fast-charging grid network.",
            keyMetrics = listOf(
                "P/E Ratio" to "36.5",
                "EV/EBITDA" to "14.2x",
                "Renewable Mix" to "41%",
                "52W High/Low" to "₹471 / ₹235"
            )
        ),
        StockPick(
            symbol = "ZOMATO",
            companyName = "Zomato (Eternal Limited)",
            sector = "Quick Commerce & Consumer Tech",
            currentPrice = "₹252.00",
            targetPrice = "₹315.00",
            potentialUpside = "+25.0%",
            stopLoss = "₹230.00",
            action = "BUY",
            timeHorizon = "Medium to Long Term",
            researchSource = "Kotak Institutional Equities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Blinkit quick-commerce dark store footprint doubling ahead of schedule with store-level EBITDA break-even achieved across top 8 metros. Food delivery GOV maintaining 22%+ compounding growth.",
            keyMetrics = listOf(
                "GOV Growth" to "+38% YoY",
                "Blinkit Contribution" to "4.2%",
                "Cash Balance" to "₹12,400 Cr",
                "52W High/Low" to "₹298 / ₹98"
            )
        ),
        StockPick(
            symbol = "SBIN",
            companyName = "State Bank of India",
            sector = "PSU Banking & Credit",
            currentPrice = "₹795.00",
            targetPrice = "₹960.00",
            potentialUpside = "+20.8%",
            stopLoss = "₹740.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "ICICI Securities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Unmatched retail deposit franchise with lowest funding costs. Corporate credit demand picking up in power, infra, and renewable capex. RoA consistently above 1.05% with Net NPA at historical lows.",
            keyMetrics = listOf(
                "P/BV" to "1.25x",
                "RoA" to "1.10%",
                "Net NPA" to "0.57%",
                "52W High/Low" to "₹912 / ₹555"
            )
        ),
        StockPick(
            symbol = "TATAMOTORS",
            companyName = "Tata Motors Limited",
            sector = "Automotive & Electric Vehicles",
            currentPrice = "₹960.00",
            targetPrice = "₹1,140.00",
            potentialUpside = "+18.8%",
            stopLoss = "₹895.00",
            action = "BUY",
            timeHorizon = "Medium Term (6 Months)",
            researchSource = "Nomura",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Demerger into Commercial Vehicles and Passenger Vehicles (PV+EV+JLR) unlocking immense shareholder value. JLR debt-free milestone reached with robust Range Rover order book.",
            keyMetrics = listOf(
                "P/E Ratio" to "14.2",
                "JLR EBIT Margin" to "8.8%",
                "EV Market Share" to "68%",
                "52W High/Low" to "₹1,179 / ₹621"
            )
        ),
        StockPick(
            symbol = "INFY",
            companyName = "Infosys Limited",
            sector = "IT Services & Generative AI",
            currentPrice = "₹1,880.00",
            targetPrice = "₹2,160.00",
            potentialUpside = "+14.9%",
            stopLoss = "₹1,760.00",
            action = "OVERWEIGHT",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "JPMorgan",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Large enterprise generative AI deal wins accelerating with Topaz platform adoption. US banking and European discretionary tech spend showing clear turnaround signals.",
            keyMetrics = listOf(
                "P/E Ratio" to "27.5",
                "Div Yield" to "2.2%",
                "Large Deal TCV" to "$4.1B",
                "52W High/Low" to "₹1,990 / ₹1,358"
            )
        ),
        StockPick(
            symbol = "TRENT",
            companyName = "Trent Limited",
            sector = "Retail & Consumer Lifestyle",
            currentPrice = "₹7,150.00",
            targetPrice = "₹8,350.00",
            potentialUpside = "+16.8%",
            stopLoss = "₹6,650.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Axis Capital",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Zudio store expansion delivering unprecedented same-store sales growth and fast cash conversion cycles. Westside and Star Bazaar formats executing high operating leverage.",
            keyMetrics = listOf(
                "Revenue Growth" to "+56% YoY",
                "EBITDA Margin" to "16.4%",
                "Store Count" to "800+",
                "52W High/Low" to "₹7,950 / ₹2,050"
            )
        ),
        StockPick(
            symbol = "SUNPHARMA",
            companyName = "Sun Pharmaceutical Industries",
            sector = "Specialty Pharma & Healthcare",
            currentPrice = "₹1,780.00",
            targetPrice = "₹2,060.00",
            potentialUpside = "+15.7%",
            stopLoss = "₹1,670.00",
            action = "OUTPERFORM",
            timeHorizon = "Medium to Long Term",
            researchSource = "Bernstein",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Global innovative specialty portfolio (Ilumya, Cequa, Winlevi) driving high gross margin expansion. Zero regulatory red flags on major domestic manufacturing facilities.",
            keyMetrics = listOf(
                "P/E Ratio" to "33.8",
                "Specialty Share" to "22%",
                "Net Debt" to "Cash Positive",
                "52W High/Low" to "₹1,960 / ₹1,110"
            )
        ),
        StockPick(
            symbol = "BHARTIARTL",
            companyName = "Bharti Airtel Limited",
            sector = "Telecom & Digital Services",
            currentPrice = "₹1,540.00",
            targetPrice = "₹1,820.00",
            potentialUpside = "+18.2%",
            stopLoss = "₹1,440.00",
            action = "BUY",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "CLSA",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Industry-leading ARPU crossing ₹220+ driven by steady tariff hikes and 2G-to-4G/5G upgrades. Airtel Business B2B cloud and cybersecurity services delivering resilient cash flows.",
            keyMetrics = listOf(
                "ARPU" to "₹226",
                "EBITDA Margin" to "53.2%",
                "Free Cash Flow" to "₹32,000 Cr",
                "52W High/Low" to "₹1,710 / ₹915"
            )
        ),
        StockPick(
            symbol = "HAL",
            companyName = "Hindustan Aeronautics Limited",
            sector = "Aerospace & Defence",
            currentPrice = "₹4,480.00",
            targetPrice = "₹5,350.00",
            potentialUpside = "+19.4%",
            stopLoss = "₹4,150.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "HDFC Securities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            rationale = "Indigenization monopoly in military combat aircraft (Tejas Mk1A/Mk2), Light Combat Helicopters (Prachand), and GE-414 jet engine co-production transfer of technology.",
            keyMetrics = listOf(
                "P/E Ratio" to "38.2",
                "ROE" to "27.4%",
                "Order Book" to "₹94,000 Cr",
                "52W High/Low" to "₹5,675 / ₹1,900"
            )
        )
    )

    val ALL_MUTUAL_FUNDS: List<MutualFundRecommendation> = listOf(
        // === NATIONAL AMC RECOMMENDATIONS ===
        MutualFundRecommendation(
            fundName = "Parag Parikh Flexi Cap Fund - Direct (Growth)",
            fundHouse = "PPFAS Mutual Fund",
            category = "Flexi Cap Fund",
            riskLevel = "Very High",
            nav = "₹84.62",
            rating = 5,
            return1Yr = "32.4%",
            return3Yr = "22.8% CAGR",
            return5Yr = "24.1% CAGR",
            expenseRatio = "0.62%",
            aum = "₹78,400 Cr",
            researchSource = "Value Research & CRISIL 5-Star",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "The premier multi-cap allocator in India with disciplined value investing philosophy. Strong downside containment during market corrections and selective international exposure (Alphabet, Microsoft) for optimal currency diversification.",
            topHoldings = listOf("HDFC Bank (8.2%)", "ITC Ltd (7.4%)", "Bajaj Holdings (6.5%)", "Alphabet Inc (5.2%)", "Coal India (4.8%)")
        ),
        MutualFundRecommendation(
            fundName = "Mirae Asset Large & Midcap Fund - Direct (Growth)",
            fundHouse = "Mirae Asset Mutual Fund",
            category = "Large & Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹148.20",
            rating = 5,
            return1Yr = "38.6%",
            return3Yr = "24.5% CAGR",
            return5Yr = "21.9% CAGR",
            expenseRatio = "0.58%",
            aum = "₹42,150 Cr",
            researchSource = "Morningstar Gold Medalist",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Exceptional balance of blue-chip stability (50% Large Cap) and dynamic high-alpha mid-cap leaders. Consistent alpha generation with superior Sharpe ratio across bull and consolidation cycles.",
            topHoldings = listOf("ICICI Bank (6.8%)", "L&T (5.1%)", "Reliance Industries (4.9%)", "Bharat Electronics (3.9%)", "Trent (3.5%)")
        ),
        MutualFundRecommendation(
            fundName = "Nippon India Small Cap Fund - Direct (Growth)",
            fundHouse = "Nippon India Mutual Fund",
            category = "Small Cap Fund",
            riskLevel = "Very High",
            nav = "₹172.40",
            rating = 5,
            return1Yr = "44.2%",
            return3Yr = "31.6% CAGR",
            return5Yr = "33.8% CAGR",
            expenseRatio = "0.69%",
            aum = "₹58,900 Cr",
            researchSource = "CRISIL Rank 1 Small Cap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Gold standard in Indian small cap investing with over 200+ diversified stocks, avoiding concentration risk while riding India's domestic manufacturing, capital goods, and digitization tailwinds.",
            topHoldings = listOf("Tube Investments (2.8%)", "HDFC Bank (2.4%)", "Apar Industries (2.2%)", "Voltamp Transformers (2.1%)", "KPIT Tech (1.9%)")
        ),
        MutualFundRecommendation(
            fundName = "ICICI Prudential Balanced Advantage Fund - Direct (Growth)",
            fundHouse = "ICICI Prudential AMC",
            category = "Dynamic Asset Allocation (BAF)",
            riskLevel = "Moderate",
            nav = "₹72.15",
            rating = 5,
            return1Yr = "19.5%",
            return3Yr = "14.8% CAGR",
            return5Yr = "15.2% CAGR",
            expenseRatio = "0.78%",
            aum = "₹62,300 Cr",
            researchSource = "Value Research 5-Star BAF",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Ideal choice for conservative to moderate equity investors. Automatically rebalances equity allocation (between 30% and 80%) based on proprietary Price-to-Book and market sentiment metrics, shielding wealth from sharp market corrections.",
            topHoldings = listOf("ICICI Bank (5.8%)", "Govt of India Bonds 7.18% (14.2%)", "Infosys (4.1%)", "Bharti Airtel (3.7%)", "TCS (3.2%)")
        ),
        MutualFundRecommendation(
            fundName = "SBI Contra Fund - Direct (Growth)",
            fundHouse = "SBI Mutual Fund",
            category = "Contra / Value Equity Fund",
            riskLevel = "Very High",
            nav = "₹418.50",
            rating = 5,
            return1Yr = "39.8%",
            return3Yr = "28.4% CAGR",
            return5Yr = "27.2% CAGR",
            expenseRatio = "0.64%",
            aum = "₹34,800 Cr",
            researchSource = "Morningstar 5-Star Rating",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Flawless contrarian track record buying fundamentally sound sectors out of market favor and riding turnaround momentum. Excellent allocation across public sector banks, capital goods, and healthcare.",
            topHoldings = listOf("State Bank of India (5.6%)", "Gail India (3.9%)", "Cognizant Technology (3.4%)", "HDFC Bank (3.1%)", "Sun Pharma (2.9%)")
        ),
        MutualFundRecommendation(
            fundName = "HDFC Mid-Cap Opportunities Fund - Direct (Growth)",
            fundHouse = "HDFC AMC",
            category = "Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹196.80",
            rating = 5,
            return1Yr = "41.5%",
            return3Yr = "27.9% CAGR",
            return5Yr = "25.6% CAGR",
            expenseRatio = "0.74%",
            aum = "₹71,200 Cr",
            researchSource = "CRISIL 5-Star Midcap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "India's largest mid-cap fund with remarkable downside protection. Focuses on companies with dominant domestic market share, strong free cash flows, and high governance standards.",
            topHoldings = listOf("Max Healthcare (4.8%)", "Tata Communications (3.9%)", "Indian Hotels (3.7%)", "Apollo Tyres (3.3%)", "Federal Bank (3.1%)")
        ),
        MutualFundRecommendation(
            fundName = "Quant Active Fund - Direct (Growth)",
            fundHouse = "Quant AMC",
            category = "Multi Cap Fund",
            riskLevel = "Very High",
            nav = "₹685.20",
            rating = 5,
            return1Yr = "42.1%",
            return3Yr = "26.2% CAGR",
            return5Yr = "29.4% CAGR",
            expenseRatio = "0.76%",
            aum = "₹11,400 Cr",
            researchSource = "Value Research Dynamic Momentum",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Proprietary VLRT (Valuation, Liquidity, Risk appetite, Time) quantitative momentum framework. Dynamically rotates into high-alpha sectors ahead of consensus macroeconomic shifts.",
            topHoldings = listOf("Reliance Industries (8.4%)", "HDFC Bank (6.2%)", "Adani Power (4.5%)", "Jio Financial Services (4.1%)", "SAIL (3.8%)")
        ),
        MutualFundRecommendation(
            fundName = "Motilal Oswal Midcap Fund - Direct (Growth)",
            fundHouse = "Motilal Oswal AMC",
            category = "Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹112.40",
            rating = 5,
            return1Yr = "48.6%",
            return3Yr = "33.5% CAGR",
            return5Yr = "28.1% CAGR",
            expenseRatio = "0.68%",
            aum = "₹16,800 Cr",
            researchSource = "Morningstar Top Quartile Performer",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "High-conviction, concentrated QGLP (Quality, Growth, Longevity, Price) investing philosophy. Exceptional stock picks in Trent, Persistent Systems, and Kalyan Jewellers generated industry-leading alpha.",
            topHoldings = listOf("Trent Ltd (9.8%)", "Persistent Systems (7.6%)", "Kalyan Jewellers (6.8%)", "Coforge (5.4%)", "Polycab India (4.9%)")
        ),
        MutualFundRecommendation(
            fundName = "Tata Digital India Fund - Direct (Growth)",
            fundHouse = "Tata Mutual Fund",
            category = "Thematic / Technology Fund",
            riskLevel = "Very High",
            nav = "₹52.10",
            rating = 4,
            return1Yr = "34.2%",
            return3Yr = "19.8% CAGR",
            return5Yr = "23.5% CAGR",
            expenseRatio = "0.38%",
            aum = "₹9,600 Cr",
            researchSource = "Value Research 4-Star Tech Thematic",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Premier dedicated technology fund capturing India's digital transformation, IT exports, and new-age internet platforms. Lowest expense ratio in the category.",
            topHoldings = listOf("Infosys (16.2%)", "TCS (14.5%)", "HCL Tech (8.9%)", "Tech Mahindra (6.7%)", "Zomato (5.2%)")
        ),
        MutualFundRecommendation(
            fundName = "Bandhan Core Equity Fund - Direct (Growth)",
            fundHouse = "Bandhan AMC",
            category = "Large & Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹128.90",
            rating = 4,
            return1Yr = "35.8%",
            return3Yr = "23.1% CAGR",
            return5Yr = "20.4% CAGR",
            expenseRatio = "0.67%",
            aum = "₹5,200 Cr",
            researchSource = "CRISIL 4-Star Large & Midcap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "Sep 2026 (Fresh)",
            verdictAndAnalysis = "Disciplined bottom-up growth at reasonable price (GARP) allocation across domestic consumption, financialization, and infrastructure beneficiaries.",
            topHoldings = listOf("ICICI Bank (6.4%)", "HDFC Bank (5.8%)", "Infosys (4.2%)", "Larsen & Toubro (3.9%)", "Bharat Electronics (3.2%)")
        )
    )

    // Backward-compatibility aliases
    val INDIAN_STOCK_PICKS get() = ALL_STOCK_PICKS.filter { !it.isInternational }
    val INTERNATIONAL_STOCK_PICKS get() = ALL_STOCK_PICKS.filter { it.isInternational }
    val MUTUAL_FUND_RECOMMENDATIONS get() = ALL_MUTUAL_FUNDS.filter { !it.isInternational }
    val GLOBAL_MUTUAL_FUNDS get() = ALL_MUTUAL_FUNDS.filter { it.isInternational }
}
