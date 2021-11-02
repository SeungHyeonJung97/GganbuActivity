package com.example.gganbuactivity.menufragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.ChatActivity;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.WriteActivity;
import com.example.gganbuactivity.databinding.FragmentMypostBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentMyPost extends Fragment {
    private FragmentMypostBinding mBinding;
    private Context context;
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMypostBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        context = container.getContext();

        setData();

        mBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        mBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.child("Post").child("" + RegisterSingleton.getInstance().getNickname()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(view.getContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String EDIT = "Edit";
                Intent intent = new Intent(view.getContext(), WriteActivity.class);
                intent.putExtra("Edit",EDIT);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setData() {
        mDatabaseRef = mDatabase.getReference("Post").child("" + RegisterSingleton.getInstance().getNickname());
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Post post = new Post();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);

                Glide.with(mBinding.getRoot())
                        .load(post.getUrl().get(0))
                        .override(mBinding.ivCardImage.getWidth(), mBinding.ivCardImage.getHeight())
                        .into(mBinding.ivCardImage);

                StringBuilder stringBuilder = new StringBuilder(post.getLocation());
                Log.d("length", ""+stringBuilder.length());
                if(stringBuilder.length() > 10){
                    stringBuilder.insert(14, "\n");
                }
                mBinding.tvLocation.setText(stringBuilder);

                mBinding.tvTitle.setText(post.getTitle());
                mBinding.tvLocation.setText(post.getLocation());
                mBinding.tvType.setText(post.getType());
                mBinding.tvDeposit.setText(post.getDeposit());
                if (post.getType().equals("월세")) {
                    mBinding.tvType.setText("월세");
                    mBinding.tvMonthLabel.setVisibility(View.VISIBLE);
                    mBinding.tvMonth.setVisibility(View.VISIBLE);
                    mBinding.tvMonth.setText(post.getMonth());
                } else {
                    mBinding.tvType.setText("전세");
                }
                mBinding.tvConditionAge.setText(post.getAge_start() + " ~ " + post.getAge_end() + " 세");
                mBinding.tvConditionGender.setText(post.getGender());
                mBinding.tvConditionEatingHabits.setText(post.getEatingHabits());
                mBinding.tvConditionLifePattern.setText(post.getLifePattern());
                mBinding.tvConditionMbti.setText(post.getMbti());
                mBinding.ivDelete.setVisibility(View.VISIBLE);
                mBinding.ivEdit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FragmentMyPost", String.valueOf(error.toException()));
            }
        });
    }
}
