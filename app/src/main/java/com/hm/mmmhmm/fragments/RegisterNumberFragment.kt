package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.OTPRequest
import com.hm.mmmhmm.models.RequestAuthenticateNumber
import com.hm.mmmhmm.models.RequestLogin
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.android.synthetic.main.fragment_register_number.*
import kotlinx.android.synthetic.main.fragment_register_number.pb_login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class RegisterNumberFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_number, container, false)
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
        tv_toolbar_title?.setText(resources.getString(R.string.app_name))
        tv_toolbar_title?.setTextColor(resources.getColor(R.color.black))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        //GlobleData.goToLoginScreen = false

        btn_next_signup.setOnClickListener(View.OnClickListener {
            validateInput()
        })
        tv_login.setOnClickListener(View.OnClickListener {
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_splash_launcher, LoginFragment())?.commit()
            }
        })

    }


    private fun validateInput() {
        val number = et_register_number.text.toString()
        if (number.isNullOrEmpty()) {
            toast(R.string.enter_phone_number,1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var requestRegisterNumber: RequestAuthenticateNumber = RequestAuthenticateNumber(number.toLong());
            hitAuthenticationAPI(requestRegisterNumber)

        }
    }

    private fun hitAuthenticationAPI( requestAuthenticateNumber: RequestAuthenticateNumber) {
        pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.registerNumber(requestAuthenticateNumber)
                withContext(Dispatchers.Main) {
                    try {
                        pb_login.visibility = View.GONE
                        if (response.body()?.OK?.length ==0) {
                            val r = response.body()
                            var otpRequest: OTPRequest = OTPRequest(et_register_number.text.toString());
                            hitSendOTPAPI(otpRequest)
                           // SessionManager.init(activity as Context)
//                            val rand = Random()
//                            SessionManager.setOTP(rand.nextInt(10000).toString())

                            //call send otp api here

//                            val oTPVerifyFragment = OTPVerifyFragment()
//                            val args = Bundle()
//                            args.putString("path", "register")
//                            args.putString("number", et_register_number.text.toString())
//                            oTPVerifyFragment.arguments = args
//                            if (activity != null) {
//                                activity?.supportFragmentManager?.beginTransaction()
//                                    ?.replace(R.id.frame_layout_splash_launcher,oTPVerifyFragment)?.commit()
//                            }


                        } else {
                            Toast.makeText(activity,R.string.user_already_registered, Toast.LENGTH_SHORT).show()

//                            CommanFunction.handleApiError(
//                                response.errorBody()?.charStream(),
//                                requireContext()
//                            )
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {

            }
        }
    }

    private fun hitSendOTPAPI( otpRequest: OTPRequest) {
        //  pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.sendOTP(otpRequest)
                withContext(Dispatchers.Main) {

                    try {
                        // pb_login.visibility = View.GONE
                        if (response.body()?.OK?.status =="Sucess") {
                            SessionManager.setOTP(response.body()?.OK?.otp?:"")
                            val oTPVerifyFragment = OTPVerifyFragment()
                            val args = Bundle()
                            args.putString("path", "register")
                            args.putString("number", et_register_number.text.toString())
                            oTPVerifyFragment.arguments = args
                            if (activity != null) {
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.replace(R.id.frame_layout_splash_launcher,oTPVerifyFragment)?.commit()
                            }

                        } else {
                            Toast.makeText(activity,"Somethings wents wrongs!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {

            }
        }
    }

}