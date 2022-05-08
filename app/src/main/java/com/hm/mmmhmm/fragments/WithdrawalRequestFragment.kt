package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.ProfileRequest
import com.hm.mmmhmm.models.RequestAuthenticateNumber
import com.hm.mmmhmm.models.RequestWithdrawalMoney
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.btn_verify
import kotlinx.android.synthetic.main.fragment_withdrawal_request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class WithdrawalRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdrawal_request, container, false)
    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.withdraw_money)
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
    private fun validateInput() {
        val withdrawMoney = et_withdraw_money.text.toString()
        if (withdrawMoney.isNullOrEmpty()) {
            toast(R.string.enter_phone_number,1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {

            var requestRegisterNumber: RequestWithdrawalMoney = RequestWithdrawalMoney(
               "1234567890" ,
                withdrawMoney.toInt() ,
               "Kotak Mahindra Bank" ,
               "HDD011D" ,
                SessionManager.getUserName()?:"" ,
               "HDD011D" ,
               "Pending" ,
                SessionManager.getUserId()?:"",
               SessionManager.getUsername()?:"" ,
            )
            sendWithdrawalRequest(requestRegisterNumber)

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        btn_verify.setOnClickListener{
            val oTPVerifyFragment = OTPVerifyFragment()
            val args = Bundle()
            args.putString("path", "withdraw")
            oTPVerifyFragment.arguments = args
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_main,oTPVerifyFragment)?.commit()
            }
        }
        tv_total_coins.text= SessionManager.getAdoroCoins()+" A"

        btn_verify.setOnClickListener {
            validateInput()
        }
    }
    private fun sendWithdrawalRequest( generalRequest: RequestWithdrawalMoney) {
        pb_withdrawal.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.sendWithdrawalRequest(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_withdrawal.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                            Toast.makeText(activity,"Withdraw request sent to admin!", Toast.LENGTH_SHORT).show()
                            stopFragment()
                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopFragment() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}