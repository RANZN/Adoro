package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.toast
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_register_number.*



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

            val oTPVerifyFragment = OTPVerifyFragment()
            val args = Bundle()
            args.putString("path", "register")
            args.putString("number", number)
            oTPVerifyFragment.arguments = args
            if (activity != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_splash_launcher,oTPVerifyFragment)?.commit()
            }
        }
    }

}