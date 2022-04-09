package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.ProfileFragment
import com.hm.mmmhmm.fragments.ResultDetailFragment
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item

class ResultAdapter(var ctx: FragmentActivity,private var resultList: List<Item>? = null) : RecyclerView.Adapter<ResultAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_result_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
           holder.tv_group_name.text= resultList?.get(position)?.brandName
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
            holder.tv_detail.text= resultList?.get(position)?.shortDescription
            holder.tv_requirement_type.text= resultList?.get(position)?.requriementType
//            holder.tv_time_left.text= "₹"+campaignList?.get(position)?.timeLeft.toString()+" left"
            holder.iv_group_pic.load(
                resultList?.get(position)?.brandLogo.toString(),
                R.color.text_gray,
                R.color.text_gray,
                true
            )
        holder.btn_see_result.setOnClickListener {
            val resultDetailFragment = ResultDetailFragment()
            val args = Bundle()
            args.putString("resultId", resultList?.get(position)?._id)
            resultDetailFragment.arguments = args
            ctx.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main,resultDetailFragment)
                .addToBackStack(null).commit()



        }

    }

    override fun getItemCount(): Int {
        return resultList?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
                    val iv_group_pic: ImageView
            val tv_group_name: TextView
            val tv_detail: TextView
//            val tv_time_left: TextView
            val tv_requirement_type: TextView
        val btn_see_result: Button
        //            val ll_item_list: LinearLayout
//
        init {
            iv_group_pic = v.findViewById(R.id.iv_group_pic)
            tv_group_name = v.findViewById(R.id.tv_group_name)
                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
                tv_requirement_type = v.findViewById(R.id.tv_requirement_type)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
            btn_see_result = v.findViewById(R.id.btn_see_result)
        }
    }
}