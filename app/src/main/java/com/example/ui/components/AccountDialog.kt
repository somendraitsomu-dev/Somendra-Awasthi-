package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.TickerNumericLg
import com.example.ui.theme.TickerNumericSm

@Composable
fun AccountDialog(
    virtualEquity: Double,
    availableCash: Double,
    onResetBalance: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ApexSurfaceCard)
                .border(1.dp, ApexOutline, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ApexCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Security",
                                tint = ApexCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Apex Institutional Desk",
                                color = ApexTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Paper Simulation Environment",
                                color = ApexTextMuted,
                                fontSize = 11.sp
                            )
                        }
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

                Spacer(modifier = Modifier.height(16.dp))

                // Balance summary card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ApexCanvas)
                        .border(1.dp, ApexOutline, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "TOTAL VIRTUAL CAPITAL",
                        color = ApexTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$" + String.format(java.util.Locale.US, "%,.2f", virtualEquity),
                        style = TickerNumericLg.copy(color = ApexCyan)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Available Buying Power:", color = ApexTextSecondary, fontSize = 12.sp)
                        Text(
                            text = "$" + String.format(java.util.Locale.US, "%,.2f", availableCash),
                            style = TickerNumericSm.copy(color = ApexTextPrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats rows
                listOf(
                    "Execution Routing" to "Smart Direct Market Access (DMA)",
                    "Data Latency" to "< 1.4ms (Simulated WebSocket)",
                    "Margin Facility" to "Tier 1 Prime Brokerage (10x Max)"
                ).forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = label, color = ApexTextMuted, fontSize = 12.sp)
                        Text(text = value, color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Reset balance button
                Button(
                    onClick = {
                        onResetBalance()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexSurfaceContainer
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = ApexCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "RESET SIM TO $10,000 USD",
                            color = ApexCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
