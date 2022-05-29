package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.ScrollView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.CommentsAdapter
import com.hm.mmmhmm.adapter.MutualLikerAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_comments.iv_feed
import kotlinx.android.synthetic.main.fragment_edit_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class EditPost : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()


        Log.d("ghghgh", requireArguments().getString("postId") ?: "");
        var generalRequest: GeneralRequest =
            GeneralRequest(requireArguments().getString("postId") ?: "");
        getSpecificPostDetail(generalRequest)


    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)

        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black))

        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {

            validateInput()

        })
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }

    private fun validateInput() {
        val description = et_feed_description.text.toString()
       if (ConnectivityObserver.isOnline(activity as Context)) {
            var generalRequest: UpdateGroupRequest =
                UpdateGroupRequest(requireArguments().getString("postId") ?: "", description);
            updatePost(generalRequest)

        }
    }

    private fun getSpecificPostDetail(generalRequest: GeneralRequest) {
        pb_edit_post.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSpecificPostDetail(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_edit_post.visibility = View.GONE
                    onResume()
                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null) {
                            var data = response.body()?.OK?.items?.get(0)
                                iv_toolbar_action_inbox.setBackgroundResource(R.drawable.save_file)
                                //iv_toolbar_action_edit.setColorFilter(resources.getColor(R.color.black))

                            et_feed_description.text =
                                Editable.Factory.getInstance().newEditable(data?.description)
                            et_feed_description.setSelection(et_feed_description.length())

                            iv_feed_edit.load(
                                data?.image,
                                R.color.text_gray,
                                R.color.text_gray,
                                false
                            )

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


    private fun updatePost(generalRequest: UpdateGroupRequest) {
        pb_edit_post.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateDescription(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_edit_post.visibility = View.GONE
                    onResume()
                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null&& response.body()?.OK?.status =="Success") {
                            (activity as MainActivity).onBackPressed()
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

    }
}