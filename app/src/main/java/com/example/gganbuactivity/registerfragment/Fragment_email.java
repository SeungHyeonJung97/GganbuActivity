package com.example.gganbuactivity.registerfragment;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.User;
import com.example.gganbuactivity.databinding.FragmentEmailBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.concurrent.Executor;

public class Fragment_email extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private FragmentEmailBinding mBinding;
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

        googleLogin();
        mBinding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mBinding.etEmail.getText().toString();
                if (email != null && !(email.isEmpty())) {
                    checkEmail(email);
                } else {
                    Toast.makeText(context, "이메일을 입력해주세요 !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOk) {
                    RegisterSingleton.getInstance().setEmail(email);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment_password fragment_password = new Fragment_password();
                    transaction.replace(R.id.register_layout, fragment_password);
                    transaction.commit();
                } else {
                    Toast.makeText(context, "중복 확인을 먼저 눌러주세요 !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void googleLogin() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        mBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                Log.d("result", "before");
                Fragment fragment = Fragment_email.this;
                fragment.startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증 요청했을 때 결과 값 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("result", "!");
        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("result",""+result.getStatus());
            if (result.isSuccess()) { // 인증 결과가 성공이면
                Log.d("result", "success");
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account); // 로그인 결과 값에 대한 처리
                Log.e("Fragment_email"    , "this");
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.e("Fragment_email"    , "resultLogin");
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인에 성공했으면
                            Toast.makeText(context,"로그인 성공 !",Toast.LENGTH_SHORT).show();
                            Log.e("Fragment_email"    ,  "Success");
                            RegisterSingleton.getInstance().setEmail(account.getEmail());
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment_password fragment_password = new Fragment_password();
                            transaction.replace(R.id.register_layout, fragment_password);
                            transaction.commit();
                        } else {
                            Log.e("Fragment_email"    , "Fail");
                            Toast.makeText(context,"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkEmail(String email) {
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
                        Log.d("toa", "asd");
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
                Log.d("Frament1", error.toString());
                mDatabaseRef.removeEventListener(this);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
