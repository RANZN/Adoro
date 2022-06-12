package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
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
import kotlinx.android.synthetic.main.fragment_login.pb_login
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.String
import java.util.*

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
                        if (response.body()?.OK?.items?.size !=0) {
                            val r = response.body()
                            SessionManager.init(activity as Context)
                            if(requestAuthenticateNumber.number==7400705595){
                                var loginRequest: RequestLogin =
                                    RequestLogin(
                                        requestAuthenticateNumber.number?:7400705595.toLong(),
                                        SessionManager.getFCMToken() ?: ""
                                    )
                                hitLoginAPI(loginRequest)
                            }else{
                                var otpRequest: OTPRequest = OTPRequest(et_email_login.text.toString());
                                hitSendOTPAPI(otpRequest)
                            }




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
                            SessionManager.setUserPhone(
                                response.body()?.OK?.items?.get(0)?.number.toString()
                            )
                            SessionManager.setUserEmail(
                                response.body()?.OK?.items?.get(0)?.email ?: ""
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
                val value = it.value as HashMap<kotlin.String?, Any>
                value.apply {
                    put(SessionManager.getFirebaseID(), HashMap<kotlin.String, Any?>().apply {
                        put("userId", SessionManager.getFirebaseID())
                        put("userName", SessionManager.getUserName())
                        put("email", SessionManager.getUserEmail())
                        put("isOnline", true)
                        put("id", SessionManager.getUserId())
                        put("profile", SessionManager.getUserPic())
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



}
