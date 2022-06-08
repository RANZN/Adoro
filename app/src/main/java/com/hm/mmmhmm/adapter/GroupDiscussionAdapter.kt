package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.*

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
        for (Like in listData?.get(position)?.like as List<Like>) {
            if(Like.id==SessionManager.getUserId()){
                holder.iv_like.isChecked=true

            }else{
                holder.iv_like.isChecked=false
            }
        }
        val animation1 = AnimationUtils.loadAnimation(ctx.applicationContext, R.anim.scale)
        holder.iv_like.setOnClickListener {
            var postItem: Item?=listData?.get(position)
            holder.iv_like.startAnimation(animation1)
            if (holder.iv_like.isChecked){
                var likeData: Like = Like(
                    SessionManager.getUserId(),
                    SessionManager.getUserPic(),
                    SessionManager.getUserName()
                )
                postItem?.like?.add(likeData)
                groupDiscussionPostUpdateLike(postItem!!)
            }else{
                try {
                    var likeList: ArrayList<Like>? = listData?.get(position)?.like
                    for(Like in (likeList as ArrayList<Like>)){
                        if(Like.id==SessionManager.getUserId()){
                            likeList.remove(Like)
                        }
                    }
                    postItem?.like=likeList
                    groupDiscussionPostUpdateLike(postItem!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

        }


        holder.ll_comments.setOnClickListener{
            val commentsFragment = GroupDiscussionCommentsFragment()
            val args = Bundle()
            args.putString("postId", listData?.get(position)?._id)
            commentsFragment.arguments = args
            ctx.supportFragmentManager.beginTransaction().add(R.id.frame_layout_main, commentsFragment).addToBackStack("GroupDiscussionCommentsFragment")
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
//          val btn_learn_more: Button
            val iv_like: CheckBox
            val tv_like_count: TextView

            init {
                iv_user_pic = v.findViewById(R.id.iv_user_pic)
                tv_username = v.findViewById(R.id.tv_username)
                tv_title = v.findViewById(R.id.tv_title)
                tv_description = v.findViewById(R.id.tv_description)
                ll_comments = v.findViewById(R.id.ll_comments)
                iv_like = v.findViewById(R.id.iv_like)
//               btn_learn_more = v.findViewById(R.id.btn_learn_more)
                tv_like_count = v.findViewById(R.id.tv_like_count)
            }
    }

     private fun groupDiscussionPostUpdateLike(item:Item) {
        // pb_group_detail.visibility = View.VISIBLE
         val apiInterface = ApiClient.getRetrofitService(ctx)
         CoroutineScope(Dispatchers.IO).launch {
             try {
                 val response = apiInterface.groupDiscussionPostUpdateLike(item)
                 withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                     try {
                         //  toast("" + response.body()?.message)
                         if (response!=null) {
//                            feedList = response.body()?.OK?.items
                             if (response != null) {
//                                 tv_like_count.text = (likeCount + 1).toString()
//                                 iv_liked.visibility=View.VISIBLE
//                                 iv_like.visibility=View.GONE
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