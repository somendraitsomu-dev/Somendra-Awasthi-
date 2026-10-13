package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AudioSquawk
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexEmerald
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import com.example.ui.theme.TickerNumericSm

@Composable
fun LiveSquawkDialog(
    alerts: List<AudioSquawk>,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w3"
    )

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
                // Header with squawk waves
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
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ApexCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Audio Feed",
                                tint = ApexCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Institutional Squawk Box",
                                color = ApexTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Live synthetic audio broadcast • Active",
                                color = ApexEmerald,
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

                // Waveform visualizer strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ApexCanvas)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(waveHeight1, waveHeight3, waveHeight2, waveHeight1, waveHeight2, waveHeight3, waveHeight1).forEach { h ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .width(4.dp)
                                .height(h.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(ApexCyan)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "TRANSMITTING HIGH-ALPHA FLOWS",
                        color = ApexCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Feed List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(alerts) { alert ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ApexSurfaceContainer)
                                .border(1.dp, ApexOutline, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF26334D))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = alert.category,
                                        color = ApexCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = alert.timestamp,
                                    style = TickerNumericSm.copy(color = ApexTextMuted, fontSize = 11.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = alert.headline,
                                color = ApexTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
