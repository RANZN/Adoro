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
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.ShowAnnouncementRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupDetail : Fragment() {


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
        setupToolBar()
        var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
        getAnnouncementAPI(showAnnouncementRequest)
        tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
        tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_announcement.setOnClickListener {
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getAnnouncementAPI(showAnnouncementRequest)
//            recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity())
        }
        tv_post.setOnClickListener {
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
//            recycler_group_detail.adapter= FeedListAdapter()
            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getPostsAPI(showAnnouncementRequest)
        }
        tv_discussion.setOnClickListener {
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))

            var showAnnouncementRequest: ShowAnnouncementRequest = ShowAnnouncementRequest(requireArguments().getString("groupId")?:"");
            getGroupDiscussionAPI(showAnnouncementRequest)
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
                            recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity())


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
                            recycler_group_detail.adapter= FeedListAdapter(requireActivity())


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
                            recycler_group_detail.adapter= GroupDiscussionAdapter(requireActivity())


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