package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.pb_login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
        if (requireArguments().getString("path") == "register"||requireArguments().getString("path")=="login") {
            tv_signup_terms.visibility = View.VISIBLE
            tv_resend_otp.visibility=View.VISIBLE
        }
        tv_resend_otp.setOnClickListener(View.OnClickListener {
                      var otpRequest: OTPRequest = OTPRequest(requireArguments().getString("number").toString());
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
                showDialog()

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
                                "Hi " + response.body()?.OK!!.items[0].name + ", Welcome to "+R.string.app_name+"!",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val r = response.body()
                            SessionManager.init(activity as Context)
                            SessionManager.setLoginStatus("true")
                            SessionManager.setUserId(response.body()?.OK!!.items[0]._id)
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
    private fun hitSendOTPAPI( otpRequest: OTPRequest) {
        pb_login.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.sendOTP(otpRequest)
                withContext(Dispatchers.Main) {

                    try {
                        pb_login.visibility = View.GONE
                        if (response.body()?.OK?.status =="Sucess") {
                            SessionManager.setOTP(response.body()?.OK?.otp?:"")
                            val r = response.body()
                            val oTPVerifyFragment = OTPVerifyFragment()
                            val args = Bundle()
                            args.putString("path", "login")
                            args.putString("number", requireArguments().getString("number"))
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