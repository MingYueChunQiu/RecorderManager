package com.mingyuechunqiu.recordermanager.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.record.RecorderOption;
import com.mingyuechunqiu.recordermanager.ui.fragment.RecordVideoFragment;
import com.mingyuechunqiu.recordermanager.ui.fragment.RecordVideoOption;

import java.io.File;

import static com.mingyuechunqiu.recordermanager.constants.Constants.EXTRA_RECORD_VIDEO_FILE_PATH;
import static com.mingyuechunqiu.recordermanager.constants.Constants.EXTRA_RECORD_VIDEO_MAX_DURATION;
import static com.mingyuechunqiu.recordermanager.constants.Constants.SUFFIX_MP4;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频Activity
 *              继承自AppCompatActivity
 *     version: 1.0
 * </pre>
 */
public class RecordVideoActivity extends AppCompatActivity {

    private RecordVideoFragment mRecordVideoFg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_video);
        if (getSupportFragmentManager() != null) {
            String filePath = getFilesDir().getAbsolutePath() + File.separator +
                    System.currentTimeMillis() + SUFFIX_MP4;
            int maxDuration = 30;
            if (getIntent() != null) {
                filePath = getIntent().getStringExtra(EXTRA_RECORD_VIDEO_FILE_PATH);
                maxDuration = getIntent().getIntExtra(EXTRA_RECORD_VIDEO_MAX_DURATION, 30);
            }
            if (TextUtils.isEmpty(filePath)) {
                File file = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                if (file != null) {
                    filePath = file.getAbsolutePath() + File.separator +
                            System.currentTimeMillis() + SUFFIX_MP4;
                }
            }
            RecordVideoOption option = new RecordVideoOption.Builder()
                    .setRecorderOption(new RecorderOption.Builder()
                            .buildDefaultVideoBean(filePath))
                    .setMaxDuration(maxDuration)
                    .setOnRecordVideoListener(new RecordVideoOption.OnRecordVideoListener() {
                        @Override
                        public void onCompleteRecordVideo(RecorderOption option) {
                            if (getIntent() != null) {
                                getIntent().putExtra(EXTRA_RECORD_VIDEO_FILE_PATH, option.getFilePath());
                                setResult(RESULT_OK, getIntent());
                            }
                            Toast.makeText(getApplicationContext(), option.getFilePath(), Toast.LENGTH_SHORT).show();
                            finishActivity();
                        }

                        @Override
                        public void onClickBack() {
                            finishActivity();
                        }
                    }).build();
            mRecordVideoFg = RecordVideoFragment.newInstance(option);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_record_video_container, mRecordVideoFg,
                            RecordVideoFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getSupportFragmentManager() != null && mRecordVideoFg != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mRecordVideoFg)
                    .commitAllowingStateLoss();
        }
        mRecordVideoFg = null;
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.fix_stand, R.anim.slide_out_bottom);
    }
}
