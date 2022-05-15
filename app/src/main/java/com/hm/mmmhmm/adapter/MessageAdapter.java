package com.hm.mmmhmm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.mmmhmm.Chat_Module.GetTimeAgo;
import com.hm.mmmhmm.Chat_Module.Message;
import com.hm.mmmhmm.R;
import com.hm.mmmhmm.helper.SessionManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Context context;
    private final List<Message> messages;
    String key;


    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
//        this.key = key;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chat_bubble, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Message message = messages.get(position);
        if (message.getSender().equals(SessionManager.INSTANCE.getFirebaseID())) {
            holder.layout.setGravity(GravityCompat.END);
            holder.tvMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_other_msg));
            if (message.getSeen()) {
                holder.tvSeen.setVisibility(View.VISIBLE);
                holder.space.setVisibility(View.VISIBLE);
            }
        } else {
            holder.layout.setGravity(GravityCompat.START);
            holder.tvMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_my_msg));
        }
        holder.tvMessage.setText(message.getMessage());
        holder.tvTime.setText(GetTimeAgo.getTimeAgo(message.getTime(), context));

//        holder.tvRightMessage.setOnClickListener(view -> holder.tvRightTime.setVisibility(View.VISIBLE));
//        holder.tvLeftMessage.setOnClickListener(view -> holder.tvLeftTime.setVisibility(View.VISIBLE));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvSeen;
        LinearLayout layout;
        View space;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.message);
            tvTime = itemView.findViewById(R.id.time);
            tvSeen = itemView.findViewById(R.id.seen);
            space = itemView.findViewById(R.id.space);
            layout = itemView.findViewById(R.id.chat_ll);
//            tvLeftMessage = itemView.findViewById(R.id.tvLeftMessage);
//            tvLeftTime = itemView.findViewById(R.id.tvLeftTime);


        }
    }
}




