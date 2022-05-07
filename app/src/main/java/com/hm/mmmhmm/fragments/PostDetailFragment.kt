package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
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

    var totalLike:Int=0

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
                val publishMemeFragment = PublishMemeFragment()
                val args = Bundle()
                args.putString("campaignId",requireArguments().getString("campaignId"))
                publishMemeFragment.arguments = args
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, publishMemeFragment)
                    .commit()
            }

        }

        iv_option_menu.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity() , iv_option_menu)
            popupMenu.inflate(R.menu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item?.itemId){
                        R.id.report -> {
                            var generalRequest: GeneralRequest = GeneralRequest(requireArguments().getString("campaignId")?:"");
                            reportCampaign(generalRequest)
                        }

                    }
                    return false
                }
            })
            popupMenu.show()

        }
        iv_like.setOnClickListener {
            var likeData: LikeData = LikeData(
                SessionManager.getUserId(),
               "",
                SessionManager.getUserPic(),
                SessionManager.getUserName());
            var postLikeRequest: CampaignUpdateLikeRequest = CampaignUpdateLikeRequest(
                requireArguments().getString("campaignId"),
                likeData);
            postUpdateLike(postLikeRequest,iv_like,tv_like_count,
                totalLike)
        }


    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
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
                        totalLike = response.body()?.OK?.items?.get(0)?.like.toString().toInt()
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

    private fun postUpdateLike(postLikeRequest: CampaignUpdateLikeRequest, iv_like: ImageView, tv_like_count: TextView, likeCount:Int) {
        // pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.campaignUpdateLike(postLikeRequest)
                withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            tv_like_count.text =(likeCount+1).toString()
                            iv_like.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
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

    private fun reportCampaign(generalRequest: GeneralRequest) {
        pb_campaign_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.reportCampaign(generalRequest)
                withContext(Dispatchers.Main) {
                     pb_campaign_detail.visibility = View.GONE
                    try {
                        if (response!=null) {
                            //toast("" + response.body()?.message)
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

