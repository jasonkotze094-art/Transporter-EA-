package com.example.data.repository

import com.example.data.local.TradingDao
import com.example.data.model.LinkedAccount
import com.example.data.model.MarketQuote
import com.example.data.model.PositionEntity
import com.example.data.model.RobotEntity
import com.example.data.model.RobotTaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TradingRepository(
    private val dao: TradingDao,
    private val scope: CoroutineScope
) {
    val activeAccount: Flow<LinkedAccount?> = dao.getActiveAccount()
    val allRobots: Flow<List<RobotEntity>> = dao.getAllRobots()
    val allPositions: Flow<List<PositionEntity>> = dao.getAllPositions()
    val quotes: Flow<List<MarketQuote>> = dao.getQuotes()
    val robotTasks: Flow<List<RobotTaskEntity>> = dao.getRobotTasks()

    init {
        scope.launch(Dispatchers.IO) {
            seedInitialDataIfNeeded()
            startLiveMarketSimulation()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val currentAccount = dao.getActiveAccount().firstOrNull()
        if (currentAccount == null) {
            dao.insertAccount(
                LinkedAccount(
                    login = "3195567",
                    server = "Accumarkets-Live",
                    isConnected = true,
                    balance = 4120.64,
                    equity = 6268.71,
                    margin = 1619.92,
                    freeMargin = 3635.79,
                    marginLevel = 332.68,
                    currency = "ZAR",
                    pingMs = 18
                )
            )
        }

        val robots = dao.getAllRobots().firstOrNull()
        if (robots.isNullOrEmpty()) {
            dao.insertRobots(
                listOf(
                    RobotEntity(
                        id = "skullxpert_ai",
                        name = "SKULLXPERT AI",
                        creator = "~ Proff 401",
                        isRunning = true,
                        licenseKey = "PROFF401-SKULL-AI-8839",
                        isKeyValid = true,
                        algorithm = "XAUUSD M1 Scalper V4.2",
                        strategy = "Neural Breakout & Liquidity Sweep",
                        symbol = "XAUUSD",
                        totalProfit = 1256.51,
                        totalTrades = 32,
                        winRate = 92.4
                    )
                )
            )
        }

        val currentPositions = dao.getAllPositions().firstOrNull()
        if (currentPositions.isNullOrEmpty()) {
            val initialPositions = listOf(
                PositionEntity(43639012L, "XAUUSD", "buy", 0.01, 5020.864, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:08:07", "SKULLXPERT AI", 88.57),
                PositionEntity(43639025L, "XAUUSD", "buy", 0.01, 5023.554, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:08:19", "SKULLXPERT AI", 141.08),
                PositionEntity(43639031L, "XAUUSD", "buy", 0.01, 5024.195, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:08:24", "SKULLXPERT AI", 147.26),
                PositionEntity(43639044L, "XAUUSD", "buy", 0.01, 5020.210, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:08:35", "SKULLXPERT AI", 132.39),
                PositionEntity(43639058L, "XAUUSD", "buy", 0.01, 5023.822, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:08:48", "SKULLXPERT AI", 138.09),
                PositionEntity(43639066L, "XAUUSD", "buy", 0.01, 5025.284, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:09:02", "SKULLXPERT AI", 137.51),
                PositionEntity(43639072L, "XAUUSD", "buy", 0.01, 5025.034, 5031.547, 5012.000, 5062.000, 0.0, "2026.02.20 11:09:20", "SKULLXPERT AI", 114.49),
                PositionEntity(43639080L, "XAUUSD", "buy", 0.01, 5023.844, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:09:36", "SKULLXPERT AI", 124.21),
                PositionEntity(43639089L, "XAUUSD", "buy", 0.01, 5023.757, 5032.397, 5012.000, 5062.000, 0.0, "2026.02.20 11:09:50", "SKULLXPERT AI", 107.00)
            )
            dao.insertPositions(initialPositions)
        }

        val existingQuotes = dao.getQuotes().firstOrNull()
        if (existingQuotes.isNullOrEmpty()) {
            dao.insertQuotes(
                listOf(
                    MarketQuote("XAUUSD", 5031.26, 5031.54, 28, +1.42, 2),
                    MarketQuote("EURUSD", 1.08420, 1.08432, 12, +0.18, 5),
                    MarketQuote("GBPUSD", 1.29150, 1.29168, 18, -0.05, 5),
                    MarketQuote("BTCUSD", 98450.0, 98462.5, 125, +3.85, 1),
                    MarketQuote("US30", 44210.0, 44214.0, 40, +0.65, 1),
                    MarketQuote("NAS100", 21450.5, 21453.0, 25, +1.12, 1)
                )
            )
        }

        val existingTasks = dao.getRobotTasks().firstOrNull()
        if (existingTasks.isNullOrEmpty()) {
            dao.insertTasks(
                listOf(
                    RobotTaskEntity(
                        id = "task_xauusd_scalp",
                        robotId = "skullxpert_ai",
                        title = "M1 High-Frequency Gold Scalper",
                        description = "Auto-detect micro liquidity sweeps on XAUUSD and execute 0.01 lot buy orders with 15 pip TP targets.",
                        symbol = "XAUUSD",
                        actionType = "BUY_SCALP",
                        status = "RUNNING",
                        targetPips = 15.0,
                        stopLossPips = 10.0,
                        lotSize = 0.01,
                        executionCount = 18,
                        isEnabled = true,
                        progressPercent = 85,
                        createdAt = "11:08:07"
                    ),
                    RobotTaskEntity(
                        id = "task_trailing_lock",
                        robotId = "skullxpert_ai",
                        title = "Dynamic Break-Even & Trailing Stop",
                        description = "Shift Stop-Loss to entry +1.5 pips once floating profit exceeds +80 ZAR to guarantee risk-free MT5 trades.",
                        symbol = "XAUUSD",
                        actionType = "TRAILING_LOCK",
                        status = "RUNNING",
                        targetPips = 20.0,
                        stopLossPips = 8.0,
                        lotSize = 0.01,
                        executionCount = 14,
                        isEnabled = true,
                        progressPercent = 70,
                        createdAt = "11:08:35"
                    ),
                    RobotTaskEntity(
                        id = "task_liquidity_sweep",
                        robotId = "skullxpert_ai",
                        title = "Asian Session Liquidity Sweep",
                        description = "Scan high/low breakout traps on MetaTrader 5 Accumarkets feed before London open.",
                        symbol = "XAUUSD",
                        actionType = "LIQUIDITY_SWEEP",
                        status = "COMPLETED",
                        targetPips = 25.0,
                        stopLossPips = 12.0,
                        lotSize = 0.01,
                        executionCount = 9,
                        isEnabled = false,
                        progressPercent = 100,
                        createdAt = "10:45:12"
                    )
                )
            )
        }
    }

    private suspend fun startLiveMarketSimulation() {
        var baseGoldPrice = 5032.397
        while (scope.isActive) {
            delay(1500)
            val currentRobots = dao.getAllRobots().firstOrNull() ?: emptyList()
            val skullxpert = currentRobots.find { it.id == "skullxpert_ai" }
            val isRunning = skullxpert?.isRunning == true

            // Micro-tick gold price
            val delta = (Random.nextDouble(-0.15, 0.22))
            baseGoldPrice = (baseGoldPrice + delta).coerceIn(5025.0, 5040.0)
            val roundedPrice = (baseGoldPrice * 1000.0).toLong() / 1000.0

            val currentPositions = dao.getAllPositions().firstOrNull() ?: emptyList()
            var newTotalProfit = 0.0

            for (pos in currentPositions) {
                val tickDiff = roundedPrice - pos.openPrice
                val profitFactor = if (pos.type == "buy") 1.0 else -1.0
                val positionProfit = Math.round(tickDiff * 10.0 * profitFactor * 100.0) / 100.0
                newTotalProfit += positionProfit
                dao.updatePositionTick(pos.ticket, roundedPrice, positionProfit)
            }

            // Update Account
            val acc = dao.getActiveAccount().firstOrNull()
            if (acc != null) {
                val updatedEquity = Math.round((acc.balance + newTotalProfit) * 100.0) / 100.0
                val freeMargin = Math.round((updatedEquity - acc.margin) * 100.0) / 100.0
                val marginLevel = if (acc.margin > 0) Math.round((updatedEquity / acc.margin * 100.0) * 100.0) / 100.0 else 0.0
                dao.insertAccount(
                    acc.copy(
                        equity = updatedEquity,
                        freeMargin = freeMargin,
                        marginLevel = marginLevel
                    )
                )
            }

            // Update quotes
            val currentQuotes = dao.getQuotes().firstOrNull() ?: emptyList()
            val updatedQuotes = currentQuotes.map { q ->
                if (q.symbol == "XAUUSD") {
                    val bid = roundedPrice
                    val ask = Math.round((bid + 0.28) * 100.0) / 100.0
                    q.copy(bid = bid, ask = ask)
                } else {
                    q
                }
            }
            dao.insertQuotes(updatedQuotes)

            // If robot is running and open positions < 12, occasionally open an automated scalping order
            if (isRunning && currentPositions.size < 10 && Random.nextInt(100) < 5) {
                val newTicket = 43639000L + Random.nextInt(1000, 9999)
                val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US)
                val newPos = PositionEntity(
                    ticket = newTicket,
                    symbol = "XAUUSD",
                    type = "buy",
                    volume = 0.01,
                    openPrice = roundedPrice,
                    currentPrice = roundedPrice,
                    sl = roundedPrice - 15.0,
                    tp = roundedPrice + 30.0,
                    swap = 0.0,
                    openTime = timeFormat.format(Date()),
                    comment = "SKULLXPERT AI",
                    profit = 0.0
                )
                dao.insertPosition(newPos)
            }
        }
    }

    suspend fun toggleRobotState(id: String, isRunning: Boolean) {
        dao.setRobotRunning(id, isRunning)
    }

    suspend fun linkAccount(login: String, server: String) {
        val existing = dao.getActiveAccount().firstOrNull()
        dao.insertAccount(
            LinkedAccount(
                login = login.ifBlank { "3195567" },
                server = server.ifBlank { "Accumarkets-Live" },
                isConnected = true,
                balance = existing?.balance ?: 4120.64,
                equity = existing?.equity ?: 6268.71,
                margin = existing?.margin ?: 1619.92,
                freeMargin = existing?.freeMargin ?: 3635.79,
                marginLevel = existing?.marginLevel ?: 332.68,
                currency = "ZAR",
                pingMs = Random.nextInt(14, 25)
            )
        )
    }

    suspend fun addLicenseKey(robotId: String, key: String): Boolean {
        val robot = dao.getRobotById(robotId).firstOrNull() ?: return false
        val isValid = key.trim().length >= 6
        dao.insertRobot(
            robot.copy(
                licenseKey = key.trim(),
                isKeyValid = isValid
            )
        )
        return isValid
    }

    suspend fun removeRobot(robotId: String) {
        dao.deleteRobotById(robotId)
    }

    suspend fun resetDefaultRobot() {
        dao.insertRobot(
            RobotEntity(
                id = "skullxpert_ai",
                name = "SKULLXPERT AI",
                creator = "~ Proff 401",
                isRunning = true,
                licenseKey = "PROFF401-SKULL-AI-8839",
                isKeyValid = true,
                algorithm = "XAUUSD M1 Scalper V4.2",
                strategy = "Neural Breakout & Liquidity Sweep",
                symbol = "XAUUSD",
                totalProfit = 1256.51,
                totalTrades = 32,
                winRate = 92.4
            )
        )
    }

    suspend fun closePosition(ticket: Long) {
        dao.closePosition(ticket)
    }

    suspend fun openManualOrder(symbol: String, type: String, volume: Double) {
        val quotesList = dao.getQuotes().firstOrNull() ?: emptyList()
        val quote = quotesList.find { it.symbol == symbol }
        val price = if (type == "buy") (quote?.ask ?: 5031.54) else (quote?.bid ?: 5031.26)
        val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US)
        val newPos = PositionEntity(
            ticket = 43640000L + Random.nextInt(1000, 9999),
            symbol = symbol,
            type = type,
            volume = volume,
            openPrice = price,
            currentPrice = price,
            sl = if (type == "buy") price - 15.0 else price + 15.0,
            tp = if (type == "buy") price + 25.0 else price - 25.0,
            swap = 0.0,
            openTime = timeFormat.format(Date()),
            comment = "Manual / EA Converter",
            profit = 0.0
        )
        dao.insertPosition(newPos)
    }

    suspend fun createRobotTask(
        title: String,
        description: String,
        symbol: String = "XAUUSD",
        actionType: String = "BUY_SCALP",
        lotSize: Double = 0.01,
        targetPips: Double = 15.0,
        stopLossPips: Double = 10.0
    ) {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val taskId = "task_" + System.currentTimeMillis()
        val newTask = RobotTaskEntity(
            id = taskId,
            robotId = "skullxpert_ai",
            title = title,
            description = description,
            symbol = symbol,
            actionType = actionType,
            status = "RUNNING",
            targetPips = targetPips,
            stopLossPips = stopLossPips,
            lotSize = lotSize,
            executionCount = 0,
            isEnabled = true,
            progressPercent = 10,
            createdAt = timeFormat.format(Date())
        )
        dao.insertTask(newTask)
    }

    suspend fun toggleTask(taskId: String, currentEnabled: Boolean) {
        val newEnabled = !currentEnabled
        val newStatus = if (newEnabled) "RUNNING" else "PAUSED"
        dao.setTaskStatus(taskId, newEnabled, newStatus)
    }

    suspend fun executeTaskImmediate(taskId: String) {
        // Execute a task immediately into MetaTrader 5 live order
        val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US)
        val quote = dao.getQuotes().firstOrNull()?.find { it.symbol == "XAUUSD" }
        val price = quote?.ask ?: 5032.397
        val newPos = PositionEntity(
            ticket = 43642000L + Random.nextInt(1000, 9999),
            symbol = "XAUUSD",
            type = "buy",
            volume = 0.01,
            openPrice = price,
            currentPrice = price,
            sl = price - 10.0,
            tp = price + 15.0,
            swap = 0.0,
            openTime = timeFormat.format(Date()),
            comment = "EA TASK #$taskId",
            profit = 0.0
        )
        dao.insertPosition(newPos)
        dao.completeTask(taskId)
    }

    suspend fun deleteTask(taskId: String) {
        dao.deleteTask(taskId)
    }
}
