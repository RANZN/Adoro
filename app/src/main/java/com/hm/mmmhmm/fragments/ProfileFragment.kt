package com.hm.mmmhmm.fragments

//import com.bumptech.glide.Glide

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.Item
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ProfileFragment : Fragment() {
    private val TAG = "profile"
    val REQUEST_IMAGE_CAPTURE = 1
    var file: File? = null
    var image_body: MultipartBody.Part? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        rv_gallery.adapter= GalleryAdapter()
      //  SessionManager.init(activity as Context)
        iv_camera.setOnClickListener(View.OnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout_main, EditProfileFragment())
                ?.addToBackStack(null)?.commit()
        })

//        iv_camera.setOnClickListener(View.OnClickListener {
////            CropImage.startPickImageActivity(activity as Activity)
//            dispatchTakePictureIntent()
//        })

//        iv_profile_pic_profile.load(
//            SessionManager.getUserData()?.profilePicture,
//            R.color.text_gray,
//            R.color.text_gray,
//            true
//        )
    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.profile)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }


    override fun onResume() {
        super.onResume()
        if (ConnectivityObserver.isOnline(activity as Context)) {
            //getUserDetailAPI()
        }

    }


    private fun getUserDetailAPI() {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface.getUserDetailApi()
            withContext(Dispatchers.Main) {
                if (activity != null && pb_prof != null) {
                    pb_prof?.visibility = View.GONE
                }

                try {
                    if (response.isSuccessful) {
                        val model = response.body()

//                        iv_profile_pic_profile.load(
//                            model?.data?.profilePicture.toString(),
//                            R.color.text_gray,
//                            R.color.text_gray,
//                            true
//                        )
//                        tv_name_prof.setText(model?.data?.name)
//                        tv_email_prof.setText(model?.data?.email)
//                        tv_address_prof.setText(model?.data?.address)
//                        tv_phone_prof.setText(model?.data?.phone)


                    } else {

//                        if (response.code().toString().equals("401")) {
//                            MainActivity().logoutUserFromAppSession()
//                        }
                        //toast(response.code().toString())

                    }
                } catch (e: Exception) {

//                    Toast.makeText(activity,""+e.toString(), Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "profile exception " + e.toString())
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            user_profile_ep.setImageBitmap(imageBitmap)
            Glide.with(this).load(imageBitmap).apply(RequestOptions.circleCropTransform())
                .into(iv_profile_pic_profile)
            convertToFile(imageBitmap)
        }
    }

    private fun convertToFile(bitmap: Bitmap) {
        file = File(activity?.cacheDir, "propic")
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()

//        ----------CALLING API--------
        if (file != null) {
            image_body = MultipartBody.Part.createFormData(
                "image", file?.name,
                (RequestBody.create(MediaType.parse("multipart/form-data"), file))
            )
            updateProfilePicAPI() //API CALL<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }

    }


    private fun updateProfilePicAPI() {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface.updateProfilePicApi(image_body!!)
            withContext(Dispatchers.Main) {
                if (activity != null && pb_prof != null) {
                    pb_prof?.visibility = View.GONE
                    SessionManager.init(activity as Context)
                }
                try {
                    if (response.isSuccessful) {
                        val data = SessionManager.getUserData()
                        data?.profilePicture = (response.body()?.data?.get(0) ?: "")
                        iv_profile_pic_profile.load(
                            SessionManager.getUserData()?.profilePicture,
                            R.color.text_gray,
                            R.color.text_gray,
                            true
                        )
                        //toast(response.body()?.message.toString())
                    } else {
                       // toast(response.message().toString())
                    }

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "Exception: " + e.toString())
                }

            }
        }
    }


}



