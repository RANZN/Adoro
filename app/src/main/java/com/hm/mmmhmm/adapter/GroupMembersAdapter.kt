package com.hm.mmmhmm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.GroupMembers
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.MemberData


class GroupMembersAdapter(var ctx: GroupMembers, val memberData: ArrayList<MemberData>?, var groupId:String ) : RecyclerView.Adapter<GroupMembersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_members, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_username_group_member.text= memberData?.get(position)?.name
            holder.iv_user_group_member.load(
                memberData?.get(position)?.profile,
                R.color.text_gray,
                R.color.text_gray,
                true
            )
//            holder.itemView.setOnClickListener {
//                //todo
//            }
        holder.tv_remove.setOnClickListener {

            //ctx.getSpecificGroupData()
            }

    }

    override fun getItemCount(): Int {
        return return memberData?.size?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_user_group_member: ImageView
            val tv_username_group_member: TextView
            val tv_remove: TextView
//            val ll_item_list: LinearLayout
//
            init {
    iv_user_group_member = v.findViewById(R.id.iv_user_group_member)
    tv_username_group_member = v.findViewById(R.id.tv_username_group_member)
    tv_remove = v.findViewById(R.id.tv_remove)
            }
    }
}