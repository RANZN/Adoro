package com.hm.mmmhmm.Chat_Module

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.SessionManager
import kotlinx.android.synthetic.main.activity_inbox.*

class InboxActivity : AppCompatActivity() {
    var users: ArrayList<User?> = ArrayList()
    private var mAdapter: Inbox.UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        init()
    }

    private fun init() {
        mAdapter = Inbox.UserAdapter(users)
        iv_back.setOnClickListener {
            onBackPressed()
        }
        pb_inbox.visibility = View.VISIBLE
        chat_list.adapter = mAdapter
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<String?>()
                    snapshot.children.forEach {
                        val message = it.getValue(Message::class.java)
                        if (message?.sender == SessionManager.getFirebaseID()) {
                            if (!list.contains(message?.receiver))
                                list.add(message?.receiver)
                        }
                        if (message?.receiver == SessionManager.getFirebaseID()) {
                            if (!list.contains(message?.sender))
                                list.add(message?.sender)
                        }
                    }
                    pb_inbox.visibility = View.GONE

                    addContact(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO("Not yet implemented")
                }

            })
    }

    private fun addContact(userIds: List<String?>) {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        var user: User? = null
                        try {
                            user = child.getValue(User::class.java)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        Log.i("TAG", "onDataChange: $user")
                        if (userIds.contains(user?.userId)) {
                            if (!containsUser(user)) {
                                lastMessage(user)
                            }
                        }
                    }
                    Log.i("TAG", "onDataChange: $users")

                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO("Not yet implemented")
                }
            })
    }

    private fun lastMessage(data: User?) {
        FirebaseDatabase.getInstance().getReference("message").child(data?.userId ?: "").get()
            .addOnSuccessListener {
                val value = it.value as Map<String, Any?>
                data?.lastMessage = value["message"] as String?
                data?.time = GetTimeAgo.getTimeAgo(value["time"].toString().toLong(), null)
                users.add(data)
                mAdapter?.notifyDataSetChanged()
            }
    }

    private fun containsUser(user: User?): Boolean {
        var has = false
        for (curUser in users) {
            if (curUser?.userId == user?.userId && curUser?.email == user?.email)
                has = true
        }
        return has
    }
}