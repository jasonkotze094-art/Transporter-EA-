package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LinkedAccount
import com.example.data.model.MarketQuote
import com.example.data.model.PositionEntity
import com.example.data.model.RobotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradingDao {

    // Accounts
    @Query("SELECT * FROM linked_accounts LIMIT 1")
    fun getActiveAccount(): Flow<LinkedAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: LinkedAccount)

    @Query("DELETE FROM linked_accounts")
    suspend fun clearAccounts()

    // Robots
    @Query("SELECT * FROM robots")
    fun getAllRobots(): Flow<List<RobotEntity>>

    @Query("SELECT * FROM robots WHERE id = :id")
    fun getRobotById(id: String): Flow<RobotEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobots(robots: List<RobotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobot(robot: RobotEntity)

    @Update
    suspend fun updateRobot(robot: RobotEntity)

    @Query("UPDATE robots SET isRunning = :isRunning WHERE id = :id")
    suspend fun setRobotRunning(id: String, isRunning: Boolean)

    @Query("DELETE FROM robots WHERE id = :id")
    suspend fun deleteRobotById(id: String)

    // Positions
    @Query("SELECT * FROM positions ORDER BY ticket DESC")
    fun getAllPositions(): Flow<List<PositionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPositions(positions: List<PositionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: PositionEntity)

    @Query("DELETE FROM positions WHERE ticket = :ticket")
    suspend fun closePosition(ticket: Long)

    @Query("DELETE FROM positions")
    suspend fun clearPositions()

    @Query("UPDATE positions SET currentPrice = :currentPrice, profit = :profit WHERE ticket = :ticket")
    suspend fun updatePositionTick(ticket: Long, currentPrice: Double, profit: Double)

    // Market Quotes
    @Query("SELECT * FROM market_quotes")
    fun getQuotes(): Flow<List<MarketQuote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<MarketQuote>)

    // Robot Tasks
    @Query("SELECT * FROM robot_tasks ORDER BY createdAt DESC")
    fun getRobotTasks(): Flow<List<com.example.data.model.RobotTaskEntity>>

    @Query("SELECT * FROM robot_tasks WHERE robotId = :robotId ORDER BY createdAt DESC")
    fun getTasksForRobot(robotId: String): Flow<List<com.example.data.model.RobotTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<com.example.data.model.RobotTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: com.example.data.model.RobotTaskEntity)

    @Update
    suspend fun updateTask(task: com.example.data.model.RobotTaskEntity)

    @Query("UPDATE robot_tasks SET isEnabled = :isEnabled, status = :status WHERE id = :taskId")
    suspend fun setTaskStatus(taskId: String, isEnabled: Boolean, status: String)

    @Query("UPDATE robot_tasks SET status = 'COMPLETED', progressPercent = 100 WHERE id = :taskId")
    suspend fun completeTask(taskId: String)

    @Query("DELETE FROM robot_tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)
}
