package com.example.gganbuactivity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.ChatActivity;
import com.example.gganbuactivity.DTO.User;
import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    private DatabaseReference mDateRef = FirebaseDatabase.getInstance().getReference();
    private int index = 0;

    public MatchAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_match, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfileImage())
                .override(holder.iv_profile.getWidth(), holder.iv_profile.getHeight())
                .into(holder.iv_profile);
        holder.tv_nickname.setText(arrayList.get(position).getNickname());

        StringBuilder locationString = new StringBuilder(arrayList.get(position).getLocation());
        if (locationString.length() > 13) {
            locationString.insert(14, "\n");
        }
        holder.tv_location.setText(locationString);

        StringBuilder commentString = new StringBuilder(arrayList.get(position).getComment());
        if (commentString.length() > 13) {
            commentString.insert(14, "\n");
        }
        holder.tv_comment.setText(commentString);
        holder.tv_condition_gender.setText(arrayList.get(position).getGender());
        holder.tv_condition_eatingHabits.setText(arrayList.get(position).getEatingHabits());
        holder.tv_condition_lifePattern.setText(arrayList.get(position).getLifePattern());
        holder.tv_condition_mbti.setText(arrayList.get(position).getMbti());

        if(arrayList.get(position).getRecommend().equals("mbti")){
            holder.iv_recommend.setImageResource(R.drawable.recommend_mbti);
        }else if(arrayList.get(position).getRecommend().equals("eatingHabits")){
            holder.iv_recommend.setImageResource(R.drawable.recommend_eatinghabits);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("nickname", holder.writer_id);
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        ImageView iv_recommend;
        TextView tv_nickname;
        TextView tv_location;
        TextView tv_comment;
        TextView tv_condition_gender;
        TextView tv_condition_eatingHabits;
        TextView tv_condition_lifePattern;
        TextView tv_condition_mbti;
        String writer_id;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_nickname = itemView.findViewById(R.id.tv_nickname);
            this.tv_location = itemView.findViewById(R.id.tv_location);
            this.tv_condition_gender = itemView.findViewById(R.id.tv_condition_gender);
            this.tv_condition_eatingHabits = itemView.findViewById(R.id.tv_condition_eatingHabits);
            this.tv_condition_lifePattern = itemView.findViewById(R.id.tv_condition_lifePattern);
            this.tv_condition_mbti = itemView.findViewById(R.id.tv_condition_mbti);
            this.tv_comment = itemView.findViewById(R.id.tv_comment);
            this.iv_recommend = itemView.findViewById(R.id.iv_recommend);

            if (index < arrayList.size()) {
                this.writer_id = arrayList.get(index).getNickname();
                Log.d("nickname", "" + this.writer_id);
                index++;
            }
        }
    }
}
