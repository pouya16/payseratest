package com.app.payseratest.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionsModel(
    @ColumnInfo(name = "fromCurrnecy")
 val fromCurrnecy: String,
    @ColumnInfo(name="fromPrice")
 val fromPrice: Double,
    @ColumnInfo(name ="toCurrency")
 val toCurrency: String,
    @ColumnInfo(name = "toPrice")
 val toPrice: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
