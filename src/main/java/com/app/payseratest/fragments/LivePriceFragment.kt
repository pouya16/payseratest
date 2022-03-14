package com.app.payseratest.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.payseratest.adapters.BalanceAdapter
import com.app.payseratest.databinding.FragmentLivePriceBinding
import com.app.payseratest.models.BalanceModel
import com.app.payseratest.viewmodels.MainViewModel
import java.util.*


class LivePriceFragment : Fragment() {


    private var mainViewModel: MainViewModel? = null
    private var _binding:FragmentLivePriceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLivePriceBinding.inflate(inflater, container, false)
        val view = binding.root
        mainViewModel = ViewModelProvider(this)
            .get(MainViewModel::class.java)


        this.context?.let { getCoinList(it) }
        ObserveChange()
        return view
    }


    private fun getCoinList(context: Context){
        mainViewModel!!.getCoinList(context)
    }

    private fun createObserver() {
        observer = Observer {

            var adapter = BalanceAdapter(it)
            binding.recyclerLive.layoutManager = LinearLayoutManager(context)
            binding.recyclerLive.adapter = adapter

        }
    }

    private fun ObserveChange() {
        createObserver()
        mainViewModel!!.getCoinsBalance().observe(viewLifecycleOwner,observer!!)
    }

    var observer: Observer<ArrayList<BalanceModel>>? = null








    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}