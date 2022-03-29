package com.hm.mmmhmm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.fragments.FragmentGroupCreation
import com.hm.mmmhmm.fragments.GroupDetail

class GroupAnnouncementAdapter( var ctx:FragmentActivity) : RecyclerView.Adapter<GroupAnnouncementAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
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
        holder.itemView.setOnClickListener {
            ctx.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, FragmentGroupCreation())
                .addToBackStack(null).commit()
//            holder.btn_learn_more.setOnClickListener {
//                val postDetailFragment = PostDetailFragment()
//                val args = Bundle()
//                args.putString("campaignId", campaignList?.get(position)?._id)
//                postDetailFragment.arguments = args
//                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()
//
//            }
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
//            val ll_item_list: LinearLayout
//
//            init {
//                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
//                tv_brand_name = v.findViewById(R.id.tv_brand_name)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
//            }
    }
}
