package com.app.payseratest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.payseratest.R
import com.app.payseratest.models.TransactionsModel

class HistoryAdapter(val items:List<TransactionsModel>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int){
        with(items.get(position)){
            holder.fromTxt.text = fromCurrnecy
            holder.toTxt.text = toCurrency
            holder.fromPriceTxt.text = fromPrice.toString()
            holder.toPriceTxt.text = toPrice.toString()
        }
    }

    override fun getItemCount(): Int  =  items.size

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val fromTxt = itemView.findViewById<TextView>(R.id.txt_sell_cu)
        val toTxt = itemView.findViewById<TextView>(R.id.txt_buy_cu)
        val fromPriceTxt = itemView.findViewById<TextView>(R.id.txt_sell)
        val toPriceTxt = itemView.findViewById<TextView>(R.id.txt_receive)

    }
}