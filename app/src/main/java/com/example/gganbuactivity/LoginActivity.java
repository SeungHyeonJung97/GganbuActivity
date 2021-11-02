package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.gganbuactivity.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding mBinding;
    private DatabaseReference mDatabaseRef;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.etId.getText().toString();
                String password = mBinding.etPwd.getText().toString();

                mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> child = snapshot.child("User").getChildren().iterator();

                        while (child.hasNext()) {
                            DataSnapshot current_user = child.next();
                            if (current_user.getKey().equals(email) &&
                                    current_user.child("password").getValue().toString().equals(password)) {
                                Toast.makeText(LoginActivity.this, "로그인 성공 !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                RegisterSingleton.getInstance().setNickname(current_user.child("nickname").getValue().toString());
                                RegisterSingleton.getInstance().setEmail(current_user.child("email").getValue().toString());
                                RegisterSingleton.getInstance().setEatingHabits(current_user.child("eatingHabits").getValue().toString());
                                RegisterSingleton.getInstance().setLocation(current_user.child("location").getValue().toString());
                                RegisterSingleton.getInstance().setLifePattern(current_user.child("lifePattern").getValue().toString());
                                RegisterSingleton.getInstance().setMbti(current_user.child("mbti").getValue().toString());
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "" + error.toString());
                    }
                });
            }
        });
    }
}