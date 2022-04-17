package com.hm.mmmhmm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.Like
import de.hdodenhof.circleimageview.CircleImageView


class MutualLikerAdapter(var ctx: FragmentActivity, private var listData: List<Like>? = null) : RecyclerView.Adapter<MutualLikerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mutual_liker, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.iv_mutual_liker.load(
                listData?.get(position)?.profile,
                R.color.text_gray,
                R.color.text_gray,
                true
            )
//            holder.itemView.setOnClickListener {
//                //todo
//
//            }

    }

    override fun getItemCount(): Int {
//        return if (listData?.size?:0>1) 1 else listData?.size?:0
        return if (listData?.size?:0>1) 1 else 0

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_mutual_liker: CircleImageView

            init {
                iv_mutual_liker = v.findViewById(R.id.iv_mutual_liker)
            }
    }
}