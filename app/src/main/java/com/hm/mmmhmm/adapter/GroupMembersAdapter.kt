package com.hm.mmmhmm.adapter

import android.util.Log
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
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroupMembersAdapter(
    var ctx: GroupMembers,
    val memberData: ArrayList<MemberData>?,
    var groupId: String
) : RecyclerView.Adapter<GroupMembersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_members, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_username_group_member.text = memberData?.get(position)?.name
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
            val deleteMemberRequest: DeleteMemberRequest =
                DeleteMemberRequest(groupId, SessionManager.getUserId() ?: "")
            deleteFromMember(deleteMemberRequest)
        }

    }

    override fun getItemCount(): Int {
        return return memberData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_user_group_member: ImageView
        val tv_username_group_member: TextView
        val tv_remove: TextView

        init {
            iv_user_group_member = v.findViewById(R.id.iv_user_group_member)
            tv_username_group_member = v.findViewById(R.id.tv_username_group_member)
            tv_remove = v.findViewById(R.id.tv_remove)
        }
    }

    fun deleteFromMember(deleteMemberRequest: DeleteMemberRequest) {
        //pb_members.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(ctx.requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.deleteMemberData(deleteMemberRequest)
                withContext(Dispatchers.Main) {
                    //  pb_members.visibility = View.GONE

                    try {
                        if (response != null && response.body()?.OK?.status == "Success") {
                            var getSpecificGroupDataRequest: GetSpecificGroupDataRequest =
                                GetSpecificGroupDataRequest(groupId)
                            ctx.getSpecificGroupData(getSpecificGroupDataRequest)

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