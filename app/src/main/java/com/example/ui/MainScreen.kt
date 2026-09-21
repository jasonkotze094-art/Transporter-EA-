package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.ui.components.AddKeyDialog
import com.example.ui.components.CreateRobotTaskDialog
import com.example.ui.components.QuotesDialog
import com.example.ui.components.RemoveRobotDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveTradesScreen
import com.example.ui.screens.MetaTraderScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: TradingViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeAccount by viewModel.activeAccount.collectAsStateWithLifecycle()
    val allRobots by viewModel.allRobots.collectAsStateWithLifecycle()
    val allPositions by viewModel.allPositions.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()

    val showQuotesModal by viewModel.showQuotesModal.collectAsStateWithLifecycle()
    val showAddKeyModal by viewModel.showAddKeyModal.collectAsStateWithLifecycle()
    val showRemoveModal by viewModel.showRemoveModal.collectAsStateWithLifecycle()
    val showCreateTaskModal by viewModel.showCreateTaskModal.collectAsStateWithLifecycle()
    val notification by viewModel.notification.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentRobot = allRobots.firstOrNull()

    LaunchedEffect(notification) {
        notification?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearNotification()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CyberBlack,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = CyberCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(20.dp)
                ) {
                    // Drawer Header (matches video 00:00)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "EA Converter Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, NeonGreen, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "EA Converter",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Trading Robot Manager",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(color = CyberCardBorder, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Drawer Items
                    DrawerNavRow(
                        title = "HOME",
                        icon = Icons.Default.Home,
                        isSelected = currentTab == ScreenTab.HOME,
                        onClick = {
                            viewModel.selectTab(ScreenTab.HOME)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerNavRow(
                        title = "METATRADER",
                        icon = Icons.Default.Computer,
                        isSelected = currentTab == ScreenTab.METATRADER,
                        onClick = {
                            viewModel.selectTab(ScreenTab.METATRADER)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerNavRow(
                        title = "LIVE TRADES",
                        icon = Icons.Default.CandlestickChart,
                        isSelected = currentTab == ScreenTab.LIVE_TRADES,
                        onClick = {
                            viewModel.selectTab(ScreenTab.LIVE_TRADES)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerNavRow(
                        title = "SETTINGS",
                        icon = Icons.Default.Settings,
                        isSelected = currentTab == ScreenTab.SETTINGS,
                        onClick = {
                            viewModel.selectTab(ScreenTab.SETTINGS)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Footer inside drawer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberCard, RoundedCornerShape(12.dp))
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (activeAccount?.isConnected == true) NeonGreen else DangerRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (activeAccount?.isConnected == true) "MT5 Connected" else "MT5 Offline",
                                    color = TextWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Server: ${activeAccount?.server ?: "Accumarkets-Live"}",
                                color = TextDim,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = CyberBlack,
            topBar = {
                // Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { coroutineScope.launch { drawerState.open() } },
                        modifier = Modifier.testTag("menu_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer Menu",
                            tint = TextWhite
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (currentTab) {
                                ScreenTab.HOME -> "EA CONVERTER"
                                ScreenTab.METATRADER -> "METATRADER 5"
                                ScreenTab.LIVE_TRADES -> "LIVE POSITIONS"
                                ScreenTab.SETTINGS -> "SETTINGS"
                            },
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Status Indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (currentRobot?.isRunning == true) NeonGreen.copy(alpha = 0.15f) else CyberCard)
                            .border(
                                1.dp,
                                if (currentRobot?.isRunning == true) NeonGreen else CyberCardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentRobot?.isRunning == true) "AUTO ON" else "STOPPED",
                            color = if (currentRobot?.isRunning == true) NeonGreen else TextDim,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            bottomBar = {
                // Material 3 Bottom Navigation
                NavigationBar(
                    containerColor = CyberCard,
                    modifier = Modifier
                        .border(width = 0.5.dp, color = CyberCardBorder)
                        .navigationBarsPadding(),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.HOME,
                        onClick = { viewModel.selectTab(ScreenTab.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonRed,
                            selectedTextColor = NeonRed,
                            indicatorColor = CyberBlack,
                            unselectedIconColor = TextDim,
                            unselectedTextColor = TextDim
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.METATRADER,
                        onClick = { viewModel.selectTab(ScreenTab.METATRADER) },
                        icon = { Icon(Icons.Default.Computer, contentDescription = "MetaTrader") },
                        label = { Text("MT5", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonGreen,
                            selectedTextColor = NeonGreen,
                            indicatorColor = CyberBlack,
                            unselectedIconColor = TextDim,
                            unselectedTextColor = TextDim
                        ),
                        modifier = Modifier.testTag("nav_metatrader")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.LIVE_TRADES,
                        onClick = { viewModel.selectTab(ScreenTab.LIVE_TRADES) },
                        icon = { Icon(Icons.Default.CandlestickChart, contentDescription = "Trades") },
                        label = { Text("Trade", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonGreen,
                            selectedTextColor = NeonGreen,
                            indicatorColor = CyberBlack,
                            unselectedIconColor = TextDim,
                            unselectedTextColor = TextDim
                        ),
                        modifier = Modifier.testTag("nav_trades")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.SETTINGS,
                        onClick = { viewModel.selectTab(ScreenTab.SETTINGS) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonRed,
                            selectedTextColor = NeonRed,
                            indicatorColor = CyberBlack,
                            unselectedIconColor = TextDim,
                            unselectedTextColor = TextDim
                        ),
                        modifier = Modifier.testTag("nav_settings")
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = CyberCard,
                        contentColor = TextWhite,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .border(1.dp, NeonRed, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(data.visuals.message, fontWeight = FontWeight.Medium)
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    ScreenTab.HOME -> HomeScreen(
                        robot = currentRobot,
                        viewModel = viewModel
                    )
                    ScreenTab.METATRADER -> MetaTraderScreen(
                        account = activeAccount,
                        viewModel = viewModel
                    )
                    ScreenTab.LIVE_TRADES -> LiveTradesScreen(
                        account = activeAccount,
                        positions = allPositions,
                        quotes = quotes,
                        viewModel = viewModel
                    )
                    ScreenTab.SETTINGS -> SettingsScreen()
                }
            }
        }
    }

    // Modals
    if (showQuotesModal) {
        QuotesDialog(
            quotes = quotes,
            onDismiss = { viewModel.setShowQuotesModal(false) }
        )
    }

    if (showAddKeyModal) {
        AddKeyDialog(
            currentKey = currentRobot?.licenseKey ?: "",
            onConfirm = { key ->
                viewModel.addLicenseKey(currentRobot?.id ?: "skullxpert_ai", key)
            },
            onDismiss = { viewModel.setShowAddKeyModal(false) }
        )
    }

    if (showRemoveModal) {
        RemoveRobotDialog(
            robotName = currentRobot?.name ?: "SKULLXPERT AI",
            onConfirmRemove = {
                viewModel.removeCurrentRobot(currentRobot?.id ?: "skullxpert_ai")
            },
            onResetDefault = {
                viewModel.restoreDefaultRobot()
                viewModel.setShowRemoveModal(false)
            },
            onDismiss = { viewModel.setShowRemoveModal(false) }
        )
    }

    if (showCreateTaskModal) {
        CreateRobotTaskDialog(
            robotName = currentRobot?.name ?: "SKULLXPERT AI",
            onConfirm = { title, desc, symbol, actionType, lotSize, tp, sl ->
                viewModel.createRobotTask(
                    title = title,
                    description = desc,
                    symbol = symbol,
                    actionType = actionType,
                    lotSize = lotSize,
                    targetPips = tp,
                    stopLossPips = sl
                )
            },
            onDismiss = { viewModel.setShowCreateTaskModal(false) }
        )
    }
}

@Composable
fun DrawerNavRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CyberCard else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) NeonRed.copy(alpha = 0.5f) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) NeonRed else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            color = if (isSelected) TextWhite else TextMuted,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
    }
}
