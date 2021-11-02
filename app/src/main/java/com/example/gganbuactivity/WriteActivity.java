package com.example.gganbuactivity;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gganbuactivity.DTO.Post;
import com.example.gganbuactivity.databinding.ActivityWriteBinding;
import com.example.gganbuactivity.zipcode.NetworkStatus;
import com.example.gganbuactivity.zipcode.WebViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {
    private ActivityWriteBinding mBinding;
    private final String TAG = "WriteActivity";
    private ImageView iv[];
    private final int OPEN_GALLERY = 1;
    private int image_index = 0;
    private ArrayList<String> image_path = new ArrayList<>();
    private Post post = new Post();
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000; // 주소 요청코드 상수 requestCode

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

        Intent intent = getIntent();
        if (intent.hasExtra("Edit")) {
            editData();
        }

        // 터치 안되게 막기
        mBinding.etLocation.setFocusable(false);
        mBinding.etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("주소설정페이지", "주소입력창 클릭");
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });

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
                                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                finish();
            }
        });
    }

    private void editData() {
        String path = (RegisterSingleton.getInstance().getNickname()).toString();
        mDatabaseRef = mDatabaseRef.child("Post").child(path);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);

                mBinding.etTitle.setText(post.getTitle());
                mBinding.etLocation.setText(post.getLocation());
                if (post.getType().equals("월세")) {
                    mBinding.chkTypeMonth.setChecked(true);
                    mBinding.etDeposit.setText(post.getDeposit());
                    mBinding.tvMonthLabel.setVisibility(View.VISIBLE);
                    mBinding.etMonth.setVisibility(View.VISIBLE);
                    mBinding.etMonth.setText(post.getMonth());
                } else {
                    mBinding.chkType.setChecked(true);
                    mBinding.etDeposit.setText(post.getDeposit());
                }
                for(int i=0;i<post.getUrl().size();i++){
                    switch (i){
                        case 0:
                            Glide.with(WriteActivity.this)
                                    .load(post.getUrl().get(i))
                                    .into(mBinding.ivImage1);
                            break;
                        case 1:
                            Glide.with(WriteActivity.this)
                                    .load(post.getUrl().get(i))
                                    .into(mBinding.ivImage2);
                        case 2:
                            Glide.with(WriteActivity.this)
                                    .load(post.getUrl().get(i))
                                    .into(mBinding.ivImage3);
                            break;
                    }
                }
                mDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.toString());
                mDatabaseRef.removeEventListener(this);
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
        ArrayList<String> downloadPath = new ArrayList<>();

        /**
         * image_path에 담긴 image를 FirebaseStorage에 업로드한다.
         */

        for (int i = 0; i < image_path.size(); i++) {
            String filename = image_path.get(i);
            Log.d(TAG, filename);
            Uri file = Uri.fromFile(new File(image_path.get(i)));
            StorageReference reference = storageReference.child("images/" + file.getLastPathSegment());
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
                            downloadPath.add(uri.toString());
                            if(downloadPath.size() == image_path.size()){
                                setPost(downloadPath);
                                uploadPost();
                            }
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
                mDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("WriteActivity", error.toString());
            }
        });
    }

    private void setPost(ArrayList<String> downloadPath) {
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
        post.setUrl(downloadPath);
    }

    /***
     * 저장소에서 이미지를 불러와, imageview에 연결해주고 image에대한 절대 경로를 저장한다.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        switch (requestCode) {
            case OPEN_GALLERY:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = intent.getData();
                    Uri photoUri = intent.getData();
                    //아래 커서 이용해서 사진의 경로 불러오기
                    Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    @SuppressLint("Range") String mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));


                    switch (image_index) {
                        case 0:
                            mBinding.ivImage1.setImageURI(Uri.parse(mediaPath));
                            image_path.add(mediaPath);
                            break;
                        case 1:
                            mBinding.ivImage2.setImageURI(Uri.parse(mediaPath));
                            image_path.add(mediaPath);
                            break;
                        case 2:
                            mBinding.ivImage3.setImageURI(Uri.parse(mediaPath));
                            image_path.add(mediaPath);
                            Log.d("image_path",""+image_path.size());
                            break;
                    }
                    mBinding.ivImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                break;
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Log.i("test", "data:" + data);
                        mBinding.etLocation.setText(data);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
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