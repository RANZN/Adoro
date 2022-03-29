package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.GlobleData
import com.hm.mmmhmm.helper.SessionManager
import com.romainpiel.shimmer.Shimmer
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Shimmer().start(shimmerText)
        Handler().postDelayed({
            terminateFragment()
        }, 4000)
    }

    private fun terminateFragment() {
        if (activity != null) {
            SessionManager.init(activity as Context)
            if (SessionManager.getLoginStatus().toString().equals("true")) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finishAffinity()
            } else {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_splash_launcher,WalkThroughFragment())?.commit()
        }
    }}

}
