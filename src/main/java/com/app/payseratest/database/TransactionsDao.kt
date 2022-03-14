package com.app.payseratest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.payseratest.models.TransactionsModel

@Dao
interface TransactionsDao {

    @Insert
    fun insertAll(transactionsModel: ArrayList<TransactionsModel>)

    @Insert
    fun insertTransaction(transactionsModel:TransactionsModel)

    @Query("SELECT * FROM transactionsModel")
    fun getTransactions() : List<TransactionsModel>

    @Query("DELETE FROM transactionsModel")
    fun deleteAll()
}