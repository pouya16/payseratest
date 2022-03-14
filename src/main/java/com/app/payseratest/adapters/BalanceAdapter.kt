package com.app.payseratest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.payseratest.R
import com.app.payseratest.models.BalanceModel

class BalanceAdapter(private val items:List<BalanceModel>)
    : RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_wallets, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.currecny.text = items.get(position).curr
        holder.remain.text = items.get(position).remain.toString()
    }

    override fun getItemCount(): Int  =  items.size


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
            val currecny = itemView.findViewById<TextView>(R.id.txt_cur)
            val remain = itemView.findViewById<TextView>(R.id.txt_price)
    }
}