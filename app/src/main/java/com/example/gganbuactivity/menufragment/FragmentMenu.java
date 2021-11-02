package com.example.gganbuactivity.menufragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gganbuactivity.DTO.Favorite;
import com.example.gganbuactivity.FavoriteActivity;
import com.example.gganbuactivity.MainActivity;
import com.example.gganbuactivity.MatchActivity;
import com.example.gganbuactivity.WriteActivity;
import com.example.gganbuactivity.databinding.FragmentMenuBinding;

public class FragmentMenu extends Fragment {
    private FragmentMenuBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMenuBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.clBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        mBinding.clMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MatchActivity.class);
                startActivity(intent);
            }
        });

        mBinding.clFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        mBinding.clWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
