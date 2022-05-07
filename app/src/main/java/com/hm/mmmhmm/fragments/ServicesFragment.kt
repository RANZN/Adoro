package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ServicesFragment : Fragment() {
    private var campaignList: List<Item>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, InboxActivity::class.java))
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        getCampaignListAPI()


    }

    private fun getCampaignListAPI() {
        pb_campaigns.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getCampaignList()
                withContext(Dispatchers.Main) {
                    pb_campaigns.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            campaignList = response.body()?.OK?.items
                            Log.d("resp", "$campaignList")
                            recycler_job_list.adapter= PostListAdapter(campaignList)

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



    inner class PostListAdapter(private val campaignList: List<Item>?) : RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_job_list, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_brand_name.text= campaignList?.get(position)?.brandName
            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
            holder.tv_detail.text= campaignList?.get(position)?.shortDescription
            holder.tv_time_left.text= "₹"+campaignList?.get(position)?.timeLeft.toString()+" left"
            holder.iv_profile_pic_profile.load(
                campaignList?.get(position)?.brandLogo.toString(),
                R.color.text_gray,
                R.color.text_gray,
                true
            )
            holder.itemView.setOnClickListener {
                //todo



            }
            holder.btn_learn_more.setOnClickListener {
                val postDetailFragment = PostDetailFragment()
                val args = Bundle()
                args.putString("campaignId", campaignList?.get(position)?._id)
                postDetailFragment.arguments = args
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
                    .commit()

            }
        }

        override fun getItemCount(): Int {
            return campaignList?.size ?: 0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_profile_pic_profile: ImageView
            val tv_brand_name: TextView
            val tv_detail: TextView
            val tv_time_left: TextView
            val tv_price: TextView
            val btn_learn_more: Button
            val ll_item_list: LinearLayout

            init {
                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
                tv_brand_name = v.findViewById(R.id.tv_brand_name)
                tv_detail = v.findViewById(R.id.tv_detail)
                tv_time_left = v.findViewById(R.id.tv_time_left)
                tv_price = v.findViewById(R.id.tv_price)
                btn_learn_more = v.findViewById(R.id.btn_learn_more)
                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
        }
    }


}