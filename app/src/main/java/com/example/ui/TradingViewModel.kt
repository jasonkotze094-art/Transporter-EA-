package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.LinkedAccount
import com.example.data.model.MarketQuote
import com.example.data.model.PositionEntity
import com.example.data.model.RobotEntity
import com.example.data.model.RobotTaskEntity
import com.example.data.repository.TradingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    METATRADER,
    LIVE_TRADES,
    SETTINGS
}

data class UiNotification(
    val message: String,
    val isError: Boolean = false
)

class TradingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TradingRepository(db.tradingDao(), viewModelScope)

    val activeAccount: StateFlow<LinkedAccount?> = repository.activeAccount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allRobots: StateFlow<List<RobotEntity>> = repository.allRobots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPositions: StateFlow<List<PositionEntity>> = repository.allPositions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quotes: StateFlow<List<MarketQuote>> = repository.quotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val robotTasks: StateFlow<List<RobotTaskEntity>> = repository.robotTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _showQuotesModal = MutableStateFlow(false)
    val showQuotesModal: StateFlow<Boolean> = _showQuotesModal.asStateFlow()

    private val _showAddKeyModal = MutableStateFlow(false)
    val showAddKeyModal: StateFlow<Boolean> = _showAddKeyModal.asStateFlow()

    private val _showRemoveModal = MutableStateFlow(false)
    val showRemoveModal: StateFlow<Boolean> = _showRemoveModal.asStateFlow()

    private val _showCreateTaskModal = MutableStateFlow(false)
    val showCreateTaskModal: StateFlow<Boolean> = _showCreateTaskModal.asStateFlow()

    private val _notification = MutableStateFlow<UiNotification?>(null)
    val notification: StateFlow<UiNotification?> = _notification.asStateFlow()

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setShowQuotesModal(show: Boolean) {
        _showQuotesModal.value = show
    }

    fun setShowAddKeyModal(show: Boolean) {
        _showAddKeyModal.value = show
    }

    fun setShowRemoveModal(show: Boolean) {
        _showRemoveModal.value = show
    }

    fun setShowCreateTaskModal(show: Boolean) {
        _showCreateTaskModal.value = show
    }

    fun clearNotification() {
        _notification.value = null
    }

    fun toggleRobot(robot: RobotEntity) {
        viewModelScope.launch {
            val newState = !robot.isRunning
            repository.toggleRobotState(robot.id, newState)
            _notification.value = UiNotification(
                message = if (newState) "${robot.name} Activated & Scalping" else "${robot.name} Stopped"
            )
        }
    }

    fun addLicenseKey(robotId: String, key: String) {
        viewModelScope.launch {
            if (key.isBlank()) {
                _notification.value = UiNotification("Please enter a valid key", isError = true)
                return@launch
            }
            val success = repository.addLicenseKey(robotId, key)
            if (success) {
                _notification.value = UiNotification("License Key Verified! Robot fully unlocked.")
                _showAddKeyModal.value = false
            } else {
                _notification.value = UiNotification("Invalid License Key format", isError = true)
            }
        }
    }

    fun linkAccount(login: String, server: String) {
        viewModelScope.launch {
            repository.linkAccount(login, server)
            _notification.value = UiNotification("MetaTrader 5 Account $login Linked Successfully!")
            _currentTab.value = ScreenTab.HOME
        }
    }

    fun removeCurrentRobot(robotId: String) {
        viewModelScope.launch {
            repository.removeRobot(robotId)
            _notification.value = UiNotification("Robot unlinked.")
            _showRemoveModal.value = false
        }
    }

    fun restoreDefaultRobot() {
        viewModelScope.launch {
            repository.resetDefaultRobot()
            _notification.value = UiNotification("SKULLXPERT AI loaded.")
        }
    }

    fun closePosition(ticket: Long) {
        viewModelScope.launch {
            repository.closePosition(ticket)
            _notification.value = UiNotification("Position #$ticket closed.")
        }
    }

    fun openManualOrder(symbol: String, type: String, volume: Double = 0.01) {
        viewModelScope.launch {
            repository.openManualOrder(symbol, type, volume)
            _notification.value = UiNotification("Order $type $volume $symbol executed at market")
        }
    }

    fun toggleTask(taskId: String, currentEnabled: Boolean, title: String) {
        viewModelScope.launch {
            repository.toggleTask(taskId, currentEnabled)
            _notification.value = UiNotification(
                if (!currentEnabled) "Task '$title' activated on MetaTrader 5" else "Task '$title' paused"
            )
        }
    }

    fun executeTaskImmediate(taskId: String, taskTitle: String) {
        viewModelScope.launch {
            repository.executeTaskImmediate(taskId)
            _notification.value = UiNotification("Task '$taskTitle' finished and executed live on MetaTrader 5!")
        }
    }

    fun createRobotTask(
        title: String,
        description: String,
        symbol: String,
        actionType: String,
        lotSize: Double,
        targetPips: Double,
        stopLossPips: Double
    ) {
        viewModelScope.launch {
            repository.createRobotTask(
                title = title,
                description = description,
                symbol = symbol,
                actionType = actionType,
                lotSize = lotSize,
                targetPips = targetPips,
                stopLossPips = stopLossPips
            )
            _showCreateTaskModal.value = false
            _notification.value = UiNotification("New task '$title' assigned to SKULLXPERT AI!")
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            _notification.value = UiNotification("Task removed from robot queue.")
        }
    }
}
