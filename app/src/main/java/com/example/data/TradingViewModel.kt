package com.example.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AudioSquawk
import com.example.model.BenchmarkIndex
import com.example.model.FlowRadarData
import com.example.model.MarketAsset
import com.example.model.MarketSession
import com.example.model.OrderHistoryItem
import com.example.model.TradePosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class NavTab {
    PORTFOLIO,
    MARKETS,
    TRADE,
    ANALYTICS
}

data class TradingUiState(
    val currentTab: NavTab = NavTab.MARKETS,
    val virtualEquity: Double = 10000.00,
    val availableCash: Double = 8120.00,
    val realizedPnl: Double = 342.50,
    val assets: List<MarketAsset> = MockData.initialAssets,
    val benchmarks: List<BenchmarkIndex> = MockData.benchmarks,
    val sessions: List<MarketSession> = MockData.marketSessions,
    val radar: FlowRadarData = FlowRadarData(),
    val categories: List<String> = listOf(
        "All Markets (9)",
        "🇮🇳 Indian / Nifty 50",
        "🇺🇸 US Tech Giants",
        "🚀 Pre-IPO Syndicates",
        "💱 FX Majors",
        "🏎️ Sports & Indices"
    ),
    val selectedCategory: String = "All Markets (9)",
    val searchQuery: String = "",
    val positions: List<TradePosition> = listOf(
        TradePosition(
            id = "pos_1",
            assetSymbol = "NVDA",
            assetName = "NVIDIA Corporation",
            isLong = true,
            shares = 10.0,
            entryPrice = 135.20,
            currentPrice = 141.20,
            leverage = 1
        ),
        TradePosition(
            id = "pos_2",
            assetSymbol = "SPAC...",
            assetName = "SpaceX Syndicate",
            isLong = true,
            shares = 5.0,
            entryPrice = 196.00,
            currentPrice = 210.00,
            leverage = 1
        )
    ),
    val orderHistory: List<OrderHistoryItem> = listOf(
        OrderHistoryItem(
            id = "ord_1",
            symbol = "NVDA",
            isBuy = true,
            orderType = "MARKET",
            shares = 10.0,
            price = 135.20,
            totalUsd = 1352.00,
            timeAgo = "18m ago"
        ),
        OrderHistoryItem(
            id = "ord_2",
            symbol = "SPAC...",
            isBuy = true,
            orderType = "MARKET",
            shares = 5.0,
            price = 196.00,
            totalUsd = 980.00,
            timeAgo = "1h ago"
        )
    ),
    val squawkAlerts: List<AudioSquawk> = MockData.squawkAlerts,
    val selectedAssetForTrade: MarketAsset? = null,
    val isSquawkDialogOpen: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val isQuickTradeOpen: Boolean = false,
    val toastNotification: String? = null
) {
    val totalUnrealizedPnl: Double
        get() = positions.sumOf { it.pnl }

    val totalPortfolioValue: Double
        get() = availableCash + positions.sumOf { it.shares * it.currentPrice }

    val filteredAssets: List<MarketAsset>
        get() {
            var list = assets
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                list = list.filter {
                    it.symbol.lowercase().contains(q) ||
                    it.subtitle.lowercase().contains(q) ||
                    it.tag.lowercase().contains(q) ||
                    it.category.lowercase().contains(q)
                }
            }
            if (!selectedCategory.startsWith("All Markets")) {
                val catClean = selectedCategory.replace(Regex("[^a-zA-Z0-9 /]"), "").trim()
                list = list.filter {
                    it.category.contains(catClean, ignoreCase = true) ||
                    catClean.contains(it.category, ignoreCase = true)
                }
            }
            return list
        }
}

class TradingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TradingUiState())
    val uiState: StateFlow<TradingUiState> = _uiState.asStateFlow()

    init {
        startLiveTickSimulation()
    }

    private fun startLiveTickSimulation() {
        viewModelScope.launch {
            while (isActive) {
                delay(2400)
                updateMarketTicks()
            }
        }
    }

    private fun updateMarketTicks() {
        _uiState.update { state ->
            val updatedAssets = state.assets.map { asset ->
                // random small tick: -0.3% to +0.3%
                val deltaPercent = (Random.nextDouble() - 0.48) * 0.4
                val newPrice = (asset.price * (1.0 + deltaPercent / 100.0)).coerceAtLeast(0.01)
                val newChange = asset.changePercent + (deltaPercent * 0.2)
                val updatedSpark = (asset.sparkline.drop(1) + newPrice.toFloat()).takeLast(10)
                asset.copy(
                    price = newPrice,
                    changePercent = newChange,
                    sparkline = updatedSpark,
                    isUp = newChange >= 0
                )
            }

            // Sync open positions with latest prices
            val updatedPositions = state.positions.map { pos ->
                val matchingAsset = updatedAssets.find { it.symbol == pos.assetSymbol || it.id == pos.assetSymbol }
                if (matchingAsset != null) {
                    pos.copy(currentPrice = matchingAsset.price)
                } else pos
            }

            // Random slight variance on radar
            val radarVariance = Random.nextInt(-1, 2)
            val newScore = (state.radar.score + radarVariance).coerceIn(40, 85)

            state.copy(
                assets = updatedAssets,
                positions = updatedPositions,
                radar = state.radar.copy(score = newScore)
            )
        }
    }

    fun setTab(tab: NavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openTradeForAsset(asset: MarketAsset) {
        _uiState.update {
            it.copy(
                selectedAssetForTrade = asset,
                isQuickTradeOpen = true
            )
        }
    }

    fun navigateToTradeDesk(asset: MarketAsset) {
        _uiState.update {
            it.copy(
                selectedAssetForTrade = asset,
                currentTab = NavTab.TRADE,
                isQuickTradeOpen = false
            )
        }
    }

    fun closeQuickTrade() {
        _uiState.update { it.copy(isQuickTradeOpen = false) }
    }

    fun toggleSquawkDialog(open: Boolean) {
        _uiState.update { it.copy(isSquawkDialogOpen = open) }
    }

    fun toggleFilterSheet(open: Boolean) {
        _uiState.update { it.copy(isFilterSheetOpen = open) }
    }

    fun dismissToast() {
        _uiState.update { it.copy(toastNotification = null) }
    }

    fun resetDemoBalance() {
        _uiState.update {
            it.copy(
                availableCash = 10000.00,
                virtualEquity = 10000.00,
                realizedPnl = 0.0,
                positions = emptyList(),
                toastNotification = "Virtual account reset to SIM $10,000.00 USD"
            )
        }
    }

    fun executeOrder(
        asset: MarketAsset,
        isBuy: Boolean,
        orderType: String,
        shares: Double,
        price: Double,
        leverage: Int = 1
    ) {
        val totalCost = (shares * price) / leverage

        _uiState.update { state ->
            if (isBuy && totalCost > state.availableCash) {
                return@update state.copy(
                    toastNotification = "Insufficient SIM buying power! Available: $${String.format("%.2f", state.availableCash)}"
                )
            }

            val newCash = if (isBuy) state.availableCash - totalCost else state.availableCash + totalCost
            val newPositions = state.positions.toMutableList()
            val existingIndex = newPositions.indexOfFirst { it.assetSymbol == asset.symbol && it.isLong == isBuy }

            if (existingIndex >= 0) {
                val existing = newPositions[existingIndex]
                val combinedShares = existing.shares + shares
                val newAvg = ((existing.entryPrice * existing.shares) + (price * shares)) / combinedShares
                newPositions[existingIndex] = existing.copy(
                    shares = combinedShares,
                    entryPrice = newAvg,
                    currentPrice = price
                )
            } else {
                newPositions.add(
                    TradePosition(
                        id = "pos_" + System.currentTimeMillis(),
                        assetSymbol = asset.symbol,
                        assetName = asset.subtitle,
                        isLong = isBuy,
                        shares = shares,
                        entryPrice = price,
                        currentPrice = price,
                        leverage = leverage
                    )
                )
            }

            val newHistory = listOf(
                OrderHistoryItem(
                    id = "ord_" + System.currentTimeMillis(),
                    symbol = asset.symbol,
                    isBuy = isBuy,
                    orderType = orderType,
                    shares = shares,
                    price = price,
                    totalUsd = shares * price,
                    timeAgo = "Just now"
                )
            ) + state.orderHistory

            state.copy(
                availableCash = newCash,
                positions = newPositions,
                orderHistory = newHistory,
                isQuickTradeOpen = false,
                toastNotification = "Filled: ${if (isBuy) "BUY" else "SELL"} ${String.format("%.2f", shares)} ${asset.symbol} @ $${String.format("%.2f", price)}"
            )
        }
    }

    fun closePosition(position: TradePosition) {
        _uiState.update { state ->
            val pnl = position.pnl
            val returnedCapital = (position.shares * position.entryPrice) / position.leverage + pnl
            val updatedPositions = state.positions.filterNot { it.id == position.id }
            val updatedCash = state.availableCash + returnedCapital
            val updatedRealized = state.realizedPnl + pnl

            val newHistory = listOf(
                OrderHistoryItem(
                    id = "ord_" + System.currentTimeMillis(),
                    symbol = position.assetSymbol,
                    isBuy = !position.isLong,
                    orderType = "MARKET_CLOSE",
                    shares = position.shares,
                    price = position.currentPrice,
                    totalUsd = position.shares * position.currentPrice,
                    timeAgo = "Just now"
                )
            ) + state.orderHistory

            state.copy(
                availableCash = updatedCash,
                realizedPnl = updatedRealized,
                positions = updatedPositions,
                orderHistory = newHistory,
                toastNotification = "Closed ${position.assetSymbol} | P&L: ${if (pnl >= 0) "+" else ""}$${String.format("%.2f", pnl)}"
            )
        }
    }
}
