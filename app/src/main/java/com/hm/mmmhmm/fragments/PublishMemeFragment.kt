package com.hm.mmmhmm.fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.MemeDetail
import com.hm.mmmhmm.models.PublishPostRequest
import com.hm.mmmhmm.models.RequestPublishCampaign
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_publish_meme.*
import kotlinx.android.synthetic.main.fragment_publish_meme.btn_upload_design
import kotlinx.android.synthetic.main.fragment_publish_meme.iv_selected_image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.*
import java.lang.Exception


class PublishMemeFragment : Fragment() {
    private var pickedBanner: Bitmap? = null
    private val TAG = "Publish meme"
    val REQUEST_IMAGE_CAPTURE = 1
    var file: File? = null
    var image_body: MultipartBody.Part? = null
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage

            Log.i(TAG, "cropImage: $uriContent $uriFilePath")

            val bitmap = BitmapFactory.decodeFile(uriFilePath)
            pickedBanner = bitmap

            iv_selected_image.setImageBitmap(bitmap)

            if (!uriFilePath.isNullOrBlank()&& !uriFilePath.isNullOrEmpty()) {
                rl_selected_meme.visibility = View.VISIBLE
                btn_upload_design.visibility = View.GONE
            } else {
                rl_selected_meme.visibility = View.GONE
                btn_upload_design.visibility = View.VISIBLE
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
        return inflater.inflate(R.layout.fragment_publish_meme, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()

        btn_upload_design.setOnClickListener(View.OnClickListener {
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
        btn_change_image.setOnClickListener(View.OnClickListener {
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

        btn_publish.setOnClickListener(View.OnClickListener {
            validateInput()
        })

    }

    private fun validateInput() {
        val description = et_description.text.toString()
        if (description.isNullOrEmpty()) {
            toast(R.string.description, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var memeDetailReq: MemeDetail = MemeDetail(
                SessionManager.getUserId(),
                getEncoded64ImageStringFromBitmap(pickedBanner),
                description,
                SessionManager.getUserId())
            var requestPublishPost: RequestPublishCampaign = RequestPublishCampaign(
                requireArguments().getString("campaignId"),
                memeDetailReq)
          publishMeme(requestPublishPost)

        }
    }



    private fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat: ByteArray = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }


    private fun publishMeme(generalRequest: RequestPublishCampaign) {
        pb_upload_meme.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.submitMemeToCampaign(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_upload_meme.visibility = View.GONE
                        showDialog()

//                        if (response.body()?.OK != null) {
//                            val r = response.body()
////
//                        } else {
//                            Toast.makeText(
//                                activity,
//                                R.string.Something_went_wrong,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
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
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
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
                                    startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
            dialog.dismiss()
        }
        dialog.show()

    }
}