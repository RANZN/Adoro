package com.hm.mmmhmm.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GroupRequestsAdapter
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.CreateGroupRequest
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_edit_group.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_group_creation.*
import kotlinx.android.synthetic.main.fragment_group_creation.btn_create_group
import kotlinx.android.synthetic.main.fragment_group_creation.et_group_about
import kotlinx.android.synthetic.main.fragment_group_creation.et_group_name
import kotlinx.android.synthetic.main.fragment_group_joining_requests.*
import kotlinx.android.synthetic.main.fragment_group_joining_requests.iv_back
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception

class EditGroup : Fragment() {

    val types = arrayOf("Private", "Public")
    private var categoryList: List<Item>? = null
    var visibilityType =types[0]
    var t:View?=null
    var category:String?=null
    private val TAG = "edit group"

    private var pickedProfile: Bitmap? = null
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage

            Log.i(TAG, "cropImage: $uriContent $uriFilePath")

            val bitmap = BitmapFactory.decodeFile(uriFilePath)

                pickedProfile = bitmap
            iv_group_pic.setImageBitmap(bitmap)

        } else {
            // an error occurred
            val exception = result.error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        t=inflater.inflate(R.layout.fragment_edit_group, container, false)
        val spinner = t?.findViewById<Spinner>(R.id.spinner_group_privacy)
        spinner?.adapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                types
            )
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        iv_back.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        btn_update_group_pic.setOnClickListener {
            EditProfileFragment.isBanner = false
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
        recycler_group_members.adapter= GroupRequestsAdapter(requireActivity())

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
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.create_group)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
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
            var createGroupRequest: CreateGroupRequest = CreateGroupRequest(category
                ,groupAbout,groupName,"",memberData,visibilityType)
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
                            val r = response.body()

                            //showDialog()
                            Toast.makeText(activity,"Group created successfully!", Toast.LENGTH_LONG).show()

                            startActivity(Intent(activity, MainActivity::class.java))
                            activity?.finish()


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
        pb_update_group.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getCategoryListForGroup()
                withContext(Dispatchers.Main) {
                    try {
                        pb_update_group.visibility = View.GONE
                        if (response.body()?.OK !=null) {
                            val r = response.body()
                            categoryList = r?.OK?.items
                            var listCategory= arrayListOf<String>()
                            for(i in 0 until categoryList?.size!!){
                                listCategory.add(categoryList!![i].name!!)
                            }
                            category = listCategory[0]
                            print("gdfgdfgdf${categoryList?.size}")
                            val spinner = t?.findViewById<Spinner>(R.id.spinner_category)
                            spinner?.adapter = activity?.applicationContext?.let {
                                ArrayAdapter(
                                    it,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    listCategory
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
                                    category= type
                                }

                            }
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