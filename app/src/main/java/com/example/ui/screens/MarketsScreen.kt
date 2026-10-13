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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingUiState
import com.example.model.BenchmarkIndex
import com.example.model.MarketAsset
import com.example.model.MarketSession
import com.example.ui.components.ApexTopBar
import com.example.ui.components.SparklineChart
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCrimson
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.TickerNumericLg
import com.example.ui.theme.TickerNumericMd
import com.example.ui.theme.TickerNumericSm

@Composable
fun MarketsScreen(
    state: TradingUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onAssetClick: (MarketAsset) -> Unit,
    onSimClick: (MarketAsset) -> Unit,
    onSquawkClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBalanceClick: () -> Unit,
    onFilterTuneClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApexCanvas)
    ) {
        // 1. Apex Top Bar
        ApexTopBar(
            virtualEquity = state.virtualEquity,
            subtitleSection = "Markets",
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
            // 2. Search Box with Filter Tune Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexOutline, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ApexTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = state.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                text = "Search 5,000+ US, Nifty 50, Forex & Pre-IPO a...",
                                color = ApexTextMuted,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = ApexTextPrimary,
                            unfocusedTextColor = ApexTextPrimary
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2638))
                            .clickable { onFilterTuneClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = ApexTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // 3. Market Sessions Pill Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.sessions.forEach { session ->
                        MarketSessionChip(session)
                    }
                }
            }

            // 4. Global Core Benchmarks Header & Carousel
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Benchmarks",
                                tint = ApexCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "GLOBAL CORE BENCHMARKS",
                                color = ApexTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Text(
                            text = "REAL-TIME FEED",
                            color = ApexTextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Benchmark cards horizontal scroll
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        state.benchmarks.forEach { bm ->
                            BenchmarkCard(bm)
                        }
                    }
                }
            }

            // 5. Simulated Flow Radar / Order Book Depth
            item {
                OrderBookRadarCard(state)
            }

            // 6. Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.categories.forEach { cat ->
                        val isSelected = state.selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(if (isSelected) ApexCyan else ApexSurfaceCard)
                                .border(
                                    1.dp,
                                    if (isSelected) ApexCyan else ApexOutline,
                                    RoundedCornerShape(100.dp)
                                )
                                .clickable { onCategorySelect(cat) }
                                .padding(horizontal = 14.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF080B11) else ApexTextSecondary
                            )
                        }
                    }
                }
            }

            // 7. Watchlist Asset Items
            items(state.filteredAssets) { asset ->
                AssetRowItem(
                    asset = asset,
                    onRowClick = { onAssetClick(asset) },
                    onSimClick = { onSimClick(asset) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MarketSessionChip(session: MarketSession) {
    val dotColor = if (session.isOpen) ApexEmerald else ApexCrimson
    val statusColor = if (session.isOpen) ApexEmerald else Color(0xFFFF7E98)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexOutline, RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = "${session.label} ",
                color = ApexTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = session.status,
                color = statusColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun BenchmarkCard(bm: BenchmarkIndex) {
    Box(
        modifier = Modifier
            .width(185.dp)
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
                Text(
                    text = bm.name,
                    color = ApexTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ApexEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = bm.changePercent,
                        color = ApexEmerald,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = bm.price,
                    style = TickerNumericMd.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = bm.change,
                    style = TickerNumericSm.copy(color = ApexEmerald, fontSize = 12.sp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sparkline wave
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            ) {
                SparklineChart(
                    data = bm.sparkline,
                    isPositive = bm.isPositive,
                    strokeWidth = 3.5f,
                    showGradientFill = false
                )
            }
        }
    }
}

@Composable
private fun OrderBookRadarCard(state: TradingUiState) {
    val radar = state.radar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexOutline, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Flow Radar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E273A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Radar",
                            tint = ApexCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "SIMULATED FLOW RADAR",
                            color = ApexTextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${radar.score}",
                                style = TickerNumericMd.copy(color = ApexTextPrimary, fontSize = 14.sp)
                            )
                            Text(
                                text = radar.state,
                                color = ApexEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Right: Depth
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "ORDER BOOK DEPTH",
                        color = ApexTextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                    Text(
                        text = "${radar.buyRatio}% Buy · ${radar.sellRatio}% Sell",
                        style = TickerNumericSm.copy(color = ApexTextPrimary, fontSize = 12.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dual Progress Bar: Emerald (64%) and Coral (36%)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF262D3D))
            ) {
                Box(
                    modifier = Modifier
                        .weight(radar.buyRatio.toFloat())
                        .height(6.dp)
                        .background(ApexEmerald)
                )
                Box(
                    modifier = Modifier
                        .weight(radar.sellRatio.toFloat())
                        .height(6.dp)
                        .background(Color(0xFFFF96A3))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer notes with dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(ApexEmerald)
                    )
                    Text(
                        text = "Bids ${radar.bidsVolume}",
                        color = ApexTextMuted,
                        fontSize = 11.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = radar.asksVolume,
                        color = ApexTextMuted,
                        fontSize = 11.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF96A3))
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetRowItem(
    asset: MarketAsset,
    onRowClick: () -> Unit,
    onSimClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexOutline.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .clickable { onRowClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Icon + Ticker + Tag + Subtitle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Icon in dark square
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E2638)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getAssetIcon(asset.iconType),
                    contentDescription = asset.symbol,
                    tint = ApexCyan,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = asset.symbol,
                        color = ApexTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF222B3D))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = asset.tag,
                            color = ApexCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = asset.subtitle,
                    color = ApexTextMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right: Price + Change % + SIM Action Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = asset.formattedPrice(),
                    style = TickerNumericMd.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = asset.formattedChange(),
                    style = TickerNumericSm.copy(
                        color = if (asset.isUp) ApexEmerald else ApexCrimson,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // SIM button (Cyan SIM ↗ for gainers, Dark SIM ⇅ for dual action/TSLA/USDINR)
            if (asset.isDualAction) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E2638))
                        .border(1.dp, ApexOutline, RoundedCornerShape(8.dp))
                        .clickable { onSimClick() }
                        .padding(horizontal = 9.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "SIM",
                            color = ApexTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Trade",
                            tint = ApexTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ApexCyan)
                        .clickable { onSimClick() }
                        .padding(horizontal = 9.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "SIM",
                            color = Color(0xFF080B11),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Simulate",
                            tint = Color(0xFF080B11),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getAssetIcon(iconType: String): ImageVector {
    return when (iconType) {
        "CHIP" -> Icons.Default.Memory
        "BANK" -> Icons.Default.AccountBalance
        "ROCKET" -> Icons.Default.RocketLaunch
        "CAR" -> Icons.Default.DirectionsCar
        "CAR2" -> Icons.Default.DirectionsCar
        "EXCHANGE" -> Icons.Default.CurrencyExchange
        "SPEED" -> Icons.Default.Speed
        "RUPEE" -> Icons.Default.CurrencyRupee
        "TECH" -> Icons.Default.SmartDisplay
        else -> Icons.Default.BarChart
    }
}
