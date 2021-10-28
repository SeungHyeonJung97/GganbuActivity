package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.databinding.FragmentBirthdayBinding;

import java.util.Calendar;
import java.util.Date;

public class Fragment_birthday extends Fragment {
    private FragmentBirthdayBinding mBinding;
    private Context context;
    private boolean isOk = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentBirthdayBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        Log.d("year : ", "" + today.get(Calendar.YEAR));

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthday = mBinding.etBirthday.getText().toString();
                if (birthday.length() < 10) {
                    Toast.makeText(context, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    int user_birth = Integer.parseInt(birthday.substring(0, 4));
                    if (year - user_birth > 19) {
                        RegisterSingleton.getInstance().setBirthday(birthday);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment_gender fragment_gender = new Fragment_gender();
                        transaction.replace(R.id.register_layout, fragment_gender);
                        transaction.commit();
                    } else {
                        Toast.makeText(context, "만 19세 미만은 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}
