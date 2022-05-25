package com.hm.mmmhmm.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.GroupMembers
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.fragment_group_members.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroupRequestsAdapter(var ctx: GroupMembers, val memberData: ArrayList<MemberData>?, var groupId:String) :
    RecyclerView.Adapter<GroupRequestsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_requests, parent, false)
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
//
//            }

        holder.btn_accept_request.setOnClickListener {
            var memberData: MemberData =
                MemberData(memberData?.get(position)?.name,memberData?.get(position)?.profile,memberData?.get(position)?.userID,memberData?.get(position)?.username,)

          var acceptGroupRequest: AcceptGroupRequest=AcceptGroupRequest(groupId, memberData)
            acceptMemberJoinGroupRequest(acceptGroupRequest)
        }

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

        //            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
        val btn_accept_request: TextView
        val tv_remove: TextView

        //            val ll_item_list: LinearLayout
//
        init {
            iv_user_group_member = v.findViewById(R.id.iv_user_group_member)
            tv_username_group_member = v.findViewById(R.id.tv_username_group_member)
            btn_accept_request = v.findViewById(R.id.btn_accept_request)
            tv_remove = v.findViewById(R.id.tv_remove)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
        }
    }

    fun acceptMemberJoinGroupRequest(acceptGroupRequest: AcceptGroupRequest) {
        //pb_members.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(ctx.requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.acceptMemberJoinGroupRequest(acceptGroupRequest)
                withContext(Dispatchers.Main) {
                  //  pb_members.visibility = View.GONE

                    try {
                        if (response != null && response.body()?.OK?.status=="Success") {
                            var getSpecificGroupDataRequest: GetSpecificGroupDataRequest =
                                GetSpecificGroupDataRequest(groupId)
                            ctx.getSpecificGroupData(getSpecificGroupDataRequest)

                            //Toast.makeText(ctx.requireActivity(), "", Toast.LENGTH_SHORT).show()

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

    fun deleteFromMember(deleteMemberRequest: DeleteMemberRequest) {
        //pb_members.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(ctx.requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.deleteRequestedMemberData(deleteMemberRequest)
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