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
    val recommendationDate: String = "02 Nov 2026 (Live Call)",
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
    val recommendationDate: String = "02 Nov 2026 (Live Rating)",
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
            currentPrice = "₹318.50",
            targetPrice = "₹395.00",
            potentialUpside = "+24.0%",
            stopLoss = "₹292.00",
            action = "STRONG BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Motilal Oswal",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Call)",
            rationale = "Robust ₹78,500+ Cr order book pipeline across indigenized radar suites, electronic warfare equipment, and naval communication systems. Operating margins expand sustainably on high-margin defense exports and quick execution.",
            keyMetrics = listOf(
                "P/E Ratio" to "42.1",
                "ROE" to "26.4%",
                "Order Book" to "₹78,500 Cr",
                "52W High/Low" to "₹340 / ₹138"
            )
        ),
        StockPick(
            symbol = "LT",
            companyName = "Larsen & Toubro Ltd",
            sector = "Infrastructure & Heavy Engineering",
            currentPrice = "₹3,680.00",
            targetPrice = "₹4,350.00",
            potentialUpside = "+18.2%",
            stopLoss = "₹3,480.00",
            action = "BUY",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "Goldman Sachs",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Call)",
            rationale = "Massive infrastructure capex execution across Indian high-speed rail, renewables, and Middle East mega EPC contracts. Record execution speed driving double-digit revenue visibility with steady balance sheet deleveraging.",
            keyMetrics = listOf(
                "P/E Ratio" to "33.8",
                "ROE" to "16.8%",
                "Order Inflow" to "+24% YoY",
                "52W High/Low" to "₹3,948 / ₹2,920"
            )
        ),
        StockPick(
            symbol = "HDFCBANK",
            companyName = "HDFC Bank Limited",
            sector = "Banking & Financial Services",
            currentPrice = "₹1,745.00",
            targetPrice = "₹2,060.00",
            potentialUpside = "+18.0%",
            stopLoss = "₹1,620.00",
            action = "OVERWEIGHT",
            timeHorizon = "Medium to Long Term",
            researchSource = "Morgan Stanley",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Call)",
            rationale = "Credit-deposit ratio improving rapidly post-merger integration. Asset quality remains pristine with Net NPA under 0.33%. Valuation at 2.45x FY27E ABV offers attractive risk-reward for long-term compounders.",
            keyMetrics = listOf(
                "P/E Ratio" to "18.4",
                "P/BV" to "2.45x",
                "Net NPA" to "0.32%",
                "52W High/Low" to "₹1,794 / ₹1,363"
            )
        ),
        StockPick(
            symbol = "TATAPOWER",
            companyName = "Tata Power Company Limited",
            sector = "Clean Energy & Transmission",
            currentPrice = "₹438.00",
            targetPrice = "₹525.00",
            potentialUpside = "+19.9%",
            stopLoss = "₹405.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Jefferies",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Call)",
            rationale = "Aggressive 20GW renewable capacity pipeline by 2030, surging rooftop solar deployments under PM Surya Ghar Muft Bijli Yojana, and expanding EV public fast-charging grid network nationwide.",
            keyMetrics = listOf(
                "P/E Ratio" to "35.2",
                "EV/EBITDA" to "13.8x",
                "Renewable Mix" to "43%",
                "52W High/Low" to "₹471 / ₹235"
            )
        ),
        StockPick(
            symbol = "ZOMATO",
            companyName = "Zomato (Eternal Limited)",
            sector = "Quick Commerce & Consumer Tech",
            currentPrice = "₹272.00",
            targetPrice = "₹340.00",
            potentialUpside = "+25.0%",
            stopLoss = "₹248.00",
            action = "BUY",
            timeHorizon = "Medium to Long Term",
            researchSource = "Kotak Institutional Equities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Call)",
            rationale = "Blinkit quick-commerce dark store footprint doubling ahead of schedule with store-level EBITDA break-even achieved across top 10 metros. Food delivery GOV maintaining 23%+ compounding growth.",
            keyMetrics = listOf(
                "GOV Growth" to "+39% YoY",
                "Blinkit Contribution" to "4.6%",
                "Cash Balance" to "₹12,800 Cr",
                "52W High/Low" to "₹298 / ₹105"
            )
        ),
        StockPick(
            symbol = "SBIN",
            companyName = "State Bank of India",
            sector = "PSU Banking & Credit",
            currentPrice = "₹825.00",
            targetPrice = "₹990.00",
            potentialUpside = "+20.0%",
            stopLoss = "₹770.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "ICICI Securities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "31 Oct 2026 (Fresh Call)",
            rationale = "Unmatched retail deposit franchise with lowest funding costs. Corporate credit demand picking up in power, infra, and renewable capex. RoA consistently above 1.12% with Net NPA at historical lows.",
            keyMetrics = listOf(
                "P/BV" to "1.28x",
                "RoA" to "1.14%",
                "Net NPA" to "0.55%",
                "52W High/Low" to "₹912 / ₹565"
            )
        ),
        StockPick(
            symbol = "TATAMOTORS",
            companyName = "Tata Motors Limited",
            sector = "Automotive & Electric Vehicles",
            currentPrice = "₹985.00",
            targetPrice = "₹1,180.00",
            potentialUpside = "+19.8%",
            stopLoss = "₹915.00",
            action = "BUY",
            timeHorizon = "Medium Term (6 Months)",
            researchSource = "Nomura",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "30 Oct 2026 (Fresh Call)",
            rationale = "Demerger into Commercial Vehicles and Passenger Vehicles (PV+EV+JLR) unlocking immense shareholder value. JLR net cash positive milestone reached with robust new Range Rover order book.",
            keyMetrics = listOf(
                "P/E Ratio" to "14.6",
                "JLR EBIT Margin" to "9.1%",
                "EV Market Share" to "67%",
                "52W High/Low" to "₹1,179 / ₹640"
            )
        ),
        StockPick(
            symbol = "INFY",
            companyName = "Infosys Limited",
            sector = "IT Services & Generative AI",
            currentPrice = "₹1,910.00",
            targetPrice = "₹2,240.00",
            potentialUpside = "+17.3%",
            stopLoss = "₹1,790.00",
            action = "OVERWEIGHT",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "JPMorgan",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Call)",
            rationale = "Large enterprise generative AI deal wins accelerating with Topaz platform adoption. US banking and European discretionary tech spend showing clear turnaround signals in Q2 FY27.",
            keyMetrics = listOf(
                "P/E Ratio" to "26.8",
                "Div Yield" to "2.3%",
                "Large Deal TCV" to "$4.4B",
                "52W High/Low" to "₹1,990 / ₹1,365"
            )
        ),
        StockPick(
            symbol = "TRENT",
            companyName = "Trent Limited",
            sector = "Retail & Consumer Lifestyle",
            currentPrice = "₹7,450.00",
            targetPrice = "₹8,850.00",
            potentialUpside = "+18.8%",
            stopLoss = "₹6,950.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "Axis Capital",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Call)",
            rationale = "Zudio store expansion delivering unprecedented same-store sales growth and fast cash conversion cycles. Westside and Star Bazaar formats executing high operating leverage.",
            keyMetrics = listOf(
                "Revenue Growth" to "+54% YoY",
                "EBITDA Margin" to "16.8%",
                "Store Count" to "850+",
                "52W High/Low" to "₹8,020 / ₹2,100"
            )
        ),
        StockPick(
            symbol = "SUNPHARMA",
            companyName = "Sun Pharmaceutical Industries",
            sector = "Specialty Pharma & Healthcare",
            currentPrice = "₹1,840.00",
            targetPrice = "₹2,150.00",
            potentialUpside = "+16.8%",
            stopLoss = "₹1,720.00",
            action = "OUTPERFORM",
            timeHorizon = "Medium to Long Term",
            researchSource = "Bernstein",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "30 Oct 2026 (Fresh Call)",
            rationale = "Global innovative specialty portfolio (Ilumya, Cequa, Winlevi) driving high gross margin expansion. Zero regulatory red flags on major domestic manufacturing facilities.",
            keyMetrics = listOf(
                "P/E Ratio" to "33.2",
                "Specialty Share" to "24%",
                "Net Debt" to "Cash Positive",
                "52W High/Low" to "₹1,960 / ₹1,140"
            )
        ),
        StockPick(
            symbol = "BHARTIARTL",
            companyName = "Bharti Airtel Limited",
            sector = "Telecom & Digital Services",
            currentPrice = "₹1,620.00",
            targetPrice = "₹1,920.00",
            potentialUpside = "+18.5%",
            stopLoss = "₹1,510.00",
            action = "BUY",
            timeHorizon = "Long Term (6–12 Months)",
            researchSource = "CLSA",
            sourceType = "International Institution",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Call)",
            rationale = "Industry-leading ARPU crossing ₹230+ driven by steady tariff hikes and 2G-to-4G/5G upgrades. Airtel Business B2B cloud and cybersecurity services delivering resilient cash flows.",
            keyMetrics = listOf(
                "ARPU" to "₹232",
                "EBITDA Margin" to "53.6%",
                "Free Cash Flow" to "₹33,500 Cr",
                "52W High/Low" to "₹1,710 / ₹925"
            )
        ),
        StockPick(
            symbol = "HAL",
            companyName = "Hindustan Aeronautics Limited",
            sector = "Aerospace & Defence",
            currentPrice = "₹4,620.00",
            targetPrice = "₹5,520.00",
            potentialUpside = "+19.5%",
            stopLoss = "₹4,280.00",
            action = "BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            researchSource = "HDFC Securities",
            sourceType = "National Research",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Call)",
            rationale = "Indigenization monopoly in military combat aircraft (Tejas Mk1A/Mk2), Light Combat Helicopters (Prachand), and GE-414 jet engine co-production transfer of technology.",
            keyMetrics = listOf(
                "P/E Ratio" to "37.8",
                "ROE" to "27.8%",
                "Order Book" to "₹96,500 Cr",
                "52W High/Low" to "₹5,675 / ₹1,950"
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
            nav = "₹88.42",
            rating = 5,
            return1Yr = "33.8%",
            return3Yr = "23.4% CAGR",
            return5Yr = "24.6% CAGR",
            expenseRatio = "0.62%",
            aum = "₹82,600 Cr",
            researchSource = "Value Research & CRISIL 5-Star",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Rating)",
            verdictAndAnalysis = "The premier multi-cap allocator in India with disciplined value investing philosophy. Strong downside containment during market corrections and selective international exposure (Alphabet, Microsoft) for optimal currency diversification.",
            topHoldings = listOf("HDFC Bank (8.4%)", "ITC Ltd (7.2%)", "Bajaj Holdings (6.6%)", "Alphabet Inc (5.0%)", "Coal India (4.7%)")
        ),
        MutualFundRecommendation(
            fundName = "Mirae Asset Large & Midcap Fund - Direct (Growth)",
            fundHouse = "Mirae Asset Mutual Fund",
            category = "Large & Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹154.60",
            rating = 5,
            return1Yr = "39.4%",
            return3Yr = "25.1% CAGR",
            return5Yr = "22.4% CAGR",
            expenseRatio = "0.58%",
            aum = "₹44,800 Cr",
            researchSource = "Morningstar Gold Medalist",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Rating)",
            verdictAndAnalysis = "Exceptional balance of blue-chip stability (50% Large Cap) and dynamic high-alpha mid-cap leaders. Consistent alpha generation with superior Sharpe ratio across bull and consolidation cycles.",
            topHoldings = listOf("ICICI Bank (6.9%)", "L&T (5.2%)", "Reliance Industries (4.8%)", "Bharat Electronics (4.1%)", "Trent (3.7%)")
        ),
        MutualFundRecommendation(
            fundName = "Nippon India Small Cap Fund - Direct (Growth)",
            fundHouse = "Nippon India Mutual Fund",
            category = "Small Cap Fund",
            riskLevel = "Very High",
            nav = "₹181.20",
            rating = 5,
            return1Yr = "45.1%",
            return3Yr = "32.4% CAGR",
            return5Yr = "34.2% CAGR",
            expenseRatio = "0.69%",
            aum = "₹63,200 Cr",
            researchSource = "CRISIL Rank 1 Small Cap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Rating)",
            verdictAndAnalysis = "Gold standard in Indian small cap investing with over 200+ diversified stocks, avoiding concentration risk while riding India's domestic manufacturing, capital goods, and digitization tailwinds.",
            topHoldings = listOf("Tube Investments (2.9%)", "HDFC Bank (2.3%)", "Apar Industries (2.2%)", "Voltamp Transformers (2.1%)", "KPIT Tech (2.0%)")
        ),
        MutualFundRecommendation(
            fundName = "ICICI Prudential Balanced Advantage Fund - Direct (Growth)",
            fundHouse = "ICICI Prudential AMC",
            category = "Dynamic Asset Allocation (BAF)",
            riskLevel = "Moderate",
            nav = "₹74.85",
            rating = 5,
            return1Yr = "20.2%",
            return3Yr = "15.4% CAGR",
            return5Yr = "15.8% CAGR",
            expenseRatio = "0.78%",
            aum = "₹65,800 Cr",
            researchSource = "Value Research 5-Star BAF",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Rating)",
            verdictAndAnalysis = "Ideal choice for conservative to moderate equity investors. Automatically rebalances equity allocation (between 30% and 80%) based on proprietary Price-to-Book and market sentiment metrics, shielding wealth from sharp market corrections.",
            topHoldings = listOf("ICICI Bank (5.9%)", "Govt of India Bonds 7.18% (14.0%)", "Infosys (4.2%)", "Bharti Airtel (3.8%)", "TCS (3.3%)")
        ),
        MutualFundRecommendation(
            fundName = "SBI Contra Fund - Direct (Growth)",
            fundHouse = "SBI Mutual Fund",
            category = "Contra / Value Equity Fund",
            riskLevel = "Very High",
            nav = "₹434.10",
            rating = 5,
            return1Yr = "40.6%",
            return3Yr = "29.1% CAGR",
            return5Yr = "27.8% CAGR",
            expenseRatio = "0.64%",
            aum = "₹37,500 Cr",
            researchSource = "Morningstar 5-Star Rating",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Rating)",
            verdictAndAnalysis = "Flawless contrarian track record buying fundamentally sound sectors out of market favor and riding turnaround momentum. Excellent allocation across public sector banks, capital goods, and healthcare.",
            topHoldings = listOf("State Bank of India (5.7%)", "Gail India (3.8%)", "Cognizant Technology (3.3%)", "HDFC Bank (3.2%)", "Sun Pharma (3.0%)")
        ),
        MutualFundRecommendation(
            fundName = "HDFC Mid-Cap Opportunities Fund - Direct (Growth)",
            fundHouse = "HDFC AMC",
            category = "Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹206.50",
            rating = 5,
            return1Yr = "42.8%",
            return3Yr = "28.6% CAGR",
            return5Yr = "26.2% CAGR",
            expenseRatio = "0.74%",
            aum = "₹74,900 Cr",
            researchSource = "CRISIL 5-Star Midcap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Rating)",
            verdictAndAnalysis = "India's largest mid-cap fund with remarkable downside protection. Focuses on companies with dominant domestic market share, strong free cash flows, and high governance standards.",
            topHoldings = listOf("Max Healthcare (4.9%)", "Tata Communications (4.0%)", "Indian Hotels (3.8%)", "Apollo Tyres (3.4%)", "Federal Bank (3.2%)")
        ),
        MutualFundRecommendation(
            fundName = "Quant Active Fund - Direct (Growth)",
            fundHouse = "Quant AMC",
            category = "Multi Cap Fund",
            riskLevel = "Very High",
            nav = "₹715.40",
            rating = 5,
            return1Yr = "43.5%",
            return3Yr = "26.9% CAGR",
            return5Yr = "29.8% CAGR",
            expenseRatio = "0.76%",
            aum = "₹12,200 Cr",
            researchSource = "Value Research Dynamic Momentum",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Rating)",
            verdictAndAnalysis = "Proprietary VLRT (Valuation, Liquidity, Risk appetite, Time) quantitative momentum framework. Dynamically rotates into high-alpha sectors ahead of consensus macroeconomic shifts.",
            topHoldings = listOf("Reliance Industries (8.5%)", "HDFC Bank (6.4%)", "Adani Power (4.6%)", "Jio Financial Services (4.2%)", "SAIL (3.7%)")
        ),
        MutualFundRecommendation(
            fundName = "Motilal Oswal Midcap Fund - Direct (Growth)",
            fundHouse = "Motilal Oswal AMC",
            category = "Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹118.90",
            rating = 5,
            return1Yr = "49.8%",
            return3Yr = "34.2% CAGR",
            return5Yr = "28.9% CAGR",
            expenseRatio = "0.68%",
            aum = "₹18,400 Cr",
            researchSource = "Morningstar Top Quartile Performer",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "02 Nov 2026 (Live Rating)",
            verdictAndAnalysis = "High-conviction, concentrated QGLP (Quality, Growth, Longevity, Price) investing philosophy. Exceptional stock picks in Trent, Persistent Systems, and Kalyan Jewellers generated industry-leading alpha.",
            topHoldings = listOf("Trent Ltd (10.2%)", "Persistent Systems (7.8%)", "Kalyan Jewellers (6.9%)", "Coforge (5.5%)", "Polycab India (5.1%)")
        ),
        MutualFundRecommendation(
            fundName = "Tata Digital India Fund - Direct (Growth)",
            fundHouse = "Tata Mutual Fund",
            category = "Thematic / Technology Fund",
            riskLevel = "Very High",
            nav = "₹54.80",
            rating = 4,
            return1Yr = "35.6%",
            return3Yr = "20.5% CAGR",
            return5Yr = "24.1% CAGR",
            expenseRatio = "0.38%",
            aum = "₹10,500 Cr",
            researchSource = "Value Research 4-Star Tech Thematic",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "31 Oct 2026 (Fresh Rating)",
            verdictAndAnalysis = "Premier dedicated technology fund capturing India's digital transformation, IT exports, and new-age internet platforms. Lowest expense ratio in the category.",
            topHoldings = listOf("Infosys (16.4%)", "TCS (14.6%)", "HCL Tech (9.1%)", "Tech Mahindra (6.8%)", "Zomato (5.4%)")
        ),
        MutualFundRecommendation(
            fundName = "Bandhan Core Equity Fund - Direct (Growth)",
            fundHouse = "Bandhan AMC",
            category = "Large & Mid Cap Fund",
            riskLevel = "Very High",
            nav = "₹134.20",
            rating = 4,
            return1Yr = "36.9%",
            return3Yr = "23.8% CAGR",
            return5Yr = "21.1% CAGR",
            expenseRatio = "0.67%",
            aum = "₹5,650 Cr",
            researchSource = "CRISIL 4-Star Large & Midcap",
            sourceType = "National AMC",
            isInternational = false,
            recommendationDate = "01 Nov 2026 (Fresh Rating)",
            verdictAndAnalysis = "Disciplined bottom-up growth at reasonable price (GARP) allocation across domestic consumption, financialization, and infrastructure beneficiaries.",
            topHoldings = listOf("ICICI Bank (6.5%)", "HDFC Bank (5.9%)", "Infosys (4.3%)", "Larsen & Toubro (4.0%)", "Bharat Electronics (3.4%)")
        )
    )

    // Backward-compatibility aliases
    val INDIAN_STOCK_PICKS get() = ALL_STOCK_PICKS.filter { !it.isInternational }
    val INTERNATIONAL_STOCK_PICKS get() = ALL_STOCK_PICKS.filter { it.isInternational }
    val MUTUAL_FUND_RECOMMENDATIONS get() = ALL_MUTUAL_FUNDS.filter { !it.isInternational }
    val GLOBAL_MUTUAL_FUNDS get() = ALL_MUTUAL_FUNDS.filter { it.isInternational }
}
