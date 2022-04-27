package com.hm.mmmhmm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.SplashFragment
import com.hm.mmmhmm.helper.SessionManager

class SplashLauncher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_launcher)
        updateToken()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_splash_launcher, SplashFragment())
            .commit()

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


}
