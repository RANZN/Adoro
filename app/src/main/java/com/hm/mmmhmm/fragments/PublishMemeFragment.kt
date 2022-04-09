package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.RequestCampaign
import com.hm.mmmhmm.models.RequestPublishPost
import com.hm.mmmhmm.models.RequestRegister
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_publish_meme.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception


class PublishMemeFragment : Fragment() {
    private val TAG = "Publish meme"
    val REQUEST_IMAGE_CAPTURE = 1
    var file: File? = null
    var image_body: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish_meme, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        btn_upload_design.setOnClickListener(View.OnClickListener {
//            val intent = Intent()
//                .setType("*/*")
//                .setAction(Intent.ACTION_GET_CONTENT)
//
//            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

             dispatchTakePictureIntent()
        })

        btn_publish.setOnClickListener(View.OnClickListener {
            validateInput()
        })
        btn_change_image.setOnClickListener(View.OnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        })


    }

    private fun validateInput() {
        val description = et_description.text.toString()
        if (description.isNullOrEmpty()) {
            toast(R.string.description, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            val comment = listOf<Any>()
            val like = listOf<Any>()
            var requestPublishPost: RequestPublishPost = RequestPublishPost(
                comment,
                description,
                file?.path.toString(),
                like,
                "",
                "")
          //  publishMeme(requestPublishPost)
            showDialog()
        }
    }


    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })


    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            user_profile_ep.setImageBitmap(imageBitmap)
            Glide.with(this).load(imageBitmap).apply(RequestOptions.noTransformation())
                .into(iv_selected_image)
            convertToFile(imageBitmap)
        }
    }

    private fun convertToFile(bitmap: Bitmap) {
        file = File(activity?.cacheDir, "propic")
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()

//        ----------CALLING API--------
//        if (file != null) {
//            image_body = MultipartBody.Part.createFormData(
//                "image", file?.name,
//                (RequestBody.create(MediaType.parse("multipart/form-data"), file))
//            )
         //   showDialog()
            // publishMeme() //API CALL<<<<<<<<<<<<<<<<<<<<<<<<<<<<
      //  }

    }

    /*private fun publishMeme( requestPublishPost: RequestPublishPost) {
        pb_upload_meme.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface.publishPostAPI(image_body!!,requestPublishPost)
            withContext(Dispatchers.Main) {
                if (activity != null && pb_upload_meme != null) {
                    pb_upload_meme?.visibility = View.GONE
                    SessionManager.init(activity as Context)
                }
                try {
                    if (response.body()?.OK!=null) {
                        showDialog()

//                        val data = SessionManager.getUserData()
//                        data?.profilePicture = (response.body()?.data?.get(0) ?: "")
//                        iv_selected_image.load(
//                            SessionManager.getUserData()?.profilePicture,
//                            R.color.text_gray,
//                            R.color.text_gray,
//                            true
//                        )
                        //toast(response.body()?.message.toString())
                    } else {
                        // toast(response.message().toString())
                    }

                } catch (e: Exception) {
                    Log.d(TAG, "Exception: " + e.toString())
                }

            }
        }
    }*/

    private fun showDialog() {
        val dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.custom_dialog_publish_meme)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        val body = dialog.findViewById(R.id.tvBody) as TextView
        //body.text = title
        val title = dialog.findViewById(R.id.tvTitle) as TextView
        // body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_ok) as Button
        yesBtn.setOnClickListener {
            //todo
            dialog.dismiss()
        }
        dialog.show()

    }
}