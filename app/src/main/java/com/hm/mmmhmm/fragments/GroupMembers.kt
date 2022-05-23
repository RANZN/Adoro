package com.hm.mmmhmm.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GroupMembersAdapter
import com.hm.mmmhmm.adapter.GroupRequestsAdapter
import com.hm.mmmhmm.models.GetSpecificGroupDataRequest
import com.hm.mmmhmm.models.MemberData
import com.hm.mmmhmm.models.ShowAnnouncementRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_edit_group.*
import kotlinx.android.synthetic.main.fragment_group_creation.*
import kotlinx.android.synthetic.main.fragment_group_creation.btn_create_group
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.fragment_group_joining_requests.*
import kotlinx.android.synthetic.main.fragment_group_joining_requests.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_group_members.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupMembers : Fragment() {

    var type:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_members, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        groupId=requireArguments().getString("groupId") ?: ""
        var getSpecificGroupDataRequest: GetSpecificGroupDataRequest =
            GetSpecificGroupDataRequest(requireArguments().getString("groupId") ?: "");
        getSpecificGroupData(getSpecificGroupDataRequest)

    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.create_group)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })
//        val memberData: ArrayList<MemberData> = requireArguments().getSerializable("members") as ArrayList<MemberData>
//        recycler_group_members.adapter= GroupRequestsAdapter(requireActivity(),memberData)

        tv_existing.setBackgroundResource(R.drawable.bg_buttun_gradient)
        tv_requested.setBackgroundResource(R.drawable.bg_unselect)
        tv_existing.setTextColor(resources.getColor(R.color.white))
        tv_requested.setTextColor(resources.getColor(R.color.black))

        tv_existing.setOnClickListener {
            tv_existing.setBackgroundResource(R.drawable.bg_buttun_gradient)
            tv_requested.setBackgroundResource(R.drawable.bg_unselect)
            tv_existing.setTextColor(resources.getColor(R.color.white))
            tv_requested.setTextColor(resources.getColor(R.color.black))
            type=0
            var getSpecificGroupDataRequest: GetSpecificGroupDataRequest =
                GetSpecificGroupDataRequest(requireArguments().getString("groupId") ?: "");
            getSpecificGroupData(getSpecificGroupDataRequest)
        }

        tv_requested.setOnClickListener {
            type=1
            tv_requested.setBackgroundResource(R.drawable.bg_buttun_gradient)
            tv_existing.setBackgroundResource(R.drawable.bg_unselect)
            tv_requested.setTextColor(resources.getColor(R.color.white))
            tv_existing.setTextColor(resources.getColor(R.color.black))
            var getSpecificGroupDataRequest: GetSpecificGroupDataRequest =
                GetSpecificGroupDataRequest(requireArguments().getString("groupId") ?: "");
            getSpecificGroupData(getSpecificGroupDataRequest)
        }
    }

     fun getSpecificGroupData(getSpecificGroupDataRequest: GetSpecificGroupDataRequest) {
        pb_members.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSpecificGroupData(getSpecificGroupDataRequest)
                withContext(Dispatchers.Main) {
                    pb_members.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null) {

                            if (type==0){

                                recycler_group_members.adapter = GroupMembersAdapter(
                                    this@GroupMembers,
                                    response.body()?.OK?.items?.get(0)?.memberData,
                                    requireArguments().getString("groupId") ?: ""
                                )
                            }else{
                                recycler_group_members.adapter = GroupRequestsAdapter(
                                    this@GroupMembers,
                                    response.body()?.OK?.items?.get(0)?.requestedMemberData,
                                    requireArguments().getString("groupId") ?: ""
                                )
                            }


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

    companion object {
        var groupId:String=""
    }
}