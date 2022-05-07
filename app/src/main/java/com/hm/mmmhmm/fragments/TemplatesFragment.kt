package com.hm.mmmhmm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.adapter.NotificationsAdapter
import com.hm.mmmhmm.adapter.TemplateAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.RequestShowMyTemplate
import com.hm.mmmhmm.models.ShowPostlRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_templates.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.template)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        tv_my_template.setTextColor(resources.getColor(R.color.white))
        tv_browse_template.setTextColor(resources.getColor(R.color.black))
        iv_create_template.setOnClickListener {
            //todo

        }
        tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
        tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
        tv_my_template.setOnClickListener {
            tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))
            tv_my_template.setTextColor(resources.getColor(R.color.white))
            tv_browse_template.setTextColor(resources.getColor(R.color.black))
            var requestShowMyTemplate: RequestShowMyTemplate =
                RequestShowMyTemplate(SessionManager.getUserId() ?: "");
            getMyTemplate(requestShowMyTemplate)
        }
        tv_browse_template.setOnClickListener {
            tv_browse_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.colorAccent))
            tv_my_template.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.transparent))

            tv_my_template.setTextColor(resources.getColor(R.color.black))
            tv_browse_template.setTextColor(resources.getColor(R.color.white))
            getBrowseTemplate()
        }

        var requestShowMyTemplate: RequestShowMyTemplate =
            RequestShowMyTemplate(SessionManager.getUserId() ?: "");
        getMyTemplate(requestShowMyTemplate)

    }

    private fun getMyTemplate(requestShowMyTemplate: RequestShowMyTemplate) {
        pb_templates.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showMyTemplate(requestShowMyTemplate)
                withContext(Dispatchers.Main) {
                    try {
                        pb_templates.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            rv_gallery.adapter = TemplateAdapter(r?.OK?.items)
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

    private fun getBrowseTemplate() {
        pb_templates.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.browseTemplate()
                withContext(Dispatchers.Main) {
                    try {
                        pb_templates.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            rv_gallery.adapter = TemplateAdapter(r?.OK?.items)
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