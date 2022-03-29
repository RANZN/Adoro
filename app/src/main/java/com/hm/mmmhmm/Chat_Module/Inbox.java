package com.hm.mmmhmm.Chat_Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hm.mmmhmm.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Inbox extends AppCompatActivity {

    private ImageView iv_back;
    private RelativeLayout rl_header;
    private RecyclerView chat_list;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    String thumb_image = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_header = (RelativeLayout) findViewById(R.id.rl_header);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        chat_list = (RecyclerView) findViewById(R.id.chat_list);
        //--GETTING CURRENT USER ID---
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);
        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);

    }
    @Override
    public void onStart() {
        super.onStart();

        //---ADDING THE RECYCLERVIEW TO FIREBASE DATABASE DIRECTLY----

        //--ORDERING THE MESSAGE BY TIME----
        Query conversationQuery = mConvDatabase.orderByChild("time_stamp");
        FirebaseRecyclerAdapter<Conv, Chat_User_adapter> friendsConvAdapter = new FirebaseRecyclerAdapter<Conv, Chat_User_adapter>(

                //--CLASS FETCHED FROM DATABASE-- LAYOUT OF THE SINGLE ITEM--- HOLDER CLASS(DEFINED BELOW)---QUERY
                Conv.class,
                R.layout.chat_inbox_adapter,
                Chat_User_adapter.class,
                conversationQuery
        ) {

            //---- GETTING DATA FROM DATABSE AND ADDING TO VIEWHOLDER-----
            @Override
            protected void populateViewHolder(final Chat_User_adapter convViewHolder,
                                              final Conv conv, int position) {

                final String list_user_id = getRef(position).getKey();
                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                //---IT WORKS WHENEVER CHILD OF mMessageDatabase IS CHANGED---
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        convViewHolder.setMessage(data, conv.isSeen());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //---ADDING NAME , IMAGE, ONLINE FEATURE , AND OPENING CHAT ACTIVITY ON CLICK----
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        final String image = dataSnapshot.child("thumb_image").getValue().toString();


                        if (dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            convViewHolder.setUserOnline(userOnline);

                        }
                        convViewHolder.setName(userName);
                        //   convViewHolder.setUserImage(thumb_image);

                        //--OPENING CHAT ACTIVITY FOR CLICKED USER----
                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chatIntent = new Intent(Inbox.this, Chat_Activity.class);
                                chatIntent.putExtra("firebase_user_id", list_user_id);
                                chatIntent.putExtra("User_Name", userName);
                                chatIntent.putExtra("thumb_image", image);
                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mConvDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                        if (thumb_image.equals("")) {

                        } else {
                            convViewHolder.setUserImage(thumb_image);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                mConvDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String time = dataSnapshot.child("time_stamp").getValue().toString();
                        convViewHolder.setTime(time);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

       chat_list.setAdapter(friendsConvAdapter);

    }

    public static class Chat_User_adapter extends RecyclerView.ViewHolder {

        View mView;

        public Chat_User_adapter(View itemView) {
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("ResourceAsColor")
        public void setMessage(String message, boolean isSeen) {
//
        }

        //--SETTING BOLD FOR NOT SEEN MESSAGES---
        public void setTime(String time) {
            TextView tv_time = mView.findViewById(R.id.chat_last_time);
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            long lastTime = Long.parseLong(time);
            String lastSeen = GetTimeAgo.getTimeAgo(lastTime, getTimeAgo);
            tv_time.setText("Last Message: " + lastSeen);


            //--SETTING BOLD FOR NOT SEEN MESSAGES---

        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.chat_user_name);
            userNameView.setText(name);
        }


        public void setUserImage(String userThumb) {

            CircleImageView userImageView = mView.findViewById(R.id.chat_user_image);
            //--SETTING IMAGE FROM USERTHUMB TO USERIMAGEVIEW--- IF ERRORS OCCUR , ADD USER_IMG----
            Picasso.get().load(userThumb).placeholder(R.mipmap.ic_launcher).into(userImageView);

        }


        public void setUserOnline(String onlineStatus) {

//          ImageView userOnlineView = mView.findViewById(R.id.tv_time);
//          if (onlineStatus.equals("true")) {
//              userOnlineView.setVisibility(View.VISIBLE);
//          } else {
//              userOnlineView.setVisibility(View.INVISIBLE);
//          }
        }
    }
}
