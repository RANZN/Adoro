package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.fragments.ProfileFragment
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item

class FeedListAdapter(private var feedList: List<Item>? = null) : RecyclerView.Adapter<FeedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_username.text= feedList?.get(position)?.username
           // holder.tv_like_count.text= feedList?.get(position)?.like.size()
           //holder.tv_apply_count.text= feedList?.get(position)?.shortDescription
           // holder.tv_time_left.text= "₹"+feedList?.get(position)?.timeLeft.toString()+" left"
//           holder.tv_apply_count.text= feedList?.get(position)?.comment?.size()
           holder.tv_feed_description.text= feedList?.get(position)?.description
            holder.iv_user_feed.load(
                feedList?.get(position)?.profile,
                R.color.text_gray,
                R.color.text_gray,
                true
            )
        holder.iv_feed.load(
                feedList?.get(position)?.image,
                R.color.text_gray,
                R.color.text_gray,
                false
            )
            holder.itemView.setOnClickListener {
                //todo

            }
        holder.tv_username.setOnClickListener {
//            val profileFragment = ProfileFragment()
//            val args = Bundle()
//            args.putString("path", "search")
//            args.putString("userId", suggestionList?.get(position)?._id)
//            profileFragment.arguments = args
//            holder.itemView.setOnClickListener {
//                (activity as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.frame_layout_main, profileFragment)
//                    .addToBackStack(null).commit()
//
//            }

        }
            }


    override fun getItemCount(): Int {
        return feedList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_user_feed: ImageView
        val iv_feed: ImageView
        val tv_username: TextView
        val tv_like_count: TextView
        val tv_apply_count: TextView
        val tv_comment_count: TextView
        val tv_feed_description: TextView

        //
        init {
            iv_user_feed = v.findViewById(R.id.iv_user_feed)
            iv_feed = v.findViewById(R.id.iv_feed)
            tv_username = v.findViewById(R.id.tv_username)
            tv_like_count = v.findViewById(R.id.tv_like_count)
            tv_apply_count = v.findViewById(R.id.tv_apply_count)
            tv_comment_count = v.findViewById(R.id.tv_comment_count)
            tv_feed_description = v.findViewById(R.id.tv_feed_description)
        }
    }
}