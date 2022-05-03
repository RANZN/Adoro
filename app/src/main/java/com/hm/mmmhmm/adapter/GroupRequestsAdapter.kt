package com.hm.mmmhmm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item


class GroupRequestsAdapter(var ctx: FragmentActivity) : RecyclerView.Adapter<GroupRequestsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_requests, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            //holder.tv_username_notification.text= listData?.get(position)?.message
//            holder.iv_user_notification.load(
//                listData?.get(position)?.image,
//                R.color.text_gray,
//                R.color.text_gray,
//                true
//            )
//            holder.itemView.setOnClickListener {
//                //todo
//
//            }

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_user_request: ImageView
            val tv_username_request: TextView
//            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
            val btn_accept_request: Button
//            val ll_item_list: LinearLayout
//
            init {
    iv_user_request = v.findViewById(R.id.iv_user_request)
    tv_username_request = v.findViewById(R.id.tv_username_request)
                btn_accept_request = v.findViewById(R.id.btn_accept_request)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
    }
}