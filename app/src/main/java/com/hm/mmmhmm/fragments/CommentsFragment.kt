package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.CommentsAdapter
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.adapter.NotificationsAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_publish_meme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class CommentsFragment : Fragment() {
    private var feedList: List<ItemComment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
        getFeedListAPI(generalRequest)

        send_message_button.setOnClickListener {
            validateInput()

        }
    }
    private fun validateInput() {
        val comment = et_comment.text.toString()
        if (comment.isNullOrEmpty()) {
            toast(R.string.comment, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var commentData: CommentData = CommentData(
                getCurrentDate(),
                SessionManager.getUserId() ?: "",
                SessionManager.getUserPic() ?: "",
                comment,
                SessionManager.getUsername() ?: ""
            )
            var postCommentRequest: PostCommentRequest = PostCommentRequest(
                requireArguments().getString("postId") ?: "",
                commentData
            )
            postComment(postCommentRequest)

        }
    }
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, Inbox::class.java))
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }


    private fun getFeedListAPI(generalRequest: GeneralRequest) {
        pb_comments.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getFeed(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_comments.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            feedList = response.body()?.OK?.items
                            for(Item in feedList!!){
                                if(requireArguments().getString("postId")==Item._id){
                                    recycler_comments.adapter= CommentsAdapter(
                                        requireActivity(),
                                        Item.comment)
                                }
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

    private fun postComment(generalRequest: PostCommentRequest) {
        pb_comments.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateComment(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_comments.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
                            getFeedListAPI(generalRequest)
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