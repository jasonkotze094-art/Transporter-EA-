package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.RobotEntity
import com.example.ui.TradingViewModel
import com.example.ui.components.RobotTasksSection
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedDim
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun HomeScreen(
    robot: RobotEntity?,
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    val isRunning = robot?.isRunning == true
    val robotTasks by viewModel.robotTasks.collectAsState()

    // Infinite breathing glow animation for active robot
    val infiniteTransition = rememberInfiniteTransition(label = "glow_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isRunning) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CyberBlack, Color(0xFF0D1219), CyberBlack)
                )
            )
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Robot Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .border(1.5.dp, NeonRed, RoundedCornerShape(50.dp))
                    .background(NeonRedDim)
                    .padding(horizontal = 24.dp, vertical = 7.dp)
                    .testTag("top_robot_badge"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = robot?.name ?: "SKULLXPERT AI",
                    color = TextWhite,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Central Glowing Circular Robot Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(240.dp)
                    .scale(if (isRunning) pulseScale else 1f)
                    .testTag("robot_avatar_container")
            ) {
                // Outer Glow Rings
                Box(
                    modifier = Modifier
                        .size(236.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isRunning) 3.dp else 1.5.dp,
                            color = if (isRunning) NeonRed else NeonRed.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    NeonRedGlow.copy(alpha = if (isRunning) 0.35f else 0.1f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Avatar Image
                Image(
                    painter = painterResource(id = R.drawable.img_skullxpert_avatar),
                    contentDescription = "SKULLXPERT AI Cyber Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(210.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, NeonRed, CircleShape)
                )

                // Status Indicator Orb at Top of Avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 2.dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(if (isRunning) NeonGreen else NeonRed)
                        .border(
                            width = 2.dp,
                            color = if (isRunning) NeonGreenGlow else NeonRedGlow,
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle Tag
            Text(
                text = robot?.creator ?: "~ Proff 401",
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Action Buttons Row: Quotes, Tasks, Stop/Start, Remove
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quotes Button
                RobotActionButton(
                    title = "Quotes",
                    icon = Icons.Default.ShowChart,
                    onClick = { viewModel.setShowQuotesModal(true) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quotes_btn")
                )

                // Tasks Button
                RobotActionButton(
                    title = "Tasks",
                    icon = Icons.Default.ElectricBolt,
                    onClick = { viewModel.setShowCreateTaskModal(true) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("new_task_btn")
                )

                // Stop / Start Toggle Button
                RobotActionButton(
                    title = if (isRunning) "Stop" else "Start",
                    icon = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    onClick = {
                        if (robot != null) {
                            viewModel.toggleRobot(robot)
                        } else {
                            viewModel.restoreDefaultRobot()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("toggle_run_btn")
                )

                // Remove Button
                RobotActionButton(
                    title = "Remove",
                    icon = Icons.Default.Delete,
                    onClick = { viewModel.setShowRemoveModal(true) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("remove_btn")
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Section: CONNECTED ROBOTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NeonRed)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CONNECTED ROBOTS",
                    color = TextDim,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Connected Robot Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonRed.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .testTag("connected_robot_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_skullxpert_avatar),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, NeonRed, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = robot?.name ?: "SKULLXPERT AI",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${robot?.symbol ?: "XAUUSD"} • ${robot?.algorithm ?: "M1 Scalper"}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isRunning) NeonGreen.copy(alpha = 0.15f) else NeonRedDim)
                            .border(
                                1.dp,
                                if (isRunning) NeonGreen else NeonRed,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isRunning) "ACTIVE" else "STANDBY",
                            color = if (isRunning) NeonGreen else NeonRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Embedded Robot Automation Tasks
            RobotTasksSection(
                tasks = robotTasks,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ADD KEYS Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonRed.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.setShowAddKeyModal(true) }
                    .testTag("add_keys_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NeonRedDim)
                            .border(1.dp, NeonRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Key",
                            tint = NeonRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "ADD KEYS",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Have a valid License Key",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun RobotActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, NeonRed, RoundedCornerShape(24.dp))
            .background(CyberCard)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeonRed,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
