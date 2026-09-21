package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RobotTaskEntity
import com.example.ui.TradingViewModel
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun RobotTasksSection(
    tasks: List<RobotTaskEntity>,
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header with "NEW TASK" action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ROBOT AUTOMATION TASKS",
                    color = TextDim,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Button to open "New Task" dialog
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NeonRed, RoundedCornerShape(20.dp))
                    .background(NeonRed.copy(alpha = 0.12f))
                    .clickable { viewModel.setShowCreateTaskModal(true) }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("add_robot_task_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = NeonRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NEW TASK",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (tasks.isEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TextDim,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No tasks currently running in robot",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.setShowCreateTaskModal(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Add First Task", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                tasks.forEach { task ->
                    RobotTaskCard(task = task, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RobotTaskCard(
    task: RobotTaskEntity,
    viewModel: TradingViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val isRunning = task.status == "RUNNING"
    val isCompleted = task.status == "COMPLETED"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.2.dp,
                when {
                    isCompleted -> NeonGreen.copy(alpha = 0.5f)
                    isRunning -> NeonRed.copy(alpha = 0.8f)
                    else -> CyberCardBorder
                },
                RoundedCornerShape(16.dp)
            )
            .clickable { expanded = !expanded }
            .testTag("robot_task_card_${task.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Task Top Row: Icon, Title, Status & Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> NeonGreen.copy(alpha = 0.15f)
                                isRunning -> NeonRed.copy(alpha = 0.15f)
                                else -> CyberBlack
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isCompleted -> NeonGreen
                                isRunning -> NeonRed
                                else -> CyberCardBorder
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            isCompleted -> Icons.Default.CheckCircle
                            isRunning -> Icons.Default.ElectricBolt
                            else -> Icons.Default.Pause
                        },
                        contentDescription = null,
                        tint = when {
                            isCompleted -> NeonGreen
                            isRunning -> NeonRed
                            else -> TextDim
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${task.symbol} • ${task.actionType.replace("_", " ")}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${task.createdAt}",
                            color = TextDim,
                            fontSize = 10.sp
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when {
                                isCompleted -> NeonGreen.copy(alpha = 0.15f)
                                isRunning -> NeonGreen.copy(alpha = 0.12f)
                                else -> CyberBlack
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isCompleted -> NeonGreen
                                isRunning -> NeonGreen
                                else -> TextDim
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = task.status,
                        color = if (isCompleted || isRunning) NeonGreen else TextDim,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Task Description preview
            Text(
                text = task.description,
                color = TextWhite.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar & Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MetaTrader 5 Progress",
                    color = TextDim,
                    fontSize = 11.sp
                )
                Text(
                    text = if (isCompleted) "100% (Finished)" else "${task.progressPercent}%",
                    color = if (isCompleted) NeonGreen else NeonRed,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { task.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isCompleted) NeonGreen else NeonRed,
                trackColor = CyberBlack,
            )

            // Expanded Controls: Finish/Execute now, Pause/Resume, Delete
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Parameters row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberBlack, RoundedCornerShape(8.dp))
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lot: ${task.lotSize}", color = TextMuted, fontSize = 11.sp)
                        Text("TP: +${task.targetPips.toInt()} pips", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("SL: -${task.stopLossPips.toInt()} pips", color = DangerRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Fills: ${task.executionCount}", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // "FINISH / EXECUTE ON MT5" button
                        Button(
                            onClick = {
                                viewModel.executeTaskImmediate(task.id, task.title)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonGreen.copy(alpha = 0.9f),
                                contentColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("finish_task_btn_${task.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = CyberBlack
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCompleted) "Re-Execute MT5" else "Finish Task on MT5",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Toggle pause/resume
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, NeonRed, RoundedCornerShape(10.dp))
                                .background(CyberCard)
                                .clickable {
                                    viewModel.toggleTask(task.id, task.isEnabled, task.title)
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (task.isEnabled) "Pause" else "Resume",
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Delete
                        IconButton(
                            onClick = { viewModel.deleteTask(task.id) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DangerRed.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Task",
                                tint = DangerRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
