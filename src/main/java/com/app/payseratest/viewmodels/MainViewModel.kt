package com.app.payseratest.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.payseratest.api.CoinRepository
import com.app.payseratest.models.BalanceModel

class MainViewModel: ViewModel {
    private var coinsRepository: CoinRepository? = null

    constructor(){
        coinsRepository = CoinRepository().getInstance()
        //coinsLiveList = coinsRepository!!.getCoinsPrice()
    }

    fun getCoinList(context: Context?){
        coinsRepository!!.getCoinListApi(context)
    }
    fun getCoinsBalance() : LiveData<ArrayList<BalanceModel>>{
        return coinsRepository!!.getCoinsPrice()!!
    }
}