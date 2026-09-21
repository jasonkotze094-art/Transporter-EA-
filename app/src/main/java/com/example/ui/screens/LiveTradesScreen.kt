package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LinkedAccount
import com.example.data.model.MarketQuote
import com.example.data.model.PositionEntity
import com.example.ui.TradingViewModel
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun LiveTradesScreen(
    account: LinkedAccount?,
    positions: List<PositionEntity>,
    quotes: List<MarketQuote>,
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    val totalFloatingProfit = positions.sumOf { it.profit }
    val goldQuote = quotes.find { it.symbol == "XAUUSD" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(horizontal = 14.dp)
            .testTag("live_trades_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))

            // MT5 Trade Header (matching exact video layout)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
                    .testTag("mt5_header_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trade",
                            color = TextMuted,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Live Floating Profit
                        Text(
                            text = String.format("%,.2f %s", totalFloatingProfit, account?.currency ?: "ZAR"),
                            color = if (totalFloatingProfit >= 0) CyberBlue else DangerRed,
                            fontSize = 19.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Financial metrics breakdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Balance:", color = TextDim, fontSize = 12.sp)
                            Text(
                                text = String.format("%,.2f", account?.balance ?: 4120.64),
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Equity:", color = TextDim, fontSize = 12.sp)
                            Text(
                                text = String.format("%,.2f", account?.equity ?: 6268.71),
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Margin:", color = TextDim, fontSize = 12.sp)
                            Text(
                                text = String.format("%,.2f", account?.margin ?: 1619.92),
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Free margin:", color = TextDim, fontSize = 12.sp)
                            Text(
                                text = String.format("%,.2f", account?.freeMargin ?: 3635.79),
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Margin Level (%):", color = TextDim, fontSize = 12.sp)
                            Text(
                                text = String.format("%.2f", account?.marginLevel ?: 332.68),
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fast Execution Market Bar (as seen in video 00:18)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SELL BY MARKET
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DangerRed.copy(alpha = 0.85f))
                            .clickable {
                                viewModel.openManualOrder("XAUUSD", "sell", 0.01)
                            }
                            .testTag("sell_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SELL", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format("%.2f", goldQuote?.bid ?: 5031.26),
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Lot indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        Text("XAUUSD", color = TextDim, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("0.01", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // BUY BY MARKET
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberBlue.copy(alpha = 0.85f))
                            .clickable {
                                viewModel.openManualOrder("XAUUSD", "buy", 0.01)
                            }
                            .testTag("buy_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("BUY", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format("%.2f", goldQuote?.ask ?: 5031.54),
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Positions Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Positions (${positions.size})",
                    color = TextDim,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Automated by SKULLXPERT AI",
                    color = NeonRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Positions List
        items(positions, key = { it.ticket }) { pos ->
            PositionRowItem(
                position = pos,
                onClose = { viewModel.closePosition(pos.ticket) }
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PositionRowItem(
    position: PositionEntity,
    onClose: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.8.dp, CyberCardBorder, RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded }
            .testTag("position_row_${position.ticket}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${position.symbol}, ${position.type} ${position.volume}",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${String.format("%.3f", position.openPrice)}  →  ${String.format("%.3f", position.currentPrice)}",
                        color = TextMuted,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format("%.2f", position.profit),
                        color = if (position.profit >= 0) CyberBlue else DangerRed,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand details",
                        tint = TextDim,
                        modifier = Modifier.size(20.dp).padding(start = 4.dp)
                    )
                }
            }

            // Expandable details (SL, TP, Swap, Open time, Robot tag, Close button)
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(CyberBlack, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ticket: #${position.ticket}", color = TextDim, fontSize = 11.sp)
                        Text(position.openTime, color = TextDim, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("S/L: ${if (position.sl > 0) String.format("%.3f", position.sl) else "-"}", color = TextMuted, fontSize = 11.sp)
                        Text("T/P: ${if (position.tp > 0) String.format("%.3f", position.tp) else "-"}", color = TextMuted, fontSize = 11.sp)
                        Text("Swap: ${String.format("%.2f", position.swap)}", color = TextMuted, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Comment: ", color = TextDim, fontSize = 11.sp)
                            Text(position.comment, color = NeonRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DangerRed.copy(alpha = 0.2f))
                                .border(1.dp, DangerRed, RoundedCornerShape(6.dp))
                                .clickable(onClick = onClose)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("close_pos_btn_${position.ticket}")
                        ) {
                            Text("Close Order", color = DangerRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
