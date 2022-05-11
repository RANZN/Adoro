package com.hm.mmmhmm.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Comment
import com.hm.mmmhmm.models.CommentData
import com.hm.mmmhmm.models.Item
import de.hdodenhof.circleimageview.CircleImageView


class CommentsAdapter(var ctx: FragmentActivity, var listData: List<Comment>? = null) :
    RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comments, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tv_comment_user_name.text = listData?.get(position)?.userName ?: ""
        holder.tv_comment.text = listData?.get(position)?.text ?: ""
        holder.tv_time.text = listData?.get(position)?.dateTime ?: ""
        holder.civ_comment_user.load(
            listData?.get(0)?.profilePhoto ?: "",
            R.color.text_gray,
            R.color.text_gray,
            true
        )

    }

    override fun getItemCount(): Int {
        return listData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val civ_comment_user: CircleImageView
        val tv_comment_user_name: TextView
        val tv_comment: TextView
        val tv_time: TextView
        init {
            civ_comment_user = v.findViewById(R.id.civ_comment_user)
            tv_comment_user_name = v.findViewById(R.id.tv_comment_user_name)
            tv_comment = v.findViewById(R.id.tv_comment)
            tv_time = v.findViewById(R.id.tv_time)
        }
    }
}