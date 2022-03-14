package com.app.payseratest.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.app.payseratest.R
import com.app.payseratest.adapters.BalanceAdapter
import com.app.payseratest.database.AppDatabase
import com.app.payseratest.database.BalanceDao
import com.app.payseratest.database.TransactionsDao
import com.app.payseratest.databinding.FragmentMainBinding
import com.app.payseratest.models.BalanceModel
import com.app.payseratest.models.TransactionsModel
import com.app.payseratest.viewmodels.MainViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MainFragment : Fragment(){

    private var mainViewModel: MainViewModel? = null
    private var _binding:FragmentMainBinding? = null
    private var db:AppDatabase? = null
    private var balanceDao:BalanceDao? = null
    private var transactionsDao:TransactionsDao? = null
    private var currunciesList: ArrayList<BalanceModel>? = null

    var sellPosition = -1
    var receivePosition = -1
    var remainBalance:Double = 0.0
    var findedSellPosition = -1

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        db = context?.let { Room.databaseBuilder(it,AppDatabase::class.java,"peysera")
            .allowMainThreadQueries().build() }
        balanceDao = db!!.balanceDao()
        transactionsDao = db!!.transactionDao()

        mainViewModel = ViewModelProvider(this)
            .get(MainViewModel::class.java)
        binding.btnSubmit.setOnClickListener({submitTransaction()})
        binding.spinnerReceive.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                receivePosition = position
                if(binding.txtSell.text.toString().length>0)
                    updateRecieve()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
        binding.spinnerSell.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                sellPosition = position
                updateRemainBalance(binding.spinnerSell.selectedItem.toString())
                if(binding.txtSell.text.toString().length>0)
                    updateRecieve()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        binding.txtSell.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.toString().length>0){
                    updateRecieve()
                }else{
                    binding.txtReceive.text = "0.00"
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        if(isWallet()){
            updateChanges()
        }

        this.context?.let { getCoinList(it) }
        ObserveChange()
        return view
    }



    fun submitTransaction(){
        if(receivePosition!=-1&&sellPosition!=-1){
            if(binding.txtSell.text.toString().toDouble()<=remainBalance&&binding.txtSell.text.toString().toDouble()>0){
                var fromBalance = remainBalance - binding.txtSell.text.toString().toDouble()
                var toBalance:Double = 0.0
                var fromName = currunciesList!!.get(findedSellPosition).curr
                var toName = binding.spinnerReceive.selectedItem.toString()
                updateBalance(fromName, fromBalance)
                if(binding.txtReceive.text.toString().toDouble()>0){
                    if(balanceDao!!.getBalance(binding.spinnerReceive.selectedItem.toString())!=null){
                        toBalance = balanceDao!!
                            .getBalance(toName).remain +
                                binding.txtReceive.text.toString().toDouble()
                        updateBalance(toName,toBalance - calculateCommision())
                    }else{
                        toBalance = binding.txtReceive.text.toString().toDouble()
                        balanceDao!!.insertOne(BalanceModel(toName,toBalance))
                    }
                }
                transactionsDao!!.insertTransaction(TransactionsModel(
                    fromName, binding.txtSell.text.toString().toDouble(), toName, toBalance))
                updateChanges()
                showAlert(fromName,binding.txtSell.text.toString().toDouble(),toName,toBalance,calculateCommision())
            }
        }else{
            Toast.makeText(context, R.string.check_spinner,Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert(from:String,fPrice:Double,to:String,tPrice:Double,commission:Double){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Currency Converted")
        builder.setMessage("You have converted $fPrice $from to $tPrice $to. " +
                "Commission Fee - $commission $from.")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(context,
                getString(R.string.done), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }


    private fun calculateCommision():Double{
        var commision = 0.0
        if(transactionsDao!!.getTransactions().size>4)
            commision = 0.007 * binding.txtSell.text.toString().toDouble()
        return commision
    }

    private fun updateBalance(curr:String,balance:Double){
        balanceDao!!.updateBalance(curr,balance)
    }

    private fun updateRecieve(){
        var price = binding.txtSell.text.toString().toDouble()
        if(sellPosition!=-1&&receivePosition!=-1){
            Log.i("Log1","price is: $price and remain is $remainBalance")
            if(remainBalance>=price){
                var newPrice = price * (currunciesList!!.get(findedSellPosition).remain/currunciesList!!.get(receivePosition).remain)
                val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
                df.setMaximumFractionDigits(5)
                binding.txtReceive.text = df.format(newPrice)
            }else{
                binding.txtReceive.text = "0.00"
                Toast.makeText(context, R.string.low_price,Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, R.string.check_spinner,Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateRemainBalance(curr:String){

        if(balanceDao!!.getBalance(curr)!=null){
            remainBalance = balanceDao!!.getBalance(curr).remain
            Log.i("Log1","remain balance is: $remainBalance")
        }
        findPosition(curr)
    }

    private fun findPosition(curr: String){
        if(currunciesList != null){
            for(i in currunciesList!!.indices){
                if(currunciesList!![i].curr.compareTo(curr) == 0){
                    findedSellPosition = i
                    Log.i("Log1","position is found in $i")
                }
            }
        }
    }


    private fun getCoinList(context: Context){
        mainViewModel!!.getCoinList(context)
    }

    private fun createObserver() {
        observer = Observer {
            currunciesList = it
            updateReceiveSpinner(it)
            if(!isWallet()){
                balanceDao!!.insertOne(BalanceModel(it[0].curr,1000.0))
                Log.i("Log1", "base inserted")
                updateChanges()
            }
        }
    }

    private fun updateChanges(){
        updateSellSpinner()
        updateBalances()
    }

    private fun ObserveChange() {
        createObserver()
        mainViewModel!!.getCoinsBalance().observe(viewLifecycleOwner,observer!!)
    }

    var observer: Observer<ArrayList<BalanceModel>>? = null


    private fun updateBalances(){
        if(isWallet()){
            var adapter = BalanceAdapter(getWallets()!!)
            binding.recyclerBalances.layoutManager = LinearLayoutManager(context)
            binding.recyclerBalances.adapter = adapter
        }
    }

    private fun updateReceiveSpinner(balanceModels: ArrayList<BalanceModel>){
        var recieveAdapter: ArrayAdapter<String>? = context?.let { it1 -> ArrayAdapter(it1,android.R.layout.simple_list_item_1) }
        for(balance:BalanceModel in balanceModels){
            recieveAdapter!!.add(balance.curr)
        }
        recieveAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerReceive.adapter = recieveAdapter
        if(!recieveAdapter.isEmpty){
            receivePosition = 0
            updateRemainBalance(binding.spinnerReceive.selectedItem.toString())
        }
    }

    private fun updateSellSpinner(){
        if(isWallet()){
            var sellAdapter: ArrayAdapter<String>? = context?.let { it1 -> ArrayAdapter(it1,android.R.layout.simple_list_item_1) }
            for(balance:BalanceModel in getWallets()!!){
                sellAdapter!!.add(balance.curr)
            }
            sellAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            if(!sellAdapter.isEmpty){
                sellPosition = 0
            }
            binding.spinnerSell.adapter = sellAdapter
        }
    }

    private fun isWallet() :Boolean{
        if(balanceDao!!.getAll().isNotEmpty())
            return true
        return false
    }

    private fun getWallets(): List<BalanceModel>? {
        if(isWallet())
            return balanceDao!!.getAll()
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}