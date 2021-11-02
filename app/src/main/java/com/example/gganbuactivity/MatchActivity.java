package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gganbuactivity.Adapter.MainAdapter;
import com.example.gganbuactivity.Adapter.MatchAdapter;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.DTO.User;
import com.example.gganbuactivity.databinding.ActivityMainBinding;
import com.example.gganbuactivity.databinding.ActivityMatchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity {
    ActivityMatchBinding mBinding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMatchBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        recyclerView = mBinding.mainRecyclerView;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("User");

        ArrayList<String> filterMbti = setFilterMbti();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user;
                    Log.d("snapshot", snapshot.getKey());
                    if (!(snapshot.getKey().equals("" + RegisterSingleton.getInstance().getEmail()))) {
                        Log.d("filter", ""+filterMbti);
                        Log.d("what?",""+snapshot.child("mbti").getValue());
                        if (filterMbti.contains(snapshot.child("mbti").getValue().toString())) {
                            user = snapshot.getValue(User.class);
                            user.setRecommend("mbti");
                            arrayList.add(user);

                        } else if (snapshot.child("eatingHabits").getValue().equals(RegisterSingleton.getInstance().getEatingHabits())) {
                            user = snapshot.getValue(User.class);
                            user.setRecommend("eatingHabits");
                            arrayList.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MainActivity", String.valueOf(error.toException()));
            }
        });

        adapter = new MatchAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<String> setFilterMbti() {
        String my_mbti = RegisterSingleton.getInstance().getMbti();
        ArrayList<String> result = new ArrayList<>();
        switch (my_mbti) {
            case "ISTJ":
            case "ISFJ":
                result.add("ESFP");
                result.add("ESTP");
                break;
            case "ISTP":
                result.add("ESFJ");
                result.add("ESTJ");
                break;
            case "ISFP":
                result.add("ENFJ");
                result.add("ESFJ");
                result.add("ESTJ");
                break;
            case "INFJ":
            case "INTJ":
                result.add("ENFP");
                result.add("ENTP");
                break;
            case "INFP":
                result.add("ENFJ");
                result.add("ENTJ");
                break;
            case "INTP":
            case "ENTJ":
                result.add("ENTJ");
                result.add("ESTJ");
                break;
            case "ESTP":
            case "ESFP":
                result.add("INTJ");
                // result.add("ISFJ");
                result.add("ISTJ");
                break;
            case "ESTJ":
            case "ESFJ":
                result.add("ISFP");
                result.add("ISTP");
                break;
            case "ENFP":
            case "ENTP":
                result.add("INFJ");
                result.add("INTJ");
                break;
            case "ENFJ":
                result.add("INFP");
                result.add("ISFP");
                break;
        }
        return result;
    }
}