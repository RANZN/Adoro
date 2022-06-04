package com.hm.mmmhmm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.fragments.FragmentGroupCreation
import com.hm.mmmhmm.fragments.GroupDetail
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item

class GroupAnnouncementAdapter( var ctx:FragmentActivity,private var listData: List<Item>? = null) : RecyclerView.Adapter<GroupAnnouncementAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_group_announcement.text= listData?.get(position)?.username
            holder.tv_location.text= ""
            holder.tv_description.text= listData?.get(position)?.message
            holder.iv_group_pic.load(
                listData?.get(position)?.profile.toString(),
                R.color.text_gray,
                R.color.text_gray,
                true
            )
        holder.itemView.setOnClickListener {
//            ctx.supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout_main, FragmentGroupCreation())
//                .addToBackStack(null).commit()
    }
    }

    override fun getItemCount(): Int {
        return listData?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_group_pic: ImageView
            val tv_group_announcement: TextView
            val tv_location: TextView
            val tv_description: TextView
            val tv_learn_more: TextView
            val iv_menu_feed: ImageView

            init {
                iv_group_pic = v.findViewById(R.id.iv_group_pic)
                tv_group_announcement = v.findViewById(R.id.tv_group_announcement)
                tv_location = v.findViewById(R.id.tv_location)
                tv_description = v.findViewById(R.id.tv_description)
                tv_learn_more = v.findViewById(R.id.tv_learn_more)
                iv_menu_feed = v.findViewById(R.id.iv_menu_feed)
            }
    }
}
