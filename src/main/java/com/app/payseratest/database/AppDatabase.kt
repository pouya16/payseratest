package com.app.payseratest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.payseratest.models.BalanceModel
import com.app.payseratest.models.TransactionsModel

@Database(entities = [BalanceModel::class,TransactionsModel::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
    abstract fun transactionDao(): TransactionsDao
}