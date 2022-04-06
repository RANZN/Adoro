package com.hm.mmmhmm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.ShowPostlRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_creation.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.item_group_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroupsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
        getUserData(generalRequest)
        tv_my_groups.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorAccent
            )
        )
        tv_browse_groups.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.transparent
            )
        )
        tv_my_groups.setOnClickListener {
            tv_my_groups.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorAccent
                )
            )
            tv_browse_groups.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.transparent
                )
            )
            var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
            getUserData(generalRequest)
        }
        tv_browse_groups.setOnClickListener {
            tv_browse_groups.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorAccent
                )
            )
            tv_my_groups.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.transparent
                )
            )
            getGroups()
        }
        rl_create_group.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, FragmentGroupCreation())
                .addToBackStack(null).commit()
        }
//  (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()

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
            startActivity(Intent(activity, Inbox::class.java))
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


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
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout_main, GroupDetail())
                    ?.addToBackStack(null)?.commit()
            }
//            holder.btn_learn_more.setOnClickListener {
//                val postDetailFragment = PostDetailFragment()
//                val args = Bundle()
//                args.putString("campaignId", campaignList?.get(position)?._id)
//                postDetailFragment.arguments = args
//                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()
//
//            }
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

            //            val btn_learn_more: Button
//            val ll_item_list: LinearLayout
//
            init {
                iv_group_pic = v.findViewById(R.id.iv_group_pic)
                tv_group_name = v.findViewById(R.id.tv_group_name)
                tv_detail = v.findViewById(R.id.tv_detail)
                tv_group_privacy = v.findViewById(R.id.tv_group_privacy)
                tv_total_memberes = v.findViewById(R.id.tv_total_memberes)
//                btn_learn_more = v.findViewById(R.id.btn_learn_more)
//                ll_item_list = v.findViewById(R.id.ll_item_list)
            }
        }
    }

    private fun getGroups() {
        pb_groups.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.browseGroup()
                withContext(Dispatchers.Main) {
                    try {
                        pb_groups.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            recycler_groups.adapter = GroupsAdapter(r?.OK?.items)
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

    private fun getUserData(generalRequest: GeneralRequest) {
        //pb_create_group.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getUserData(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        //  pb_create_group.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            //recycler_groups.adapter= GroupsAdapter(r?.OK?.items[0]?.myGroupInfo)
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