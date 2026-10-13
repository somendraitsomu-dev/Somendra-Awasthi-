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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.TickerNumericSm

@Composable
fun ApexTopBar(
    virtualEquity: Double,
    subtitleSection: String = "Markets",
    onSquawkClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBalanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Logo and Brand Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Apex geometric logo mark
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0F2231), Color(0xFF0B1724))
                        )
                    )
                    .border(1.dp, ApexCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Futuristic apex triangle
                androidx.compose.foundation.Canvas(modifier = Modifier.size(18.dp)) {
                    val w = size.width
                    val h = size.height
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        lineTo(w * 0.95f, h * 0.9f)
                        lineTo(w * 0.05f, h * 0.9f)
                        close()
                    }
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            listOf(ApexCyan, ApexEmerald)
                        )
                    )
                }
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "ApexTrade",
                        color = ApexTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp
                    )

                    // LIVE Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(ApexEmerald.copy(alpha = 0.15f))
                            .border(1.dp, ApexEmerald.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
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
                                text = "LIVE",
                                color = ApexEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // SIM Balance + Subtitle
                Row(
                    modifier = Modifier.clickable { onBalanceClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SIM $" + String.format(java.util.Locale.US, "%,.0f", virtualEquity) + " USD",
                        style = TickerNumericSm.copy(
                            color = ApexCyan,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "  •  $subtitleSection",
                        color = ApexTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Right: Squawk audio radio and User avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Squawk broadcast button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceCard)
                    .border(1.dp, ApexOutline, RoundedCornerShape(12.dp))
                    .clickable { onSquawkClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Squawk Audio Feed",
                    tint = ApexCyan,
                    modifier = Modifier.size(20.dp)
                )
            }

            // User avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E2838))
                    .border(1.5.dp, ApexOutline, CircleShape)
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                // High-fidelity executive avatar icon or photo representation
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF3B4861), Color(0xFF1A2233))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile",
                        tint = ApexTextSecondary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Tiny active dot
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(ApexEmerald)
                        .border(1.5.dp, ApexCanvas, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}
