package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.MarketAsset
import com.example.ui.components.ApexTopBar
import com.example.ui.components.SparklineChart
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
fun TradeScreen(
    state: TradingUiState,
    onSelectAsset: (MarketAsset) -> Unit,
    onExecuteOrder: (asset: MarketAsset, isBuy: Boolean, orderType: String, shares: Double, price: Double, leverage: Int) -> Unit,
    onSquawkClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBalanceClick: () -> Unit
) {
    val currentAsset = state.selectedAssetForTrade ?: state.assets.first()
    var isBuy by remember { mutableStateOf(true) }
    var orderType by remember { mutableStateOf("MARKET") } // MARKET, LIMIT, STOP
    var units by remember { mutableDoubleStateOf(5.0) }
    var leverage by remember { mutableIntStateOf(1) }
    var selectedTimeframe by remember { mutableStateOf("1D") }
    var isAssetDropdownOpen by remember { mutableStateOf(false) }

    val requiredMargin = (units * currentAsset.price) / leverage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApexCanvas)
    ) {
        ApexTopBar(
            virtualEquity = state.virtualEquity,
            subtitleSection = "Execution Desk",
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
            // Asset Selector Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Asset Picker Trigger
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ApexSurfaceContainer)
                                    .clickable { isAssetDropdownOpen = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = currentAsset.symbol,
                                    color = ApexTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF222B3D))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = currentAsset.tag,
                                        color = ApexCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select",
                                    tint = ApexTextSecondary
                                )
                            }

                            DropdownMenu(
                                expanded = isAssetDropdownOpen,
                                onDismissRequest = { isAssetDropdownOpen = false },
                                modifier = Modifier.background(ApexSurfaceCard)
                            ) {
                                state.assets.forEach { a ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = a.symbol, color = ApexTextPrimary, fontWeight = FontWeight.Bold)
                                                Text(text = a.formattedPrice(), color = ApexCyan)
                                            }
                                        },
                                        onClick = {
                                            onSelectAsset(a)
                                            isAssetDropdownOpen = false
                                        }
                                    )
                                }
                            }

                            // Price & Change
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = currentAsset.formattedPrice(),
                                    style = TickerNumericLg.copy(fontSize = 20.sp)
                                )
                                Text(
                                    text = currentAsset.formattedChange(),
                                    style = TickerNumericSm.copy(
                                        color = if (currentAsset.isUp) ApexEmerald else ApexCrimson,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats row (24h High, Low, Vol)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "24H HIGH", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$${currentAsset.high24h}", style = TickerNumericSm.copy(fontSize = 12.sp))
                            }
                            Column {
                                Text(text = "24H LOW", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$${currentAsset.low24h}", style = TickerNumericSm.copy(fontSize = 12.sp))
                            }
                            Column {
                                Text(text = "VOLUME", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = currentAsset.volume, style = TickerNumericSm.copy(fontSize = 12.sp, color = ApexCyan))
                            }
                        }
                    }
                }
            }

            // Interactive Chart Canvas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        // Timeframe chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("1M", "15M", "1H", "1D", "1W").forEach { tf ->
                                    val isSelected = selectedTimeframe == tf
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSelected) ApexCyan else ApexSurfaceContainer)
                                            .clickable { selectedTimeframe = tf }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = tf,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFF080B11) else ApexTextSecondary
                                        )
                                    }
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ApexEmerald)
                                )
                                Text(text = "Live Stream", color = ApexEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Sparkline / Line Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            SparklineChart(
                                data = currentAsset.sparkline,
                                isPositive = currentAsset.isUp,
                                strokeWidth = 4f,
                                showGradientFill = true
                            )
                        }
                    }
                }
            }

            // Level 2 Order Book Simulation
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "LEVEL 2 DEPTH LADDER",
                                color = ApexTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = "SPREAD 0.02 USD",
                                color = ApexCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Header labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "BID SIZE", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "PRICE (USD)", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "ASK SIZE", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Ladder sample rows
                        val base = currentAsset.price
                        listOf(
                            Triple(420, base + 0.15, 680),
                            Triple(640, base + 0.08, 920),
                            Triple(1250, base, 1100),
                            Triple(890, base - 0.07, 450),
                            Triple(1500, base - 0.14, 310)
                        ).forEach { (bid, px, ask) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$bid",
                                    style = TickerNumericSm.copy(color = ApexEmerald, fontSize = 11.sp)
                                )
                                Text(
                                    text = String.format(java.util.Locale.US, "%.2f", px),
                                    style = TickerNumericSm.copy(color = ApexTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "$ask",
                                    style = TickerNumericSm.copy(color = ApexCrimson, fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }

            // Order Execution Ticket Console
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        // Side toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ApexCanvas)
                                .padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isBuy) ApexEmerald else Color.Transparent)
                                    .clickable { isBuy = true }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "BUY / LONG",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isBuy) Color(0xFF051A12) else ApexTextSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (!isBuy) ApexCrimson else Color.Transparent)
                                    .clickable { isBuy = false }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SELL / SHORT",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (!isBuy) Color.White else ApexTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Order Type pills (Market, Limit, Stop)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("MARKET", "LIMIT", "STOP-LOSS").forEach { ot ->
                                val sel = orderType == ot
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (sel) ApexSurfaceContainer else ApexCanvas)
                                        .border(1.dp, if (sel) ApexCyan else ApexOutline, RoundedCornerShape(6.dp))
                                        .clickable { orderType = ot }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ot,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sel) ApexCyan else ApexTextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quantity Stepper
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ApexCanvas)
                                .border(1.dp, ApexOutline, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (units > 1.0) units -= 1.0 },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = ApexTextPrimary)
                            }

                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", units)} UNITS",
                                style = TickerNumericMd.copy(color = ApexTextPrimary, fontSize = 15.sp)
                            )

                            IconButton(
                                onClick = { units += 1.0 },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = ApexTextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Leverage Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "LEVERAGE", color = ApexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(1, 2, 5, 10).forEach { lev ->
                                    val sel = leverage == lev
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (sel) ApexCyan else ApexSurfaceContainer)
                                            .clickable { leverage = lev }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${lev}x",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (sel) Color(0xFF080B11) else ApexTextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Margin Required:", color = ApexTextSecondary, fontSize = 12.sp)
                            Text(
                                text = "$" + String.format(java.util.Locale.US, "%,.2f", requiredMargin),
                                style = TickerNumericSm.copy(color = ApexCyan, fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Available Cash:", color = ApexTextSecondary, fontSize = 12.sp)
                            Text(
                                text = "$" + String.format(java.util.Locale.US, "%,.2f", state.availableCash),
                                style = TickerNumericSm.copy(color = ApexTextPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Execution Trigger
                        Button(
                            onClick = {
                                onExecuteOrder(currentAsset, isBuy, orderType, units, currentAsset.price, leverage)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isBuy) ApexEmerald else ApexCrimson
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isBuy) "PLACE PAPER BUY ORDER (${currentAsset.symbol})" else "PLACE PAPER SELL ORDER (${currentAsset.symbol})",
                                color = if (isBuy) Color(0xFF051A12) else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
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
