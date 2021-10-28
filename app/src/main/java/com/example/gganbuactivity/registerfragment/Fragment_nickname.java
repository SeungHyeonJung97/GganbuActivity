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
import com.example.gganbuactivity.databinding.FragmentNicknameBinding;
import com.example.gganbuactivity.databinding.FragmentPasswordBinding;

public class Fragment_nickname extends Fragment {
    private FragmentNicknameBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentNicknameBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mBinding.etNickname.getText().toString();
                RegisterSingleton.getInstance().setNickname(nickname);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_birthday fragment_birthday = new Fragment_birthday();
                transaction.replace(R.id.register_layout, fragment_birthday);
                transaction.commit();
            }
        });
        return view;
    }
}
