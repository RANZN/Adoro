package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.AdoroCoinsAdapter
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.RequestAuthenticateNumber
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_adoro_coins.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class AdoroCoinsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adoro_coins, container, false)
    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.adoro_coins)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, InboxActivity::class.java))
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        btn_withdrawal_coins.setOnClickListener{
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, WithdrawalRequestFragment())
                .addToBackStack(null).commit()
        }
        var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
        hitShowAdoroAPI(generalRequest)

    }

    private fun hitShowAdoroAPI( generalRequest: GeneralRequest) {
        pb_adoro_coins.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showAdoro(generalRequest)
                withContext(Dispatchers.Main) {

                    try {
                        pb_adoro_coins.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                           // tv_coins.text = r?.OK?.amount+" C"
                            SessionManager.setAdoroCoins(r?.OK?.amount ?: "")
                            hitShowTrancationsAPI(generalRequest)
                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()

//                            CommanFunction.handleApiError(
//                                response.errorBody()?.charStream(),
//                                requireContext()
//                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {

            }
        }
    }

    private fun hitShowTrancationsAPI( generalRequest: GeneralRequest) {
        pb_adoro_coins.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showTrancations(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_adoro_coins.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                            recycler_adoro_coins.adapter= AdoroCoinsAdapter(r?.OK?.trancations)
                          //  tv_total_earing.text =  R.string.total_coins_earned + r?.OK?.amount.toString()
                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}