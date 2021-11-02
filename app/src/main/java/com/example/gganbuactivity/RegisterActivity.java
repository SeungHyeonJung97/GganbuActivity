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
    }
}