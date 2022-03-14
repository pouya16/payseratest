package com.app.payseratest.api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.payseratest.models.BalanceModel
import org.json.JSONObject
import java.util.*

class ApiClient {
    private var instance: ApiClient? = null
    private var coinsMutableList: MutableLiveData<ArrayList<BalanceModel>>? = null

    public fun getInstance() : ApiClient{
        if(instance == null){
            instance = ApiClient()
        }
        return instance as ApiClient
    }


    fun getCoinsMutable(): LiveData<ArrayList<BalanceModel>>?{
        return coinsMutableList
    }

    fun getCoinsApi(context: Context?){
        coinsMutableList = MutableLiveData()
        retrieveCoins(context)

    }


    fun outPut(response: String){
        var list: ArrayList<BalanceModel> = ArrayList()
        var jsonResponse: JSONObject = JSONObject(response)
        if(jsonResponse.has("base"))
            list.add(BalanceModel(jsonResponse.getString("base"),1.0))
        var jsonCoins:JSONObject = jsonResponse.getJSONObject("rates")
        var iterat = jsonCoins.keys();
        while (iterat.hasNext()){
            val key = iterat.next() as String
            list.add(BalanceModel(key,jsonCoins.getDouble(key)))
        }
        coinsMutableList!!.postValue(list)
    }


    fun retrieveCoins(context: Context?){
        var apiClient: ApiClient = ApiClient().getInstance()
        var queue : RequestQueue = Volley.newRequestQueue(context)
        var url = "http://api.exchangeratesapi.io/latest?access_key=570702a043df572d7cd9036ede4cc301"
        Log.i("Log1", "url is: $url")
        try {
            val stringRequest: StringRequest = object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    Log.i("Log1", "response is: $response")
                    outPut(response)
                },
                Response.ErrorListener { error ->
                    Log.i("Log1", "error is: $error")
                    Log.i("Log1", "error is: " + error.message)
                }) {
                //This is for Headers If You Needed
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    return params
                }
            }
            queue.add<String>(stringRequest)
        } catch (e: Exception) {
            Log.i("Log1", "exception is: $e")
        }


    }

}