package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gganbuactivity.MainActivity;
import com.example.gganbuactivity.MenuActivity;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.DTO.User;
import com.example.gganbuactivity.databinding.FragmentFinishBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class Fragment_finish extends Fragment {
    private FragmentFinishBinding mBinding;
    private Context context;
    private String downloadPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFinishBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUpload();
                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void imageUpload() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        RegisterSingleton path = RegisterSingleton.getInstance();
        Uri file = Uri.fromFile(new File(path.getProfileImage()));
        int index = file.getLastPathSegment().lastIndexOf(".");
        String fileName = file.getLastPathSegment().substring(0,index) + ".jpg";
        StorageReference reference = storageReference.child("images/" + fileName);
        Log.d("filename",file.getLastPathSegment());
        UploadTask uploadTask = reference.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.toString();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getMetadata().getPath();

                storageReference.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadPath = uri.toString();
                        Log.d("downloadPath",downloadPath);
                        saveData();
                    }
                });
            }
        });
    }

    private void saveData() {
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        User user = new User();
        user.setEmail(RegisterSingleton.getInstance().getEmail());
        user.setNickname(RegisterSingleton.getInstance().getNickname());
        user.setGender(RegisterSingleton.getInstance().getGender());
        user.setBirth(RegisterSingleton.getInstance().getBirthday());
        user.setEatingHabits(RegisterSingleton.getInstance().getEatingHabits());
        user.setLifePattern(RegisterSingleton.getInstance().getLifePattern());
        user.setPassword(RegisterSingleton.getInstance().getPassword());
        user.setMbti(RegisterSingleton.getInstance().getMbti());
        user.setProfileImage(downloadPath);
        user.setLocation(RegisterSingleton.getInstance().getLocation());
        user.setComment(RegisterSingleton.getInstance().getComment());

        mDatabaseRef.child("User").child(user.getEmail()).setValue(user);
    }
}
