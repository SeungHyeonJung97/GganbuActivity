package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gganbuactivity.MainActivity;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.User;
import com.example.gganbuactivity.databinding.FragmentFinishBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_finish extends Fragment {
    private FragmentFinishBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFinishBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void saveData() {
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        User user = new User();
        user.setEmail(RegisterSingleton.getInstance().getEmail());
        user.setNickname(RegisterSingleton.getInstance().getNickname());
        user.setGender(RegisterSingleton.getInstance().getGender());
        user.setBirth(RegisterSingleton.getInstance().getBirthday());
        user.setEatingHabits(RegisterSingleton.getInstance().getEatingHabits());
        user.setLifePattern(RegisterSingleton.getInstance().getLifePattern());
        user.setPassword(RegisterSingleton.getInstance().getPassword());
        user.setMbti(RegisterSingleton.getInstance().getMbti());

        mDatabaseRef.child("User").child(user.getNickname()).setValue(user);
    }
}
