package com.hm.mmmhmm.fragments

import android.Manifest
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.models.PostGroupRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_group_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class GroupPost : Fragment() {


    private var pickedBanner: Bitmap? = null

    private val TAG = "group post Fragment"
    var file: String? = null
    var postType:Int=0
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
            file = result.getUriFilePath(requireContext()).toString() // optional usage

            Log.i(TAG, "cropImage: $uriContent $uriFilePath")

            val bitmap = BitmapFactory.decodeFile(uriFilePath)
            pickedBanner = bitmap


            iv_selected_image.setImageBitmap(bitmap)

            if (!uriFilePath.isNullOrBlank()&& !uriFilePath.isNullOrEmpty()) {
                rl_selected_image.visibility = View.VISIBLE
                ll_after_image_selection.visibility = View.VISIBLE
                ll_choose_image.visibility = View.GONE
                val bits: List<String> = (file?:"").split("/")
                val lastOne = bits[bits.size - 1]
                tv_file_name.text= lastOne
            } else {
                rl_selected_image.visibility = View.GONE
                ll_after_image_selection.visibility = View.GONE
                btn_choose_design.visibility = View.VISIBLE
            }


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_post, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        postType= requireArguments().getInt("postType")?:0
        setupToolBar()
        ll_choose_image.setOnClickListener(View.OnClickListener {
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
        })
        btn_change_selected_image.setOnClickListener(View.OnClickListener {
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
        })
//"postType":"ANNOUNCEMENT/GROUPFEED/DISCUSSION"

        btn_upload_design.setOnClickListener {
            if (postType==0){
                //{"message":"This is my new message for a new upaomcing project", "profile":"base 64 data",
                    // "buttonLink":"https://www.linkme.com",
                    // "groupId":"74f161c1-9ec5-4e92-bea0-f3d21749d207",
                        // "postType":"ANNOUNCEMENT/GROUPFEED/DISCUSSION"}
                var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                        "",
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "ANNOUNCEMENT",
                        "",
                        SessionManager.getUsername(),
                        "",
                        SessionManager.getUserPic(),
                        "",
                        et_description_group_post.text.toString(),
                        ""
                    )
                publishPost(postGroupRequest)

            }
            else  if (postType==1){
                var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                        et_description_group_post.text.toString(),
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "GROUPFEED",
                        SessionManager.getUserPic(),
                        SessionManager.getUsername(),
                        et_description_group_post.text.toString(),
                        getEncoded64ImageStringFromBitmap(pickedBanner),
                        "",
                        et_description_group_post.text.toString(),
                        ""
                    )
                publishPost(postGroupRequest)

            } else  if (postType==2){
               var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                        et_description_group_post.text.toString(),
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "DISCUSSION",
                        SessionManager.getUserPic(),
                        SessionManager.getUsername(),
                        et_description_group_post.text.toString(),
                        getEncoded64ImageStringFromBitmap(pickedBanner),
                        "",
                        et_description_group_post.text.toString(),
                        et_description_group_post_content.text.toString()
                    )
                publishPost(postGroupRequest)

            }

        }

        btn_post.setOnClickListener {
            if (postType==0){
                //{"message":"This is my new message for a new upaomcing project", "profile":"base 64 data",
                // "buttonLink":"https://www.linkme.com",
                // "groupId":"74f161c1-9ec5-4e92-bea0-f3d21749d207",
                // "postType":"ANNOUNCEMENT/GROUPFEED/DISCUSSION"}
                var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                        "",
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "ANNOUNCEMENT",
                        "",
                        SessionManager.getUsername(),
                        "",
                        SessionManager.getUserPic(),
                        "",
                        et_description_group_post.text.toString(),
                        ""
                    )
                publishPost(postGroupRequest)

            }
            else  if (postType==1){
                var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                        et_description_group_post.text.toString(),
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "ANNOUNCEMENT",
                        SessionManager.getUserPic(),
                        SessionManager.getUsername(),
                        et_description_group_post.text.toString(),
                        getEncoded64ImageStringFromBitmap(pickedBanner),
                        "",
                        et_description_group_post.text.toString(),
                        ""
                    )
                publishPost(postGroupRequest)

            } else  if (postType==2){
                var postGroupRequest: PostGroupRequest =
                    PostGroupRequest(
                        listOf<Any>(),
                       et_description_group_post.text.toString(),
                        requireArguments().getString("groupId")?:"",
                        listOf<Any>(),
                        "DISCUSSION",
                        SessionManager.getUserPic(),
                        SessionManager.getUsername(),
                        et_description_group_post.text.toString(),
                        getEncoded64ImageStringFromBitmap(pickedBanner),
                        "",
                        et_description_group_post.text.toString(),
                        et_description_group_post_content.text.toString(),
                    )
                publishPost(postGroupRequest)

            }

        }

    }

    private fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat: ByteArray = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }


    private fun publishPost(generalRequest: PostGroupRequest) {
        pb_publish_post.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.groupPost(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_publish_post.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            (activity as MainActivity).onBackPressed()
                            if (r?.OK?.status=="success"){

                            }
//
                        } else {
                            Toast.makeText(
                                activity,
                                R.string.Something_went_wrong,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
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
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black))
        tv_toolbar_title.visibility=View.VISIBLE
        if(requireArguments().getInt("postType")==0){
            tv_toolbar_title.text = resources.getString(R.string.announcement)
            rl_meme.visibility=View.GONE
            btn_post.visibility=View.VISIBLE
        }else if(requireArguments().getInt("postType")==1){
            tv_toolbar_title.text = resources.getString(R.string.post)
            btn_post.visibility=View.VISIBLE
            btn_post.visibility=View.GONE
        }else if(requireArguments().getInt("postType")==2){
            tv_toolbar_title.text = resources.getString(R.string.discussion)
            rl_meme.visibility=View.GONE
            btn_post.visibility=View.VISIBLE
            et_description_group_post_content_card.visibility=View.VISIBLE
        }
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })


    }

    companion object {

    }
}