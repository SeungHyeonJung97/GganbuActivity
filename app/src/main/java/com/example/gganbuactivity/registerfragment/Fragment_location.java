package com.example.gganbuactivity.registerfragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.gganbuactivity.databinding.FragmentLocationBinding;
import com.example.gganbuactivity.zipcode.NetworkStatus;
import com.example.gganbuactivity.zipcode.WebViewActivity;

public class Fragment_location extends Fragment{
    private FragmentLocationBinding mBinding;
    private Context context;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLocationBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.etLocation.setFocusable(false);
        mBinding.etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("주소설정페이지", "주소입력창 클릭");
                int status = NetworkStatus.getConnectivityStatus(context);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent i = new Intent(context, WebViewActivity.class);
                    // 주소결과
                    Fragment fragment = Fragment_location.this;
                    fragment.startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mBinding.etLocation.getText().toString();
                RegisterSingleton.getInstance().setLocation(location);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_birthday fragment_birthday = new Fragment_birthday();
                transaction.replace(R.id.register_layout, fragment_birthday);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(requestCode == SEARCH_ADDRESS_ACTIVITY && resultCode == RESULT_OK){
            String data = intent.getExtras().getString("data");
            if (data != null) {
                Log.i("test", "data:" + data);
                mBinding.etLocation.setText(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
