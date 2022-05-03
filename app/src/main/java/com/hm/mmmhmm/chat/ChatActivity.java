package com.hm.mmmhmm.chat;

import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hm.mmmhmm.R;
import com.hm.mmmhmm.adapter.MessageAdapter;
import com.hm.mmmhmm.helper.SessionManager;
import com.hm.mmmhmm.helper.util;
import com.hm.mmmhmm.models.UsernameModel;
import com.hm.mmmhmm.web_service.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kotlinx.coroutines.Dispatchers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    int pos = 0;
    RecyclerView rvMessage;
    String key;
    EditText etMessage;
    ImageView ivSend;
    Firebase reference1, reference2, referenc3, referenc4;
    String[] mColors = {"#e0d547", "#4e8cc5", "#e094bb", "#e17362", "#661830", "#ed8b32", "#d5607a", "#4530fb", "#c94ad5", "#ab9370"};
    String user1 = "", friendID = "", username = "", color = "", usernameId = "", user_id = "", blockstatus = "", blockby = "", receiverUsernameId = "";
    LinearLayout linearChatDesign;
    ScrollView scrollView;
    TextView tvUserName, tvStatus, tvSeen, tvReport, tvBlock;
   EditText emojiconEditText;
//    EmojIconActions emojIcon;
    ImageView ivEmoji, iv_menu;
    LinearLayout linearlayout, layoutMessage, layoutBlock;
    private float x1;
    static final int MIN_DISTANCE = 150;

    private static ChatActivity instance;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        instance = this;
        util.setStatusBarGradiant(this);
        Log.e("qwerty", "aayushiiiiiiiii");
        user1 = SessionManager.INSTANCE.getUserId();
        util.setOnlineStatus(ChatActivity.this);
        SessionManager.INSTANCE.setChat(true);
        friendID = getIntent().getStringExtra("firebaseUserId");
        SessionManager.INSTANCE.setFriend(friendID);


        Log.d("dsdfgasd",friendID+"___");
        username = getIntent().getStringExtra("username");
        color = getIntent().getStringExtra("hexCodeTop");
        if (getIntent().getStringExtra("tag") != null) {
            if (getIntent().getStringExtra("tag").equals("1")) {
                receiverUsernameId = getIntent().getStringExtra("senderId");
                usernameId = SessionManager.INSTANCE.getUserNameId();
            } else {
                Log.e("***************-", usernameId + "_____");
                Log.e("***************-", receiverUsernameId + "_____");
                usernameId = getIntent().getStringExtra("senderId");
                receiverUsernameId = getIntent().getStringExtra("id");

            }
        } else {
            usernameId = getIntent().getStringExtra("senderId");
            receiverUsernameId = getIntent().getStringExtra("id");
            Log.e("!!!!!!!!!!!!!!-", usernameId + "_____");
            Log.e("!!!!!!!!!!!!!!!!!-", receiverUsernameId + "_____");

        }
//        senderId = getIntent().getStringExtra("senderId");
//        id = getIntent().getStringExtra("id");


        user_id = getIntent().getStringExtra("user_id");
        blockstatus = getIntent().getStringExtra("blockstatus");
        blockby = getIntent().getStringExtra("blockby");

        Log.e("firebaseUserId-", friendID + "_____");
        Log.e("hexCodeTop-", color + "_____");
        Log.e("senderId-", receiverUsernameId + "_____");
        Log.e("id-", usernameId + "_____");
        Log.e("user_id-", user_id + "_____");
        Log.e("blockstatus-", blockstatus + "_____");
        Log.e("blockby-", blockby + "_____");
        Log.e("username-", username + "_____");

        SessionManager.INSTANCE.setselectedUserNameId(receiverUsernameId + "_" +
                username);

        linearChatDesign = findViewById(R.id.linearChatDesign);
        scrollView = findViewById(R.id.scrollView);
        layoutBlock = findViewById(R.id.layoutBlock);
        layoutMessage = findViewById(R.id.layoutMessage);
        tvReport = findViewById(R.id.tvReport);
        tvBlock = findViewById(R.id.tvBlock);

        if (blockstatus.equals("1")) {
            layoutMessage.setVisibility(View.GONE);
            layoutBlock.setVisibility(View.VISIBLE);
        } else {
            layoutMessage.setVisibility(View.VISIBLE);
            layoutBlock.setVisibility(View.GONE);
        }
        //emojiconEditText = findViewById(R.id.bottompanel);
        linearlayout = findViewById(R.id.linearlayout);
        ivEmoji = findViewById(R.id.ivEmoji);

        findViewById(R.id.ivBack).setOnClickListener(view -> {
//            setOtherUnSeen();

            Map<String, String> hashMap1 = new HashMap<>();
            hashMap1.put("receiverUsernameId", receiverUsernameId);
            resetChatBack(hashMap1);
        });

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/messages1/" + user1 + "_" + username);
        reference1.child(friendID + "_" + username);
        reference2 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/messages1/" + friendID);
        reference2.child(user1 + "_" + username);
        referenc3 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/seen/");
        referenc4 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/statusNew/" + user_id);
//        setOtherSeen();
//        emojIcon = new EmojIconActions(this, linearlayout, emojiconEditText, ivEmoji,
//                "#495C66", "#DCE1E2", "#E6EBEF");
//        emojIcon.ShowEmojIcon();
//        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
//            @Override
//            public void onKeyboardOpen() {
//                onKeyboardClose();
//                Log.e("TAG", "Keyboard is Opened!");
//            }
//
//            @Override
//            public void onKeyboardClose() {
//                Log.e("TAG", "Keyboard is Closed");
//            }
//        });

        rvMessage = findViewById(R.id.rvMessage);
        etMessage = findViewById(R.id.etMessage);
        tvUserName = findViewById(R.id.tvUserName);
        tvStatus = findViewById(R.id.tvStatus);
        tvSeen = findViewById(R.id.tvSeen);
        ivSend = findViewById(R.id.ivSend);
        iv_menu = findViewById(R.id.iv_menu);
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        tvStatus.setTextColor(Color.parseColor(color));
        tvBlock.setTextColor(Color.parseColor(color));
        tvReport.setTextColor(Color.parseColor(color));
        MessageAdapter messageAdapter = new MessageAdapter(this, key);
        rvMessage.setAdapter(messageAdapter);
        Map<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("receiverUsernameId", receiverUsernameId);
        resetChat(hashMap1);
        tvReport.setOnClickListener(view -> showFeedbackDialog());

        if (blockby.equals(SessionManager.INSTANCE.getUserId())) {
            tvBlock.setText("Unblock");
        } else tvBlock.setText("Block");

        tvBlock.setOnClickListener(view -> {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("userId", user_id);
            blockUser(hashMap);
        });

        iv_menu.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(ChatActivity.this, iv_menu);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            Menu menuOpts = popup.getMenu();

            if (blockstatus.equals("1")) {
                menuOpts.getItem(1).setTitle("Unblock");
            } else menuOpts.getItem(1).setTitle("Block");
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {
                    case R.id.report:
                        showFeedbackDialog();
                        break;
                    case R.id.block:
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("userId", user_id);
                        blockUser(hashMap);
                        break;
                }
                // deletStory(storyViews.get(counter).getId()+"");
                return true;
            });

            popup.show();//showing popup menu

        });
        String upperString = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        tvUserName.setText(upperString);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    ivSend.setImageDrawable(getResources().getDrawable(R.drawable.send_message));
                } else ivSend.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//
//        });
        referenc3.child(user1 + "_" + username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map map = dataSnapshot.getValue(Map.class);
//                Log.e("reference3", map.toString());
//                String seen = map.get("seen").toString();
//                Log.e("seen", seen + ".........");
//
//                if (seen.equals("0")) {
//                    tvSeen.setVisibility(View.GONE);
//                } else {
//                    tvSeen.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (dataSnapshot.exists()) {
                    // code if data exists
                    Log.e("reference3", map.toString());
                    String seen = map.get("seen").toString();
                    Log.e("seen", seen + ".........");
                    Log.e("namee", receiverUsernameId + "_" + username + "------" + SessionManager.INSTANCE.getselectedUserNameId());
//                if(!(receiverUsernameId+"_"+username).equals(Utility.getselectedUserNameId())){
                    if (seen.equals("0")) {
                        tvSeen.setVisibility(View.GONE);
                    } else {
                        tvSeen.setVisibility(View.VISIBLE);
                    }
                } else {
                    // code if data does not  exists
                }

//                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        referenc4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map post = dataSnapshot.getValue(Map.class);
                    String status = post.get("status").toString();
                    if (status.equals("1")) {
                        tvStatus.setText("Online");
                    } else tvStatus.setText("Offline");
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        reference1.child(friendID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (dataSnapshot.exists()) {
                    Log.e("map", map.toString());
                    String message = map.get("message").toString();
                    String userName = map.get("from").toString();
                    String time = map.get("timestamp").toString();
                    String seen = map.get("seen").toString();
                    Log.e("fromeeeeeeeeeeee", username + friendID);
//                    Toast.makeText(ChatActivity.this, String.valueOf(Utility.isChat()), Toast.LENGTH_SHORT).show();
//                    if (61 = ) {
//
//                    }++

                    Log.e("fromeeeeeeeeeeee",  friendID+"____"+user1 + "_" + username+"!!!!"+userName);
                    String SELFid = user1 + "_" + username;
                    String RECIVERid = friendID;
                    String LASTMESSAGEId = userName;
                    Log.e("fromeeeeeeeeeeee",  SELFid+"_SELFid__");
                    Log.e("fromeeeeeeeeeeee",  SessionManager.INSTANCE.getFriend()+"_RECIVERid___");
                    Log.e("fromeeeeeeeeeeee",  LASTMESSAGEId+"__LASTMESSAGEId__");

                    if(SessionManager.INSTANCE.isChat()){
                        if(!LASTMESSAGEId.equals(SELFid))
                        {
                          if (LASTMESSAGEId.equals(SessionManager.INSTANCE.getFriend())){
                              Log.e("fromeeeeeeeeeeee",  "VISHAL");
                              setOtherSeen();
                          }
                        }

                    }
                    if (userName.equals(user1 + "_" + username)) {
                        addMessageBox(message, time, 1, seen);

                    } else {
                        addMessageBox(message, time, 2, seen);
                    }
                }
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
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        ivSend.setOnClickListener(view -> {
            String messageText = emojiconEditText.getText().toString();
            Map<String, String> hashMap = new HashMap<>();
//            hashMap.put("usernameId", senderId);
//            hashMap.put("receiverUsernameId", id);
            hashMap.put("usernameId", usernameId);
            hashMap.put("receiverUsernameId", receiverUsernameId);
            hashMap.put("message", messageText);
            Log.d("qwerty___________-", usernameId + "_____");
            Log.d("qwerty___________-", receiverUsernameId + "_____");

            Log.e("qwertgvfcx", hashMap.toString());
            sendChat(hashMap);
            if (!messageText.equals("")) {
                Map<String, String> map = new HashMap<>();
                map.put("from", user1 + "_" + username);
                map.put("imagepath", "");
                map.put("message", messageText);
                map.put("seen", "0");
                map.put("timestamp", String.valueOf(new Date().getTime()));
                map.put("type", "text");
                reference1.child(friendID).push().setValue(map);
                reference2.child(user1 + "_" + username).push().setValue(map);
                setSelfSeen();
                //emojiconEditText.setText("");
            }
        });

/*
        emojiconEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //do here your stuff f
                    String messageText = emojiconEditText.getText().toString();
                    Map<String, String> hashMap = new HashMap<>();
//                    hashMap.put("usernameId", senderId);
//                    hashMap.put("receiverUsernameId", id);
                    hashMap.put("usernameId", usernameId);
                    hashMap.put("receiverUsernameId", receiverUsernameId);
                    hashMap.put("message", messageText);
                    Log.e("qwertgvfcx", hashMap.toString());
                    sendChat(hashMap);
                    if (!messageText.equals("")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("from", user1 + "_" + username);
                        map.put("imagepath", "");
                        map.put("message", messageText);
                        map.put("seen", "0");
                        map.put("timestamp", String.valueOf(new Date().getTime()));
                        map.put("type", "text");
                        reference1.child(friendID).push().setValue(map);
                        reference2.child(user1 + "_" + username).push().setValue(map);
                        setSelfSeen();
                        emojiconEditText.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
*/
    }

    @SuppressLint({"RtlHardcoded", "ClickableViewAccessibility"})
    public void addMessageBox(String message, String time, int type, String seen) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View custom = vi.inflate(R.layout.message_layout, null);
        LinearLayout root = custom.findViewById(R.id.Message_Layout);
        RelativeLayout layoutLeft = custom.findViewById(R.id.layoutLeft);
        RelativeLayout layoutRight = custom.findViewById(R.id.layoutRight);
        TextView tvRightMessage = custom.findViewById(R.id.tvRightMessage);
        TextView tvRightTime = custom.findViewById(R.id.tvRightTime);
        TextView tvLeftMessage = custom.findViewById(R.id.tvLeftMessage);
        TextView tvLeftTime = custom.findViewById(R.id.tvLeftTime);
        TextView tvsendername = custom.findViewById(R.id.tvsendername);
        TextView tvSeen = custom.findViewById(R.id.tvSeen);
        String upperString = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        tvsendername.setText(upperString);
//        pos = Integer.parseInt(color);
        tvsendername.setTextColor(Color.parseColor(color));
        String ago = DateFormat.format("hh:mm", Long.parseLong(time)).toString();
        tvRightMessage.setText(message);
        tvRightTime.setText(ago);
        tvLeftMessage.setText(message);
        tvLeftTime.setText(ago);
        tvRightMessage.setOnClickListener(view -> tvRightTime.setVisibility(View.VISIBLE));
        tvLeftMessage.setOnClickListener(view -> tvLeftTime.setVisibility(View.VISIBLE));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (type == 1) {
            lp.gravity = Gravity.RIGHT;
            layoutRight.setVisibility(View.VISIBLE);
//            String[] mColors = {"#e0d547", "#4e8cc5", "#e094bb", "#e17362", "#661830", "#ed8b32", "#d5607a", "#4530fb", "#c94ad5", "#3f1082"};
            switch (color) {
                case "#e0d547":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chat_bubble);
                    break;
                case "#4e8cc5":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_1);
                    break;
                case "#e094bb":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_2);
                    break;
                case "#e17362":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_3);
                    break;
                case "#661830":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_4);
                    break;
                case "#ed8b32":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_5);
                    break;
                case "#d5607a":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_6);
                    break;
                case "#4530fb":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_7);
                    break;
                case "#c94ad5":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_8);
                    break;
                case "#ab9370":
                    tvRightMessage.setBackgroundResource(R.drawable.right_chatbubble_9);
                    break;
            }
        } else {
            layoutLeft.setVisibility(View.VISIBLE);
            lp.gravity = Gravity.LEFT;
            tvLeftMessage.setLayoutParams(lp);
            tvLeftMessage.setTextColor(getResources().getColor(R.color.white));
            tvLeftMessage.setBackgroundResource(R.drawable.left_chat_bubble);
        }
        linearChatDesign.addView(custom, lp);
//        scrollView.scrollTo(0, scrollView.getBottom());
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 500);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
//                    Utility.setChat(false);
                    Map<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("receiverUsernameId", receiverUsernameId);
                   resetChatBack(hashMap1);
//                    onBackPressed();
//                    this.overridePendingTransition(R.anim.anim_slide_in_right,
//                            R.anim.anim_slide_out_right);
                }  // consider as something else - a screen tap for example

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        setOtherUnSeen();
//        Utility.setChat(false);
        Map<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("receiverUsernameId", receiverUsernameId);
        resetChatBack(hashMap1);
//        startActivity(new Intent(ChatActivity.this, MainActivity.class)
//                .putExtra("name", username)
//                .putExtra("id", recuid));
//        this.overridePendingTransition(R.anim.anim_slide_in_right,
//                R.anim.anim_slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        util.setOfflineStatus(ChatActivity.this);
//        setOtherUnSeen();
    }

    public void sendChat(final Map<String, String> map) {

//        Call<UsernameModel> call = ApiClient.getRetrofitService(this).sendChat("Bearer " + SessionManager.INSTANCE.getFCMToken(), map);
//        call.enqueue(new Callback<UsernameModel>() {
//            @Override
//            public void onResponse(@NotNull Call<UsernameModel> call, @NotNull Response<UsernameModel> response) {
//
//                UsernameModel user = response.body();
//                Log.e("response", user.toString());
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UsernameModel> call, @NotNull Throwable t) {
//                Log.d("response", "gh" + t.getMessage());
//            }
//        });
    }


    public void resetChat(final Map<String, String> map) {
//        Call<UsernameModel> call = Apis.getAPIService().resetChat("Bearer " + Utility.getToken(), map);
//        call.enqueue(new Callback<UsernameModel>() {
//            @Override
//            public void onResponse(@NotNull Call<UsernameModel> call, @NotNull Response<UsernameModel> response) {
//
//                UsernameModel user = response.body();
//                Log.e("response", user.toString());
////                Utility.setChat(false);
////                startActivity(new Intent(ChatActivity.this, MainActivity.class)
////                        .putExtra("name", username)
////                        .putExtra("id", recuid));
////                overridePendingTransition(R.anim.anim_slide_in_right,
////                        R.anim.anim_slide_out_right);
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UsernameModel> call, @NotNull Throwable t) {
//                Log.d("response", "gh" + t.getMessage());
//            }
//        });
    }

    public void resetChatBack(final Map<String, String> map) {
//        Call<UsernameModel> call = Apis.getAPIService().resetChat("Bearer " + Utility.getToken(), map);
//        call.enqueue(new Callback<UsernameModel>() {
//            @Override
//            public void onResponse(@NotNull Call<UsernameModel> call, @NotNull Response<UsernameModel> response) {
//
//                UsernameModel user = response.body();
//                Log.e("response", user.toString());
//                Utility.setChat(false);
//                Utility.setUserName(username);
//
//                startActivity(new Intent(ChatActivity.this, MainActivity.class)
//                        .putExtra("name", username)
//                        .putExtra("id", usernameId));
//                overridePendingTransition(R.anim.anim_slide_in_right,
//                        R.anim.anim_slide_out_right);
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UsernameModel> call, @NotNull Throwable t) {
//                Log.d("response", "gh" + t.getMessage());
//            }
//        });
    }
    private void showFeedbackDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reportuser);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(v -> dialog.dismiss());
        EditText etFeedback = dialog.findViewById(R.id.etFeedback);

        TextView tvConfirm = dialog.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(view -> {
            String Feedback = etFeedback.getText().toString().trim();
            if (TextUtils.isEmpty(Feedback)) {
//            Toast.makeText(this, "First Name", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Please enter your feedback", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("userId", user_id);
            map.put("message", Feedback);
            reportuser(map);
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void reportuser(final Map<String, String> map) {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
//        Call<UsernameModel> call = Apis.getAPIService().reposrtUser("Bearer " + Utility.getToken(), map);
//        call.enqueue(new Callback<UsernameModel>() {
//            @Override
//            public void onResponse(@NotNull Call<UsernameModel> call, @NotNull Response<UsernameModel> response) {
//
//                UsernameModel user = response.body();
//                dialog.dismiss();
//                if (user.getStatusCode().equals("200")) {
//                    Toast.makeText(ChatActivity.this, "You have reported this user.", Toast.LENGTH_SHORT).show();
//                }  // Toast.makeText(getApplicationContext(), user.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UsernameModel> call, @NotNull Throwable t) {
//                dialog.dismiss();
//                Log.d("response", "gh" + t.getMessage());
//            }
//        });
    }

    public void blockUser(final Map<String, String> map) {
//        Call<UsernameModel> call = Apis.getAPIService().blockUser("Bearer " + Utility.getToken(), map);
//        call.enqueue(new Callback<UsernameModel>() {
//            @Override
//            public void onResponse(@NotNull Call<UsernameModel> call, @NotNull Response<UsernameModel> response) {
//
//                UsernameModel user = response.body();
//                if (user.getStatusCode().equals("200")) {
//                    Toast.makeText(ChatActivity.this, "Done.", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ChatActivity.this, MainActivity.class)
//                            .putExtra("name", username)
//                            .putExtra("id", usernameId));
//                    overridePendingTransition(R.anim.anim_slide_in_right,
//                            R.anim.anim_slide_out_right);
//                }  // Toast.makeText(getApplicationContext(), user.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UsernameModel> call, @NotNull Throwable t) {
//                Log.d("response", "gh" + t.getMessage());
//            }
//        });
    }
    private void setSelfSeen() {
        referenc3.child(user1 + "_" + username).child(friendID).child("seen").setValue("0");
        Log.e("ids",friendID+"-----"+user1 + "_" + username+"     0");
    }

    private void setOtherSeen() {
        referenc3.child(friendID).child(user1 + "_" + username).child("seen").setValue("1");
        Log.e("ids",friendID+"-----"+user1 + "_" + username+"    1");
    }

//    private void setOtherUnSeen() {
//            referenc3.child(user2).child(user1 + "_" + username).child("seen").setValue("0");
//    }

    @Override
    public void onResume() {
        super.onResume();
        util.setOnlineStatus(ChatActivity.this);
    }

    //    @Override
//    public void onPause() {
//        super.onPause();
//
//        util.setOfflineStatus(ChatActivity.this);
//
//    }
//
//    @Override
//    public void onStop() {
//
//        util.setOfflineStatus(ChatActivity.this);
//        super.onStop();
//    }
    public static ChatActivity getInstance() {

        return instance;
    }

    public  void  back(){

        finish();
    }
}