package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.fragments.CommentsFragment
import com.hm.mmmhmm.fragments.PostDetailFragment
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field


class FeedListAdapter(var ctx: FragmentActivity, private var feedList: List<Item>? = null) :
    RecyclerView.Adapter<FeedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_username.text = feedList?.get(position)?.username
        // holder.tv_like_count.text= feedList?.get(position)?.like.size()
        //holder.tv_apply_count.text= feedList?.get(position)?.shortDescription
        // holder.tv_time_left.text= "₹"+feedList?.get(position)?.timeLeft.toString()+" left"
//           holder.tv_apply_count.text= feedList?.get(position)?.comment?.size()
        holder.tv_feed_description.text = feedList?.get(position)?.description
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
        holder.iv_menu_feed.setOnClickListener {
            val popupMenu = PopupMenu(ctx, holder.iv_menu_feed)
            popupMenu.inflate(R.menu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.delete -> {
                            // here are the logic to delete an item from the list
//                        val tempLang = languageList[position]
//                        languageList.remove(tempLang)
//                        rvAdapter.notifyDataSetChanged()
                            return true
                        }
                        // in the same way you can implement others
//                        R.id.edit -> {
//                            // define
//                            Toast.makeText(ctx , "Item 2 clicked" , Toast.LENGTH_SHORT).show()
//                            return true
//                        }

                    }
                    return false
                }
            })
            popupMenu.show()

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
        holder.tv_like_count.text =
            (feedList?.get(position)?.like as List<PostLikeData>).size.toString()

        holder.iv_like.setOnClickListener {
            var likeData: PostLikeData = PostLikeData(
                SessionManager.getUserId(),
                SessionManager.getUserPic(),
                SessionManager.getUserName()
            );
            var postLikeRequest: PostLikeRequest = PostLikeRequest(
                feedList?.get(position)?._id, likeData
            );
            postUpdateLike(
                postLikeRequest,
                holder.iv_like,
                holder.tv_like_count,
                (feedList?.get(position)?.like as List<PostLikeData>).size
            )
        }
        holder.ll_comments.setOnClickListener{
            val commentsFragment = CommentsFragment()
            val args = Bundle()
            args.putString("postId", feedList?.get(position)?._id)
            commentsFragment.arguments = args
            ctx.supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, commentsFragment)
                .commit()
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
        val iv_like: ImageView
        val tv_username: TextView
        val tv_like_count: TextView
        val tv_apply_count: TextView
        val tv_comment_count: TextView
        val tv_feed_description: TextView
        val iv_menu_feed: ImageView
        val ll_comments: LinearLayout

        //
        init {
            iv_user_feed = v.findViewById(R.id.iv_user_feed)
            iv_feed = v.findViewById(R.id.iv_feed)
            iv_like = v.findViewById(R.id.iv_like)
            tv_username = v.findViewById(R.id.tv_username)
            tv_like_count = v.findViewById(R.id.tv_like_count)
            tv_apply_count = v.findViewById(R.id.tv_apply_count)
            tv_comment_count = v.findViewById(R.id.tv_comment_count)
            tv_feed_description = v.findViewById(R.id.tv_feed_description)
            iv_menu_feed = v.findViewById(R.id.iv_menu_feed)
            ll_comments = v.findViewById(R.id.ll_comments)
        }
    }

    private fun postUpdateLike(
        postLikeRequest: PostLikeRequest,
        iv_like: ImageView,
        tv_like_count: TextView,
        likeCount: Int
    ) {
        // pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateLike(postLikeRequest)
                withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null) {
                            tv_like_count.text = (likeCount + 1).toString()
                            iv_like.setColorFilter(
                                ContextCompat.getColor(ctx, R.color.red),
                                android.graphics.PorterDuff.Mode.MULTIPLY
                            );
                        } else {
                            Log.d("resp", "complet else: ")
                        }

                    } catch (e: Exception) {
                        Log.d("resp", "cathch: " + e.toString())
                    }
                }

            } catch (e: Exception) {
                Log.d("weweewefw", e.toString())
            }
        }

    }


}