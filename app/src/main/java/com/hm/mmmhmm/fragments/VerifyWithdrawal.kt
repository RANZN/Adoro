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
import kotlinx.android.synthetic.main.fragment_o_t_p_verify.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyWithdrawal.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyWithdrawal : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_withdrawal, container, false)
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
//        tv_resend_otp.setOnClickListener(View.OnClickListener {
//            var otpRequest: OTPRequest =
//                OTPRequest(requireArguments().getString("number").toString())
//            hitSendOTPAPI(otpRequest)
//        })
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


            Toast.makeText(activity,"Withdraw request sent to admin!", Toast.LENGTH_SHORT).show()
stopFragment()

        }


    }

    private fun stopFragment() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
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
                            val r = response.body()
                            val oTPVerifyFragment = OTPVerifyFragment()
                            val args = Bundle()
                            args.putString("path", "login")
                            args.putString("number", requireArguments().getString("number"))
                            oTPVerifyFragment.arguments = args
                            if (activity != null) {
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.replace(R.id.frame_layout_splash_launcher, oTPVerifyFragment)
                                    ?.commit()
                            }

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

}