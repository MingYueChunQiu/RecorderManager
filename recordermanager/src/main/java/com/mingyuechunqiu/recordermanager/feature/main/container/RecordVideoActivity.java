package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.feature.main.detail.RecordVideoFragment;
import com.mingyuechunqiu.recordermanager.ui.activity.BaseRecordVideoActivity;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_FILE_PATH;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_MAX_DURATION;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_RESULT_INFO;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.SUFFIX_MP4;

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
public class RecordVideoActivity extends BaseRecordVideoActivity implements EasyPermissions.PermissionCallbacks {

    private RecordVideoFragment mRecordVideoFg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_record_video);
        if (!RecordPermissionUtils.checkRecordPermissions(this)) {
            finishActivity();
            return;
        }
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
                        public void onCompleteRecordVideo(String filePath, int videoDuration) {
                            if (getIntent() != null) {
                                getIntent().putExtra(EXTRA_RECORD_VIDEO_RESULT_INFO, new RecordVideoResultInfo.Builder()
                                        .setDuration(videoDuration)
                                        .setFilePath(filePath)
                                        .build());
                                setResult(RESULT_OK, getIntent());
                            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    /**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    protected AppCompatTextView getTimingView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getTimingView();
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    protected CircleProgressButton getCircleProgressButton() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCircleProgressButton();
    }

    /**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    protected AppCompatImageView getFlipCameraView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getFlipCameraView();
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    protected AppCompatImageView getPlayView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getPlayView();
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    protected AppCompatImageView getCancelView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCancelView();
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    protected AppCompatImageView getConfirmView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getConfirmView();
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    protected AppCompatImageView getBackView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getBackView();
    }

    /**
     * 销毁界面
     */
    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.rm_fix_stand, R.anim.rm_slide_out_bottom);
    }
}
