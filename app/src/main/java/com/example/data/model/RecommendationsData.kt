package com.example.data.model

data class StockPick(
    val symbol: String,
    val companyName: String,
    val sector: String,
    val currentPrice: String,
    val targetPrice: String,
    val potentialUpside: String,
    val stopLoss: String,
    val action: String = "BUY", // BUY, ACCUMULATE, STRONG BUY
    val timeHorizon: String, // Short Term (1-3 weeks), Medium Term (3-6 months), Long Term (1 yr+)
    val rationale: String,
    val keyMetrics: List<Pair<String, String>> // e.g. "P/E" to "22.4", "ROE" to "18.5%", "52W High" to "₹3,450"
)

data class MutualFundRecommendation(
    val fundName: String,
    val fundHouse: String,
    val category: String, // Large & Mid Cap, Flexi Cap, Small Cap, Sectoral/Thematic, Balanced Advantage
    val riskLevel: String, // Very High, Moderately High, Moderate
    val nav: String,
    val rating: Int, // 1 to 5 Stars
    val return1Yr: String,
    val return3Yr: String,
    val return5Yr: String,
    val expenseRatio: String,
    val aum: String,
    val verdictAndAnalysis: String,
    val topHoldings: List<String>
)

object RecommendationsData {

    val INDIAN_STOCK_PICKS = listOf(
        StockPick(
            symbol = "BEL",
            companyName = "Bharat Electronics Limited",
            sector = "Defence & Aerospace",
            currentPrice = "₹308.50",
            targetPrice = "₹365.00",
            potentialUpside = "+18.3%",
            stopLoss = "₹288.00",
            action = "STRONG BUY",
            timeHorizon = "Medium Term (3–6 Months)",
            rationale = "Robust ₹76,000+ Cr order book pipeline across indigenized radar suites, electronic warfare equipment, and naval communication systems. Operating margins expand sustainably on high-margin defense exports.",
            keyMetrics = listOf(
                "P/E Ratio" to "41.2",
                "ROE" to "25.8%",
                "Order Book" to "₹76,200 Cr",
                "52W High/Low" to "₹340 / ₹132"
            )
        ),
        StockPick(
            symbol = "L&T",
            companyName = "Larsen & Toubro Ltd",
            sector = "Infrastructure & Heavy Engineering",
            currentPrice = "₹3,620.00",
            targetPrice = "₹4,150.00",
            potentialUpside = "+14.6%",
            stopLoss = "₹3,440.00",
            action = "BUY",
            timeHorizon = "Long Term (6–12 Months)",
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
            targetPrice = "₹1,950.00",
            potentialUpside = "+16.4%",
            stopLoss = "₹1,580.00",
            action = "BUY",
            timeHorizon = "Medium to Long Term",
            rationale = "Credit-deposit ratio improving rapidly post-merger integration. Asset quality remains pristine with Net NPA under 0.35%. Valuation at 2.4x FY26E ABV offers attractive risk-reward for long-term compounders.",
            keyMetrics = listOf(
                "P/E Ratio" to "18.8",
                "P/BV" to "2.4x",
                "Net NPA" to "0.33%",
                "52W High/Low" to "₹1,794 / ₹1,363"
            )
        ),
        StockPick(
            symbol = "TATACHEM",
            companyName = "Tata Chemicals Limited",
            sector = "Specialty Chemicals & EV Materials",
            currentPrice = "₹1,095.00",
            targetPrice = "₹1,260.00",
            potentialUpside = "+15.1%",
            stopLoss = "₹1,030.00",
            action = "ACCUMULATE",
            timeHorizon = "Short to Medium Term",
            rationale = "Soda ash pricing stabilization globally combined with strategic capacity expansions in specialty silica and fermentation products for EV battery chemistry partnerships.",
            keyMetrics = listOf(
                "P/E Ratio" to "22.5",
                "Div Yield" to "1.65%",
                "Debt/Equity" to "0.31",
                "52W High/Low" to "₹1,349 / ₹926"
            )
        )
    )

    val MUTUAL_FUND_RECOMMENDATIONS = listOf(
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
            verdictAndAnalysis = "Ideal choice for conservative to moderate equity investors. Automatically rebalances equity allocation (between 30% and 80%) based on proprietary Price-to-Book and market sentiment metrics, shielding wealth from sharp market corrections.",
            topHoldings = listOf("ICICI Bank (5.8%)", "Govt of India Bonds 7.18% (14.2%)", "Infosys (4.1%)", "Bharti Airtel (3.7%)", "TCS (3.2%)")
        )
    )
}
