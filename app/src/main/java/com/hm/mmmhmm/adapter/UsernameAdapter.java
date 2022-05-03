package com.hm.mmmhmm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hm.mmmhmm.R;
import com.hm.mmmhmm.helper.ItemClickListener;
import com.hm.mmmhmm.models.UserSuggestionModel;

import java.util.List;

public class UsernameAdapter extends RecyclerView.Adapter<UsernameAdapter.ViewHolder> {
    private Context context;
    List<UserSuggestionModel.Data> userSuggestionModelList;
    private ItemClickListener itemClickListener;


    public UsernameAdapter(Context context, List<UserSuggestionModel.Data> userSuggestionModelList, ItemClickListener itemClickListener) {
        this.context = context;
        this.userSuggestionModelList = userSuggestionModelList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.username_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvUserName.setText(userSuggestionModelList.get(position).getUsername());
        holder.btn_Select.setOnClickListener(view -> itemClickListener.itemClick(position) );
    }

    @Override
    public int getItemCount() {
        return userSuggestionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName,btn_Select;
        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btn_Select = itemView.findViewById(R.id.btn_Select);
        }
    }
}



