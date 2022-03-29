package com.hm.mmmhmm.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment() {

    private val TAG = "edit profile"
    private val genderList = arrayOf("male", "female")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        setupSpinner()
        if (ConnectivityObserver.isOnline(activity as Context)) {
            // getUserDetailAPI()
        }


        btn_update_profile.setOnClickListener(View.OnClickListener {
            //validation()
        })

//        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
//            activity?.supportFragmentManager?.popBackStack()
//        })

    }

    private fun setupToolBar() {
    //    iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
    //    tb_root.setBackgroundColor(resources.getColor(R.color.black))
        tv_toolbar_title.setText("Edit Profile")
    }

    private fun setupSpinner() {
        val spinerAdapter =
            ArrayAdapter(context as Context, android.R.layout.simple_spinner_item, genderList)
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner_edit_profile.adapter = spinerAdapter

        spinner_edit_profile.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                (parent.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.text_gray))
                val item = parent.getItemAtPosition(position)
                Log.d("possssss", item.toString() + "<<<<" + position)
//                    updateView(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }

}


//    private fun getUserDetailAPI() {
//        pb_update_prof.visibility = View.VISIBLE
//        val apiInterface = ApiClient.getRetrofitService(requireContext())
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//
//
//                val response = apiInterface.getUserDetailApi()
//                withContext(Dispatchers.Main) {
//
//                    try {
//                        pb_update_prof.visibility = View.GONE
//                        if (response.isSuccessful && response.body()?.status == 200) {
//                            val model = response.body()
//                            et_full_name_ep.setText(model?.data?.name)
//                            et_email_ep.setText(model?.data?.email)
//                            et_address_ep.setText(model?.data?.address)
//                            et_phone_ep.setText(model?.data?.phone)
//                            Log.d("sdsadsa", model?.data?.gender.toString())
//                            spinner_edit_profile.setSelection(genderList.indexOf(model?.data?.gender))
//                            et_dob_ep.setText(model?.data?.dob)
//                            et_identity_ep.setText(model?.data?.identityNumber)
//                            et_sexual_orient_ep.setText(model?.data?.sexualOrientation)
//                        } else {
//                      //      toast(response.body()?.message.toString())
//                        }
//                    } catch (e: Exception) {
//                        if (activity != null)
//                            Toast.makeText(activity, "" + e.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//
//            }
//
//        }
//    }
//
//    private fun updateProfileAPI(
//        name: String,
//        address: String,
//        phone: String,
//        gender: String,
//        dob: String,
//        id: String,
//        sex: String
//    ) {
//        pb_update_prof.visibility = View.VISIBLE
//
//        val apiInterface = ApiClient.getRetrofitService(requireContext())
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//
//                val response =
//                    apiInterface.updateUserInfoApi(name, address, phone, gender, dob, id, sex)
//                withContext(Dispatchers.Main) {
//                    if (activity != null && pb_update_prof != null) {
//                        pb_update_prof.visibility = View.GONE
//                        SessionManager.init(activity as Context)
//                    }
//                    try {
//                        if (response.isSuccessful && response.body()?.status == 200) {
//                            val model = response.body()?.data
//                            SessionManager.setUserData(model)
//
//                            //toast(response.body()?.message)
//                            activity?.supportFragmentManager?.popBackStack()
//                        } else {
//                          //  toast(response.body()?.message)
//                        }
//                    } catch (e: Exception) {
//                        Log.d(TAG, "exceptionn: " + e.toString())
//                    }
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }

