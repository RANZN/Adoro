package com.hm.mmmhmm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_services.*


class GroupsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        recycler_groups.adapter= GroupsAdapter()
        tv_my_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
        tv_browse_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_my_groups.setOnClickListener {
            tv_my_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_browse_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            recycler_groups.adapter= GroupsAdapter()
        }
        tv_browse_groups.setOnClickListener {
            tv_browse_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_my_groups.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            recycler_groups.adapter= GroupsAdapter()
        }
//  (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()

    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }

    inner class GroupsAdapter() : RecyclerView.Adapter<GroupsAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_group_list, parent, false)
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
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_main, GroupDetail())
                    ?.addToBackStack(null)?.commit()



            }
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

}