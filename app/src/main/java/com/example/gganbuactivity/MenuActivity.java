package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.gganbuactivity.databinding.ActivityMenuBinding;
import com.example.gganbuactivity.menufragment.FragmentMenu;
import com.example.gganbuactivity.menufragment.FragmentMyPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    private ActivityMenuBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentMenu fragmentMenu = new FragmentMenu();
        transaction.replace(mBinding.mainFrame.getId(), fragmentMenu);
        transaction.commit();

        mBinding.naviBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragmentMyPost fragmentMyPost = new FragmentMyPost();

                switch (item.getItemId()){
                    case R.id.menu_view:
                        transaction.replace(mBinding.mainFrame.getId(), fragmentMenu);
                        transaction.commit();
                        break;
                    case R.id.menu_my_post:
                        transaction.replace(mBinding.mainFrame.getId(), fragmentMyPost);
                        transaction.commit();
                        break;
                }
                return false;
            }
        });
    }
}