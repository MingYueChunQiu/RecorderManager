package com.mingyuechunqiu.recordermanagerproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_RESULT_INFO;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //    private static final String[] permissions = new String[]{
//            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_main);
//        AppCompatButton btnVideo = findViewById(R.id.btn_main_system_record_video);
//        btnVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (!EasyPermissions.hasPermissions(MainActivity.this, permissions)) {
////                    EasyPermissions.requestPermissions(MainActivity.this, "请求拍摄", 1, permissions);
////                    return;
////                }
//                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                //设置视频录制的最长时间
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
//                //设置视频录制的画质
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                startActivityForResult(intent, 0);
//            }
//        });
//        AppCompatButton btnRecord = findViewById(R.id.btn_main_record_video);
//        btnRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (!EasyPermissions.hasPermissions(MainActivity.this, permissions)) {
////                    EasyPermissions.requestPermissions(MainActivity.this, "请求拍摄", 1, permissions);
////                    return;
////                }
////                startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
//                Log.d("份dewv ", getResources().getDisplayMetrics().widthPixels + " " +
//                        getResources().getDisplayMetrics().heightPixels + " "
//                        + (getResources().getDisplayMetrics().widthPixels * 1.0f / getResources().getDisplayMetrics().heightPixels));
//                RecorderManagerFactory.getRecordVideoRequest().startRecordVideo(MainActivity.this, 0);
//            }
//        });

        setContentView(R.layout.activity_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_container, new TestFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
//            Uri uri = data.getData();
            RecordVideoResultInfo info = data.getParcelableExtra(EXTRA_RECORD_VIDEO_RESULT_INFO);
            Log.e("MainActivity", "onActivityResult: " + " "
                    + info.getDuration() + " " + info.getFilePath());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d("份", requestCode + "");
        RecorderManagerFactory.getRecordVideoRequest().startRecordVideo(MainActivity.this, 0);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        new AppSettingsDialog.Builder(this)
//                .setTitle("申请")
//                .build();
    }
}
