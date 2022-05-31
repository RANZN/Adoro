package com.hm.mmmhmm.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
//import com.hm.mmmhmm.adapter.SearchSuggestionAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.JoinGroupRequest
import com.hm.mmmhmm.models.SearchRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_cms_page.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SearchFragment : Fragment() {
    var searchType: String?="account"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.search)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        var searchRequest: SearchRequest = SearchRequest("");
        getSearchAccounts(searchRequest)
        iv_search.setOnClickListener {
            var searchRequest: SearchRequest = SearchRequest(et_search.text.toString());
            if (searchType=="account"){
                getSearchAccounts(searchRequest)
            }else if(searchType=="campaign"){
                getCampaignListAPI(searchRequest)
            }else if(searchType=="groups"){
                getGroups(searchRequest)
            }
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchType=="account"){
                    var searchRequest: SearchRequest = SearchRequest(s.toString());
                    getSearchAccounts(searchRequest)
                }else if(searchType=="campaign"){
                    var searchRequest: SearchRequest = SearchRequest(s.toString());
                    getCampaignListAPI(searchRequest)
                }else if(searchType=="groups"){
                    var searchRequest: SearchRequest = SearchRequest(s.toString());
                    getGroups(searchRequest)
                }
            }
        })

        tv_search_account.setOnClickListener {
            searchType="account"
            tv_search_account.setBackgroundResource(R.drawable.bg_edittext)
            tv_search_campaigns.setBackgroundResource(0)
            tv_search_groups.setBackgroundResource(0)
            var searchRequest: SearchRequest = SearchRequest(et_search.text.toString());
            getSearchAccounts(searchRequest)

        }
        tv_search_campaigns.setOnClickListener {
            searchType="campaign"
            tv_search_campaigns.setBackgroundResource(R.drawable.bg_edittext)
            tv_search_account.setBackgroundResource(0)
            tv_search_groups.setBackgroundResource(0)
                var searchRequest: SearchRequest = SearchRequest(et_search.text.toString());
            getCampaignListAPI(searchRequest)

        }
        tv_search_groups.setOnClickListener {
            searchType="groups"
            tv_search_groups.setBackgroundResource(R.drawable.bg_edittext)
            tv_search_account.setBackgroundResource(0)
            tv_search_campaigns.setBackgroundResource(0)
                var searchRequest: SearchRequest = SearchRequest(et_search.text.toString());
                getGroups(searchRequest)

        }

    }

    private fun getSearchAccounts( searchRequest: SearchRequest) {
        pb_search.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.searchAccount(searchRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_search.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()

                            recycler_search_suggestions.adapter= SearchSuggestionAdapter(r?.OK?.items)

                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

   inner class SearchSuggestionAdapter( private var suggestionList: List<Item>? = null) : RecyclerView.Adapter<SearchSuggestionAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_suggestion, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_username_search_suggestion.text= suggestionList?.get(position)?.name
//            holder.iv_profile_pic_profile.load(
//                campaignList?.get(position)?.brandLogo.toString(),
//                R.color.text_gray,
//                R.color.text_gray,
//                true
//            )


                            val profileFragment = ProfileFragment()
                            val args = Bundle()
                            args.putString("path", "search")
                            args.putString("userId", suggestionList?.get(position)?._id)
            profileFragment.arguments = args
            holder.itemView.setOnClickListener {
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(null).commit()


            }

        }

        override fun getItemCount(): Int {
            return suggestionList?.size ?: 0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            //            val iv_profile_pic_profile: ImageView
            val tv_username_search_suggestion: TextView
            //            val tv_detail: TextView
//            val tv_time_left: TextView
//            val tv_price: TextView
//            val btn_learn_more: Button
//            val ll_item_list: LinearLayout
            init {
//                iv_profile_pic_profile = v.findViewById(R.id.iv_profile_pic_profile)
                tv_username_search_suggestion = v.findViewById(R.id.tv_username_search_suggestion)
//                tv_detail = v.findViewById(R.id.tv_detail)
//                tv_time_left = v.findViewById(R.id.tv_time_left)
//                tv_price = v.findViewById(R.id.tv_price)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
        }
    }



    private fun getCampaignListAPI(searchRequest: SearchRequest) {
        pb_search.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.searchCampaign(searchRequest)
                withContext(Dispatchers.Main) {
                    pb_search.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            recycler_search_suggestions.adapter= PostListAdapter(response.body()?.OK?.items)

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

    inner class GroupsAdapter(private var groupsList: List<Item>? = null) :
        RecyclerView.Adapter<GroupsAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_group_list, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_group_name.text = groupsList?.get(position)?.groupName
            holder.tv_detail.text = groupsList?.get(position)?.description
            holder.tv_group_privacy.text = groupsList?.get(position)?.category
            holder.tv_detail.text = groupsList?.get(position)?.shortDescription
            holder.tv_total_memberes.text =
                groupsList?.get(position)?.memberData?.size.toString() + " Members"
            holder.iv_group_pic.load(
                groupsList?.get(position)?.groupProfile.toString(),
                R.color.text_gray,
                R.color.text_gray,
                false
            )
            holder.itemView.setOnClickListener {
                val groupDetailFragment = GroupDetail()
                val args = android.os.Bundle()
                args.putString("groupId", groupsList?.get(position)?._id)
                args.putString("groupName", groupsList?.get(position)?.groupName)
                groupDetailFragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_main, groupDetailFragment)
                    ?.addToBackStack(null)?.commit()
            }
            holder.btn_enter.setOnClickListener {
                var groupDetail: com.hm.mmmhmm.models.GroupDetail = com.hm.mmmhmm.models.GroupDetail(
                    groupsList?.get(position)?._id,
                    groupsList?.get(position)?.category,
                    groupsList?.get(position)?.description,
                    groupsList?.get(position)?.groupName,
                    groupsList?.get(position)?.groupProfile,
                    groupsList?.get(position)?.privacy,)

                var memberDetail: com.hm.mmmhmm.models.MemberDetail = com.hm.mmmhmm.models.MemberDetail(
                    SessionManager.getUserName(),
                    SessionManager.getUserPic(),
                    SessionManager.getUserId(),
                    SessionManager.getUsername(),)
                var joinGroupRequest: JoinGroupRequest = JoinGroupRequest(groupDetail,memberDetail)
                joinGroup(joinGroupRequest)

            }
        }

        override fun getItemCount(): Int {
            return groupsList?.size ?: 0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val iv_group_pic: ImageView
            val tv_group_name: TextView
            val tv_detail: TextView
            val tv_group_privacy: TextView
            val tv_total_memberes: TextView
            val btn_enter: Button

            init {
                iv_group_pic = v.findViewById(R.id.iv_group_pic)
                tv_group_name = v.findViewById(R.id.tv_group_name)
                tv_detail = v.findViewById(R.id.tv_detail)
                tv_group_privacy = v.findViewById(R.id.tv_group_privacy)
                tv_total_memberes = v.findViewById(R.id.tv_total_memberes)
                btn_enter = v.findViewById(R.id.btn_enter)
            }
        }
    }

    private fun getGroups(searchRequest: SearchRequest) {
        pb_search.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.searchGroup(searchRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_search.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            recycler_search_suggestions.adapter = GroupsAdapter(r?.OK?.items)
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

    private fun joinGroup(joinGroupRequest: JoinGroupRequest) {
        pb_search.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.addMemberToGroup(joinGroupRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_search.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Group Joined" , Toast.LENGTH_SHORT)
                            .show()
                        // Add ?.OK
                        if (response.body() != null) {
                            val r = response.body()
//                            Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
//                                .show()
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
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}