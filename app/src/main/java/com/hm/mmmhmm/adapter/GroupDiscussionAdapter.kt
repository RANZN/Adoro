package com.hm.mmmhmm.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.models.GroupDiscussionPostUpdateLikeRequest
import com.hm.mmmhmm.models.LikeData
import com.hm.mmmhmm.models.ShowAnnouncementRequest
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
            holder.ll_like_discussion.setOnClickListener {

                var likeData: LikeData = LikeData("","","","");
                var groupDiscussionPostUpdateLikeRequest: GroupDiscussionPostUpdateLikeRequest = GroupDiscussionPostUpdateLikeRequest(
                    "",likeData);
                groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest)


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
            val ll_like_discussion: LinearLayout
//
            init {
//                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
//                tv_brand_name = v.findViewById(R.id.tv_brand_name)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
    ll_like_discussion = v.findViewById(R.id.ll_like_discussion)
            }
    }

     private fun groupDiscussionPostUpdateLike(groupDiscussionPostUpdateLikeRequest: GroupDiscussionPostUpdateLikeRequest) {
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