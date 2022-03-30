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
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
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

class OTPVerifyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            tv_signup_terms.visibility = View.VISIBLE;
        }


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
                var loginRequest: RequestLogin =
                    RequestLogin(requireArguments().getString("number")!!.toLong());
                hitLoginAPI(loginRequest)

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
        );
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
                    ?.replace(R.id.frame_layout_main,HomeFragment())?.commit()
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
                                "Hi " + response.body()?.OK!!.items[0].name + ", Welcome to ADARO! ",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val r = response.body()
                            SessionManager.init(activity as Context)
                            SessionManager.setLoginStatus("true")
                            SessionManager.setUserId(response.body()?.OK!!.items[0]._id)
                            startActivity(Intent(activity, MainActivity::class.java))
                            activity?.finish()


                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


}