package com.hm.mmmhmm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.hm.mmmhmm.Chat_Module.MessageAdapter
import com.hm.mmmhmm.Chat_Module.Messages
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import kotlinx.android.synthetic.main.custom_toolbar.*

class ChatFragment : Fragment() {


    //chat firbase method global declare
    var mCurrentUserId: String? = null
    var mDatabaseReference: DatabaseReference? = null
    var mImageStorage: StorageReference? = null
    private val mChatUser: String? = null
    private val mAuth: FirebaseAuth? = null
    private val mRootReference: DatabaseReference? = null
    private val mMessageAdapter: MessageAdapter? = null
    private val messagesList: List<Messages> = ArrayList()
    private val mCurrentPage = 1

    //Solution for descending list on refresh
    private val itemPos = 0
    private val mLastKey = ""
    private val mPrevKey = ""
    val TOTAL_ITEM_TO_LOAD = 10
    var thumb_image = ""
    private val GALLERY_PICK = 1
    var mNotificationReference: DatabaseReference? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var user: HashMap<String, String>? = null
    var Thumb_Image1: String? = null
    var chat_user_token: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
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
            val inboxFragment = InboxFragment()
            val args = Bundle()
            inboxFragment.arguments = args
            //                args.putString("campaignId", campaignList?.get(position)?._id)
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, inboxFragment)
                .addToBackStack(null) .commit()
        })

        iv_toolbar_action_search.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, SearchFragment())
                .addToBackStack(null).commit()
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
//        List<Messages> mMessagesList
//        chat_message_list.adapter= MessageAdapter(mMessagesList, activity)


    }
}