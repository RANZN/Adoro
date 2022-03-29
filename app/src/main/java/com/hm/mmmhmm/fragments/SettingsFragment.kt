package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.activity.SplashLauncher
import com.hm.mmmhmm.adapter.NotificationsAdapter
import com.hm.mmmhmm.helper.SessionManager
import kotlinx.android.synthetic.main.custom_navigation_view_sidebar.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.settings)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        // pb_cms_page.visibility= View.VISIBLE
        switch_notification.setOnClickListener {
            if (switch_notification.isChecked){
                //todo
                //switch_notification.setTextColor(Color.WHITE)
                switch_notification.text=  resources.getString(R.string.notification)+"(On)"
            }
            else{
               // todo
                //switch_notification.setTextColor(Color.BLACK)
                switch_notification.text=  resources.getString(R.string.notification)+"(Off)"
            }
        }

        rl_logout.setOnClickListener(View.OnClickListener {
//            Toast.makeText(applicationContext,"Logout", Toast.LENGTH_SHORT).show()
            logoutUserFromAppSession()
            SessionManager.setLoginStatus("false")
        })
        // pb_cms_page.visibility= View.GONE

    }

    fun logoutUserFromAppSession() {
        // SessionManager.setLoginStatus("false")
        // SessionManager.logout()
        // GlobleData.goToLoginScreen = true
        startActivity(Intent(activity, SplashLauncher::class.java))
        activity?.finish()
    }


}