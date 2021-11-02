package com.example.gganbuactivity.registerfragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.WriteActivity;
import com.example.gganbuactivity.databinding.FragmentProfileBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class Fragment_profile extends Fragment {
    private FragmentProfileBinding mBinding;
    private Context context;
    private final int OPEN_GALLERY = 1;
    private String mediaPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();

        mBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent();

                        if (Build.VERSION.SDK_INT < 19) {
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, OPEN_GALLERY);
                        } else {
                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            Fragment fragment = Fragment_profile.this;
                            fragment.startActivityForResult(intent, OPEN_GALLERY);
                        }
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    }
                };

                // 저장소에 대한 권한을 얻어옴
                TedPermission.with(context)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("거부하셨습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileImage = mediaPath;
                RegisterSingleton.getInstance().setProfileImage(profileImage);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_location fragment_location = new Fragment_location();
                transaction.replace(R.id.register_layout, fragment_location);
                transaction.commit();
            }
        });
        return view;
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = intent.getData();
            Uri photoUri = intent.getData();
            //아래 커서 이용해서 사진의 경로 불러오기
            Cursor cursor = context.getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

            mBinding.ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mBinding.ivProfile.setImageURI(Uri.parse(mediaPath));
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
