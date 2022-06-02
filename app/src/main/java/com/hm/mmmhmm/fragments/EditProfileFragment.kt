package com.hm.mmmhmm.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.CompleteAddress
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.ProfileRequest
import com.hm.mmmhmm.models.UpdateProfileRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.iv_camera
import kotlinx.android.synthetic.main.fragment_edit_profile.iv_cover_pic_profile
import kotlinx.android.synthetic.main.fragment_edit_profile.iv_profile_pic_profile
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class EditProfileFragment : Fragment() {

    companion object {
        var isBanner = false
    }

    private var pickedProfile: Bitmap? = null
    private var pickedBanner: Bitmap? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage

            Log.i(TAG, "cropImage: $uriContent $uriFilePath")

            val bitmap = BitmapFactory.decodeFile(uriFilePath)
            if (isBanner) {
                pickedBanner = bitmap
                iv_cover_pic_profile.setImageBitmap(bitmap)
            } else {
                pickedProfile = bitmap
                iv_profile_pic_profile.setImageBitmap(bitmap)
            }
        } else {
            // an error occurred
            val exception = result.error
        }
    }

    private val TAG = "edit profile"
    private val genderList = arrayOf("male", "female")


    var username: String? = null
    var name: String? = null
    var userId: String? = null
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
        iv_back.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        setupSpinner()
        if (ConnectivityObserver.isOnline(activity as Context)) {
            // getUserDetailAPI()
        }


        btn_update_profile.setOnClickListener(View.OnClickListener {
            //validation()

            val completeAddress: CompleteAddress = CompleteAddress(
                et_area_name.text.toString(),
                et_city.text.toString(),
                et_landmark.text.toString(),
                et_state_name.text.toString(),
                et_street_address.text.toString(),
                if (et_zip_code.text.toString().isEmpty()) 0 else et_zip_code.text.toString()
                    .toInt(),
            )

            val updateProfileRequest: UpdateProfileRequest = UpdateProfileRequest(
                SessionManager.getUserId() ?: "",
                et_account_number.text.toString(),
                0,
                0,
                et_bank_name.text.toString(),
                completeAddress,
                "",
                et_email.text.toString(),
                "Male",
                et_ifsc_code.text.toString(),
                et_full_name.text.toString(),
                9997854380,
                "",
                et_username.text.toString(),
                getEncoded64ImageStringFromBitmap(pickedProfile),
                getEncoded64ImageStringFromBitmap(pickedBanner)
            )
            updateProfileAPI(updateProfileRequest)
        })

        btn_update_profile_pic.setOnClickListener {
            isBanner = false
            if (checkPermission()) {
                cropImage.launch(options {
                    setGuidelines(CropImageView.Guidelines.ON)
                })
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
                cropImage.launch(options {
                    setGuidelines(CropImageView.Guidelines.ON)
                })
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

        var generalRequest: ProfileRequest =
            ProfileRequest(SessionManager.getUserId() ?: "", SessionManager.getUserId() ?: "")
        getUserData(generalRequest)
    }

    private fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat: ByteArray = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
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

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_action_search.visibility=View.GONE
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black))
//        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
//        tv_toolbar_title.text = resources.getString(R.string.edit_profile)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, InboxActivity::class.java))
        })

//        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
//            (activity as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout_main, SearchFragment())
//                .addToBackStack(null).commit()
//        })


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

    private fun updateProfileAPI(updateProfileRequest: UpdateProfileRequest) {
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

    private fun getUserData(generalRequest: ProfileRequest) {
        pb_edit_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getUserData(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_edit_prof.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()

                            iv_profile_pic_profile.load(
                                r?.OK?.items?.get(0)?.profile,
                                R.color.text_gray,
                                R.color.text_gray,
                                true
                            )
                            iv_cover_pic_profile.load(
                                r?.OK?.items?.get(0)?.bannerImage,
                                R.color.text_gray,
                                R.color.text_gray,
                                false
                            )
                            et_full_name.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.name)
                            et_username.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.username)
                            et_bio.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.bio)
                            et_email.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.email)
                            et_street_address.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.streetAddress)
                            et_landmark.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.landmark)
                            et_area_name.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.areaName)
                            et_city.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.city)
                            et_state_name.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.state)
                            et_zip_code.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.completeAddress?.zipCode.toString())
                            et_bank_name.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.bankName)
                            et_beneficiary_name.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.bankName)
                            et_account_number.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.accountNumber)
                            et_ifsc_code.text = Editable.Factory.getInstance().newEditable(r?.OK?.items?.get(0)?.ifseCode)

                            //tv_toolbar_title.text = r?.OK?.items?.get(0)?.username
                            userId = r?.OK?.items?.get(0)?._id ?: "";
                            username = r?.OK?.items?.get(0)?.username ?: "";
                            name = r?.OK?.items?.get(0)?.name ?: "";
//
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


