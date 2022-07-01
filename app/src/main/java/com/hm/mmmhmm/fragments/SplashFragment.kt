package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.AddAdoroCoinsRequest
import com.hm.mmmhmm.web_service.ApiClient
import com.romainpiel.shimmer.Shimmer
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

       // var addAdoroCoinsRequest: AddAdoroCoinsRequest =
//            AddAdoroCoinsRequest(580,SessionManager.getUserId() ?: "");
//        addAdoro(addAdoroCoinsRequest)
        Handler().postDelayed({
            terminateFragment()
        }, 4000)
    }

    private fun terminateFragment() {
        if (activity != null) {
            //SessionManager.init(activity?.applicationContext!!)
            if (SessionManager.getLoginStatus().toString().equals("true")) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finishAffinity()
            } else {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_layout_splash_launcher, WalkThroughFragment()).commit()
        }
    }}

    private fun addAdoro(addAdoroCoinsRequest: AddAdoroCoinsRequest) {
        Shimmer().start(shimmerText)
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.addAdoro(addAdoroCoinsRequest)
                withContext(Dispatchers.Main) {
                    try {
                        Shimmer().cancel()
                        if (response.isSuccessful) {
                            val r = response.body()

                        } else {
                            Toast.makeText(
                                activity,
                                R.string.Something_went_wrong,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
