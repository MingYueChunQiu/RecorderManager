package com.mingyuechunqiu.recordermanager.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.record.RecorderOption;
import com.mingyuechunqiu.recordermanager.ui.activity.KeyBackCallback;
import com.mingyuechunqiu.recordermanager.ui.activity.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频界面
 *              继承自Fragment
 *     version: 1.0
 * </pre>
 */
public class RecordVideoFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_RECORD_VIDEO = 1;

    private static final String[] permissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private AppCompatTextView tvTiming;
    private CircleProgressButton cpbRecord;
    private AppCompatImageView ivFlipCamera, ivPlay, ivCancel, ivConfirm, ivBack;

    private RecordVideoOption mOption;
    private RecordVideoDelegateable mDelegateable;
    private boolean isSurfaceHolderDestroyed;//标记SurfaceHolder是否被销毁了

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_video, container, false);
        SurfaceView svVideo = view.findViewById(R.id.sv_record_video_screen);
        tvTiming = view.findViewById(R.id.tv_record_video_timing);
        cpbRecord = view.findViewById(R.id.cpb_record_video_record);
        ivFlipCamera = view.findViewById(R.id.iv_record_video_flip_camera);
        ivPlay = view.findViewById(R.id.iv_record_video_play);
        ivCancel = view.findViewById(R.id.iv_record_video_cancel);
        ivConfirm = view.findViewById(R.id.iv_record_video_confirm);
        ivBack = view.findViewById(R.id.iv_record_video_back);
        svVideo.getHolder().addCallback(this);
        svVideo.getHolder().setKeepScreenOn(true);
        svVideo.setOnClickListener(this);
        tvTiming.setText(getString(R.string.fill_record_timing, "00"));
        cpbRecord.setMaxProgress(mOption.getMaxDuration());
        cpbRecord.setOnCircleProgressButtonListener(new CircleProgressButton.OnCircleProgressButtonListener() {
            @Override
            public boolean onPreProgress(CircleProgressButton v) {
                if (!checkHasPermissions() || mDelegateable == null) {
                    return false;
                }
                return mDelegateable.pressToStartRecordVideo();
            }

            @Override
            public void onProgress(CircleProgressButton v, float progress) {

            }

            @Override
            public int onReleaseProgress(CircleProgressButton v) {
                if (mDelegateable != null) {
                    mDelegateable.releaseToStopRecordVideo(false);
                }
                return 360;
            }

            @Override
            public boolean onCancelProgress(CircleProgressButton v) {
                if (mDelegateable != null) {
                    mDelegateable.releaseToStopRecordVideo(true);
                }
                return false;
            }
        });
        ivFlipCamera.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivConfirm.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        if (getContext() != null) {
            mDelegateable = new RecordVideoDelegate(getContext(), tvTiming, svVideo, cpbRecord,
                    ivFlipCamera, ivPlay, ivCancel, ivConfirm, ivBack, mOption);
        }
        if (getActivity() instanceof KeyBackCallback) {
            ((KeyBackCallback) getActivity()).addOnKeyBackListener(new RecordVideoActivity.OnKeyBackListener() {
                @Override
                public void onClickKeyBack(KeyEvent event) {
                    if (mDelegateable != null) {
                        mDelegateable.onClickBack();
                    }
                }
            });
        }
        checkHasPermissions();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在红米Note5A上，锁屏没有调用surfaceDestroyed方法，所以要加以判断
        if (!isSurfaceHolderDestroyed && mDelegateable != null) {
            mDelegateable.resumePlayVideo(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDelegateable != null) {
            mDelegateable.pausePlayVideo(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDelegateable != null) {
            mDelegateable.release();
            mDelegateable = null;
        }
        mOption = null;
        isSurfaceHolderDestroyed = false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sv_record_video_screen) {
            if (mDelegateable != null) {
                mDelegateable.controlPlayOrPauseVideo();
            }
        } else if (id == R.id.iv_record_video_flip_camera) {
            if (mDelegateable != null) {
                mDelegateable.flipCamera();
            }
        } else if (id == R.id.iv_record_video_play) {
            if (mDelegateable != null) {
                mDelegateable.resumePlayVideo(true);
            }
        } else if (id == R.id.iv_record_video_cancel) {
            if (mDelegateable != null) {
                mDelegateable.resetResource();
            }
        } else if (id == R.id.iv_record_video_confirm) {
            if (mDelegateable != null) {
                mDelegateable.onCompleteRecordVideo();
            }
        } else if (id == R.id.iv_record_video_back) {
            if (mDelegateable != null) {
                mDelegateable.onClickBack();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mDelegateable != null) {
            mDelegateable.onSurfaceCreated(holder);
        }
        isSurfaceHolderDestroyed = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mDelegateable != null) {
            mDelegateable.releaseCamera();
        }
        isSurfaceHolderDestroyed = true;
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
                .setTitle(R.string.request_record_video)
                .setRationale("")
                .setPositiveButton(R.string.confirm)
                .setNegativeButton(R.string.cancel)
                .build();
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(String filePath) {
        return newInstance(filePath, 30);
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath    存储文件路径
     * @param maxDuration 最大时长（秒数）
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(String filePath, int maxDuration) {
        return newInstance(new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(filePath))
                .setMaxDuration(maxDuration)
                .build());
    }

    /**
     * 获取录制视频Fragment实例
     *
     * @param option 录制配置信息对象
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(RecordVideoOption option) {
        RecordVideoFragment fragment = new RecordVideoFragment();
        fragment.mOption = option;
        if (fragment.mOption == null) {
            fragment.mOption = new RecordVideoOption();
        }
        if (fragment.mOption.getRecorderOption() == null && fragment.getContext() != null) {
            File file = fragment.getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (file != null) {
                fragment.mOption.setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(
                        file.getAbsolutePath() +
                                File.separator + System.currentTimeMillis() + ".mp4"));
            }
        }
        return fragment;
    }

    /**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    public AppCompatTextView getTimingView() {
        return tvTiming;
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    public CircleProgressButton getCircleProgressButton() {
        return cpbRecord;
    }

    /**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    public AppCompatImageView getFlipCameraView() {
        return ivFlipCamera;
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    public AppCompatImageView getPlayView() {
        return ivPlay;
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    public AppCompatImageView getCancelView() {
        return ivCancel;
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    public AppCompatImageView getConfirmView() {
        return ivConfirm;
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    public AppCompatImageView getBackView() {
        return ivBack;
    }

    /**
     * 检测是否已经获取到权限
     *
     * @return 如果获取到权限返回true，否则返回false
     */
    private boolean checkHasPermissions() {
        if (getContext() == null) {
            return false;
        }
        if (!EasyPermissions.hasPermissions(getContext(), permissions)) {
            EasyPermissions.requestPermissions(RecordVideoFragment.this,
                    getString(R.string.request_record_video_rationale), REQUEST_RECORD_VIDEO, permissions);
            return false;
        }
        return true;
    }
}
