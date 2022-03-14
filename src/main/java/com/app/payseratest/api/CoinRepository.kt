package com.app.payseratest.api

import android.content.Context
import androidx.lifecycle.LiveData
import com.app.payseratest.models.BalanceModel
import java.util.*

class CoinRepository {

    private var instance: CoinRepository? = null
    private var apiClient: ApiClient? = null

    constructor() {
        this.apiClient = ApiClient().getInstance()
    }


    fun getInstance(): CoinRepository?{
        if(instance == null){
            instance = CoinRepository()
        }
        return instance
    }
    fun getCoinsPrice(): LiveData<ArrayList<BalanceModel>>? {
        return apiClient!!.getCoinsMutable()
    }

    fun getCoinListApi(context: Context?) {
        apiClient!!.getCoinsApi(context)
    }

}