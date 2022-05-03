package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.*
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.OTPRequest
import com.hm.mmmhmm.models.RequestAuthenticateNumber
import com.hm.mmmhmm.models.RequestLogin
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.String
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class LoginFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
     //   tv_toolbar_title?.setText("Adoro")
       // tv_toolbar_title?.setTextColor(resources.getColor(R.color.black))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        //GlobleData.goToLoginScreen = false


        tv_register.setOnClickListener(View.OnClickListener {
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_splash_launcher,RegisterNumberFragment())?.commit()
            }
        })
        btn_next_login.setOnClickListener(View.OnClickListener {
            validateInput()
        })


    }

    private fun stopFragment() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }


    private fun validateInput() {
        val number = et_email_login.text.toString()
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
                        if (response.body()?.OK?.length !=0) {
                            val r = response.body()

                           SessionManager.init(activity as Context)
                            //val rand = Random()
                            //SessionManager.setOTP(rand.nextInt(10000).toString())

                            var otpRequest: OTPRequest = OTPRequest(et_email_login.text.toString());
                            hitSendOTPAPI(otpRequest)



//                            val oTPVerifyFragment = OTPVerifyFragment()
//                            val args = Bundle()
//                            args.putString("path", "login")
//                            args.putString("number", et_email_login.text.toString())
//                            oTPVerifyFragment.arguments = args
//                            if (activity != null) {
//                                activity?.supportFragmentManager?.beginTransaction()
//                                    ?.replace(R.id.frame_layout_splash_launcher,oTPVerifyFragment)?.commit()
//                            }

                        } else {
                            Toast.makeText(activity,"User is not registered with us!", Toast.LENGTH_SHORT).show()

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
                            val r = response.body()
                            val oTPVerifyFragment = OTPVerifyFragment()
                            val args = Bundle()
                            args.putString("path", "login")
                            args.putString("number", et_email_login.text.toString())
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
