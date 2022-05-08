package com.hm.mmmhmm.fragments

//import com.bumptech.glide.Glide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.hm.mmmhmm.Chat_Module.ChatActivity
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.GalleryAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.File


class ProfileFragment : Fragment() {
    private val TAG = "profile"
    val REQUEST_IMAGE_CAPTURE = 1
    var file: File? = null
    var image_body: MultipartBody.Part? = null

    var username: String? = null
    var name: String? = null
    var userId: String? = null

    var currentEmail = ""


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
                ?.replace(
                    R.id.frame_layout_main,
                    EditProfileFragment(),
                    EditProfileFragment::class.java.simpleName
                )
                ?.addToBackStack(EditProfileFragment::class.java.simpleName)?.commit()
        })



        btn_follow.setOnClickListener(View.OnClickListener {
            var myDetail: MyDetail = MyDetail(
                SessionManager.getUserId(),
                SessionManager.getUserName(),
                SessionManager.getUsername()
            )
            var personWhomeImFollowingData: PersonWhomeImFollowingData = PersonWhomeImFollowingData(
                requireArguments().getString("userId") ?: "",
                name,
                username
            )
            var followRequest: FollowRequest = FollowRequest(myDetail, personWhomeImFollowingData)
            followUser(followRequest)
        })

        iv_inbox.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                currentEmail,
                "test@123"
            ).addOnSuccessListener {
                val id = it.user?.uid
                pushUserToFirebase(id)
                FirebaseAuth.getInstance().signOut()
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        ChatActivity::class.java
                    ).apply {
                        putExtra("user_id", id)
                        putExtra("user_name", username)
                    }
                )
            }.addOnFailureListener {
                FirebaseAuth.getInstance().signOut()
                if (it is FirebaseAuthUserCollisionException) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        currentEmail,
                        "test@123"
                    ).addOnSuccessListener { result ->
                        val id = result.user?.uid
                        pushUserToFirebase(id)
                        FirebaseAuth.getInstance().signOut()
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                ChatActivity::class.java
                            ).apply {
                                putExtra("user_id", id)
                                putExtra("user_name", username)
                            }
                        )
                    }
                }
            }
        }

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
            var generalRequest: ProfileRequest =
                requireArguments().getString("userId")
                    ?.let { ProfileRequest(it, SessionManager.getUserId() ?: "") }!!
            getUserData(generalRequest)
        } else {
            var generalRequest: ProfileRequest =
                ProfileRequest(SessionManager.getUserId() ?: "", SessionManager.getUserId() ?: "")
            getUserData(generalRequest)
        }

    }

    private fun pushUserToFirebase(user:String?) {
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.get().addOnSuccessListener {
            try {
                val value = it.value as HashMap<String?, Any>
                value.apply {
                    put(user, HashMap<String, Any?>().apply {
                        put("userId", user)
                        put("userName", username)
                        put("email", currentEmail)
                    })
                }
                reference.updateChildren(value)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupToolBar() {
        //iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black))
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black))
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        //tv_toolbar_title.text = resources.getString(R.string.app_name)
//        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
//            (activity as MainActivity).manageDrawer()
//        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, InboxActivity::class.java))
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


    private fun getUserData(generalRequest: ProfileRequest) {
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
                                false
                            )
                            tv_name.text = r?.OK?.items?.get(0)?.name
                            tv_bio.text = r?.OK?.items?.get(0)?.bio
                            //tv_total_posts.text= r?.OK?.items?.get(0)?.bio+"Posts"
                            tv_total_fans.text = (r?.OK?.items?.get(0)?.followerData?.size?:0).toString()+" Fans"
                            total_fans.text = (r?.OK?.items?.get(0)?.followerData?.size?:0).toString()+" Fans"
                            tv_total_coins.text = (r?.OK?.items?.get(0)?.adoroCoins?:0).toString() + " A"
                            total_coins.text = (r?.OK?.items?.get(0)?.adoroCoins?:0).toString() + " A"
                            tv_toolbar_title.visibility= View.VISIBLE
                            iv_toolbar_app_icon.visibility= View.GONE
                            tv_toolbar_title.text = r?.OK?.items?.get(0)?.username
                            userId = r?.OK?.items?.get(0)?._id ?: ""
                            username = r?.OK?.items?.get(0)?.username ?: ""
                            name = r?.OK?.items?.get(0)?.name ?: ""
                            currentEmail = r?.OK?.items?.get(0)?.email ?: ""
                            if (r?.relation == "follower") {
                                ll_follow_user.visibility = View.VISIBLE
                                btn_follow.text= "Follow Back"
                                iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
                                iv_toolbar_icon.setOnClickListener(View.OnClickListener {
                                    (activity as MainActivity).onBackPressed()
                                })
                            }
                           else if(r?.relation=="following"){
                                ll_follow_user.visibility = View.VISIBLE
                                btn_follow.text= "Unfollow"
                                iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
                                iv_toolbar_icon.setOnClickListener(View.OnClickListener {
                                    (activity as MainActivity).onBackPressed()
                                })
                            }
                            else if(r?.relation=="ownProfile"){
                                ll_follow_user.visibility = View.GONE
                                iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
                                iv_toolbar_icon.setOnClickListener(View.OnClickListener {
                                    (activity as MainActivity).manageDrawer()
                                })
                            }
                            else if(r?.relation=="newVisitor"){
                                ll_follow_user.visibility = View.VISIBLE
                                btn_follow.text= "Follow"
                                iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
                                iv_toolbar_icon.setOnClickListener(View.OnClickListener {
                                    (activity as MainActivity).onBackPressed()
                                })
                            }
                            /*else if(r?.OK?.relation=="mutual"){
                                ll_follow_user.visibility = View.VISIBLE
                                btn_follow.text= "Unfollow"
                            }*/
                            //follower(follow back button to show),following(unfollow button to show), ownProfile(no button), newVisitor(follow button to show)
//                            SessionManager.setUserId(r?.OK?.items?.get(0)?._id ?: "")
//                            SessionManager.setUsername(r?.OK?.items?.get(0)?.username ?: "")
//                            SessionManager.setUserName(r?.OK?.items?.get(0)?.name ?: "")
//                            SessionManager.setUserPic(r?.OK?.items?.get(0)?.profile ?: "")
                            var showPostlRequest: GeneralRequest =
                                GeneralRequest(SessionManager.getUserId() ?: "")
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


    private fun getPosts(showPostRequest: GeneralRequest) {
        pb_prof.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getProfilePost(showPostRequest)
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
                            btn_follow.text = resources.getString(R.string.following)
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
}




