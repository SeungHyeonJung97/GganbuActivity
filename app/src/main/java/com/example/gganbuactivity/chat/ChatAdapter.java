package com.example.gganbuactivity.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gganbuactivity.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;

    public ChatAdapter(List<ChatData> mDataset, Context context, String myNickName) {
        this.mDataset = mDataset;
        this.myNickName = myNickName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chat = mDataset.get(position);

        holder.tv_nickname.setText(chat.getNickname());
        holder.tv_msg.setText(chat.getMsg());

        if(chat.getNickname().equals(this.myNickName)){
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }else{
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position){
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nickname;
        public TextView tv_msg;
        public View rootView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_nickname = v.findViewById(R.id.tv_nickname);
            tv_msg = v.findViewById(R.id.tv_msg);
        }
    }

}
