package com.hm.mmmhmm.Chat_Module

import android.os.Bundle
import android.util.Log
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
        Log.i("Sanjeev", "onCreate: called")
        init()
    }

    private fun init() {
        mAdapter = Inbox.UserAdapter(users)
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
//                            addContact(message?.receiver)
                        }
                        if (message?.receiver == SessionManager.getFirebaseID()) {
                            if (!list.contains(message?.sender))
                                list.add(message?.sender)
//                            addContact(message?.sender)
                        }
                    }
                    Log.i("Sanjeev", "onDataChange: $list")
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
                                users.add(user)
                                mAdapter?.notifyDataSetChanged()
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

    private fun containsUser(user: User?): Boolean {
        var has = false
        for (curUser in users) {
            if (curUser?.userId == user?.userId && curUser?.email == user?.email)
                has = true
        }
        return has
    }
}