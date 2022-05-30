package com.hm.mmmhmm.Chat_Module

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.SessionManager
import kotlinx.android.synthetic.main.activity_inbox.*

class InboxActivity : AppCompatActivity() {
    var users: ArrayList<User?> = ArrayList()
    private var mAdapter: Inbox.UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
    }

    override fun onResume() {
        super.onResume()
        init()
//        for (user in users) {
//            hasUnread(user)
//        }
    }

    private fun init() {
        mAdapter = Inbox.UserAdapter(users)
        iv_back.setOnClickListener {
            onBackPressed()
        }
        pb_inbox.visibility = View.VISIBLE
        chat_list.adapter = mAdapter

        iv_search.setOnClickListener {
            val filtered = ArrayList<User?>()
            users.forEach {
                if (it?.userName?.contains(et_search.text.toString().trim(), true) == true) {
                    filtered.add(it)
                }
            }
            Log.i("Sanjeev", "init: $filtered")
            mAdapter?.updateUsers(filtered)
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val filtered = ArrayList<User?>()
                users.forEach {
                    if (it?.userName?.contains(et_search.text.toString().trim(), true) == true) {
                        filtered.add(it)
                    }
                }
                Log.i("Sanjeev", "init: $filtered")
                mAdapter?.updateUsers(filtered)
            }
        })
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
            }.addOnCompleteListener {
                for (user in users) {
                    hasUnread(user)
                }
            }
    }

    private fun hasUnread(data: User?) {
        FirebaseDatabase.getInstance().getReference("chats").orderByChild("time")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)
                    Log.i("Sanjeev", "onChildAdded: $message")
//                    if (!checked) {
                    if (message?.sender == data?.userId && message?.receiver == SessionManager.getFirebaseID() && message?.seen == false) {
                        Log.i("Sanjeev", "onChildAdded: $message")
                        mAdapter?.updateChild(message.sender)
                    }
//                        mList.add(message)
//                        (chat_message_list.layoutManager as LinearLayoutManager).scrollToPosition(
//                            mList.size - 1
//                        )
//                        mAdapter.notifyDataSetChanged()
//                    } else {
//                        mAdapter?.updateChild(message?.sender)
//                    }
//                        checked=true
//                    }
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

    private fun containsUser(user: User?): Boolean {
        var has = false
        for (curUser in users) {
            if (curUser?.userId == user?.userId && curUser?.email == user?.email)
                has = true
        }
        return has
    }
}