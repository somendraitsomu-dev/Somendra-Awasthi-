package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.model.MarketAsset
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
fun QuickTradeDialog(
    asset: MarketAsset,
    availableCash: Double,
    onDismiss: () -> Unit,
    onExecuteOrder: (asset: MarketAsset, isBuy: Boolean, orderType: String, shares: Double, price: Double, leverage: Int) -> Unit,
    onOpenFullTradeDesk: (asset: MarketAsset) -> Unit
) {
    var isBuy by remember { mutableStateOf(true) }
    var orderType by remember { mutableStateOf("MARKET") } // MARKET or LIMIT
    var shares by remember { mutableDoubleStateOf(5.0) }
    var leverage by remember { mutableIntStateOf(1) }

    val totalEstimate = (shares * asset.price) / leverage

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ApexSurfaceCard)
                .border(1.dp, ApexOutline, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header with Symbol & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = asset.symbol,
                                color = ApexTextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E2838))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = asset.tag,
                                    color = ApexCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = asset.subtitle,
                            color = ApexTextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Row {
                        IconButton(onClick = { onOpenFullTradeDesk(asset) }) {
                            Icon(
                                imageVector = Icons.Default.OpenInFull,
                                contentDescription = "Open Full Desk",
                                tint = ApexCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = ApexTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price display & change
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = asset.formattedPrice(),
                        style = TickerNumericLg.copy(color = ApexTextPrimary)
                    )
                    Text(
                        text = asset.formattedChange(),
                        style = TickerNumericMd.copy(
                            color = if (asset.isUp) ApexEmerald else ApexCrimson
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buy / Sell Selector Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ApexCanvas)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isBuy) ApexEmerald else Color.Transparent)
                            .clickable { isBuy = true }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BUY / LONG",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isBuy) Color(0xFF051A12) else ApexTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isBuy) ApexCrimson else Color.Transparent)
                            .clickable { isBuy = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SELL / SHORT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (!isBuy) Color.White else ApexTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Shares Stepper Control
                Text(
                    text = "QUANTITY / UNITS",
                    color = ApexTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ApexCanvas)
                        .border(1.dp, ApexOutline, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (shares > 1.0) shares -= 1.0 },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = ApexTextPrimary
                        )
                    }

                    Text(
                        text = String.format(java.util.Locale.US, "%.1f", shares),
                        style = TickerNumericLg.copy(fontSize = 20.sp),
                        color = ApexTextPrimary
                    )

                    IconButton(
                        onClick = { shares += 1.0 },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = ApexTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Shares Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1.0, 5.0, 10.0, 25.0, 50.0).forEach { qty ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (shares == qty) ApexCyan.copy(alpha = 0.2f) else ApexSurfaceContainer)
                                .border(
                                    1.dp,
                                    if (shares == qty) ApexCyan else ApexOutline,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { shares = qty }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${qty.toInt()}",
                                style = TickerNumericSm.copy(fontSize = 11.sp),
                                color = if (shares == qty) ApexCyan else ApexTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Execution Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Virtual Value:",
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "$" + String.format(java.util.Locale.US, "%,.2f", totalEstimate),
                        style = TickerNumericSm.copy(fontWeight = FontWeight.Bold, color = ApexTextPrimary)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SIM Buying Power:",
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "$" + String.format(java.util.Locale.US, "%,.2f", availableCash),
                        style = TickerNumericSm.copy(color = ApexCyan)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Primary Execution Button
                Button(
                    onClick = {
                        onExecuteOrder(asset, isBuy, orderType, shares, asset.price, leverage)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBuy) ApexEmerald else ApexCrimson
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isBuy) "EXECUTE SIM BUY (${asset.symbol})" else "EXECUTE SIM SELL (${asset.symbol})",
                        color = if (isBuy) Color(0xFF051A12) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
