package com.hm.mmmhmm.adapter

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
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupDiscussionAdapter(var ctx: FragmentActivity) : RecyclerView.Adapter<GroupDiscussionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_discussion, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            holder.tv_brand_name.text= campaignList?.get(position)?.brandName
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_time_left.text= "₹"+campaignList?.get(position)?.timeLeft.toString()+" left"
//            holder.iv_profile_pic_profile.load(
//                campaignList?.get(position)?.brandLogo.toString(),
//                R.color.text_gray,
//                R.color.text_gray,
//                true
//            )
        holder.iv_like.setOnClickListener {
            var likeData: LikeData = LikeData(
                SessionManager.getUserId(),
                "",
                SessionManager.getUserPic(),
                SessionManager.getUserName()
            );
            var groupDiscussionPostUpdateLikeRequest: GroupDiscussionPostUpdateLikeRequest = GroupDiscussionPostUpdateLikeRequest(
                "",likeData);
            groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest,  holder.iv_like,
                holder.iv_liked,
                holder.tv_like_count,
                /*(feedList?.get(position)?.like as List<PostLikeData>).size*/0
            )

        }


    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//            val iv_profile_pic_profile: ImageView
//            val tv_brand_name: TextView
//            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
//            val btn_learn_more: Button
val iv_like: ImageView
        val iv_liked: ImageView
        val tv_like_count: TextView

            init {
//                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
//                tv_brand_name = v.findViewById(R.id.tv_brand_name)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
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