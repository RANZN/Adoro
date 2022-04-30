package com.hm.mmmhmm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GroupRequestsAdapter
import com.hm.mmmhmm.adapter.NotificationsAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_group_joining_requests.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroupJoiningRequests : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_joining_requests, container, false)
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

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupToolBar()
            iv_back.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
            recycler_group_request.adapter= GroupRequestsAdapter(requireActivity())



//            var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId()?:"");
//            getNotificationListAPI(generalRequest)

        }

        private fun getNotificationListAPI(generalRequest: GeneralRequest) {
            pb_notifications.visibility = View.VISIBLE
            val apiInterface = ApiClient.getRetrofitService(requireContext())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiInterface.showNotification(generalRequest)
                    withContext(Dispatchers.Main) {
                        pb_notifications.visibility = View.GONE

                        try {
                            //  toast("" + response.body()?.message)
                            if (response!=null) {
                                recycler_notifications.adapter= NotificationsAdapter(requireActivity(), response.body()?.OK?.items)

                            } else {
                                Log.d("resp", "complet else: ")
                            }

                        } catch (e: Exception) {
                            Log.d("resp", "cathch: " + e.toString())
                        }
                    }

                } catch (e: Exception) {
                    pb_notifications.visibility = View.GONE
                    Log.d("weweewefw", e.toString())
                }
            }

        }



}