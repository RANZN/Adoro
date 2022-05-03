package com.hm.mmmhmm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.mmmhmm.R;
import com.hm.mmmhmm.helper.ItemClickListener;
import com.hm.mmmhmm.models.UserListModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private final Context context;
    private final List<UserListModel.Userlist> userlists;
    private final ItemClickListener itemClickListener;
    String list;

    String[] mColors = {"#e0d547", "#4e8cc5", "#e094bb", "#e17362", "#661830", "#ed8b32", "#d5607a", "#4530fb", "#c94ad5", "#3f1082"};

    public ChatListAdapter(Context context, List<UserListModel.Userlist> userlists, String list, ItemClickListener itemClickListener) {
        this.context = context;
        this.userlists = userlists;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chat_user_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String upperString = userlists.get(position).getUsername().substring(0, 1).toUpperCase()
                + userlists.get(position).getUsername().substring(1).toLowerCase();
        holder.tvUserName.setText(upperString);
        holder.tv_Msg.setText(userlists.get(position).getMessage());
//        holder.tvUserName.setTextColor(Color.parseColor(mColors[position % 10]));
        holder.tvUserName.setTextColor(Color.parseColor(userlists.get(position).getHexCodeTop()));
        if (!(userlists.get(position).getMessagecount()).equals("0")) {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.opensans_bold);
            holder.tvUserName.setTypeface(typeface);
            holder.tv_Msg.setTypeface(typeface);
        }
        if (userlists.get(position).getSenderId().equals(list)) {
            holder.tv_From.setVisibility(View.VISIBLE);
        }
        holder.layoutChat.setOnClickListener(view -> itemClickListener.itemClick(position));
    }

    @Override
    public int getItemCount() {
        return userlists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tv_Msg, tv_From;
        LinearLayout layoutChat;
        ImageView ivBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tv_Msg = itemView.findViewById(R.id.tv_Msg);
            tv_From = itemView.findViewById(R.id.tv_From);
            layoutChat = itemView.findViewById(R.id.layoutChat);
            ivBlock = itemView.findViewById(R.id.ivBlock);
        }
    }
}



