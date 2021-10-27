package com.example.gganbuactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_main, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getUrl())
                .into(holder.iv_cardImage);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_location.setText(arrayList.get(position).getLocation());
        if(arrayList.get(position).getType().equals("월세")){
            holder.tv_type.setText("월세");
            holder.tv_month_label.setVisibility(View.VISIBLE);
            holder.tv_month.setVisibility(View.VISIBLE);
            holder.tv_month.setText(arrayList.get(position).getMonth());
        }
        holder.tv_type.setText("전세");
        holder.tv_deposit.setText(arrayList.get(position).getDeposit());
        holder.tv_condition_age.setText(arrayList.get(position).getAge_start() + " ~ " + arrayList.get(position).getAge_end() + " 세");
        holder.tv_condition_gender.setText(arrayList.get(position).getGender());
        holder.tv_condition_eatingHabits.setText(arrayList.get(position).getEatingHabits());
        holder.tv_condition_lifePattern.setText(arrayList.get(position).getLifePattern());
        holder.tv_condition_mbti.setText(arrayList.get(position).getMbti());


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cardImage;
        TextView tv_title;
        TextView tv_location;
        TextView tv_type;
        TextView tv_deposit;
        TextView tv_month_label;
        TextView tv_month;
        TextView tv_condition_age;
        TextView tv_condition_gender;
        TextView tv_condition_eatingHabits;
        TextView tv_condition_lifePattern;
        TextView tv_condition_mbti;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_cardImage = itemView.findViewById(R.id.iv_cardImage);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_location = itemView.findViewById(R.id.tv_location);
            this.tv_type = itemView.findViewById(R.id.tv_type);
            this.tv_deposit = itemView.findViewById(R.id.tv_deposit);
            this.tv_month_label = itemView.findViewById(R.id.tv_month_label);
            this.tv_month = itemView.findViewById(R.id.tv_month);
            this.tv_condition_gender = itemView.findViewById(R.id.tv_condition_gender);
            this.tv_condition_age = itemView.findViewById(R.id.tv_condition_age);
            this.tv_condition_eatingHabits = itemView.findViewById(R.id.tv_condition_eatingHabits);
            this.tv_condition_lifePattern = itemView.findViewById(R.id.tv_condition_lifePattern);
            this.tv_condition_mbti = itemView.findViewById(R.id.tv_condition_mbti);

        }
    }
}
