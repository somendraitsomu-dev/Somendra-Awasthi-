package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.NavTab
import com.example.data.TradingViewModel
import com.example.ui.components.AccountDialog
import com.example.ui.components.BottomNavBar
import com.example.ui.components.FilterSheet
import com.example.ui.components.LiveSquawkDialog
import com.example.ui.components.QuickTradeDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.MarketsScreen
import com.example.ui.screens.PortfolioScreen
import com.example.ui.screens.TradeScreen
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ApexTradingApp()
            }
        }
    }
}

@Composable
fun ApexTradingApp(
    viewModel: TradingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isAccountDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ApexCanvas,
        bottomBar = {
            BottomNavBar(
                selectedTab = uiState.currentTab,
                onTabSelected = { tab -> viewModel.setTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Viewport
            when (uiState.currentTab) {
                NavTab.MARKETS -> {
                    MarketsScreen(
                        state = uiState,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onCategorySelect = { viewModel.setSelectedCategory(it) },
                        onAssetClick = { asset -> viewModel.navigateToTradeDesk(asset) },
                        onSimClick = { asset -> viewModel.openTradeForAsset(asset) },
                        onSquawkClick = { viewModel.toggleSquawkDialog(true) },
                        onAvatarClick = { isAccountDialogOpen = true },
                        onBalanceClick = { isAccountDialogOpen = true },
                        onFilterTuneClick = { viewModel.toggleFilterSheet(true) }
                    )
                }
                NavTab.TRADE -> {
                    TradeScreen(
                        state = uiState,
                        onSelectAsset = { viewModel.navigateToTradeDesk(it) },
                        onExecuteOrder = { asset, isBuy, orderType, shares, price, leverage ->
                            viewModel.executeOrder(asset, isBuy, orderType, shares, price, leverage)
                        },
                        onSquawkClick = { viewModel.toggleSquawkDialog(true) },
                        onAvatarClick = { isAccountDialogOpen = true },
                        onBalanceClick = { isAccountDialogOpen = true }
                    )
                }
                NavTab.PORTFOLIO -> {
                    PortfolioScreen(
                        state = uiState,
                        onClosePosition = { pos -> viewModel.closePosition(pos) },
                        onResetBalance = { viewModel.resetDemoBalance() },
                        onSquawkClick = { viewModel.toggleSquawkDialog(true) },
                        onAvatarClick = { isAccountDialogOpen = true },
                        onBalanceClick = { isAccountDialogOpen = true }
                    )
                }
                NavTab.ANALYTICS -> {
                    AnalyticsScreen(
                        state = uiState,
                        onSquawkClick = { viewModel.toggleSquawkDialog(true) },
                        onAvatarClick = { isAccountDialogOpen = true },
                        onBalanceClick = { isAccountDialogOpen = true }
                    )
                }
            }

            // Quick Trade Sheet Dialog
            if (uiState.isQuickTradeOpen && uiState.selectedAssetForTrade != null) {
                QuickTradeDialog(
                    asset = uiState.selectedAssetForTrade!!,
                    availableCash = uiState.availableCash,
                    onDismiss = { viewModel.closeQuickTrade() },
                    onExecuteOrder = { asset, isBuy, orderType, shares, price, leverage ->
                        viewModel.executeOrder(asset, isBuy, orderType, shares, price, leverage)
                    },
                    onOpenFullTradeDesk = { asset ->
                        viewModel.navigateToTradeDesk(asset)
                    }
                )
            }

            // Squawk Box Feed Dialog
            if (uiState.isSquawkDialogOpen) {
                LiveSquawkDialog(
                    alerts = uiState.squawkAlerts,
                    onDismiss = { viewModel.toggleSquawkDialog(false) }
                )
            }

            // Account / Reset Balance Dialog
            if (isAccountDialogOpen) {
                AccountDialog(
                    virtualEquity = uiState.totalPortfolioValue,
                    availableCash = uiState.availableCash,
                    onResetBalance = { viewModel.resetDemoBalance() },
                    onDismiss = { isAccountDialogOpen = false }
                )
            }

            // Filter sheet
            if (uiState.isFilterSheetOpen) {
                FilterSheet(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { viewModel.setSelectedCategory(it) },
                    onDismiss = { viewModel.toggleFilterSheet(false) }
                )
            }

            // Toast feedback banner
            AnimatedVisibility(
                visible = uiState.toastNotification != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                uiState.toastNotification?.let { msg ->
                    LaunchedEffect(msg) {
                        delay(3500)
                        viewModel.dismissToast()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ApexSurfaceCard)
                            .border(1.dp, ApexCyan, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = msg,
                            color = ApexEmerald,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
