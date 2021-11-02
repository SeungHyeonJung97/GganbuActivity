package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gganbuactivity.Adapter.MainAdapter;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.databinding.ActivityFavoriteBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding mBinding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList = new ArrayList<>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        recyclerView = mBinding.rvFavorite;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Favorite").child(RegisterSingleton.getInstance().getNickname());

        setData();

        adapter = new MainAdapter(arrayList, FavoriteActivity.this);

        Log.d("array",arrayList.size()+"");
    }

    private void setData() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("snapshot",""+snapshot.getKey());
                    DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference("Post").child(snapshot.getKey());
                    mDataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            arrayList.add(snapshot1.getValue(Post.class));
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FavoriteActivity", String.valueOf(error.toException()));
            }
        });
    }
}