package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.OTPRequest
import com.hm.mmmhmm.models.RequestLogin
import com.hm.mmmhmm.models.RequestWithdrawalMoney
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.android.synthetic.main.fragment_withdrawal_request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OTPVerifyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_o_t_p_verify, container, false)
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
        tv_toolbar_title?.text = resources.getString(R.string.app_name)
        tv_toolbar_title?.setTextColor(resources.getColor(R.color.black))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        //GlobleData.goToLoginScreen = false
        if (requireArguments().getString("path") == "register") {
            //tv_signup_terms.visibility = View.VISIBLE
            tv_resend_otp.visibility = View.VISIBLE
            ll_signup.visibility = View.VISIBLE
            ll_login.visibility = View.GONE
            tv_resend_otp.setTextColor(resources.getColor(R.color.black))
            iv_logo_otp.background = resources.getDrawable(R.drawable.logo_signup)
            btn_verify.background = resources.getDrawable(R.drawable.bg_buttun_gradient)
            btn_verify.setTextColor(resources.getColor(R.color.white))
            view_line.setBackgroundColor(Color.parseColor("#ffffff"))

        }
        if ( requireArguments().getString("path") == "login") {
            //tv_signup_terms.visibility = View.VISIBLE
            tv_resend_otp.visibility = View.VISIBLE
            ll_login.visibility = View.VISIBLE
            ll_signup.visibility = View.GONE
            tv_resend_otp.setTextColor(resources.getColor(R.color.white))
            iv_logo_otp.background = resources.getDrawable(R.drawable.login_screen_logo)
            view_line.setBackgroundColor(Color.parseColor("#000000"))
            btn_verify.background = resources.getDrawable(R.drawable.bg_button_white)
            btn_verify.setTextColor(resources.getColor(R.color.black))

        }
        tv_resend_otp.setOnClickListener(View.OnClickListener {
            var otpRequest: OTPRequest =
                OTPRequest(requireArguments().getString("number").toString())
            hitSendOTPAPI(otpRequest)
        })



        btn_verify.setOnClickListener(View.OnClickListener {
            validateInput()
        })


    }

    private fun validateInput() {
        var otp = et_otp.text.toString()
        if (otp.isNullOrEmpty()) {
            toast(R.string.enter_otp, 1)
            return
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            if (requireArguments().getString("path") == "register" && requireArguments().getString("number") != null) {
                val signupFragment = SignupFragment()
                val args = android.os.Bundle()
                args.putString("number", requireArguments().getString("number"))
                signupFragment.arguments = args
                if (activity != null) {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(com.hm.mmmhmm.R.id.frame_layout_splash_launcher, signupFragment)
                        ?.commit()
                }

            } else if (requireArguments().getString("path") == "withdraw") {
                if (otp == SessionManager.getOTP()) {
                    var requestRegisterNumber: RequestWithdrawalMoney = RequestWithdrawalMoney(
                        SessionManager.getAccountNumber()?:"" ,
                        requireArguments().getInt("withdrawMoney") ,
                        SessionManager.getBank()?:"" ,
                        SessionManager.getIFSC()?:"",
                        SessionManager.getUserName()?:"" ,
                        SessionManager.getPanNumber()?:"" ,
                        "Pending" ,
                        SessionManager.getUserId()?:"",
                        SessionManager.getUsername()?:"" ,
                    )
                    sendWithdrawalRequest(requestRegisterNumber)
                }else{
                    Toast.makeText(
                        activity,
                        "Invalid OTP",
                        Toast.LENGTH_SHORT
                    ).show()
                }




            } else {
                //check otp if match call below api
                if (otp == SessionManager.getOTP()) {
                    var loginRequest: RequestLogin =
                        RequestLogin(
                            requireArguments().getString("number")!!.toLong(),
                            SessionManager.getFCMToken() ?: ""
                        )
                    hitLoginAPI(loginRequest)
                } else {
                    Toast.makeText(
                        activity,
                        "Wrong OTP!",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }


    }

    private fun showDialog() {
        val dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
//        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        val body = dialog.findViewById(R.id.tvBody) as TextView
        //body.text = title
        body.text = "Withdrawal request sent!"
        val title = dialog.findViewById(R.id.tvTitle) as TextView
        // body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_ok) as Button
        yesBtn.setOnClickListener {
            //todo
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_main, HomeFragment())?.commit()
            }
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun hitLoginAPI(loginRequest: RequestLogin) {
        pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.loginUser(loginRequest)
                withContext(Dispatchers.Main) {

                    try {
                        pb_login.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            Toast.makeText(
                                activity,
                                "Hi " + response.body()?.OK!!.items[0].name + ", Welcome to "+getString(R.string.app_name)+"!",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val r = response.body()
                            SessionManager.init(activity as Context)
                            SessionManager.setLoginStatus("true")
                            SessionManager.setUserId(response.body()?.OK?.items?.get(0)?._id ?: "")
                            SessionManager.setUsername(
                                response.body()?.OK?.items?.get(0)?.username ?: ""
                            )
                            SessionManager.setUserName(
                                response.body()?.OK?.items?.get(0)?.name ?: ""
                            )
                            SessionManager.setUserPic(
                                response.body()?.OK?.items?.get(0)?.profile ?: ""
                            )
                            SessionManager.setUserEmail(
                                response.body()?.OK?.items?.get(0)?.email ?: ""
                            )
                            SessionManager.setUserPhone(
                                response.body()?.OK?.items?.get(0)?.number.toString()
                            )

                            SessionManager.setRefrerCode(
                                response.body()?.OK?.items?.get(0)?.referCode ?: ""
                            )

                            SessionManager.setTotalFollowers(
                                response.body()?.OK?.items?.get(0)?.followerData?.size?: 0
                            )

                            SessionManager.setAdoroCoins(
                                response.body()?.OK?.items?.get(0)?.adoroCoins?: 0
                            )
                            SessionManager.setAdoroShield(
                                response.body()?.OK?.items?.get(0)?.adoroShield?: 0
                            )
                            startActivity(Intent(activity, MainActivity::class.java))

                            loginUserForFirebase()

                            activity?.finish()


                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun pushUserToFirebase() {
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.get().addOnSuccessListener {
            try {
                val value = it.value as HashMap<String?, Any>
                value.apply {
                    put(SessionManager.getFirebaseID(), HashMap<String, Any?>().apply {
                        put("userId", SessionManager.getFirebaseID())
                        put("userName", SessionManager.getUserName())
                        put("email", SessionManager.getUserEmail())
                        put("isOnline", true)
                    })
                }
                reference.updateChildren(value)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loginUserForFirebase() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            SessionManager.getUserEmail(),
            "test@123"
        ).addOnSuccessListener {
            SessionManager.setFirebaseID(it.user?.uid)
            FirebaseAuth.getInstance().signOut()
            pushUserToFirebase()
        }.addOnFailureListener {
            FirebaseAuth.getInstance().signOut()
            if (it is FirebaseAuthUserCollisionException) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    SessionManager.getUserEmail(),
                    "test@123"
                ).addOnSuccessListener { result ->
                    SessionManager.setFirebaseID(result.user?.uid)
                    FirebaseAuth.getInstance().signOut()
                    pushUserToFirebase()
                }
            }
        }
    }

    private fun hitSendOTPAPI(otpRequest: OTPRequest) {
        pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.sendOTP(otpRequest)
                withContext(Dispatchers.Main) {

                    try {
                        pb_login.visibility = View.GONE
                        if (response.body()?.OK?.status == "Sucess") {
                            SessionManager.setOTP(response.body()?.OK?.otp ?: "")
                            Toast.makeText(activity, R.string.otp_sent_please_verify, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(activity, "Somethings went wrong!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: java.lang.Exception) {

            }
        }
    }

    private fun sendWithdrawalRequest( generalRequest: RequestWithdrawalMoney) {
        pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.sendWithdrawalRequest(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_login.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                            showDialog()

                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}