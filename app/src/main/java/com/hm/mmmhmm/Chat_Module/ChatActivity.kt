package com.hm.mmmhmm.Chat_Module

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.adapter.MessageAdapter
import com.hm.mmmhmm.fragments.HomeFragment
import com.hm.mmmhmm.fragments.ProfileFragment
import com.hm.mmmhmm.helper.SessionManager
import kotlinx.android.synthetic.main.activity__chat.*

class ChatActivity : AppCompatActivity() {
    private var mList: ArrayList<Message?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__chat)
        init()
        setupChatList(SessionManager.getFirebaseID(), intent.getStringExtra("user_id"))
        registerToToggleOnlineOffline()
        registerSeenListener(intent.getStringExtra("user_id"))
    }

    private fun init() {
        tv_header.text = intent.getStringExtra("user_name")
        val message = et_message.text
        ll_user.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java)
                .putExtra("tag","chat")
                .putExtra("userId",intent.getStringExtra("user_id"))
            )
        }
        send_message_button.setOnClickListener {
            if (message.isNullOrEmpty()) {
                Toast.makeText(this, R.string.enter_your_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sendMessage(
                intent.getStringExtra("user_id"),
                et_message.text.toString().trim()
            )
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun registerToToggleOnlineOffline() {
        FirebaseDatabase.getInstance().getReference("users")
            .child(intent.getStringExtra("user_id") ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                Log.i(TAG, "onDataChange: ")
                    val value = snapshot.value as Map<String, Any?>
                    if (value.containsKey("isOnline") && value["isOnline"] == true) {
                        status.visibility = View.VISIBLE
                    } else {
                        status.visibility = View.GONE
                    }
                    // TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO("Not yet implemented")
                }

            })
    }

    private fun registerSeenListener(receiver: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //TODO("Not yet implemented")
                    for (it in snapshot.children) {
                        val chat = it.getValue(Message::class.java)
                        if (chat?.receiver == SessionManager.getFirebaseID() && chat?.sender == receiver) {
                            it.ref.updateChildren(HashMap<String, Any?>().apply {
                                put("seen", true)
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO("Not yet implemented")
                }

            })
    }

    private fun sendMessage(receiver: String?, message: String) {
        val time = ServerValue.TIMESTAMP
        val hashmap = HashMap<String, Any?>().apply {
            put("sender", SessionManager.getFirebaseID())
            put("receiver", receiver)
            put("message", message)
            put("time", time)
            put("type", "text")
            put("seen", false)
        }
        FirebaseDatabase.getInstance().reference.child("chats").push().setValue(hashmap)
        FirebaseDatabase.getInstance().getReference("message")
            .child(SessionManager.getFirebaseID() ?: "").setValue(HashMap<String, Any?>().apply {
                put("message", message)
                put("time", time)
            })
        FirebaseDatabase.getInstance().getReference("message").child(receiver ?: "")
            .setValue(HashMap<String, Any?>().apply {
                put("message", message)
                put("time", time)
            })
        et_message.setText("")
    }

    private fun setupChatList(first: String?, second: String?) {
        val mAdapter = MessageAdapter(this, mList)
        chat_message_list.adapter = mAdapter

        FirebaseDatabase.getInstance().getReference("chats").orderByChild("time")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message?.receiver == first && message?.sender == second || message?.sender == first && message?.receiver == second) {
                        mList.add(message)
                        (chat_message_list.layoutManager as LinearLayoutManager).scrollToPosition(
                            mList.size - 1
                        )
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // TODO("Not yet implemented")
                    val updated = snapshot.getValue(Message::class.java)
                    for (it in mList.indices) {
                        val item = mList[it]
                        if (item?.sender == updated?.sender && item?.receiver == updated?.receiver && item?.message == updated?.message && item?.time == updated?.time) {
                            mList[it] = updated
                            // mList[it]?.seen = true
                            mAdapter.notifyDataSetChanged()
                            break
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // TODO("Not yet implemented")
                }

            override fun onCancelled(error: DatabaseError) {
                // TODO("Not yet implemented")
            }
        })
    }
}