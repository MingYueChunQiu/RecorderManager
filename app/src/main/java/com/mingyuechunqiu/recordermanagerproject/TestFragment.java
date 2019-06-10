package com.mingyuechunqiu.recordermanagerproject;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                RecorderManagerFactory.getRecordVideoRequest().startRecordVideo(TestFragment.this, 0);
            }
        });
        return view;
    }
}
