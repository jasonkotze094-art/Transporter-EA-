package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linked_accounts")
data class LinkedAccount(
    @PrimaryKey val login: String,
    val server: String,
    val isConnected: Boolean = true,
    val balance: Double = 4120.64,
    val equity: Double = 6268.71,
    val margin: Double = 1619.92,
    val freeMargin: Double = 3635.79,
    val marginLevel: Double = 332.68,
    val currency: String = "ZAR",
    val pingMs: Int = 18,
    val lastConnected: Long = System.currentTimeMillis()
)

@Entity(tableName = "robots")
data class RobotEntity(
    @PrimaryKey val id: String,
    val name: String,
    val creator: String,
    val isRunning: Boolean,
    val licenseKey: String,
    val isKeyValid: Boolean,
    val algorithm: String,
    val strategy: String,
    val symbol: String = "XAUUSD",
    val totalProfit: Double = 1256.51,
    val totalTrades: Int = 28,
    val winRate: Double = 89.2
)

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey val ticket: Long,
    val symbol: String = "XAUUSD",
    val type: String = "buy",
    val volume: Double = 0.01,
    val openPrice: Double,
    val currentPrice: Double,
    val sl: Double = 0.0,
    val tp: Double = 0.0,
    val swap: Double = 0.0,
    val openTime: String,
    val comment: String = "SKULLXPERT AI",
    val profit: Double
)

@Entity(tableName = "market_quotes")
data class MarketQuote(
    @PrimaryKey val symbol: String,
    val bid: Double,
    val ask: Double,
    val spread: Int,
    val changePercent: Double,
    val digits: Int = 2
)
