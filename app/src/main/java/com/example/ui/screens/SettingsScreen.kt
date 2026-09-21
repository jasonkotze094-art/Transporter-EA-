package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var autoTrading by remember { mutableStateOf(true) }
    var trailingStop by remember { mutableStateOf(true) }
    var soundAlerts by remember { mutableStateOf(true) }
    var lotSize by remember { mutableFloatStateOf(0.01f) }
    var maxPositions by remember { mutableFloatStateOf(10f) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SETTINGS & RISK",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Configure EA Converter automation parameters",
                color = TextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // General Automation Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
                    .testTag("automation_settings_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "EXECUTION RULES",
                        color = NeonRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Auto-Trading Execution",
                        subtitle = "Enable automated trade placement via MetaTrader 5 API",
                        checked = autoTrading,
                        onCheckedChange = { autoTrading = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Dynamic Trailing Stop",
                        subtitle = "Lock in profit as Gold / Forex moves in favorable direction",
                        checked = trailingStop,
                        onCheckedChange = { trailingStop = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Trade Audio & Haptics",
                        subtitle = "Sound chime when positions are filled or closed",
                        checked = soundAlerts,
                        onCheckedChange = { soundAlerts = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Lot Size & Risk Slider Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "LOT SIZE & CAPACITY",
                        color = NeonRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Default Lot Volume", color = TextWhite, fontSize = 14.sp)
                        Text(
                            text = String.format("%.2f", lotSize),
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Slider(
                        value = lotSize,
                        onValueChange = { lotSize = it },
                        valueRange = 0.01f..0.10f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonRed,
                            activeTrackColor = NeonRed,
                            inactiveTrackColor = CyberCardBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Max Open Concurrent Positions", color = TextWhite, fontSize = 14.sp)
                        Text(
                            text = "${maxPositions.toInt()}",
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Slider(
                        value = maxPositions,
                        onValueChange = { maxPositions = it },
                        valueRange = 1f..25f,
                        steps = 23,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonRed,
                            activeTrackColor = NeonRed,
                            inactiveTrackColor = CyberCardBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // About Proff 401 & EA Converter Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ABOUT SYSTEM",
                        color = TextDim,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "EA Converter • Trading Robot Manager",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Developed by @proff_401\nVersion 4.2.1 • Built for MetaTrader 5 High-Frequency Scalping",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = TextDim, fontSize = 11.sp, lineHeight = 14.sp)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = NeonGreen,
                uncheckedThumbColor = TextDim,
                uncheckedTrackColor = CyberCardBorder
            )
        )
    }
}
