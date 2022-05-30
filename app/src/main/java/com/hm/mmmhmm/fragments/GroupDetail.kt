package com.hm.mmmhmm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.adapter.GroupAnnouncementAdapter
import com.hm.mmmhmm.adapter.GroupDiscussionAdapter
import com.hm.mmmhmm.adapter.GroupFeedListAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.GetSpecificGroupDataRequest
import com.hm.mmmhmm.models.MemberData
import com.hm.mmmhmm.models.ShowAnnouncementRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupDetail : Fragment() {

    var itemType:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_announcement.setBackgroundResource( R.drawable.bg_buttun_gradient )
        tv_discussion.setBackgroundResource( R.drawable.bg_unselect )
        tv_post.setBackgroundResource( R.drawable.bg_unselect )
        tv_announcement.setTextColor(resources.getColor(R.color.white))
        tv_discussion.setTextColor(resources.getColor(R.color.black))
        tv_post.setTextColor(resources.getColor(R.color.black))
        itemType=0
        setupToolBar()

        var getSpecificGroupDataRequest: GetSpecificGroupDataRequest = GetSpecificGroupDataRequest(requireArguments().getString("groupId")?:"");
        getSpecificGroupData(getSpecificGroupDataRequest)

        tv_announcement.setOnClickListener {
            tv_announcement.setBackgroundResource( R.drawable.bg_buttun_gradient )
            tv_discussion.setBackgroundResource( R.drawable.bg_unselect )
            tv_post.setBackgroundResource( R.drawable.bg_unselect )
            tv_announcement.setTextColor(resources.getColor(R.color.white))
            tv_discussion.setTextColor(resources.getColor(R.color.black))
            tv_post.setTextColor(resources.getColor(R.color.black))
            itemType=0
            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getAnnouncementAPI(showAnnouncementRequest)
//            recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity())
        }
        tv_post.setOnClickListener {
            tv_announcement.setBackgroundResource( R.drawable.bg_unselect )
            tv_discussion.setBackgroundResource( R.drawable.bg_unselect )
            tv_post.setBackgroundResource( R.drawable.bg_buttun_gradient )
            tv_announcement.setTextColor(resources.getColor(R.color.black))
            tv_discussion.setTextColor(resources.getColor(R.color.black))
            tv_post.setTextColor(resources.getColor(R.color.white))
            itemType=1
//            recycler_group_detail.adapter= FeedListAdapter()
            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getPostsAPI(showAnnouncementRequest)
        }
        tv_discussion.setOnClickListener {
            tv_announcement.setBackgroundResource( R.drawable.bg_unselect )
            tv_discussion.setBackgroundResource( R.drawable.bg_buttun_gradient )
            tv_post.setBackgroundResource( R.drawable.bg_unselect )
            tv_announcement.setTextColor(resources.getColor(R.color.black))
            tv_discussion.setTextColor(resources.getColor(R.color.white))
            tv_post.setTextColor(resources.getColor(R.color.black))
            itemType=2
            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getGroupDiscussionAPI(showAnnouncementRequest)
        }
//  (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()

    }

    private fun setupToolBar() {
        iv_toolbar_action_add.setBackgroundResource(R.drawable.add_post)
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_action_members.setBackgroundResource(R.drawable.ic_group)
        iv_toolbar_action_edit.setBackgroundResource(R.drawable.ic_edit_group)



        iv_toolbar_action_add.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_add.visibility=View.VISIBLE

        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))

        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_members.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_edit.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })

        iv_toolbar_action_members.setOnClickListener {
            val groupMembers = GroupMembers()
            val args = Bundle()
            args.putString("groupId", requireArguments().getString("groupId")?:"")
            groupMembers.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, groupMembers).addToBackStack(null)
                .commit()
        }
        iv_toolbar_action_edit.setOnClickListener(View.OnClickListener {
            val editGroup = EditGroup()
            val args = android.os.Bundle()
            args.putString("groupId", requireArguments().getString("groupId")?:"")
            editGroup.arguments = args
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout_main, editGroup)
                ?.addToBackStack(null)?.commit()
        })

        iv_toolbar_action_add.setOnClickListener(View.OnClickListener {
            val editGroup = GroupPost()
            val args = android.os.Bundle()
            args.putString("groupId", requireArguments().getString("groupId")?:"")
            args.putInt("postType", itemType)
            editGroup.arguments = args
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout_main, editGroup)
                ?.addToBackStack(null)?.commit()
        })


    }
    private fun getSpecificGroupData(getSpecificGroupDataRequest: GetSpecificGroupDataRequest) {
        pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSpecificGroupData(getSpecificGroupDataRequest)
                withContext(Dispatchers.Main) {
                    pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            if(response.body()?.OK?.items?.get(0)?.ownerId==SessionManager.getUserId()){
                                iv_toolbar_action_members.setColorFilter(resources.getColor(R.color.black));
                                iv_toolbar_action_members.visibility=View.VISIBLE
                                iv_toolbar_action_edit.setColorFilter(resources.getColor(R.color.black));
                                iv_toolbar_action_edit.visibility=View.VISIBLE
                            }
                            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
                            getAnnouncementAPI(showAnnouncementRequest)
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

    private fun getAnnouncementAPI(showAnnouncementRequest: ShowAnnouncementRequest) {
        pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showAnnouncement(showAnnouncementRequest)
                withContext(Dispatchers.Main) {
                    pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
//                            feedList = response.body()?.OK?.items
                            recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity(), response.body()?.OK?.items)


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

    private fun getPostsAPI(showAnnouncementRequest: ShowAnnouncementRequest) {
        pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showGroupPost(showAnnouncementRequest)
                withContext(Dispatchers.Main) {
                    pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
//                            feedList = response.body()?.OK?.items
                            recycler_group_detail.adapter= GroupFeedListAdapter(requireActivity(), response.body()?.OK?.items)


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

    private fun getGroupDiscussionAPI(showAnnouncementRequest: ShowAnnouncementRequest) {
        pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showGroupDiscussion(showAnnouncementRequest)
                withContext(Dispatchers.Main) {
                    pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
//                            feedList = response.body()?.OK?.items
                            recycler_group_detail.adapter= GroupDiscussionAdapter(requireActivity(),response.body()?.OK?.items)


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