package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
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
import com.hm.mmmhmm.models.RequestRegister
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SignupFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
        tv_toolbar_title?.setText("Adoro")
        tv_toolbar_title?.setTextColor(resources.getColor(R.color.black))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        //GlobleData.goToLoginScreen = false

        btn_signup.setOnClickListener(View.OnClickListener {
            validateInput()
        })


    }

//    private fun stopFragment() {
//        startActivity(Intent(activity, MainActivity::class.java))
//        activity?.finish()
//    }


    private fun validateInput() {
        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val number = arguments!!.getString("number")
        val username = et_username.text.toString()
        if (name.isNullOrEmpty()) {
            toast(R.string.full_name, 1)
        } else if (email.isNullOrEmpty()) {
            toast(R.string.email_address, 1)
        } else if (username.isNullOrEmpty()) {
            toast(R.string.username, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var requestRegister: RequestRegister = RequestRegister(name,number?.toLong(),email,username);
            hitRegisteruserAPI(requestRegister)

        }
    }

    private fun showDialog() {
        val dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        val body = dialog.findViewById(R.id.tvBody) as TextView
        //body.text = title
        val title = dialog.findViewById(R.id.tvTitle) as TextView
        // body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_ok) as Button
        yesBtn.setOnClickListener {
            //todo
            openNext()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun openNext() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finishAffinity()
    }

    private fun hitRegisteruserAPI( requestRegister: RequestRegister) {
        pb_signup.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.registerUser(requestRegister)
                withContext(Dispatchers.Main) {

                    try {
                        pb_signup.visibility = View.GONE
                        if (response.body()?.OK != null) {
//                        Toast.makeText(activity," "+response.body()?.message, Toast.LENGTH_SHORT).show()
                            val r = response.body()
//                            SessionManager.init(activity as Context)
//                            SessionManager.setAccessToken(response.body()?.data?.token.toString())

                            SessionManager.init(activity as Context)
                            SessionManager.setLoginStatus("true")
                            showDialog()

                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity!!, "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}