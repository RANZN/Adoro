package com.hm.mmmhmm.activity

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupToolBar()
        btnSendPassword.setOnClickListener {
            if (etEmail.text.toString().isEmpty()) {
                toast("Please enter email")
            } else if (!isValidEmail(etEmail.text.toString())) {
                toast("Invalid email")
            } else {
                //forgotPassword()
            }
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
//        tv_toolbar_title?.setText("Recover Account")
    }

//    private fun forgotPassword() {
//        pb_forgot.visibility = View.VISIBLE
//        val apiInterface = ApiClient.getRetrofitService(this)
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = apiInterface.forgotPassword(etEmail.text.toString())
//                withContext(Dispatchers.Main) {
//                    pb_forgot.visibility = View.GONE
//                    try {
//                        if (response.isSuccessful && response.body()?.status == 200) {
//                            val builder = AlertDialog.Builder(this@ForgotPassword)
//                            builder.setTitle("Message")
//                            builder.setMessage(response.body()?.message ?: "")
//                            builder.setCancelable(false)
//                            builder.setPositiveButton(
//                                "Cancel",
//                                DialogInterface.OnClickListener { di, i ->
//                                    di.cancel()
//                                    onBackPressed()
//                                })
//                            builder.create().show()
//                        } else {
//                            toast(response.body()?.message)
//                        }
//                    } catch (e: Exception) {
//                        Log.d("resp", "cathch: " + e.toString())
//                    }
//                }
//
//            } catch (e: Exception) {
//
//            }
//        }
//    }
}