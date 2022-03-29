package com.hm.mmmhmm.Chat_Module;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.hm.mmmhmm.R;
import com.hm.mmmhmm.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat_Activity extends AppCompatActivity {

    private ImageView menu_button;
    private LinearLayout send_offer_rel;
    private RecyclerView chat_message_list;
    private AppCompatEditText messageET;
    //chat firbase method global declare
    String mCurrentUserId;
    DatabaseReference mDatabaseReference;
    StorageReference mImageStorage;
    private String mChatUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootReference;
    private MessageAdapter mMessageAdapter;
    private final List<Messages> messagesList = new ArrayList<>();
    private int mCurrentPage = 1;
    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    public static final int TOTAL_ITEM_TO_LOAD = 10;
    String thumb_image = "";
    private static final int GALLERY_PICK = 1;
    DatabaseReference mNotificationReference;
    LinearLayoutManager mLinearLayoutManager;
    TextView chat_user_name;
    HashMap<String, String> user;
    private ImageView send_message_button;
    private AppCompatEditText et_message;
    String Thumb_Image1;
    String chat_user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__chat);
        initView();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initView() {
        menu_button = (ImageView) findViewById(R.id.iv_back);
        chat_message_list = (RecyclerView) findViewById(R.id.chat_message_list);
        chat_user_name = findViewById(R.id.tv_header);
        et_message = findViewById(R.id.et_message);
        send_message_button = findViewById(R.id.send_message_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mChatUser = getIntent().getStringExtra("firebase_user_id");
        chat_user_name.setText(getIntent().getStringExtra("User_Name"));
        thumb_image = getIntent().getStringExtra("thumb_image");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mNotificationReference = FirebaseDatabase.getInstance().getReference().child("notifications");


        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        Log.d("mchat",mChatUser);
        Log.d("mcurrent",mCurrentUserId);
        mMessageAdapter = new MessageAdapter(messagesList, Chat_Activity.this);

        chat_message_list.setAdapter(mMessageAdapter);
        loadMessages();

        //----ADDING UPDATE USER IMAGE  NODE -----
        mDatabaseReference.child("users").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("dgsf", mChatUser);
//                if (SessionManager.getUserPic().equals("")){
//                    Thumb_Image1="default";
//                }else {
//                    Thumb_Image1=Utility.getpic(Chat_Activity.this);
//                }
                Thumb_Image1="";
                if (!dataSnapshot.hasChild(mCurrentUserId)) {
                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("image", "default");
//                    chatAddMap.put("name", SessionManager.getUserName();
                    chatAddMap.put("name", "kapil");
                    chatAddMap.put("online", true);
                    chatAddMap.put("status", "HELLO");
                    chatAddMap.put("device_token", token_id);
                    chatAddMap.put("thumb_image", Thumb_Image1);
                    Map chatUserMap = new HashMap();
                    chatUserMap.put("users/" + mCurrentUserId, chatAddMap);

                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                // Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });


        //ADD IMAGE IN CHAT NODE FOR BOTH USER



        //SEND MESSAGE BUTTON CODE
        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_message.getText().toString();
                if (!TextUtils.isEmpty(message)) {

                    String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();
                    String push_id = user_message_push.getKey();
                    Map messageMap = new HashMap();
                    messageMap.put("message", message);
                    messageMap.put("seen", false);
                    messageMap.put("type", "text");
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from", mCurrentUserId);
                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);
//                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(mChatUser).push();
//
//                    String newNotificationId = newNotificationReference.getKey();
//
//                    HashMap<String,String> notificationData=new HashMap<String, String>();
//                    notificationData.put("from",mCurrentUserId);
//                    notificationData.put("type","request");
//
//                    Map requestMap = new HashMap();
//                    requestMap.put("chats/"+mCurrentUserId+ "/"+mChatUser + "/request_type","sent");
//                    requestMap.put("chats/"+mChatUser+"/"+mCurrentUserId+"/request_type","received");
//                    requestMap.put("notifications/"+mChatUser+"/"+newNotificationId,notificationData);

                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                            } else {
                                //   Toast.makeText(Chat_Activity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                et_message.setText("");
                            }

                        }
                    });
                    Log.d("image",thumb_image);
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", true);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);
                    chatAddMap.put("thumb_image", thumb_image);
                    Map chatAddMap1 = new HashMap();

                    chatAddMap1.put("seen", true);
                    chatAddMap1.put("time_stamp", ServerValue.TIMESTAMP);
                    chatAddMap1.put("thumb_image", "");
//                    chatAddMap1.put("thumb_image", Utility.getpic(Chat_Activity.this));
                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
                    chatUserMap.put("chats/" + mChatUser + "/" + mCurrentUserId, chatAddMap1);
                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                //  Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }


    private void submit() {
        // validate
        String messageETString = messageET.getText().toString().trim();
        if (TextUtils.isEmpty(messageETString)) {
            Toast.makeText(this, "type your message here...", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
    }

    //--- MESSAGES WILL LOAD ON START----
    private void loadMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
//        final Dialog pDialog = new Dialog(Chat_Activity.this);
//        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pDialog.setContentView(R.layout.dialog_loader);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pDialog.show();
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);

                itemPos++;

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();

                    mLastKey = mMessageKey;
                    mPrevKey = mMessageKey;
                }

                messagesList.add(messages);
                mMessageAdapter.notifyDataSetChanged();
                chat_message_list.scrollToPosition(messagesList.size() - 1);
                chat_message_list.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        chat_message_list.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                    }
                });
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
    }
    @Override
    protected void onStart() {
        super.onStart();
        //mDatabaseReference.child(mCurrentUserId).child("online").setValue("true");
    }
    @Override
    protected void onStop() {
        super.onStop();
        // mDatabaseReference.child(mCurrentUserId).child("online").setValue(ServerValue.TIMESTAMP);
    }
    //---THIS FUNCTION IS CALLED WHEN SYSTEM ACTIVITY IS CALLED---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //---FOR PICKING IMAGE FROM GALLERY ACTIVITY AND SENDING---
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            //---GETTING IMAGE DATA IN FORM OF URI--
            Uri imageUri = data.getData();
            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();

            //---PUSHING IMAGE INTO STORAGE---
            StorageReference filepath = mImageStorage.child("message_images").child(push_id + ".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        @SuppressWarnings("VisibleForTests")
                        //    String download_url = task.getResult().getDownloadUrl().toString();

                                Map messageMap = new HashMap();
                        // messageMap.put("message", download_url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);
                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                } else {
                                    //    Toast.makeText(Chat_Activity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                    et_message.setText("");
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
