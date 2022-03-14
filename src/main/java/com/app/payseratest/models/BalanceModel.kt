package com.app.payseratest.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BalanceModel(
    @PrimaryKey
    @NonNull
    val curr:String,
    @ColumnInfo(name = "remain")
    val remain:Double
)
