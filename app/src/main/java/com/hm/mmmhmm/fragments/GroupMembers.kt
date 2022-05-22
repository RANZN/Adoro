package com.hm.mmmhmm.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GroupRequestsAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_edit_group.*
import kotlinx.android.synthetic.main.fragment_group_creation.*
import kotlinx.android.synthetic.main.fragment_group_creation.btn_create_group
import kotlinx.android.synthetic.main.fragment_group_joining_requests.*
import kotlinx.android.synthetic.main.fragment_group_joining_requests.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_group_members.*
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupMembers : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_members, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()


    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.create_group)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })
        recycler_group_members.adapter= GroupRequestsAdapter(requireActivity())

        tv_existing.setBackgroundResource( R.drawable.bg_buttun_gradient )
        tv_requested.setBackgroundResource( R.drawable.bg_unselect )
        tv_existing.setTextColor(resources.getColor(R.color.white))
        tv_requested.setTextColor(resources.getColor(R.color.black))

        tv_existing.setOnClickListener {
            tv_existing.setBackgroundResource( R.drawable.bg_buttun_gradient )
            tv_requested.setBackgroundResource( R.drawable.bg_unselect )
            tv_existing.setTextColor(resources.getColor(R.color.white))
            tv_requested.setTextColor(resources.getColor(R.color.black))
        }

        tv_requested.setOnClickListener {
            tv_requested.setBackgroundResource( R.drawable.bg_buttun_gradient )
            tv_existing.setBackgroundResource( R.drawable.bg_unselect )
            tv_requested.setTextColor(resources.getColor(R.color.white))
            tv_existing.setTextColor(resources.getColor(R.color.black))
        }
    }

    companion object {

    }
}