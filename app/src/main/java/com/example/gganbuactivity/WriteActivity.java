package com.example.gganbuactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gganbuactivity.databinding.ActivityWriteBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.InputStream;
import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {
    private ActivityWriteBinding mBinding;
    private final String TAG = "WriteActivity";
    private ImageView iv[];
    private final int OPEN_GALLERY = 1;
    private int image_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityWriteBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        iv = new ImageView[]{mBinding.ivImage1, mBinding.ivImage2, mBinding.ivImage3};

        for (int i = 0; i < iv.length; i++) {
            iv[i].setOnClickListener(new OnClickListenerPutIndex(i) {
                @Override
                public void onClick(View view) {
                    image_index = index;

                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, OPEN_GALLERY);
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        }
                    };

                    TedPermission.with(WriteActivity.this)
                            .setPermissionListener(permissionListener)
                            .setRationaleMessage("갤러리 이용을 위해서 갤러리 접근 권한이 필요해요")
                            .setDeniedMessage("거부하셨습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OPEN_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                iv[image_index].setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public abstract class OnClickListenerPutIndex implements View.OnClickListener{
        protected int index;
        public OnClickListenerPutIndex(int index){
            this.index = index;
        }
    }
}