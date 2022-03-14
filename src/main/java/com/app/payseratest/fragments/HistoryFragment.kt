package com.app.payseratest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.app.payseratest.adapters.HistoryAdapter
import com.app.payseratest.database.AppDatabase
import com.app.payseratest.database.TransactionsDao
import com.app.payseratest.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {



    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var db: AppDatabase? = null
    private var transactionsDao: TransactionsDao? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        db = context?.let { Room.databaseBuilder(it,AppDatabase::class.java,"peysera")
            .allowMainThreadQueries().build() }
        transactionsDao = db!!.transactionDao()


        return view
    }

    override fun onResume() {
        super.onResume()
        if(transactionsDao!!.getTransactions()!=null){
            binding.recyclerHistory.layoutManager = LinearLayoutManager(context)
            var adapter=HistoryAdapter(transactionsDao!!.getTransactions().reversed())
            binding.recyclerHistory.adapter = adapter
            if(adapter.items.size>0){
                binding.txtHistory.visibility = View.GONE
            }
        }

    }


}