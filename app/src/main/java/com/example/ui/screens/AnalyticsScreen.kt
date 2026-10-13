package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingUiState
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
fun AnalyticsScreen(
    state: TradingUiState,
    onSquawkClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBalanceClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApexCanvas)
    ) {
        ApexTopBar(
            virtualEquity = state.virtualEquity,
            subtitleSection = "Quantitative Analytics",
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
            // Simulated Flow Radar Deep Dive
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
                                text = "SIMULATED FLOW RADAR",
                                color = ApexTextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ApexEmerald.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "EXPANSION REGIME",
                                    color = ApexEmerald,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "${state.radar.score}",
                                style = TickerNumericLg.copy(fontSize = 36.sp, color = ApexCyan)
                            )
                            Text(
                                text = "/ 100 GREED & VIRTUAL ACCUMULATION",
                                color = ApexTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Sentiment distribution
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Box(modifier = Modifier.weight(0.64f).height(8.dp).background(ApexEmerald))
                            Box(modifier = Modifier.weight(0.36f).height(8.dp).background(Color(0xFFFF96A3)))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "● 64% Bullish Flow (${state.radar.bidsVolume})", color = ApexEmerald, fontSize = 11.sp)
                            Text(text = "● 36% Bearish (${state.radar.asksVolume})", color = Color(0xFFFF96A3), fontSize = 11.sp)
                        }
                    }
                }
            }

            // Sector Relative Heatmap
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
                        Text(
                            text = "SECTOR HEATMAP & RELATIVE STRENGTH",
                            color = ApexTextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        listOf(
                            Triple("US Mega Tech", "+4.25%", 0.85f),
                            Triple("Pre-IPO Syndicates", "+6.80%", 0.95f),
                            Triple("Sports & Global Indices", "+3.40%", 0.70f),
                            Triple("Indian ADR / Nifty 50", "+1.15%", 0.55f),
                            Triple("FX Currencies", "+0.32%", 0.35f),
                            Triple("NASDAQ EV / Mobility", "-1.52%", 0.20f)
                        ).forEach { (sector, returnVal, strength) ->
                            val isPos = !returnVal.startsWith("-")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = sector, color = ApexTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 60.dp, height = 4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(ApexSurfaceContainer)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(strength)
                                                .height(4.dp)
                                                .background(if (isPos) ApexEmerald else ApexCrimson)
                                        )
                                    }
                                    Text(
                                        text = returnVal,
                                        style = TickerNumericSm.copy(
                                            color = if (isPos) ApexEmerald else ApexCrimson,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Risk & Alpha Metrics
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
                        Text(
                            text = "DESK PERFORMANCE RATIOS",
                            color = ApexTextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "SIM WIN RATE", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "74.2%", style = TickerNumericMd.copy(color = ApexEmerald))
                            }
                            Column {
                                Text(text = "PROFIT FACTOR", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "2.38", style = TickerNumericMd.copy(color = ApexCyan))
                            }
                            Column {
                                Text(text = "MAX DRAWDOWN", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "-2.10%", style = TickerNumericMd.copy(color = ApexCrimson))
                            }
                            Column {
                                Text(text = "SHARPE RATIO", color = ApexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "1.89", style = TickerNumericMd.copy(color = ApexTextPrimary))
                            }
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
