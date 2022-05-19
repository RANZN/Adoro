package com.hm.mmmhmm.adapter

import android.graphics.Color
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


class NotificationsAdapter(var ctx: FragmentActivity, private var listData: List<Item>? = null) : RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_username_notification.text= listData?.get(position)?.message
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
            holder.tv_time.text= listData?.get(position)?._createdDate
        if(position%2==0){
            holder.tv_time.setTextColor(Color.parseColor("#DA6377"))

        }else{
            holder.tv_time.setTextColor(Color.parseColor("#1CB8B8"));
        }
            holder.iv_user_notification.load(
                listData?.get(position)?.image,
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
        return listData?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_user_notification: ImageView
            val tv_username_notification: TextView
            val tv_time: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
//            val btn_learn_more: Button
//            val ll_item_list: LinearLayout
//
            init {
                iv_user_notification = v.findViewById(R.id.iv_user_notification)
                tv_username_notification = v.findViewById(R.id.tv_username_notification)
    tv_time = v.findViewById(R.id.tv_time)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
    }
}