package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.databinding.FragmentGenderBinding;
import com.example.gganbuactivity.databinding.FragmentPasswordBinding;

public class Fragment_gender extends Fragment {
    private FragmentGenderBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGenderBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.chkMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.chkFemale.setChecked(false);
            }
        });
        mBinding.chkFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.chkMale.setChecked(false);
            }
        });
        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = mBinding.chkMale.isChecked() ? "남자" : "여자";
                RegisterSingleton.getInstance().setGender(gender);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_eatinghabits fragment_eatinghabits = new Fragment_eatinghabits();
                transaction.replace(R.id.register_layout, fragment_eatinghabits);
                transaction.commit();
            }
        });
        return view;
    }
}
