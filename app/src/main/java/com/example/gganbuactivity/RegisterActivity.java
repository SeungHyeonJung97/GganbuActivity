package com.example.gganbuactivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.gganbuactivity.databinding.ActivityRegisterBinding;
import com.example.gganbuactivity.registerfragment.Fragment_email;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding mBinding;
    private DatabaseReference mDatabaseRef;

    private final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment_email fragment_email = new Fragment_email();
        transaction.replace(mBinding.registerLayout.getId(), fragment_email);
        transaction.commit();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
//        mBinding.btnAge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, listener, 1900,0,1);
//                datePickerDialog.show();
//            }
//        });
//
//        setSpinner(mBinding.spEatingHabits, R.array.eatingHabits_array);
//        setSpinner(mBinding.spLifePattern, R.array.lifePattern_array);
//        setSpinner(mBinding.spMbti, R.array.mbti_array);
//
//        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User();
//                setAccount(user);
//
//                mDatabaseRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Iterator<DataSnapshot> child = snapshot.child("User").getChildren().iterator();
//
//                        while (child.hasNext()) {
//                            if (user.getId().equals(child.next().getKey())) {
//                                Toast.makeText(RegisterActivity.this, "존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
//                                mDatabaseRef.removeEventListener(this);
//                                return;
//                            }
//                        }
//                        makeNewId(user);
//                        mDatabaseRef.removeEventListener(this);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(RegisterActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG,error.toString());
//                        mDatabaseRef.removeEventListener(this);
//                    }
//                });
//            }
//        });
//    }
//
//    private void setAccount(User user) {
//        String id = mBinding.etId.getText().toString();
//        String password = mBinding.etPwd.getText().toString();
//        String name = mBinding.etName.getText().toString();
//        String age = mBinding.btnAge.getText().toString();
//        String gender = mBinding.rbMale.isChecked() ? "남성" : "여성";
//        String eatingHabits = mBinding.spEatingHabits.getSelectedItem().toString();
//        String lifePattern = mBinding.spLifePattern.getSelectedItem().toString();
//        String mbti = mBinding.spMbti.getSelectedItem().toString();
//
//        user.setId(id);
//        user.setPassword(password);
//        user.setName(name);
//        user.setAge(age);
//        user.setGender(gender);
//        user.setEatingHabits(eatingHabits);
//        user.setLifePattern(lifePattern);
//        user.setMbti(mbti);
//
//    }
//
//    public void setSpinner(Spinner spinner, int array){
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisterActivity.this, array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        // spinner.setOnItemSelectedListener(new SelectSpinner());
//    }
//
//    private void makeNewId(User user) {
//        mDatabaseRef.child("User").child(user.getId()).setValue(user);
//        Toast.makeText(RegisterActivity.this, "회원 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            mBinding.btnAge.setText(getString(R.string.set_age,year,month+1,dayOfMonth));
//        }
//    };
//}