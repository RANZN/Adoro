package com.hm.mmmhmm.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.adapter.GroupAnnouncementAdapter
import com.hm.mmmhmm.adapter.GroupDiscussionAdapter
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.CreateGroupRequest
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.models.RequestRegister
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_creation.*
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.et_username
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class FragmentGroupCreation : Fragment() {

    val types = arrayOf("Group Privacy", "Public")
    private var categoryList: List<Item>? = null
    var visibilityType =types[0]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val t=inflater.inflate(R.layout.fragment_group_creation, container, false)
        val spinner = t.findViewById<Spinner>(R.id.spinner_group_privacy)
        spinner?.adapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                types
            )
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
               // Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)
                visibilityType= type
            }

        }
        return t
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        getCategoryListForGroup()
        btn_create_group.setOnClickListener {
            validateInput()
        }

    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.create_group)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }

    private fun validateInput() {
        val groupName = et_group_name.text.toString()
        val groupAbout = et_group_about.text.toString()
//        val number = requireArguments().getString("number")
//        val username = et_username.text.toString()
        if (groupName.isNullOrEmpty()) {
            toast(R.string.group_name, 1)
        } else if (groupAbout.isNullOrEmpty()) {
            toast(R.string.email_address, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            val memberData : Array<Int> = emptyArray()
            var createGroupRequest: CreateGroupRequest = CreateGroupRequest(groupName,groupAbout,"","",memberData,"")
            createGroupAPI(createGroupRequest)

        }
    }

    private fun createGroupAPI( createGroupRequest: CreateGroupRequest) {
        pb_create_group.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.createGroup(createGroupRequest)
                withContext(Dispatchers.Main) {

                    try {
                        pb_create_group.visibility = View.GONE
                        if (response.body()?.OK != null) {
//                        Toast.makeText(activity," "+response.body()?.message, Toast.LENGTH_SHORT).show()
                            val r = response.body()

                            //showDialog()

                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getCategoryListForGroup( ) {
        pb_create_group.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getCategoryListForGroup()
                withContext(Dispatchers.Main) {
                    try {
                        pb_create_group.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                            categoryList = r?.OK?.items
                                // recycler_groups.adapter= GroupsAdapter(r?.OK?.items)
                        } else {
                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}