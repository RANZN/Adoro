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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hm.mmmhmm.Chat_Module.Inbox
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.AdoroCoinsAdapter
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_adoro_coins.*
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

    var username: String?=null
    var name: String ?=null
    var userId: String?=null


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

        //  SessionManager.init(activity as Context)
//        myProfileView = requireArguments().getInt("viewType")?:0
//        if (myProfileView==1){
//            iv_camera.visibility= View.GONE
//            progress_profile_complete.visibility= View.GONE
//            progress_profile_complete.visibility= View.GONE
//            tv_completed_total.visibility= View.GONE
//        }
        iv_camera.setOnClickListener(View.OnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout_main, EditProfileFragment())
                ?.addToBackStack(null)?.commit()
        })



        btn_follow.setOnClickListener(View.OnClickListener {
            var myDetail: MyDetail = MyDetail(
                SessionManager.getUserId(),
                SessionManager.getUserName(),
                SessionManager.getUsername()
            );
            var personWhomeImFollowingData: PersonWhomeImFollowingData = PersonWhomeImFollowingData(
                requireArguments().getString("userId") ?: "",
                name,
                username
            );
            var followRequest: FollowRequest = FollowRequest(myDetail, personWhomeImFollowingData);
            followUser(followRequest)
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

        if (requireArguments().getString("path") == "search") {
            iv_camera.visibility = View.GONE
            var generalRequest: GeneralRequest =
                requireArguments().getString("userId")?.let { GeneralRequest(it) }!!;
            getUserData(generalRequest)
        } else {
            var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId() ?: "");
            getUserData(generalRequest)
        }

    }

    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        //  tv_toolbar_title.text = resources.getString(R.string.app_name)
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


    override fun onResume() {
        super.onResume()
        if (ConnectivityObserver.isOnline(activity as Context)) {
            //getUserDetailAPI()
        }

    }


    private fun getUserData(generalRequest: GeneralRequest) {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getUserData(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_prof.visibility = View.GONE
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
                                true
                            )
                            tv_name.text = r?.OK?.items?.get(0)?.name
                            tv_bio.text = r?.OK?.items?.get(0)?.bio
                            //tv_total_posts.text= r?.OK?.items?.get(0)?.bio+"Posts"
                            tv_total_fans.text = r?.OK?.items?.get(0)?.followerData?.size.toString()+"Fans"
                            tv_total_coins.text = r?.OK?.items?.get(0)?.adoroCoins.toString() + "A"
                            tv_toolbar_title.text = r?.OK?.items?.get(0)?.username
                            userId = r?.OK?.items?.get(0)?._id ?: "";
                            username = r?.OK?.items?.get(0)?.username ?: "";
                            name = r?.OK?.items?.get(0)?.name ?: "";
                            if (SessionManager.getUserId().equals(r?.OK?.items?.get(0)?._id)) {
                                ll_follow_user.visibility = View.GONE
                            }else{
                                ll_follow_user.visibility = View.VISIBLE
                            }
//                            SessionManager.setUserId(r?.OK?.items?.get(0)?._id ?: "")
//                            SessionManager.setUsername(r?.OK?.items?.get(0)?.username ?: "")
//                            SessionManager.setUserName(r?.OK?.items?.get(0)?.name ?: "")
//                            SessionManager.setUserPic(r?.OK?.items?.get(0)?.profile ?: "")
                            var showPostlRequest: ShowPostlRequest =
                                ShowPostlRequest(SessionManager.getUserId() ?: "");
                            getPosts(showPostlRequest)
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

    private fun getPosts(showPostRequest: ShowPostlRequest) {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.showPost(showPostRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_prof.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            rv_gallery.adapter = GalleryAdapter(r?.OK?.items)
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

    private fun followUser(followRequest: FollowRequest) {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.follow(followRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_prof.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            //todo
                            btn_follow.text= resources.getString(R.string.following)
                                Toast.makeText(requireActivity(), r?.OK?.status, Toast.LENGTH_SHORT)
                                    .show()

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
            // updateProfilePicAPI() //API CALL<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
    }

//    private fun updateProfilePicAPI( ) {
//        pb_prof.visibility = View.VISIBLE
//        val apiInterface = ApiClient.getRetrofitService(requireContext())
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = apiInterface.getUserData(generalRequest)
//                withContext(Dispatchers.Main) {
//                    try {
//                        pb_prof.visibility = View.GONE
//                        if (response.body()?.OK !=null) {
//                            val r = response.body()
//
////                        iv_profile_pic_profile.load(
////                            model?.data?.profilePicture.toString(),
////                            R.color.text_gray,
////                            R.color.text_gray,
////                            true
////                        )
//                            tv_name.text= r?.OK?.items?.get(0)?.name
//                            tv_toolbar_title.text =r?.OK?.items?.get(0)?.username
////                        tv_email_prof.setText(model?.data?.email)
////                        tv_address_prof.setText(model?.data?.address)
////                        tv_phone_prof.setText(model?.data?.phone)
//                        } else {
//                            Toast.makeText(activity,R.string.Something_went_wrong, Toast.LENGTH_SHORT).show()
//                        }
//                    } catch (e: java.lang.Exception) {
//                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


}



