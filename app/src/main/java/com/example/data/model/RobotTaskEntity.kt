package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "robot_tasks")
data class RobotTaskEntity(
    @PrimaryKey val id: String,
    val robotId: String = "skullxpert_ai",
    val title: String,
    val description: String,
    val symbol: String = "XAUUSD",
    val actionType: String = "BUY_SCALP", // BUY_SCALP, SELL_PULLBACK, BREAKOUT, TRAILING_LOCK, LIQUIDITY_SWEEP
    val status: String = "RUNNING", // RUNNING, COMPLETED, PENDING
    val targetPips: Double = 15.0,
    val stopLossPips: Double = 10.0,
    val lotSize: Double = 0.01,
    val executionCount: Int = 14,
    val isEnabled: Boolean = true,
    val progressPercent: Int = 75,
    val createdAt: String = "11:08:07"
)
