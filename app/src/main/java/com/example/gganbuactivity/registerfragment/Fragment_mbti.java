package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.databinding.FragmentMbtiBinding;
import com.example.gganbuactivity.databinding.FragmentPasswordBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragment_mbti extends Fragment {
    private FragmentMbtiBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMbtiBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();
        setSpinner(mBinding.spMbti, R.array.mbti_array);

        Linkify.TransformFilter mTransfrom = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String s) {
                return "";
            }
        };

        Pattern pattern1 = Pattern.compile("여기");
        Linkify.addLinks(mBinding.tvLink,pattern1,"https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC");

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mbti = mBinding.spMbti.getSelectedItem().toString();
                RegisterSingleton.getInstance().setMbti(mbti);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_comment fragment_comment = new Fragment_comment();
                transaction.replace(R.id.register_layout, fragment_comment);
                transaction.commit();
            }
        });
        return view;
    }

    public void setSpinner(Spinner spinner, int array){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, array, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
