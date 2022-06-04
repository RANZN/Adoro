package com.hm.mmmhmm.Chat_Module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.hm.mmmhmm.helper.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Inbox extends AppCompatActivity {

    String thumb_image = "";
    private ImageView iv_back;
    private RelativeLayout rl_header;
    private RecyclerView chat_list;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        rl_header = findViewById(R.id.rl_header);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        chat_list = findViewById(R.id.chat_list);
        //--GETTING CURRENT USER ID---
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = SessionManager.INSTANCE.getUserId();  //Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);
        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);

        Log.i("TAG", "initView: " + mCurrent_user_id);

    }

    @Override
    public void onStart() {
        super.onStart();

        //---ADDING THE RECYCLERVIEW TO FIREBASE DATABASE DIRECTLY----

//        mUsersDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<User> users = new ArrayList<>();
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    User value = snap.getValue(User.class);
//                    users.add(value);
//                }
//
//                chat_list.setAdapter(new UserAdapter(users));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
                                Intent chatIntent = new Intent(Inbox.this, ChatActivity.class);
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

    static class UserAdapter extends RecyclerView.Adapter<Chat_User_adapter> {
        List<User> users;

        public UserAdapter(List<User> user) {
            this.users = user;
        }

        @NonNull
        @Override
        public Chat_User_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Chat_User_adapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_inbox_adapter, parent, false),parent.getContext());
        }

        @Override
        public void onBindViewHolder(@NonNull Chat_User_adapter holder, int position) {
            holder.setName(users.get(position).getUserName());
            holder.onItemClick(users.get(position));
            holder.setTime(users.get(position).getTime());
            holder.setMessage(users.get(position).getLastMessage(), false);

        }
        public void updateUsers(List<User> list) {
            users=list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    public static class Chat_User_adapter extends RecyclerView.ViewHolder {

        View mView;
        Context mContext;

        public Chat_User_adapter(View itemView,Context context ) {
            super(itemView);
            mView = itemView;
            mContext = context;
        }

        @SuppressLint("ResourceAsColor")
        public void setMessage(String message, boolean isSeen) {
            TextView textView = mView.findViewById(R.id.last_message);
            textView.setText(message+": ");
        }

        //--SETTING BOLD FOR NOT SEEN MESSAGES---
        public void setTime(String time) {
            TextView tv_time = mView.findViewById(R.id.last_time);
//            GetTimeAgo getTimeAgo = new GetTimeAgo();
//            long lastTime = Long.parseLong(time);
//            String lastSeen = GetTimeAgo.getTimeAgo(lastTime, getTimeAgo);
            tv_time.setText(time);


            //--SETTING BOLD FOR NOT SEEN MESSAGES---

        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.chat_user_name);
            userNameView.setText(name);
        }


        public void setUserImage(String userThumb) {

            CircleImageView userImageView = mView.findViewById(R.id.chat_user_image);

            try {
                Glide.with(mContext)
                        .load(Uri.parse(userThumb))
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(userImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void setUserOnline(String onlineStatus) {

//          ImageView userOnlineView = mView.findViewById(R.id.tv_time);
//          if (onlineStatus.equals("true")) {
//              userOnlineView.setVisibility(View.VISIBLE);
//          } else {
//              userOnlineView.setVisibility(View.INVISIBLE);
//          }
        }

        public void onItemClick(User user) {
            mView.findViewById(R.id.user_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mView.getContext(), ChatActivity.class);
                    intent.putExtra("user_name", user.getUserName());
                    intent.putExtra("user_id", user.getUserId());
                    intent.putExtra("isOnline", user.isOnline());
                    intent.putExtra("id", user.getId());
                    intent.putExtra("profile", user.getProfile());
                    mView.getContext().startActivity(intent);
                }
            });

        }


    }
}
