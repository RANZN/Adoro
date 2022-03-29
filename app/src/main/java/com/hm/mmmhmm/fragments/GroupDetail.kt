package com.hm.mmmhmm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.adapter.GroupAnnouncementAdapter
import com.hm.mmmhmm.adapter.GroupDiscussionAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_detail.*

class GroupDetail : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity())
        tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
        tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_announcement.setOnClickListener {
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            recycler_group_detail.adapter= GroupAnnouncementAdapter(requireActivity())
        }
        tv_post.setOnClickListener {
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            recycler_group_detail.adapter= FeedListAdapter()
        }
        tv_discussion.setOnClickListener {
            tv_discussion.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_announcement.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_post.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            recycler_group_detail.adapter= GroupDiscussionAdapter()
        }
//  (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment)
//                    .commit()

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

}