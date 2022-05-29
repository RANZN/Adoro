package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.CommentsAdapter
import com.hm.mmmhmm.adapter.MutualLikerAdapter
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_comments.et_comment
import kotlinx.android.synthetic.main.fragment_comments.iv_feed
import kotlinx.android.synthetic.main.fragment_comments.iv_like
import kotlinx.android.synthetic.main.fragment_comments.iv_liked
import kotlinx.android.synthetic.main.fragment_comments.iv_share
import kotlinx.android.synthetic.main.fragment_comments.pb_comments
import kotlinx.android.synthetic.main.fragment_comments.recycler_comments
import kotlinx.android.synthetic.main.fragment_comments.recycler_mutual_like_user
import kotlinx.android.synthetic.main.fragment_comments.scroll_controller
import kotlinx.android.synthetic.main.fragment_comments.send_message_button
import kotlinx.android.synthetic.main.fragment_comments.tv_like_count
import kotlinx.android.synthetic.main.fragment_comments.tv_like_status
import kotlinx.android.synthetic.main.fragment_group_discussion_comments.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_publish_meme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class GroupDiscussionCommentsFragment : Fragment() {
    private var feedList: List<ItemComment>? = null
    private var likes: List<PostLikeData> ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_discussion_comments, container, false)
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()


        Log.d("ghghgh",requireArguments().getString("postId")?:"");
        var generalRequest: GeneralRequest = GeneralRequest(requireArguments().getString("postId") ?: "");
        getSpecificPostDetail(generalRequest)
        send_message_button.setOnClickListener {
            validateInput()

        }

        iv_share.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:"+"test")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }

        iv_like.setOnClickListener {

            var likeData: LikeData = LikeData(
                SessionManager.getUserId(),
                SessionManager.getUserPic(),
                SessionManager.getUserPic(),
                SessionManager.getUserName()
            );
            var postLikeRequest: GroupDiscussionPostUpdateLikeRequest = GroupDiscussionPostUpdateLikeRequest(
                requireArguments().getString("postId") ?: "", likeData
            );
            postUpdateLike(
                postLikeRequest,
                iv_like,
                iv_liked,
                tv_like_count,
                likes?.size?:0
            )
        }
    }

    private fun postUpdateLike(
        postLikeRequest: GroupDiscussionPostUpdateLikeRequest,
        iv_like: ImageView,
        iv_liked: ImageView,
        tv_like_count: TextView,
        likeCount: Int
    ) {
        // pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.groupDiscussionPostUpdateLike(postLikeRequest)
                withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null) {
                            tv_like_count.text = (likeCount + 1).toString()
                            iv_like.setColorFilter(
                                ContextCompat.getColor(requireActivity(), R.color.red),
                                android.graphics.PorterDuff.Mode.MULTIPLY
                            )
                                iv_liked.visibility=View.VISIBLE
                                iv_like.visibility=View.GONE


                            var generalRequest: GeneralRequest = GeneralRequest(requireArguments().getString("postId") ?: "");
                            getSpecificPostDetail(generalRequest)
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
    private fun validateInput() {
        val comment = et_comment.text.toString()
        if (comment.isNullOrEmpty()) {
            toast(R.string.comment, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var commentData: CommentData = CommentData(
                getCurrentDate(),
                SessionManager.getUserId() ?: "",
                SessionManager.getUserPic() ?: "",
                comment,
                SessionManager.getUsername() ?: ""
            )
            var postCommentRequest: PostCommentRequest = PostCommentRequest(
                requireArguments().getString("postId") ?: "",
                commentData
            )
            postComment(postCommentRequest)

        }
    }
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.ic_back_arrow)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })
    }

    private fun getSpecificPostDetail(generalRequest: GeneralRequest) {
        pb_comments.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSpecificDiscussionDetailGroup(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_comments.visibility = View.GONE
                    onResume()
                    try {
                        //  toast("" + response.body()?.message)
                        if (response!=null) {
                            var data= response.body()?.OK?.items?.get(0)
                            for (Like in (data?.like as List<Like>)) {
                                if(Like.id==SessionManager.getUserId()){
                                    iv_liked.visibility=View.VISIBLE
                                    iv_like.visibility=View.GONE

                                }else{
                                    iv_liked.visibility=View.GONE
                                    iv_like.visibility=View.VISIBLE
                                }
                            }
                            tv_like_status.text= "and "+(data?.like as List<Like>).size.toString()+" others "+ getResources().getString(R.string.also_liked_the_post)
                            tv_like_status.visibility= if ((data.like as List<Like>).size>1) View.VISIBLE else  View.GONE

                            recycler_mutual_like_user.adapter= MutualLikerAdapter(requireActivity(),data.like as List<Like>)
                             var adapter = CommentsAdapter(
                            requireActivity(),
                            data.comment)
                            recycler_comments.adapter= adapter
                            adapter.notifyDataSetChanged()
                            likes = (data.like as List<PostLikeData>)
                          //  tv_username.text = data?.username
                            tv_title.text= data.contentTitle
                            tv_description.text= data.content
                            tv_feed_description.text = data.description
//                            iv_user_feed.load(
//                                data?.profile,
//                                R.color.text_gray,
//                                R.color.text_gray,
//                                true
//                            )
                            iv_feed.load(
                                data.image,
                                R.color.text_gray,
                                R.color.text_gray,
                                false
                            )
                            try {
                                val imm: InputMethodManager =
                                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(
                                    requireActivity().currentFocus!!.windowToken,
                                    0
                                )
                            } catch (e: java.lang.Exception) {
                                // TODO: handle exception
                            }

                            scroll_controller.fullScroll(View.FOCUS_DOWN)
                            scroll_controller.postDelayed(
                                Runnable { scroll_controller.fullScroll(ScrollView.FOCUS_DOWN) },
                                2000
                            )
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


    private fun postComment(generalRequest: PostCommentRequest) {
        pb_comments.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.grouDiscussionPostUpdateComment(generalRequest)
                withContext(Dispatchers.Main) {
                    pb_comments.visibility = View.GONE
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            if (response!=null) {
                                if(response.body()?.OK?.status=="success"){
                                    et_comment.text= Editable.Factory.getInstance().newEditable("")
                                    var generalRequest: GeneralRequest = GeneralRequest(requireArguments().getString("postId") ?: "");
                                    getSpecificPostDetail(generalRequest)
                                }else{
                                    Toast.makeText(
                                        activity,
                                        R.string.Something_went_wrong,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                Log.d("resp", "complet else: ")
                            }
                        },
                        2000 // value in milliseconds
                    )

                }

            } catch (e: Exception) {
                Log.d("weweewefw", e.toString())
            }
        }

    }



}