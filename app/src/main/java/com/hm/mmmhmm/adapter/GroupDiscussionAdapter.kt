package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.GroupCommentsFragment
import com.hm.mmmhmm.fragments.GroupDiscussionCommentsFragment
import com.hm.mmmhmm.fragments.HomeFragment
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupDiscussionAdapter(var ctx: FragmentActivity,  private var listData: List<Item>? = null) : RecyclerView.Adapter<GroupDiscussionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_discussion, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_username.text= listData?.get(position)?.username
            holder.tv_title.text= listData?.get(position)?.contentTitle
            holder.tv_description.text= listData?.get(position)?.content
            holder.iv_user_pic.load(
                listData?.get(position)?.profile.toString(),
                R.color.text_gray,
                R.color.text_gray,
                true
            )
        holder.iv_like.setOnClickListener {
            var likeData: LikeData = LikeData(
                SessionManager.getUserId(),
                "",
                SessionManager.getUserPic(),
                SessionManager.getUserName()
            );
            var groupDiscussionPostUpdateLikeRequest: GroupDiscussionPostUpdateLikeRequest = GroupDiscussionPostUpdateLikeRequest(
                listData?.get(position)?._id,likeData);
            groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest,  holder.iv_like,
                holder.iv_liked,
                holder.tv_like_count,
                (listData?.get(position)?.like?: emptyList()).size
            )

        }

        holder.ll_comments.setOnClickListener{
            val commentsFragment = GroupDiscussionCommentsFragment()
            val args = Bundle()
            args.putString("postId", listData?.get(position)?._id)
            commentsFragment.arguments = args
            ctx.supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, commentsFragment).addToBackStack(null)
                .commit()
            SessionManager.setFeedLastPosition(position)
            (HomeFragment).lastFirstVisiblePosition = position
        }


    }

    override fun getItemCount(): Int {
        return listData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_user_pic: ImageView
            val tv_username: TextView
            val tv_title: TextView
            val tv_description: TextView
            val ll_comments: LinearLayout
//            val btn_learn_more: Button
val iv_like: ImageView
        val iv_liked: ImageView
        val tv_like_count: TextView

            init {
                iv_user_pic = v.findViewById(R.id.iv_user_pic)
                tv_username = v.findViewById(R.id.tv_username)
                tv_title = v.findViewById(R.id.tv_title)
                tv_description = v.findViewById(R.id.tv_description)
                ll_comments = v.findViewById(R.id.ll_comments)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
    iv_like = v.findViewById(R.id.iv_like)
    iv_liked = v.findViewById(R.id.iv_liked)
                tv_like_count = v.findViewById(R.id.tv_like_count)
            }
    }

     private fun groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest: GroupDiscussionPostUpdateLikeRequest,
                                               iv_like: ImageView,
                                               iv_liked: ImageView,
                                               tv_like_count: TextView,
                                               likeCount: Int) {
        // pb_group_detail.visibility = View.VISIBLE
         val apiInterface = ApiClient.getRetrofitService(ctx)
         CoroutineScope(Dispatchers.IO).launch {
             try {
                 val response = apiInterface.groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest)
                 withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                     try {
                         //  toast("" + response.body()?.message)
                         if (response!=null) {
//                            feedList = response.body()?.OK?.items
                             if (response != null) {
                                 tv_like_count.text = (likeCount + 1).toString()
                                 iv_liked.visibility=View.VISIBLE
                                 iv_like.visibility=View.GONE
                             } else {
                                 Log.d("resp", "complet else: ")
                             }

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