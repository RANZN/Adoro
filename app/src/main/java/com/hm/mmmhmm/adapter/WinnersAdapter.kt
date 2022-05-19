package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.HomeFragment
import com.hm.mmmhmm.fragments.ProfileFragment
import com.hm.mmmhmm.fragments.ResultDetailFragment
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.WinnerDetail


class WinnersAdapter(var ctx: FragmentActivity, private var winnersList: List<WinnerDetail>? = null) : RecyclerView.Adapter<WinnersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_winner_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_winner_name.text= winnersList?.get(position)?.name
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_time_left.text= "₹"+campaignList?.get(position)?.timeLeft.toString()+" left"
            holder.iv_winner_pic.load(
                winnersList?.get(position)?.profileImage.toString(),
                R.color.text_gray,
                R.color.text_gray,
                true
            )
//        holder.itemView.setOnClickListener {
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.frame_layout_main, ResultDetailFragment())
//                ?.addToBackStack(null)?.commit()
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "search")
                args.putString("userId", winnersList?.get(position)?._id ?: "")
                profileFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(null).commit()
            }})


       // }
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

    override fun getItemCount(): Int {
        return winnersList?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_winner_pic: ImageView
            val tv_winner_name: TextView
//            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
//            val btn_learn_more: Button
//            val ll_item_list: LinearLayout
//
            init {
    iv_winner_pic = v.findViewById(R.id.iv_winner_pic)
    tv_winner_name = v.findViewById(R.id.tv_winner_name)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
    }
}