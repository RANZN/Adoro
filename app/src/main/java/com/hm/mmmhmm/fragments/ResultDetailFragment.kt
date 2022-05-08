package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.mmmhmm.Chat_Module.ChatActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.ResultAdapter
import com.hm.mmmhmm.adapter.WinnersAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.android.synthetic.main.fragment_result_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        var generalRequest: GeneralRequest = GeneralRequest(requireArguments().getString("resultId") ?: "");
        getResultDetailAPI(generalRequest)

    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.result)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
        //todo
            startActivity(Intent(activity as MainActivity, ChatActivity::class.java))

        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }

    private fun getResultDetailAPI(generalRequest: GeneralRequest) {
        pb_result_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSpecificResultData(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_result_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                           // recycler_results.adapter= ResultAdapter(requireActivity(),response.body()?.OK?.items )
                            recycler_winners.adapter= WinnersAdapter(requireActivity(),
                                response.body()?.OK?.items?.get(0)?.winnerDetails
                            )

                            tv_brand_name.text =response.body()?.OK?.items?.get(0)?.brandName
                            tv_type_title.text =response.body()?.OK?.items?.get(0)?.requriementType
                            iv_brand_pic.load(
                                response.body()?.OK?.items?.get(0)?.brandLogo,
                                R.color.text_gray,
                                R.color.text_gray,
                                true
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
}