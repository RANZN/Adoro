package com.hm.mmmhmm.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hm.mmmhmm.Chat_Module.InboxActivity
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.FeedListAdapter
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.GeneralRequest
import com.hm.mmmhmm.models.ItemComment
import com.hm.mmmhmm.models.ShowPostlRequest
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private var feedList: List<ItemComment>? = null
    private  var sessionId: Long?=null
    lateinit  var feedListAdapter: FeedListAdapter

    companion object {
         var lastFirstVisiblePosition : Int=0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var root: View = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }
    private fun setupToolBar() {
        iv_toolbar_icon.setBackgroundResource(R.drawable.hamburger_icon)
        iv_toolbar_action_inbox.setBackgroundResource(R.drawable.chat)
        iv_toolbar_action_search.setBackgroundResource(R.drawable.iv_search)
        iv_toolbar_icon.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_inbox.setColorFilter(resources.getColor(R.color.black));
        iv_toolbar_action_search.setColorFilter(resources.getColor(R.color.black));
        tv_toolbar_title.setTextColor(resources.getColor(R.color.black))
        tv_toolbar_title.text = resources.getString(R.string.app_name)
        iv_toolbar_icon.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).manageDrawer()
        })

        iv_toolbar_action_inbox.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, InboxActivity::class.java))
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
//        var generalRequest: GeneralRequest = GeneralRequest(SessionManager.getUserId()?:"");
//        getSessionForPosts(generalRequest)
        getFeedListAPI()
    }
    private fun getSessionForPosts(generalRequest: GeneralRequest) {
        pb_feeds.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getSessionID(generalRequest)
                withContext(Dispatchers.Main) {
                    try {
                        pb_feeds.visibility = View.GONE
                        if (response.body()?.OK != null) {
                            val r = response.body()
                            sessionId= r?.OK?.items?.get(0)?.sessionId
                            var showPostlRequest: ShowPostlRequest =
                                ShowPostlRequest(r?.OK?.items?.get(0)?.sessionId ?:0);
                            //getFeedListAPI(showPostlRequest)
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

    private fun getFeedListAPI() {
        pb_feeds.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getFeed()
                withContext(Dispatchers.Main) {
                    pb_feeds.visibility = View.GONE
                    try {
                        if (response!=null) {
                            feedList = response.body()?.OK?.items
                            getContest()
                            feedListAdapter= FeedListAdapter(requireActivity(),feedList, sessionId, pb_feeds)
                            recycler_feed_list.adapter= feedListAdapter
                            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPosition(lastFirstVisiblePosition)
                            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPosition(SessionManager.getFeedLastPosition())
                            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPositionWithOffset(SessionManager.getFeedLastPosition(),0)
//                            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPosition(3)
//                            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPositionWithOffset(3,0)
                           feedListAdapter.notifyDataSetChanged()
                            Log.d("lastFirstVisiblePosition", lastFirstVisiblePosition.toString())
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

    private fun getContest() {
        pb_feeds.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.getContest()
                withContext(Dispatchers.Main) {
                    pb_feeds.visibility = View.GONE
                    try {
                        if (response!=null) {
                            iv_contest.load(
                                response.body()?.OK?.featuredImage?:"https://static.wixstatic.com/media/6efda8_6ec75aa2cdc7452fa80e02b7d43520d8~mv2.png/v1/fill/w_207,h_75,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/Webhood%20official%20logo%20banner.png",
                                R.color.text_gray,
                                R.color.text_gray,
                                false
                            )
                            iv_contest.setOnClickListener {
                                val postDetailFragment = PostDetailFragment()
                                val args = Bundle()
                                args.putString("campaignId", response.body()?.OK?._id)
                                postDetailFragment.arguments = args
                                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, postDetailFragment).addToBackStack(null).commit()

                            }
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

//    override fun onPause() {
//        lastFirstVisiblePosition =
//            (recycler_feed_list.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
//
//        super.onPause()
//    }

//    override fun onResume() {
//        (recycler_feed_list.getLayoutManager() as LinearLayoutManager).scrollToPosition(lastFirstVisiblePosition)
//
//        super.onResume()
//    }



}


