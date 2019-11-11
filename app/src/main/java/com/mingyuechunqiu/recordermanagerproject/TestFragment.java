package com.mingyuechunqiu.recordermanagerproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoButtonOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/6/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        AppCompatButton btnVideo = view.findViewById(R.id.btn_main_system_record_video);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!EasyPermissions.hasPermissions(MainActivity.this, permissions)) {
//                    EasyPermissions.requestPermissions(MainActivity.this, "请求拍摄", 1, permissions);
//                    return;
//                }
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                //设置视频录制的最长时间
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
                //设置视频录制的画质
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 0);
            }
        });
        AppCompatButton btnRecord = view.findViewById(R.id.btn_main_record_video);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!EasyPermissions.hasPermissions(MainActivity.this, permissions)) {
//                    EasyPermissions.requestPermissions(MainActivity.this, "请求拍摄", 1, permissions);
//                    return;
//                }
//                startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
                Log.d("份dewv ", getResources().getDisplayMetrics().widthPixels + " " +
                        getResources().getDisplayMetrics().heightPixels + " "
                        + (getResources().getDisplayMetrics().widthPixels * 1.0f / getResources().getDisplayMetrics().heightPixels));
                RecorderManagerFactory.getRecordVideoRequest().startRecordVideo(TestFragment.this, 0,
                        new RecordVideoRequestOption.Builder()
                                .setMaxDuration(20)
                                .setRecordVideoOption(new RecordVideoOption.Builder()
                                        .setHideFlipCameraButton(true)
                                        .setMaxDuration(10)
                                        .setRecordVideoButtonOption(new RecordVideoButtonOption.Builder()
                                                .setIdleCircleColor(Color.BLUE)
                                                .setPressedRingColor(Color.GREEN)
                                                .build())
                                        .setCameraType(RecorderManagerConstants.CameraType.CAMERA_FRONT)
                                        .build())
                                .build()
//                        , new RecordVideoRequestOption.Builder()
//                                .setMaxDuration(10)
//                                .setHideFlipCameraButton(true)
//                                .setRecorderOption(new RecorderOption.Builder()
//                                        .buildDefaultVideoBean(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
//                                                + File.separator + "test.mp4"))
//                                .build()
                );
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0 && data != null) {
//            Uri uri = data.getData();
            RecordVideoResultInfo info = RecorderManagerFactory.getRecordVideoResult(data);
            if (info != null) {
                Log.e("MainActivity", "onActivityResult: " + " "
                        + info.getDuration() + " " + info.getFilePath());
            }
        }
    }
}
