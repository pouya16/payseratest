package com.app.payseratest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.payseratest.models.BalanceModel
import java.util.*

@Dao
interface BalanceDao {

    @Insert
    fun insertAll(balanceModel: ArrayList<BalanceModel>)

    @Insert
    fun insertOne(balanceModel: BalanceModel)

    @Query("SELECT * FROM balanceModel")
    fun getAll():List<BalanceModel>

    @Query("SELECT * FROM balanceModel WHERE curr LIKE :currency")
    fun getBalance(currency: String): BalanceModel

    @Query("DELETE FROM balanceModel")
    fun deleteAll()

    @Query("UPDATE BalanceModel SET remain = :rem WHERE curr = :currency")
    fun updateBalance(currency: String,rem:Double)

}