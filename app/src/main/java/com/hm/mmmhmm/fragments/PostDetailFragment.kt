package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.RequestCampaign
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       setupToolBar()
        var requestCampaign: RequestCampaign=RequestCampaign(requireArguments().getString("campaignId")?:"")
        Log.d("fdffg",requireArguments().getString("campaignId").toString())
        getCampaignDetail(requestCampaign)

        btn_upload.setOnClickListener {

            if (activity != null) {

                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, PublishMemeFragment())
                    .commit()
            }

        }


    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }
private fun getCampaignDetail(requestCampaign: RequestCampaign) {
    pb_campaign_detail.visibility = View.VISIBLE
    val apiInterface = ApiClient.getRetrofitService(requireContext())
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiInterface.getCampaignDetail(requestCampaign)
            withContext(Dispatchers.Main) {
                pb_campaign_detail.visibility = View.GONE

                try {
                    //  toast("" + response.body()?.message)
                    if (response.body()?.OK!=null) {
                        tv_brand_name.text = response.body()?.OK?.items?.get(0)?.brandName
                        tv_description.text = response.body()?.OK?.items?.get(0)?.description
                        tv_short_description.text = response.body()?.OK?.items?.get(0)?.shortDescription
                        tv_like_count.text = response.body()?.OK?.items?.get(0)?.like.toString()
                        tv_apply_count.text = response.body()?.OK?.items?.get(0)?.appliedNumber.toString()
                        tv_comment_count.text = response.body()?.OK?.items?.get(0)?.comment.toString()
                        iv_profile_pic_profile.load(
                            response.body()?.OK?.items?.get(0)?.brandLogo,
                            R.color.text_gray,
                            R.color.text_gray,
                            true
                        )

                        iv_campaign.load(
                            response.body()?.OK?.items?.get(0)?.campignImage,
                            R.color.text_gray,
                            R.color.text_gray,
                            true
                        )


                    } else {
                        Log.d("resp", "complet else: ")
                    }

                } catch (e: Exception) {
                    Log.d("resp", "cathch: " + e.toString())
                }
            }

        } catch (e: Exception) {
            Log.d("weweewefw", e.toString())
        }
    }

}


    }

