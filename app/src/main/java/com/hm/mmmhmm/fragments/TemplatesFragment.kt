package com.hm.mmmhmm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.adapter.NotificationsAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_templates.*

class TemplatesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_templates, container, false)
    }


    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.template)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        // pb_cms_page.visibility= View.VISIBLE
        tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
        tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_my_template.setOnClickListener {
            tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            rv_template.adapter= GalleryAdapter()
        }
        tv_browse_template.setOnClickListener {
            tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            rv_template.adapter= GalleryAdapter()
        }
        rv_template.adapter= GalleryAdapter()

        // pb_cms_page.visibility= View.GONE

    }
}