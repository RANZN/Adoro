package com.hm.mmmhmm.activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.*
import com.hm.mmmhmm.helper.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_navigation_view_sidebar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "main activity"
    private var isOpen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)
       // SessionManager.init(this)
       // GlobleData.ACCESS_TOKEN = SessionManager.getAccessToken().toString()
//        iv_profile_pic_navigation.load(
//            SessionManager.getUserData()?.profilePicture,
//            R.color.text_gray,
//            R.color.text_gray,
//            true
//        )

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment())
            .commit()
        clickMethod()

        //updateFcmToken()

        liTab_home.setOnClickListener(this)
        iv_tab_home.setOnClickListener(this)
        tv_tab_home.setOnClickListener(this)

        liTab_group.setOnClickListener(this)
        iv_tab_group.setOnClickListener(this)
        tv_tab_group.setOnClickListener(this)

        liTab_add.setOnClickListener(this)
        iv_tab_add.setOnClickListener(this)
        tv_tab_add.setOnClickListener(this)

        liTab_services.setOnClickListener(this)
        iv_tab_service.setOnClickListener(this)
        tv_tab_service.setOnClickListener(this)

        liTab_account.setOnClickListener(this)
        iv_tab_account.setOnClickListener(this)
        tv_tab_account.setOnClickListener(this)

    }

    private fun updateFcmToken() {
        SessionManager.init(this)
        val apiInterface = ApiClient.getRetrofitService(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apiInterface.updateFcmToken("android", SessionManager.getFCMToken() ?: "")

            } catch (e: Exception) {

            }
        }
    }

    private fun clickMethod() {
        rl_notification.setOnClickListener(View.OnClickListener {
            resetView()
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, NotificationFragment()).addToBackStack(null).commit()
            drawer_layout.closeDrawers()
        })

        rl_settings.setOnClickListener(View.OnClickListener {

            resetView()
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, SettingsFragment()).addToBackStack(null).commit()
            drawer_layout.closeDrawers()

        })

        rl_template.setOnClickListener(View.OnClickListener {
            resetView()
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, TemplatesFragment()).addToBackStack(null).commit()
            drawer_layout.closeDrawers()

        })

        rl_refer.setOnClickListener(View.OnClickListener {
           // resetView()
           // iv_blog_icon.setColorFilter(resources.getColor(R.color.text_gray))
           // tv_blog_drawer.setTextColor(resources.getColor(R.color.text_gray))

            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, ReferFragment()).addToBackStack(null).commit()
            drawer_layout.closeDrawers()

        })

        rl_privacy.setOnClickListener(View.OnClickListener {
            clearBackStack()
            val cmsPageFragment = CMSPageFragment()
            val args = Bundle()
            args.putString("title", "Privacy Policy")
            args.putString("url", "https://marqueberry.com/privacy-and-policy-wap")
            cmsPageFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cmsPageFragment).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })


        rl_terms.setOnClickListener(View.OnClickListener {

            clearBackStack()
            val cmsPageFragment = CMSPageFragment()
            val args = Bundle()
            args.putString("title", "Terms & Conditions")
            args.putString("url", "https://marqueberry.com/terms-and-condition-wap")
            cmsPageFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cmsPageFragment).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })

        rl_about.setOnClickListener(View.OnClickListener {
            clearBackStack()
            val cmsPageFragment = CMSPageFragment()
            val args = Bundle()
            args.putString("title", "About us")
            args.putString("url", "https://marqueberry.com/about-us-wap")
            cmsPageFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cmsPageFragment).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })

        rl_support.setOnClickListener(View.OnClickListener {
            clearBackStack()
            val cmsPageFragment = CMSPageFragment()
            val args = Bundle()
            args.putString("title", "Support")
            args.putString("url", "https://marqueberry.com/contact-us-wap")
            cmsPageFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cmsPageFragment).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })


//        --------------------------------------

//        iv_profile_pic_navigation.setOnClickListener(View.OnClickListener {
//
//            clearBackStack()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout_main, ProfileFragment()).addToBackStack(null).commit()
//            drawer_layout.closeDrawers()
//        })

                iv_close_drawer.setOnClickListener(View.OnClickListener {

            clearBackStack()
            drawer_layout.closeDrawers()
        })




//        --------------EDIT BUTTON TO OPEN EDIT SCREEN------------
//        iv_edit_all_drawer.setOnClickListener(View.OnClickListener {
//            clearBackStack()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout_main, EditProfileFragment()).addToBackStack(null)
//                .commit()
//            drawer_layout.closeDrawers()
//        })

    }

    private fun resetView() {
        //iv_home_icon.setColorFilter(resources.getColor(R.color.text_gray))
       // tv_invite_drawer.setTextColor(resources.getColor(R.color.text_gray))

        iv_nda_icon.setColorFilter(resources.getColor(R.color.text_gray))
        tv_nda_drawer.setTextColor(resources.getColor(R.color.text_gray))

        iv_agree_icon.setColorFilter(resources.getColor(R.color.text_gray))
      //  tv_agreement_drawer.setTextColor(resources.getColor(R.color.text_gray))

        iv_blog_icon.setColorFilter(resources.getColor(R.color.text_gray))
       // tv_blog_drawer.setTextColor(resources.getColor(R.color.text_gray))


    }

    private fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("adsfdf", "Fetching FCM registration token failed ${task.exception}")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            SessionManager.init(this)
            SessionManager.setFcmToken(token ?: "")
            // Log and toast
            Log.d("sdfdfsd", token!!)
        })
    }


    public fun manageDrawer() {
        isOpen = drawer_layout.isDrawerOpen(GravityCompat.END)
        Log.d("drawerrrrrrr", isOpen.toString())

        if (isOpen) {
            drawer_layout.closeDrawers()
        } else {
//            tv_name_drawer.setText(SessionManager.getUserData()?.name)
//            tv_email_drawer.setText(SessionManager.getUserData()?.email)
//
//
//            iv_profile_pic_navigation.load(
//                SessionManager.getUserData()?.profilePicture,
//                R.color.text_gray,
//                R.color.text_gray,
//                true
//            )
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }


    private fun clearBackStack() {
        val fm: FragmentManager = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }




    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.liTab_home, R.id.iv_tab_home, R.id.tv_tab_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment())
                    .commit()
                view_tab_home.visibility = View.VISIBLE
                view_tab_group.visibility = View.GONE
                view_tab_add.visibility = View.GONE
                view_tab_services.visibility = View.GONE
                view_tab_account.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_group, R.id.tv_tab_group, R.id.liTab_group -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, GroupsFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.setVisibility(View.VISIBLE)
                view_tab_add.setVisibility(View.GONE)
                view_tab_services.visibility = View.GONE
                view_tab_account.setVisibility(View.GONE)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.liTab_add, R.id.iv_tab_add, R.id.tv_tab_add -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, AddFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.setVisibility(View.GONE)
                view_tab_add.setVisibility(View.VISIBLE)
                view_tab_services.visibility = View.GONE
                view_tab_account.setVisibility(View.GONE)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_service, R.id.tv_tab_service, R.id.liTab_services -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, ServicesFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.setVisibility(View.GONE)
                view_tab_add.setVisibility(View.GONE)
                view_tab_services.visibility = View.VISIBLE
                view_tab_account.setVisibility(View.GONE)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_account, R.id.tv_tab_account, R.id.liTab_account -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, ProfileFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.setVisibility(View.GONE)
                view_tab_add.setVisibility(View.GONE)
                view_tab_services.visibility = View.GONE
                view_tab_account.setVisibility(View.VISIBLE)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("file:::::", selectedFile.toString())
        }
        Toast.makeText(this,"File uploaded successfully!", Toast.LENGTH_SHORT).show()
    }
}
