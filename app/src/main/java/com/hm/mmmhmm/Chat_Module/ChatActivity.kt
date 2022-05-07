package com.hm.mmmhmm.Chat_Module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.SessionManager
import kotlinx.android.synthetic.main.activity__chat.*

class ChatActivity : AppCompatActivity() {
    private var mList: ArrayList<Message?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__chat)

        init()
        setupChatList(SessionManager.getFirebaseID(), intent.getStringExtra("user_id"))
    }

    private fun init() {
        tv_header.text = intent.getStringExtra("user_name")
        send_message_button.setOnClickListener {
            sendMessage(
                intent.getStringExtra("user_id"),
                et_message.text.toString()
            )
        }
    }

    private fun sendMessage(receiver: String?, message: String) {
        val hashmap = HashMap<String, Any?>().apply {
            put("sender", SessionManager.getFirebaseID())
            put("receiver", receiver)
            put("message", message)
            put("time", ServerValue.TIMESTAMP)
            put("type", "text")
            put("seen", false)
        }
        FirebaseDatabase.getInstance().reference.child("chats").push().setValue(hashmap)
        et_message.setText("")
    }

    private fun setupChatList(first: String?, second: String?) {
        val mAdapter = MessageAdapter(mList, this)
        chat_message_list.adapter = mAdapter

        val reference = FirebaseDatabase.getInstance().getReference("chats")
        reference.ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message?.receiver == first && message?.sender == second || message?.sender == first && message?.receiver == second) {
                    mList.add(message)
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // TODO("Not yet implemented")
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