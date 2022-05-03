package com.hm.mmmhmm.Chat_Module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hm.mmmhmm.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    DatabaseReference mDatabaseReference;
    DatabaseReference mConvDatabase;
    Context context;
    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    String thumb_image = "";

    //-----GETTING LIST OF ALL MESSAGES FROM CHAT ACTIVITY ----
    public MessageAdapter(List<Messages> mMessagesList, Context context) {
        this.mMessagesList = mMessagesList;
        this.context = context;
    }


    //---CREATING SINGLE HOLDER AND RETURNING ITS VIEW---
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_adapter, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
       // mConvDatabase = FirebaseDatabase.getInstance().getReference();
        return new MessageViewHolder(view);
    }

    //----SETTING EACH HOLDER WITH DATA----
    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages mes = mMessagesList.get(position);
        String from_user_id = mes.getFrom();
        String message_type = mes.getType();

        //----CHANGING TIMESTAMP TO TIME-----

        long timeStamp = mes.getTime();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String[] cal = calendar.getTime().toString().split(" ");
        // String time_of_message = cal[1] + "," + cal[2] + "  " + cal[3].substring(0, 5);
        String time_of_message = cal[3].substring(0, 5);
        Log.e("TIME IS : ", calendar.getTime().toString());

        holder.time_recieve.setText(time_of_message);
        holder.sender_time.setText(time_of_message);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(from_user_id);

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(from_user_id);
//        //---ADDING NAME THUMB_IMAGE TO THE HOLDER----


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();
                if(thumb_image.equals("default")){

                }else {
//                    holder.recieve_image.load(image,
//                            R.color.text_gray,
//                            R.color.text_gray,
//                            true
//            );
                    Picasso.get().load(image)
                            .error(R.mipmap.ic_launcher)
                            .priority(Picasso.Priority.HIGH)
                            .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.recieve_image);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //---ADDING  THUMB_IMAGE TO THE HOLDER----
//        mConvDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
//                if (thumb_image==null){
//                    Toast.makeText(context, thumb_image+"", Toast.LENGTH_SHORT)
//                            .show();
//                }else {
//                    Picasso.get().load(thumb_image)
//                            .error(R.mipmap.ic_launcher)
//                            .priority(Picasso.Priority.HIGH)
//                            .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.recieve_image);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        mConvDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
//                    Picasso.get().load(thumb_image)
//                            .error(R.mipmap.ic_launcher)
//                            .priority(Picasso.Priority.HIGH)
//                            .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.recieve_image);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        mConvDatabase.child(from_user_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
//
//                Picasso.get().load(thumb_image)
//                        .error(R.mipmap.ic_launcher)
//                        .priority(Picasso.Priority.HIGH)
//                        .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.recieve_image);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
        if (message_type.equals("image")) {
            Picasso.get().load(mes.getMessage())
                    .error(R.mipmap.ic_launcher).resize(300, 300).centerCrop()
                    .priority(Picasso.Priority.HIGH)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.messageImage);
        } else {
//            if (from_user_id.equals(SessionManager.getFirbaseID())) {
//                holder.messageText.setText(mes.getMessage());
//                //holder.messageText.setBackgroundResource(R.drawable.background4);
//                holder.sender_rel.setVisibility(View.VISIBLE);
//                holder.sender_time.setVisibility(View.VISIBLE);
//                holder.time_recieve.setVisibility(View.GONE);
//                holder.recever_rel.setVisibility(View.GONE);
//            } else {
                //  holder.messageText.setBackgroundResource(R.drawable.background5);
                holder.reciever_message.setText(mes.getMessage());
                holder.sender_rel.setVisibility(View.GONE);
                holder.recever_rel.setVisibility(View.VISIBLE);
                holder.sender_time.setVisibility(View.GONE);
                holder.time_recieve.setVisibility(View.VISIBLE);

          //  }
        }


    }

    //---NO OF ITEMS TO BE ADDED----
    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    //----RETURNING VIEW OF SINGLE HOLDER----
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView reciever_message;
        public TextView time_recieve, sender_time;
        public CircleImageView recieve_image;
        public ImageView messageImage;
        public LinearLayout recever_rel, sender_rel;


        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.sender_message);
            recever_rel = itemView.findViewById(R.id.recever_rel);
            sender_rel = itemView.findViewById(R.id.sender_rel);
            time_recieve = itemView.findViewById(R.id.time_recieve);
            sender_time = itemView.findViewById(R.id.sender_time);
            reciever_message = itemView.findViewById(R.id.reciever_message);
            recieve_image = itemView.findViewById(R.id.recieve_image);
            // messageImage = (ImageView)itemView.findViewById(R.id.message_image_layout);


            //---DELETE FUNCTION---
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence[] options = new CharSequence[]{"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete this message");
                    builder.setItems(options, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == 0) {
                                /*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 */
                                long mesPos = getAdapterPosition();
                                String mesId = mMessagesList.get((int) mesPos).toString();
                                Log.e("Message Id is ", mesId);
                                Log.e("Message is : ", mMessagesList.get((int) mesPos).getMessage());

                            }

                            if (which == 1) {

                            }

                        }
                    });
                    builder.show();

                    return true;
                }
            });

        }


    }
}
/*
    //----FOR SENDING IMAGE----
        if(message_type.equals("text")){

            holder.messageText.setText(mes.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);

        }
        else{

            holder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(holder.profileImage.getContext()).load(mes.getMessage()).placeholder(R.drawable.user_img).into(holder.messageImage);

        }
    */




       /* if(from_user_id.equals(current_user_id)){
            holder.messageText.setBackgroundColor(Color.WHITE);
            //holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.BLACK);
        }
        else{

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
      }
            */