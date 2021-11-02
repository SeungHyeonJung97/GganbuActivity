package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.databinding.ActivityViewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewActivity extends AppCompatActivity {
    private ActivityViewBinding mBinding;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String writer_nickname = intent.getStringExtra("nickname");
        setData(writer_nickname);

        mBinding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressString = mBinding.tvLocationLabel.getText().toString();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("geo")
                        .path("0,0")
                        .appendQueryParameter("q",addressString);
                Uri addressUri= builder.build();

                showMap(addressUri);
            }
        });

        mBinding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivity.this, ChatActivity.class);
                intent.putExtra("nickname", writer_nickname);
                startActivity(intent);
            }
        });

        mBinding.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkString = post.getLink();
                showLink(linkString);
            }
        });
    }

    private void showLink(String linkString) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkString));

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void showMap(Uri addressUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void setData(String writer_nickname) {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Post").child(""+writer_nickname);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                mBinding.tvTitleLabel.setText(post.getTitle());
                mBinding.tvLocationLabel.setText(post.getLocation());
                Glide.with(ViewActivity.this)
                        .load(post.getUrl())
                        .into(mBinding.ivImage1);
                mBinding.tvMoney.setText("보증금 : " + post.getDeposit());
                if(post.getMonth() != null){
                    mBinding.tvMoney.append(" 월세 : " + post.getMonth());
                }
                // mBinding.tvComment.setText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}