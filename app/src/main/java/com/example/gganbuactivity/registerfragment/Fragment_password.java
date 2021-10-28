package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.databinding.FragmentPasswordBinding;


public class Fragment_password extends Fragment {
    private FragmentPasswordBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPasswordBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.chkPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinding.etPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
                    mBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    mBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mBinding.etPassword.getText().toString();
                RegisterSingleton.getInstance().setPassword(password);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_nickname fragment_nickname = new Fragment_nickname();
                transaction.replace(R.id.register_layout, fragment_nickname);
                transaction.commit();
            }
        });
        return view;
    }
}
