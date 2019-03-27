package com.mingyuechunqiu.recordermanager.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.record.RecorderOption;
import com.mingyuechunqiu.recordermanager.ui.fragment.RecordVideoFragment;
import com.mingyuechunqiu.recordermanager.ui.fragment.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.constants.Constants.EXTRA_RECORD_VIDEO_DURATION;
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
public class RecordVideoActivity extends AppCompatActivity implements KeyBackCallback, EasyPermissions.PermissionCallbacks {

    private static final String[] permissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RecordVideoFragment mRecordVideoFg;
    private List<OnKeyBackListener> mListeners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_record_video);
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, getString(R.string.warn_allow_record_video_permissions), 1, permissions);
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
                                getIntent().putExtra(EXTRA_RECORD_VIDEO_FILE_PATH, filePath);
                                getIntent().putExtra(EXTRA_RECORD_VIDEO_DURATION, videoDuration);
                                setResult(RESULT_OK, getIntent());
                            }
                            if (mRecordVideoFg != null) {
                                mRecordVideoFg.unLockCamera(true);
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
        if (mListeners != null) {
            mListeners.clear();
            mListeners = null;
        }
        if (getSupportFragmentManager() != null && mRecordVideoFg != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mRecordVideoFg)
                    .commitAllowingStateLoss();
        }
        mRecordVideoFg = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mListeners != null && mListeners.size() > 0) {
            for (OnKeyBackListener listener : mListeners) {
                if (listener != null) {
                    listener.onClickKeyBack(event);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
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
        new AppSettingsDialog.Builder(this)
                .setTitle(R.string.set_permission)
                .setRationale(R.string.warn_allow_record_video_permissions)
                .setPositiveButton(R.string.set)
                .setNegativeButton(R.string.cancel)
                .build().show();
    }

    /**
     * 添加点击返回事件监听器
     *
     * @param listener 监听器
     */
    @Override
    public void addOnKeyBackListener(OnKeyBackListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
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
