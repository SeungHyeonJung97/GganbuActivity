package com.example.gganbuactivity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.ChatActivity;
import com.example.gganbuactivity.DTO.Favorite;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;
    private DatabaseReference mDateRef = FirebaseDatabase.getInstance().getReference();
    private int index = 0;

    public MainAdapter(ArrayList<Post> arrayList, Context context) {
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
                .load(arrayList.get(position).getUrl().get(0))
                .override(holder.iv_cardImage.getWidth(), holder.iv_cardImage.getHeight())
                .into(holder.iv_cardImage);
        holder.tv_title.setText(arrayList.get(position).getTitle());

        StringBuilder stringBuilder = new StringBuilder(arrayList.get(position).getLocation());
        if (stringBuilder.length() > 13) {
            stringBuilder.insert(14, "\n");
        }
        holder.tv_location.setText(stringBuilder);

        if (arrayList.get(position).getType().equals("월세")) {
            holder.tv_type.setText("월세");
            holder.tv_month_label.setVisibility(View.VISIBLE);
            holder.tv_month.setVisibility(View.VISIBLE);
            holder.tv_month.setText(arrayList.get(position).getMonth());
        } else {
            holder.tv_type.setText("전세");
        }
        holder.tv_deposit.setText(arrayList.get(position).getDeposit());
        holder.tv_condition_age.setText(arrayList.get(position).getAge_start() + " ~ " + arrayList.get(position).getAge_end() + " 세");
        holder.tv_condition_gender.setText(arrayList.get(position).getGender());
        holder.tv_condition_eatingHabits.setText(arrayList.get(position).getEatingHabits());
        holder.tv_condition_lifePattern.setText(arrayList.get(position).getLifePattern());
        holder.tv_condition_mbti.setText(arrayList.get(position).getMbti());

        DatabaseReference path = mDateRef.child("Favorite").child("" + RegisterSingleton.getInstance().getNickname());
        path.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while(child.hasNext()){
                    if(child.next().getKey().equals(holder.writer_id)){
                        holder.iv_favorite.setImageResource(R.drawable.ic_baseline_star_24);
                    }
                }
                path.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Favorite favorite = new Favorite();
                        Log.d("mainsnapshot",""+snapshot);
                        if(snapshot.child(holder.writer_id).getValue() != null){
                            Toast.makeText(context,"즐겨찾기가 해제되었습니다.",Toast.LENGTH_SHORT).show();
                            path.child(holder.writer_id).removeValue();
                            holder.iv_favorite.setImageResource(R.drawable.ic_baseline_star_outline_24);
                        }else{
                            Toast.makeText(context,"즐겨찾기에 등록되셨습니다.",Toast.LENGTH_SHORT).show();
                            favorite.setFavorite_post(holder.writer_id);
                            path.child(holder.writer_id).setValue(favorite.getFavorite_post());
                            holder.iv_favorite.setImageResource(R.drawable.ic_baseline_star_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
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
        ImageView iv_cardImage;
        ImageView iv_favorite;
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
        String writer_id;

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
            this.iv_favorite = itemView.findViewById(R.id.iv_favorite);

            if(index < arrayList.size()){
                this.writer_id = arrayList.get(index).getNickname();
                Log.d("nickname",""+this.writer_id);
                index++;
            }
        }
    }
}
