//package com.hm.mmmhmm.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.hm.mmmhmm.R
//import com.hm.mmmhmm.activity.MainActivity
//import com.hm.mmmhmm.fragments.SearchFragment
//import com.hm.mmmhmm.models.Item
//import kotlinx.android.synthetic.main.custom_toolbar.*
//
//
//class SearchSuggestionAdapter( private var suggestionList: List<Item>? = null) : RecyclerView.Adapter<SearchSuggestionAdapter.MyViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view: View =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_search_suggestion, parent, false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            holder.tv_username_search_suggestion.text= suggestionList?.get(position)?.name
////            holder.iv_profile_pic_profile.load(
////                campaignList?.get(position)?.brandLogo.toString(),
////                R.color.text_gray,
////                R.color.text_gray,
////                true
////            )
//            holder.itemView.setOnClickListener {
//                    (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
//                        .addToBackStack(null).commit()
//
//
//            }
//
//    }
//
//    override fun getItemCount(): Int {
//        return suggestionList?.size ?: 0
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
////            val iv_profile_pic_profile: ImageView
//            val tv_username_search_suggestion: TextView
////            val tv_detail: TextView
////            val tv_time_left: TextView
////            val tv_price: TextView
////            val btn_learn_more: Button
////            val ll_item_list: LinearLayout
//            init {
////                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
//        tv_username_search_suggestion = v.findViewById(R.id.tv_username_search_suggestion)
////                tv_detail = v.findViewById(R.id.tv_detail)
////                tv_time_left = v.findViewById(R.id.tv_time_left)
////                tv_price = v.findViewById(R.id.tv_price)
////                btn_learn_more = v.findViewById(R.id.btn_learn_more)
////                ll_item_list = v.findViewById(R.id.ll_item_list)
//            }
//    }
//}