package com.hm.mmmhmm.activity

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.*
import com.hm.mmmhmm.helper.GlobleData
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_navigation_view_sidebar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "main activity"
    private var isOpen: Boolean = false
    var homePressed = true
    var doubleBackToExitPressedOnce:kotlin.Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // SessionManager.init(this)
        // GlobleData.ACCESS_TOKEN = SessionManager.getAccessToken().toString()
        FirebaseMessaging.getInstance().subscribeToTopic(SessionManager.getUsername()!!.lowercase())

        iv_user.load(
            SessionManager.getUserPic(),
            R.color.text_gray,
            R.color.text_gray,
            true
        )

        tv_user.text= SessionManager.getUserName()
        tv_followers.text= SessionManager.getTotalFollowers().toString()+" Followers"
        tv_total_coins.text= SessionManager.getTotalFollowers().toString()+" adoro"


        ll_user_details.setOnClickListener {
            val profileFragment = ProfileFragment()
            val args = Bundle()
            args.putString("path", "home")
            args.putString("userId", SessionManager.getUserId())
            profileFragment.arguments = args
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, profileFragment)
                .commit()
            view_tab_home.visibility = View.GONE
            view_tab_group.visibility = View.GONE
            view_tab_add.visibility = View.GONE
            view_tab_services.visibility = View.GONE
            view_tab_account.visibility = View.VISIBLE
            drawer_layout.closeDrawer(GravityCompat.START)

        }
        val extras = intent.extras
        if (extras != null&&extras.getString("tag")=="chat") {
            val profileFragment = ProfileFragment()
            val args = Bundle()
            args.putString("path", "search")
            args.putString("userId", extras.getString("userId") ?: "")
            profileFragment.arguments = args
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, profileFragment)
                .addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment())
                .commit()
        }

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment())
            .commit()
        clickMethod()

        // updateFcmToken()

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

        val uri: Uri? = intent.data
        Log.i("Sanjeev", "onCreate: $uri")
        if (uri != null) {
            for (i in uri.queryParameterNames) {
                if (i == "postId") {
                    // TODO("Change this fragment to whatever fragment")
                    val frag = CommentsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout_main,
                            frag::class.java,
                            Bundle().apply {
                                putString(
                                    "postId",
                                    uri.getQueryParameter("postId")
                                )
                            },
                            frag::class.java.simpleName
                        )
                        .commit()
                    break
                }
            }
        }

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        if (intent?.hasExtra("tag") == true) {
            if (intent.getStringExtra("tag") == "chat") {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "search")
                args.putString("userId", intent.getStringExtra("userId") ?: "")
                profileFragment.arguments = args
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(profileFragment::class.java.simpleName).commit()
            }else if (intent.getStringExtra("tag") == "inbox") {
                val searchFragment = SearchFragment()
                val args = Bundle()
                //args.putString("path", "search")
                searchFragment.arguments = args
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, searchFragment)
                    .addToBackStack(searchFragment::class.java.simpleName).commit()
            }
        }
    }

    private fun clickMethod() {
        rl_notification.setOnClickListener(View.OnClickListener {
            // resetView()
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, NotificationFragment()).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })

        rl_settings.setOnClickListener(View.OnClickListener {

            //resetView()
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, SettingsFragment()).addToBackStack(null).commit()
            drawer_layout.closeDrawers()

        })

        rl_template.setOnClickListener(View.OnClickListener {
            //resetView()
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
            args.putString("url", "https://www.marqueberry.com/adoro-support-page")
            cmsPageFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cmsPageFragment).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })

        rl_result.setOnClickListener(View.OnClickListener {
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, ResultFragment()).addToBackStack(null)
                .commit()
            drawer_layout.closeDrawers()
        })


        rl_adoro_coins.setOnClickListener(View.OnClickListener {
            clearBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, AdoroCoinsFragment()).addToBackStack(null)
                .commit()

            drawer_layout.closeDrawers()
        })

        rl_nav_logout.setOnClickListener(View.OnClickListener {
            showDialog()
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

//    private fun resetView() {
//        //iv_home_icon.setColorFilter(resources.getColor(R.color.text_gray))
//       // tv_invite_drawer.setTextColor(resources.getColor(R.color.text_gray))
//
//        iv_nda_icon.setColorFilter(resources.getColor(R.color.text_gray))
//        tv_nda_drawer.setTextColor(resources.getColor(R.color.text_gray))
//
//        iv_agree_icon.setColorFilter(resources.getColor(R.color.text_gray))
//      //  tv_agreement_drawer.setTextColor(resources.getColor(R.color.text_gray))
//
//        iv_blog_icon.setColorFilter(resources.getColor(R.color.text_gray))
//       // tv_blog_drawer.setTextColor(resources.getColor(R.color.text_gray))
//
//
//    }

//    private fun updateToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("adsfdf", "Fetching FCM registration token failed ${task.exception}")
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//            SessionManager.init(this)
//            SessionManager.setFcmToken(token ?: "")
//            // Log and toast
//            Log.d("sdfdfsd", token!!)
//        })
//    }

    private fun showDialog() {
        val btn_no: Button
        val btn_yes: Button
        val tv_logout_text: TextView
        val dialog = Dialog(this)
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
        startActivity(Intent(this, SplashLauncher::class.java))
        this.finish()
    }

    fun manageDrawer() {
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
        when (v?.id) {
            R.id.liTab_home, R.id.iv_tab_home, R.id.tv_tab_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, HomeFragment())
                    .commit()
                view_tab_home.visibility = View.VISIBLE
                view_tab_group.visibility = View.GONE
                view_tab_add.visibility = View.GONE
                view_tab_services.visibility = View.GONE
                view_tab_account.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_group, R.id.tv_tab_group, R.id.liTab_group -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, GroupsFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.visibility = View.VISIBLE
                view_tab_add.visibility = View.GONE
                view_tab_services.visibility = View.GONE
                view_tab_account.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.liTab_add, R.id.iv_tab_add, R.id.tv_tab_add -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, AddFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.visibility = View.GONE
                view_tab_add.visibility = View.VISIBLE
                view_tab_services.visibility = View.GONE
                view_tab_account.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_service, R.id.tv_tab_service, R.id.liTab_services -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ServicesFragment())
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.visibility = View.GONE
                view_tab_add.visibility = View.GONE
                view_tab_services.visibility = View.VISIBLE
                view_tab_account.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.iv_tab_account, R.id.tv_tab_account, R.id.liTab_account -> {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "home")
                args.putString("userId", "")
                profileFragment.arguments = args
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .commit()
                view_tab_home.visibility = View.GONE
                view_tab_group.visibility = View.GONE
                view_tab_add.visibility = View.GONE
                view_tab_services.visibility = View.GONE
                view_tab_account.visibility = View.VISIBLE
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     //   Toast.makeText(this, "File uploaded successfully!", Toast.LENGTH_SHORT).show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 125) {
            var granted = true
            for (result in grantResults) {
                granted = result == PackageManager.PERMISSION_GRANTED
            }
            if (!granted) {
                // TODO("show why we need permission")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseDatabase.getInstance().getReference("users")
            .child(SessionManager.getFirebaseID() ?: "")
            .updateChildren(HashMap<String, Any?>().apply { put("isOnline", true) })
    }

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance().getReference("users")
            .child(SessionManager.getFirebaseID() ?: "")
            .updateChildren(HashMap<String, Any?>().apply { put("isOnline", true) })
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseDatabase.getInstance().getReference("users")
            .child(SessionManager.getFirebaseID() ?: "")
            .updateChildren(HashMap<String, Any?>().apply { put("isOnline", false) })
    }

    override fun onStop() {
        super.onStop()
        FirebaseDatabase.getInstance().getReference("users")
            .child(SessionManager.getFirebaseID() ?: "")
            .updateChildren(HashMap<String, Any?>().apply { put("isOnline", false) })
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        //backStackEntryCount==0 -> no fragments more.. so close the activity with warning
        if (backStackEntryCount == 0) {
            if (homePressed) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
            } else {
                homePressed = true
            }
        } else {
            super.onBackPressed()
        }
    }

}
