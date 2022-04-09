package com.hm.mmmhmm.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.CompleteAddress
import com.hm.mmmhmm.models.UpdateProfileRequest
import com.hm.mmmhmm.web_service.ApiClient
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditProfileFragment : Fragment() {

    companion object {
        var isBanner = false
    }

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

            var completeAddress: CompleteAddress = CompleteAddress(
                et_area_name.text.toString(),
                et_city.text.toString(),
                et_landmark.text.toString(),
                et_state_name.text.toString(),
                et_street_address.text.toString(),
                et_zip_code.text.toString().toInt(),
            )

            var updateProfileRequest: UpdateProfileRequest = UpdateProfileRequest(
                SessionManager.getUserId() ?: "",
                et_account_number.text.toString(),
                0,
                0,
                et_bank_name.text.toString(),
                completeAddress,
                "",
                "",
                "",
                "",
                "",
                9997854380,
                "",
                "",
                "",
                ""
            )
            updateProfileAPI(updateProfileRequest)
        })

        btn_update_profile_pic.setOnClickListener {
            isBanner = false
            if (checkPermission()) {
                CropImage.startPickImageActivity(requireActivity())
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 125
                )
            }

        }
        iv_camera.setOnClickListener {
            isBanner = true
            if (checkPermission()) {
                CropImage.startPickImageActivity(requireActivity())
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 125
                )
            }
        }


//        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
//            activity?.supportFragmentManager?.popBackStack()
//        })

    }

    private fun checkPermission(): Boolean {
        for (permission in arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////
////            if (resultCode == Activity.RESULT_OK) {
////                val resultUri: Uri = result.uri
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                val error = result.error
////            }
//        }
//    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black))
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.edit_profile)
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


    private fun setupSpinner() {
        val spinerAdapter =
            ArrayAdapter(context as Context, android.R.layout.simple_spinner_item, genderList)
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner_edit_profile.adapter = spinerAdapter

        spinner_edit_profile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        }
    }

    fun updateProfileAPI(updateProfileRequest: UpdateProfileRequest) {
        pb_edit_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateProfile(updateProfileRequest)
                withContext(Dispatchers.Main) {
                    pb_edit_prof.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response.isSuccessful) {

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

