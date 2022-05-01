package com.hm.mmmhmm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hm.mmmhmm.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    String key;


    public MessageAdapter(Context context,String key) {
        this.context = context;
        this.key = key;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.message_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tvRightMessage.setOnClickListener(view -> holder.tvRightTime.setVisibility(View.VISIBLE));
        holder.tvLeftMessage.setOnClickListener(view -> holder.tvLeftTime.setVisibility(View.VISIBLE));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRightMessage,tvRightTime,tvLeftTime,tvLeftMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRightMessage = itemView.findViewById(R.id.tvRightMessage);
            tvRightTime = itemView.findViewById(R.id.tvRightTime);
            tvLeftMessage = itemView.findViewById(R.id.tvLeftMessage);
            tvLeftTime = itemView.findViewById(R.id.tvLeftTime);


        }
    }
}




