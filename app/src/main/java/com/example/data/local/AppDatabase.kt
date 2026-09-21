package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.LinkedAccount
import com.example.data.model.MarketQuote
import com.example.data.model.PositionEntity
import com.example.data.model.RobotEntity
import com.example.data.model.RobotTaskEntity

@Database(
    entities = [
        LinkedAccount::class,
        RobotEntity::class,
        PositionEntity::class,
        MarketQuote::class,
        RobotTaskEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradingDao(): TradingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ea_converter_trading.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
