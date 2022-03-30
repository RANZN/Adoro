package com.hm.mmmhmm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.ResultDetailFragment
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.Trancation


class AdoroCoinsAdapter(private val transactionsList:List<Trancation>? ) : RecyclerView.Adapter<AdoroCoinsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_adoro_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tvCoins.text= transactionsList?.get(position)?.amount.toString()
            holder.tvAdoroDate.text= ""+transactionsList?.get(position)?._createdDate.toString()
            holder.tvAdoroDate.text= "Date : "+transactionsList?.get(position)?._createdDate.toString()
    }

    override fun getItemCount(): Int {
        return transactionsList?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
         val tvCoins: TextView
             val tvAdoroDate: TextView
            init {
                tvCoins = v.findViewById(R.id.tv_coins)
                tvAdoroDate = v.findViewById(R.id.tv_date)
            }
    }
}