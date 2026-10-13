package com.example.data

import com.example.model.AudioSquawk
import com.example.model.BenchmarkIndex
import com.example.model.MarketAsset
import com.example.model.MarketSession

object MockData {

    val marketSessions = listOf(
        MarketSession("1", "NYSE", "OPEN", true),
        MarketSession("2", "NSE INDIA", "CLOSED", false),
        MarketSession("3", "FX 24/5", "ACTIVE", true),
        MarketSession("4", "LONDON", "OPEN", true),
        MarketSession("5", "TOKYO", "CLOSED", false)
    )

    val benchmarks = listOf(
        BenchmarkIndex(
            name = "NIFTY 50",
            price = "24,834.10",
            change = "+207.3",
            changePercent = "+0.84%",
            isPositive = true,
            sparkline = listOf(10f, 12f, 11f, 15f, 14f, 18f, 22f, 21f, 26f, 28f, 32f, 31f, 36f)
        ),
        BenchmarkIndex(
            name = "S&P 500",
            price = "5,864.67",
            change = "+64.8",
            changePercent = "+1.12%",
            isPositive = true,
            sparkline = listOf(8f, 9f, 8.5f, 11f, 10f, 13f, 16f, 15f, 19f, 22f, 24f, 27f, 29f)
        ),
        BenchmarkIndex(
            name = "NASDAQ 100",
            price = "20,412.30",
            change = "+291.5",
            changePercent = "+1.45%",
            isPositive = true,
            sparkline = listOf(12f, 14f, 13f, 17f, 19f, 21f, 25f, 27f, 31f, 34f, 37f)
        ),
        BenchmarkIndex(
            name = "BTC/USD",
            price = "67,420.00",
            change = "+1,865.0",
            changePercent = "+2.85%",
            isPositive = true,
            sparkline = listOf(20f, 22f, 25f, 23f, 28f, 31f, 33f, 37f, 42f, 45f)
        )
    )

    val initialAssets = listOf(
        MarketAsset(
            id = "NVDA",
            symbol = "NVDA",
            fullSymbol = "NVIDIA Corp.",
            tag = "US TECH",
            category = "US Tech Giants",
            subtitle = "NVIDIA Corporation",
            price = 141.20,
            changePercent = 4.25,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "CHIP",
            sparkline = listOf(132f, 134f, 133f, 137f, 139f, 140f, 141.2f),
            high24h = 142.80,
            low24h = 135.50,
            volume = "$48.2B",
            isDualAction = false
        ),
        MarketAsset(
            id = "NIFTY_BASKET",
            symbol = "NIFT...",
            fullSymbol = "Nifty 50 Basket",
            tag = "IND INDEX",
            category = "Indian / Nifty 50",
            subtitle = "Nifty Basket · ₹24,834",
            price = 298.50,
            changePercent = 0.84,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "BANK",
            sparkline = listOf(292f, 294f, 293f, 296f, 297f, 298.5f),
            high24h = 301.20,
            low24h = 294.10,
            volume = "₹18,400 Cr",
            isDualAction = false
        ),
        MarketAsset(
            id = "SPACEX",
            symbol = "SPAC...",
            fullSymbol = "SpaceX Syndicate",
            tag = "PRE-IPO",
            category = "Pre-IPO Syndicates",
            subtitle = "Space Exploration Syn...",
            price = 210.00,
            changePercent = 6.80,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "ROCKET",
            sparkline = listOf(195f, 198f, 202f, 204f, 209f, 210f),
            high24h = 215.00,
            low24h = 194.00,
            volume = "$640M",
            isDualAction = false
        ),
        MarketAsset(
            id = "TSLA",
            symbol = "TSLA",
            fullSymbol = "Tesla Inc.",
            tag = "NASDAQ",
            category = "US Tech Giants",
            subtitle = "Tesla Incorporated",
            price = 238.40,
            changePercent = -1.52,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "CAR",
            sparkline = listOf(244f, 242f, 240f, 241f, 239f, 238.4f),
            high24h = 245.50,
            low24h = 236.10,
            volume = "$22.4B",
            isDualAction = true
        ),
        MarketAsset(
            id = "TTM",
            symbol = "TTM",
            fullSymbol = "Tata Motors Mobility",
            tag = "ADR / IND",
            category = "Indian / Nifty 50",
            subtitle = "Tata Motors Mobility",
            price = 28.40,
            changePercent = 2.15,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "CAR2",
            sparkline = listOf(27.5f, 27.8f, 28.0f, 28.2f, 28.4f),
            high24h = 29.10,
            low24h = 27.30,
            volume = "$320M",
            isDualAction = false
        ),
        MarketAsset(
            id = "EURUSD",
            symbol = "EUR/...",
            fullSymbol = "EUR / USD",
            tag = "MAJOR FX",
            category = "FX Majors",
            subtitle = "Euro / US Dollar",
            price = 1.0864,
            changePercent = 0.32,
            pricePrefix = "",
            decimalPlaces = 4,
            iconType = "EXCHANGE",
            sparkline = listOf(1.082f, 1.083f, 1.085f, 1.0864f),
            high24h = 1.0890,
            low24h = 1.0815,
            volume = "$2.4T",
            isDualAction = false
        ),
        MarketAsset(
            id = "RB_INDX",
            symbol = "RB-INDX",
            fullSymbol = "Red Bull Global Index",
            tag = "SPORTS",
            category = "Sports & Indices",
            subtitle = "Red Bull Global Index",
            price = 88.50,
            changePercent = 3.40,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "SPEED",
            sparkline = listOf(84f, 85.5f, 86.2f, 87.1f, 88.5f),
            high24h = 90.00,
            low24h = 83.50,
            volume = "$140M",
            isDualAction = false
        ),
        MarketAsset(
            id = "USDINR",
            symbol = "USD/INR",
            fullSymbol = "USD / INR Spot",
            tag = "FX SPOT",
            category = "FX Majors",
            subtitle = "US Dollar / Rupee",
            price = 83.92,
            changePercent = -0.12,
            pricePrefix = "",
            decimalPlaces = 2,
            iconType = "RUPEE",
            sparkline = listOf(84.10f, 84.05f, 83.98f, 83.92f),
            high24h = 84.22,
            low24h = 83.85,
            volume = "$14.8B",
            isDualAction = true
        ),
        MarketAsset(
            id = "AAPL",
            symbol = "AAPL",
            fullSymbol = "Apple Inc.",
            tag = "MEGA CAP",
            category = "US Tech Giants",
            subtitle = "Apple Incorporated",
            price = 231.50,
            changePercent = 0.95,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "TECH",
            sparkline = listOf(228f, 229f, 230f, 231f, 231.5f),
            high24h = 233.20,
            low24h = 227.80,
            volume = "$34.1B",
            isDualAction = false
        ),
        MarketAsset(
            id = "MSFT",
            symbol = "MSFT",
            fullSymbol = "Microsoft Corp.",
            tag = "MEGA CAP",
            category = "US Tech Giants",
            subtitle = "Microsoft Cloud & AI",
            price = 428.10,
            changePercent = 1.18,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "TECH",
            sparkline = listOf(420f, 423f, 425f, 428.1f),
            high24h = 431.00,
            low24h = 419.50,
            volume = "$28.5B",
            isDualAction = false
        ),
        MarketAsset(
            id = "RELIANCE",
            symbol = "RELIANCE",
            fullSymbol = "Reliance Ind. GDR",
            tag = "IND ADR",
            category = "Indian / Nifty 50",
            subtitle = "Reliance Conglomerate",
            price = 68.20,
            changePercent = 1.75,
            pricePrefix = "$",
            decimalPlaces = 2,
            iconType = "BANK",
            sparkline = listOf(66f, 66.8f, 67.5f, 68.2f),
            high24h = 69.10,
            low24h = 65.80,
            volume = "$450M",
            isDualAction = false
        )
    )

    val squawkAlerts = listOf(
        AudioSquawk("1", "10:14:02", "FED SWAPS PRICE IN 25BPS RATE CUT PROBABILITY SURGE TO 88%", "MACRO", "BULLISH"),
        AudioSquawk("2", "10:12:45", "NVIDIA DATA CENTER ACCELERATOR ORDER FLOW TOPS RECORD REVENUE CONSENSUS", "TECH", "BULLISH"),
        AudioSquawk("3", "10:09:18", "NSE NIFTY 50 CROSSES PSYCHOLOGICAL 24,800 RESISTANCE ON FII EXPANSION", "INDIA", "BULLISH"),
        AudioSquawk("4", "10:04:30", "US TREASURY 10-YR YIELD RETREATS 4.2 BPS TO 4.02% SPURRING TECH RECOVERY", "BONDS", "NEUTRAL"),
        AudioSquawk("5", "09:58:12", "SPACEX SYNDICATE SECONDARY TENDER OFFERS HEAVY BID AT $210/SH EQUIVALENT", "PRE-IPO", "BULLISH")
    )
}
