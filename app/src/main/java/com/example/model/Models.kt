package com.example.model

data class MarketAsset(
    val id: String,
    val symbol: String,
    val fullSymbol: String,
    val tag: String,
    val category: String,
    val subtitle: String,
    val price: Double,
    val changePercent: Double,
    val pricePrefix: String = "$",
    val decimalPlaces: Int = 2,
    val iconType: String,
    val sparkline: List<Float>,
    val high24h: Double,
    val low24h: Double,
    val volume: String,
    val isUp: Boolean = changePercent >= 0,
    val isDualAction: Boolean = false
) {
    fun formattedPrice(): String {
        return if (pricePrefix.isNotEmpty()) {
            "$pricePrefix" + String.format(java.util.Locale.US, "%.${decimalPlaces}f", price)
        } else {
            String.format(java.util.Locale.US, "%.${decimalPlaces}f", price)
        }
    }

    fun formattedChange(): String {
        val sign = if (changePercent >= 0) "+" else ""
        return "$sign" + String.format(java.util.Locale.US, "%.2f%%", changePercent)
    }
}

data class BenchmarkIndex(
    val name: String,
    val price: String,
    val change: String,
    val changePercent: String,
    val isPositive: Boolean,
    val sparkline: List<Float>
)

data class MarketSession(
    val id: String,
    val label: String,
    val status: String,
    val isOpen: Boolean
)

data class FlowRadarData(
    val score: Int = 68,
    val state: String = "GREED / EXPANSION",
    val buyRatio: Int = 64,
    val sellRatio: Int = 36,
    val bidsVolume: String = "$42.8M Virtual Vol",
    val asksVolume: String = "Asks $24.1M"
)

data class OrderBookItem(
    val price: Double,
    val quantity: Double,
    val depthRatio: Float
)

data class TradePosition(
    val id: String,
    val assetSymbol: String,
    val assetName: String,
    val isLong: Boolean,
    val shares: Double,
    val entryPrice: Double,
    val currentPrice: Double,
    val leverage: Int = 1
) {
    val pnl: Double
        get() {
            val delta = if (isLong) currentPrice - entryPrice else entryPrice - currentPrice
            return delta * shares * leverage
        }

    val pnlPercent: Double
        get() {
            if (entryPrice == 0.0) return 0.0
            val delta = if (isLong) currentPrice - entryPrice else entryPrice - currentPrice
            return (delta / entryPrice) * 100.0 * leverage
        }
}

data class OrderHistoryItem(
    val id: String,
    val symbol: String,
    val isBuy: Boolean,
    val orderType: String,
    val shares: Double,
    val price: Double,
    val totalUsd: Double,
    val timeAgo: String
)

data class AudioSquawk(
    val id: String,
    val timestamp: String,
    val headline: String,
    val category: String,
    val sentiment: String
)
