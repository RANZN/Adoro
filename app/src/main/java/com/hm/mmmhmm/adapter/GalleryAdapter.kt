package com.hm.mmmhmm.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.CommentsFragment
import com.hm.mmmhmm.fragments.HomeFragment
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item

class GalleryAdapter(var ctx: FragmentActivity, private var postsList: List<Item>? = null) : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            holder.tv_brand_name.text= campaignList?.get(position)?.brandName
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
//            holder.tv_time_left.text= "₹"+campaignList?.get(position)?.timeLeft.toString()+" left"
            holder.iv_gallery.load(
                postsList?.get(position)?.image.toString(),
                R.color.text_gray,
                R.color.text_gray,
                false
            )
            holder.itemView.setOnClickListener {
                //todo
                val commentsFragment = CommentsFragment()
                val args = Bundle()
                args.putString("postId", postsList?.get(position)?._id)
                commentsFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, commentsFragment).addToBackStack(null)
                    .commit()

                (HomeFragment).lastFirstVisiblePosition = position

            }

        }

        override fun getItemCount(): Int {
            return postsList?.size?:0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_gallery: ImageView
//            val tv_brand_name: TextView
//            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
//            val btn_learn_more: Button
//            val ll_item_list: LinearLayout

            init {
                iv_gallery = v.findViewById(R.id.iv_gallery)
//                tv_brand_name = v.findViewById(R.id.tv_brand_name)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
        }
    }