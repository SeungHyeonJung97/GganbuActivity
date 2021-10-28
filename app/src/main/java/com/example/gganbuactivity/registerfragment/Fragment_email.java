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
import com.example.gganbuactivity.User;
import com.example.gganbuactivity.databinding.FragmentEmailBinding;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Fragment_email extends Fragment {
    private FragmentEmailBinding mBinding;
    private SignInButton btn_google;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    private DatabaseReference mDatabaseRef;
    private Context context;
    private boolean isOk = false;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEmailBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mBinding.etEmail.getText().toString();
                if(email != null && !(email.isEmpty())){
                    checkEmail(email);
                }else{
                    Toast.makeText(context,"이메일을 입력해주세요 !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOk){
                    RegisterSingleton.getInstance().setEmail(email);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment_password fragment_password = new Fragment_password();
                    transaction.replace(R.id.register_layout, fragment_password);
                    transaction.commit();
                }else{
                    Toast.makeText(context,"중복 확인을 먼저 눌러주세요 !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void checkEmail(String email){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        User user = new User();
        user.setEmail(email);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.child("User").getChildren().iterator();
                while (child.hasNext()) {
                    if (user.getEmail().equals(child.next().getKey())) {
                        Toast.makeText(context, "존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        Log.d("toa","asd");
                        mDatabaseRef.removeEventListener(this);
                        return;
                    }
                }
                Toast.makeText(context, "사용하실 수 있는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                isOk = true;
                mDatabaseRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("Frament1",error.toString());
                mDatabaseRef.removeEventListener(this);
            }
        });
    }
}
