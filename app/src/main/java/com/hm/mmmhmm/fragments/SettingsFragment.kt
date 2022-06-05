package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.activity.SplashLauncher
import com.hm.mmmhmm.adapter.NotificationsAdapter
import com.hm.mmmhmm.helper.GlobleData
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.ProfileRequest
import com.hm.mmmhmm.models.UpdateUserNotificationStatusRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_navigation_view_sidebar.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.settings)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var generalRequest: ProfileRequest =ProfileRequest(SessionManager.getUserId() ?: "",SessionManager.getUserId() ?: "")
        getUserData(generalRequest)
        setupToolBar()
        // pb_cms_page.visibility= View.VISIBLE
        switch_notification.setOnClickListener {
            if (switch_notification.isChecked){
                //todo
                //switch_notification.setTextColor(Color.WHITE)
                tv_notification_status.text=  resources.getString(R.string.notification)+"(On)"
                var generalRequest: UpdateUserNotificationStatusRequest = UpdateUserNotificationStatusRequest(
                    SessionManager.getUserId() ?: "",
                    "ON");
                updateNotificationSetting(generalRequest)
            }
            else{
               // todo
                //switch_notification.setTextColor(Color.BLACK)
                tv_notification_status.text=  resources.getString(R.string.notification)+"(Off)"
                var generalRequest: UpdateUserNotificationStatusRequest = UpdateUserNotificationStatusRequest(
                    SessionManager.getUserId() ?: "",
                    "OFF");
                updateNotificationSetting(generalRequest)
            }
        }

        rl_logout.setOnClickListener(View.OnClickListener {
            showDialog()
        })

    }
    private fun showDialog() {
        val btn_no: Button
        val btn_yes: Button
        val tv_logout_text: TextView
        val dialog = Dialog(activity as MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv_logout_text = dialog.findViewById(R.id.tv_logout_text)
        btn_no = dialog.findViewById(R.id.btn_no)
        btn_yes = dialog.findViewById(R.id.btn_yes)
        btn_no.setOnClickListener { dialog.dismiss() }
        btn_yes.setOnClickListener {
            logoutUserFromAppSession()
        }
        dialog.show()
    }
    fun logoutUserFromAppSession() {
        SessionManager.setLoginStatus("false")
        SessionManager.logout()
        GlobleData.goToLoginScreen = true
        startActivity(Intent(activity, SplashLauncher::class.java))
        activity?.finish()
    }


    private fun updateNotificationSetting(generalRequest: UpdateUserNotificationStatusRequest) {
        pb_settings.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateUserNotificationStatus(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_settings.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
//todo
                        } else {
                            Log.d("resp", "complet else: ")
                        }

                    } catch (e: Exception) {
                        Log.d("resp", "cathch: " + e.toString())
                    }
                }

            } catch (e: Exception) {
                pb_notifications.visibility = View.GONE
                Log.d("weweewefw", e.toString())
            }
        }

    }

    private fun getUserData(generalRequest: ProfileRequest) {
        pb_settings.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getUserData(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_settings.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()

                            switch_notification.isChecked = r?.OK?.items?.get(0)?.notificationStatus=="ON"


                        } else {
                            Toast.makeText(
                                activity,
                                R.string.Something_went_wrong,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: java.lang.Exception) {
//                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }



}