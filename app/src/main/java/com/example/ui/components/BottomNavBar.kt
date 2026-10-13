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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NavTab
import com.example.ui.theme.ApexCanvas
import com.example.ui.theme.ApexCyan
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ApexCanvas)
            .border(width = 1.dp, color = ApexOutline.copy(alpha = 0.6f))
            .navigationBarsPadding()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. PORTFOLIO
            NavTabItem(
                label = "PORTFOLIO",
                icon = Icons.Default.WorkOutline,
                isSelected = selectedTab == NavTab.PORTFOLIO,
                onClick = { onTabSelected(NavTab.PORTFOLIO) }
            )

            // 2. MARKETS
            NavTabItem(
                label = "MARKETS",
                icon = Icons.Default.Language,
                isSelected = selectedTab == NavTab.MARKETS,
                onClick = { onTabSelected(NavTab.MARKETS) }
            )

            // 3. TRADE (Highlighted center action)
            TradeCenterTabItem(
                isSelected = selectedTab == NavTab.TRADE,
                onClick = { onTabSelected(NavTab.TRADE) }
            )

            // 4. ANALYTICS
            NavTabItem(
                label = "ANALYTICS",
                icon = Icons.Default.AutoGraph,
                isSelected = selectedTab == NavTab.ANALYTICS,
                onClick = { onTabSelected(NavTab.ANALYTICS) }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) ApexCyan else ApexTextMuted,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = if (isSelected) ApexCyan else ApexTextMuted
        )
    }
}

@Composable
private fun TradeCenterTabItem(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) ApexCyan else ApexSurface)
                .border(
                    1.dp,
                    if (isSelected) ApexCyan else ApexOutline,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "TRADE",
                tint = if (isSelected) Color(0xFF080B11) else ApexCyan,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "TRADE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = if (isSelected) ApexCyan else ApexTextMuted
        )
    }
}
