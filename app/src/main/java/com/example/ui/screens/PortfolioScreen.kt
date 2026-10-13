package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingUiState
import com.example.model.TradePosition
import com.example.ui.components.ApexTopBar
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCrimson
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.TickerNumericLg
import com.example.ui.theme.TickerNumericMd
import com.example.ui.theme.TickerNumericSm

@Composable
fun PortfolioScreen(
    state: TradingUiState,
    onClosePosition: (TradePosition) -> Unit,
    onResetBalance: () -> Unit,
    onSquawkClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBalanceClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("POSITIONS") } // POSITIONS or HISTORY

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApexCanvas)
    ) {
        ApexTopBar(
            virtualEquity = state.virtualEquity,
            subtitleSection = "Portfolio & Ledger",
            onSquawkClick = onSquawkClick,
            onAvatarClick = onAvatarClick,
            onBalanceClick = onBalanceClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Portfolio Equity Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL SIMULATED CAPITAL",
                                color = ApexTextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ApexCyan.copy(alpha = 0.15f))
                                    .clickable { onResetBalance() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reset",
                                        tint = ApexCyan,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Reset $10K",
                                        color = ApexCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$" + String.format(java.util.Locale.US, "%,.2f", state.totalPortfolioValue),
                            style = TickerNumericLg.copy(fontSize = 28.sp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Grid stats: Unrealized P&L, Realized P&L, Available Cash
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "UNREALIZED P&L", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                val uPnl = state.totalUnrealizedPnl
                                Text(
                                    text = (if (uPnl >= 0) "+" else "") + "$" + String.format(java.util.Locale.US, "%.2f", uPnl),
                                    style = TickerNumericSm.copy(
                                        color = if (uPnl >= 0) ApexEmerald else ApexCrimson,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Column {
                                Text(text = "REALIZED GAINS", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                val rPnl = state.realizedPnl
                                Text(
                                    text = (if (rPnl >= 0) "+" else "") + "$" + String.format(java.util.Locale.US, "%.2f", rPnl),
                                    style = TickerNumericSm.copy(
                                        color = if (rPnl >= 0) ApexEmerald else ApexCrimson,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Column {
                                Text(text = "FREE CASH", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "$" + String.format(java.util.Locale.US, "%,.2f", state.availableCash),
                                    style = TickerNumericSm.copy(color = ApexCyan, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            // Allocation Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "PORTFOLIO ALLOCATION",
                            color = ApexTextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        ) {
                            Box(modifier = Modifier.weight(0.45f).height(6.dp).background(ApexCyan))
                            Box(modifier = Modifier.weight(0.20f).height(6.dp).background(ApexEmerald))
                            Box(modifier = Modifier.weight(0.15f).height(6.dp).background(Color(0xFFFFB300)))
                            Box(modifier = Modifier.weight(0.20f).height(6.dp).background(Color(0xFF384357)))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "● US Tech (45%)", color = ApexCyan, fontSize = 10.sp)
                            Text(text = "● Pre-IPO (20%)", color = ApexEmerald, fontSize = 10.sp)
                            Text(text = "● Nifty (15%)", color = Color(0xFFFFB300), fontSize = 10.sp)
                            Text(text = "● Cash (20%)", color = ApexTextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Segmented Tab Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ApexSurfaceCard)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == "POSITIONS") ApexCyan else Color.Transparent)
                            .clickable { selectedTab = "POSITIONS" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OPEN POSITIONS (${state.positions.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "POSITIONS") Color(0xFF080B11) else ApexTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == "HISTORY") ApexCyan else Color.Transparent)
                            .clickable { selectedTab = "HISTORY" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ORDER HISTORY (${state.orderHistory.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "HISTORY") Color(0xFF080B11) else ApexTextSecondary
                        )
                    }
                }
            }

            // Tab Content
            if (selectedTab == "POSITIONS") {
                if (state.positions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No open positions. Execute simulated trades from Markets or Trade tab.",
                                color = ApexTextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    items(state.positions) { pos ->
                        PositionCardItem(
                            position = pos,
                            onClose = { onClosePosition(pos) }
                        )
                    }
                }
            } else {
                items(state.orderHistory) { order ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ApexSurfaceCard)
                            .border(1.dp, ApexOutline, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = order.symbol,
                                        color = ApexTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (order.isBuy) ApexEmerald.copy(alpha = 0.2f) else ApexCrimson.copy(alpha = 0.2f))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (order.isBuy) "BUY" else "SELL",
                                            color = if (order.isBuy) ApexEmerald else ApexCrimson,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "${order.shares} units @ $${order.price} • ${order.timeAgo}",
                                    color = ApexTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Text(
                                text = "$" + String.format(java.util.Locale.US, "%,.2f", order.totalUsd),
                                style = TickerNumericSm.copy(color = ApexTextPrimary, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PositionCardItem(
    position: TradePosition,
    onClose: () -> Unit
) {
    val pnl = position.pnl
    val pnlPct = position.pnlPercent
    val isProfit = pnl >= 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexOutline, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = position.assetSymbol,
                        color = ApexTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (position.isLong) ApexEmerald.copy(alpha = 0.15f) else ApexCrimson.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (position.isLong) "LONG" else "SHORT",
                            color = if (position.isLong) ApexEmerald else ApexCrimson,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Close Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ApexSurfaceContainer)
                        .clickable { onClose() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "CLOSE",
                        color = ApexCrimson,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "ENTRY / CURRENT", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$${String.format(java.util.Locale.US, "%.2f", position.entryPrice)} → $${String.format(java.util.Locale.US, "%.2f", position.currentPrice)}",
                        style = TickerNumericSm.copy(fontSize = 12.sp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "UNREALIZED P&L", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${if (isProfit) "+" else ""}$${String.format(java.util.Locale.US, "%.2f", pnl)} (${if (isProfit) "+" else ""}${String.format(java.util.Locale.US, "%.2f", pnlPct)}%)",
                        style = TickerNumericSm.copy(
                            color = if (isProfit) ApexEmerald else ApexCrimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
