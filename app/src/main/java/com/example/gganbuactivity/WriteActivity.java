package com.example.gganbuactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.databinding.ActivityWriteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {
    private ActivityWriteBinding mBinding;
    private final String TAG = "WriteActivity";
    private ImageView iv[];
    private final int OPEN_GALLERY = 1;
    private int image_index = 0;
    private String image_path[] = new String[]{"", "", ""};
    private Post post;
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityWriteBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        iv = new ImageView[]{mBinding.ivImage1, mBinding.ivImage2, mBinding.ivImage3};

        setSpinner(mBinding.spConditionEatingHabits, R.array.eatingHabits_array);
        setSpinner(mBinding.spConditionLifePattern, R.array.lifePattern_array);
        setSpinner(mBinding.spConditionMbti, R.array.mbti_array);

        chkListener();

        for (int i = 0; i < iv.length; i++) {
            iv[i].setOnClickListener(new OnClickListenerPutIndex(i) {
                @Override
                public void onClick(View view) {
                    image_index = index;

                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent();

                            if (Build.VERSION.SDK_INT < 19) {
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, OPEN_GALLERY);
                            } else {
                                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(intent, OPEN_GALLERY);
                            }
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        }
                    };

                    // 저장소에 대한 권한을 얻어옴
                    TedPermission.with(WriteActivity.this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("거부하셨습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();
                }
            });
        }

        mBinding.btnConditionRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataUpload();
            }
        });
    }

    private void chkListener() {
        mBinding.chkTypeMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    mBinding.chkType.setChecked(false);
                    mBinding.tvMonthLabel.setVisibility(View.VISIBLE);
                    mBinding.etMonth.setVisibility(View.VISIBLE);
                }
            }
        });
        mBinding.chkType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    mBinding.chkTypeMonth.setChecked(false);
                    mBinding.tvMonthLabel.setVisibility(View.INVISIBLE);
                    mBinding.etMonth.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void dataUpload() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        UploadTask uploadTask = null;
        String image_name = "";

        /**
         * image_path에 담긴 image를 FirebaseStorage에 업로드한다.
         */
        for (int i = 0; i < image_path.length; i++) {
            Log.d(TAG, "" + image_path[i]);
            if (!(image_path[i].equals(""))) {
                String filename = image_path[i] + i + ".jpg";
                Log.d(TAG, filename);
                Uri file = Uri.fromFile(new File(image_path[i]));
                StorageReference reference = storageReference.child("images/" + file.getLastPathSegment());
                uploadTask = reference.putFile(file);
            } else {
                continue;
            }
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.toString();
                    Log.d(TAG, "실패");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "성공");
                    post = new Post();
                    String url = taskSnapshot.getMetadata().getPath();
                    storageReference.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            setPost(post, uri);
                            uploadPost();
                            finish();
                        }
                    });
                }
            });
        }
    }

    private void uploadPost() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabaseRef.child("Post").child("" + RegisterSingleton.getInstance().getNickname()).setValue(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPost(Post post, Uri url) {
        post.setTitle(mBinding.etTitle.getText().toString());
        post.setLocation(mBinding.etLocation.getText().toString());
        if (mBinding.chkType.isChecked()) {
            post.setType("전세");
        } else {
            post.setType("월세");
            post.setMonth(mBinding.etMonth.getText().toString());
        }
        post.setDeposit(mBinding.etDeposit.getText().toString());
        post.setAge_start(mBinding.etConditionAgeStart.getText().toString());
        post.setAge_end(mBinding.etConditionAgeEnd.getText().toString());
        post.setGender(mBinding.chkMale.isChecked() ? "남자" : "여자");
        post.setEatingHabits(mBinding.spConditionEatingHabits.getSelectedItem().toString());
        post.setLifePattern(mBinding.spConditionLifePattern.getSelectedItem().toString());
        post.setMbti(mBinding.spConditionMbti.getSelectedItem().toString());
        post.setLink(mBinding.etLink.getText().toString());
        post.setNickname(RegisterSingleton.getInstance().getNickname());
        post.setUrl(url.toString());
    }

    /***
     * 저장소에서 이미지를 불러와, imageview에 연결해주고 image에대한 절대 경로를 저장한다.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OPEN_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                // Bitmap img = BitmapFactory.decodeStream(in);
                image_path[image_index] = getFullPathFromUri(this, data.getData());
                Log.d(TAG, "" + image_path[image_index]);
                in.close();

                iv[image_index].setImageURI(Uri.parse(image_path[image_index]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getFullPathFromUri(Context context, Uri fileUri) {
        String fullPath = null;
        final String column = "_data";
        Cursor cursor = context.getContentResolver().query(fileUri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            if (document_id == null) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (column.equalsIgnoreCase(cursor.getColumnName(i))) {
                        fullPath = cursor.getString(i);
                        break;
                    }
                }
            } else {
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                final String[] projection = {column};
                try {
                    cursor = context.getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            projection, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        fullPath = cursor.getString(cursor.getColumnIndexOrThrow(column));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            }
        }
        return fullPath;
    }


    public abstract class OnClickListenerPutIndex implements View.OnClickListener {
        protected int index;

        public OnClickListenerPutIndex(int index) {
            this.index = index;
        }
    }

    public void setSpinner(Spinner spinner, int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(WriteActivity.this, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}