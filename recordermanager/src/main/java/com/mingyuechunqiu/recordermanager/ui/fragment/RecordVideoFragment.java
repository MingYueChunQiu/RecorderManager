package com.mingyuechunqiu.recordermanager.ui.fragment;

import android.Manifest;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.record.RecorderManager;
import com.mingyuechunqiu.recordermanager.record.RecorderManagerable;
import com.mingyuechunqiu.recordermanager.record.RecorderOption;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RecordVideoFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_RECORD_VIDEO = 1;

    private static final String[] permissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private SurfaceView svVideo;
    private AppCompatTextView tvTiming;
    private CircleProgressButton cpbRecord;
    private AppCompatImageView ivPlay, ivCancel, ivConfirm;
    private AppCompatImageView ivBack;

    private RecorderManagerable mManager;
    private RecordVideoOption mOption;
    private Camera mCamera;
    private boolean isRecording;
    private boolean isReleaseRecord;
    private long mTiming;
    private Disposable mTimingDisposable;
    private boolean isInPlayingState;//标记是否处于播放视频状态
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_video, container, false);
        svVideo = view.findViewById(R.id.sv_record_video_screen);
        tvTiming = view.findViewById(R.id.tv_record_video_timing);
        cpbRecord = view.findViewById(R.id.cpb_record_video_record);
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
                if (!checkHasPermissions() || mManager == null || isRecording) {
                    return false;
                }
                ivBack.setVisibility(View.GONE);
                isRecording = true;
                isReleaseRecord = false;
                releaseTiming();
                mTimingDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                mTiming = aLong;
                                StringBuilder sbTiming = new StringBuilder(mTiming + "");
                                if (mTiming < 10) {
                                    sbTiming.insert(0, "0");
                                }
                                tvTiming.setText(getString(R.string.fill_record_timing, sbTiming.toString()));
                            }
                        });
                startRecordVideo();
                return true;
            }

            @Override
            public void onProgress(CircleProgressButton v, float progress) {

            }

            @Override
            public int onReleaseProgress(CircleProgressButton v) {
                if (stopRecordVideo()) {
                    playVideo();
                    controlRecordOrPlayVisibility(true);
                }
                return 360;
            }

            @Override
            public boolean onCancelProgress(CircleProgressButton v) {
                stopRecordVideo();
                return false;
            }
        });
        ivPlay.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivConfirm.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        checkHasPermissions();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isInPlayingState && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInPlayingState && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecordVideo();
        isRecording = false;
        isReleaseRecord = false;
        mTiming = 0;
        releaseMediaPlayer();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sv_record_video_screen) {
            if (isInPlayingState && mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    ivPlay.setVisibility(View.VISIBLE);
                } else {
                    resumePlayVideo();
                }
            }
        } else if (id == R.id.iv_record_video_play) {
            if (isInPlayingState && mMediaPlayer != null) {
                resumePlayVideo();
            }
        } else if (id == R.id.iv_record_video_cancel) {
            resetResource();
        } else if (id == R.id.iv_record_video_confirm) {
            if (mOption.getOnRecordVideoListener() != null) {
                mOption.getOnRecordVideoListener().onCompleteRecordVideo(mOption.getRecorderOption());
            }
        } else if (id == R.id.iv_record_video_back) {
            if (mOption.getOnRecordVideoListener() != null) {
                mOption.getOnRecordVideoListener().onClickBack();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        checkOrCreateRecorderManagerable();
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mManager == null) {
            return;
        }
        mManager.releaseCamera(mCamera);
        mCamera = null;
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

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(String filePath) {
        return newInstance(new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(filePath))
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
     * 开始图像预览
     */
    private void startPreview() {
        checkOrCreateRecorderManagerable();
        if (mCamera == null) {
            mCamera = mManager.initCamera();
        }
        try {
            mCamera.setPreviewDisplay(svVideo.getHolder());
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录制视频
     */
    private void startRecordVideo() {
        isRecording = mManager.recordVideo(mCamera, svVideo.getHolder().getSurface(), mOption.getRecorderOption());
    }

    /**
     * 停止录制视频，返回是否录制成功
     *
     * @return 录制成功返回true，否则返回false
     */
    private boolean stopRecordVideo() {
        if (!isRecording) {
            return false;
        }
        if (!isReleaseRecord && mManager != null) {
            mManager.release();
            mManager.releaseCamera(mCamera);
            mCamera = null;
            isReleaseRecord = true;
            mManager = null;
        }
        boolean isRecordSuccessful = true;//标记记录录音是否成功
        if (mTiming < 1) {
            Toast.makeText(getContext(), R.string.warn_record_time_too_short, Toast.LENGTH_SHORT).show();
            isRecordSuccessful = false;
        }
        releaseTiming();
        if (isRecordSuccessful) {
            //停顿200毫秒，确保写入数据结束完成
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isRecording = false;
        return isRecordSuccessful;
    }

    /**
     * 释放计时资源
     */
    private void releaseTiming() {
        if (mTimingDisposable != null && !mTimingDisposable.isDisposed()) {
            mTimingDisposable.dispose();
            mTimingDisposable = null;
        }
    }

    /**
     * 播放录制好的视频
     */
    private void playVideo() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        try {
            mMediaPlayer.setDataSource(mOption.getRecorderOption().getFilePath());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setDisplay(svVideo.getHolder());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isInPlayingState = true;
    }

    /**
     * 恢复播放视频
     */
    private void resumePlayVideo() {
        mMediaPlayer.start();
        ivPlay.setVisibility(View.GONE);
    }

    /**
     * 释放播放资源
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        isInPlayingState = false;
    }

    /**
     * 重置资源进行下次拍摄
     */
    private void resetResource() {
        releaseMediaPlayer();
        startPreview();
        controlRecordOrPlayVisibility(false);
    }

    /**
     * 控制录制或播放的控件可见性
     *
     * @param isInPlayingState 是否正在播放
     */
    private void controlRecordOrPlayVisibility(boolean isInPlayingState) {
        int playViewsVisibility, recordViewsVisibility;
        if (isInPlayingState) {
            playViewsVisibility = View.VISIBLE;
            recordViewsVisibility = View.GONE;
        } else {
            playViewsVisibility = View.GONE;
            recordViewsVisibility = View.VISIBLE;
            tvTiming.setText(getString(R.string.fill_record_timing, "00"));
            ivPlay.setVisibility(playViewsVisibility);
        }
        ivCancel.setVisibility(playViewsVisibility);
        ivConfirm.setVisibility(playViewsVisibility);
        tvTiming.setVisibility(recordViewsVisibility);
        cpbRecord.setVisibility(recordViewsVisibility);
        ivBack.setVisibility(recordViewsVisibility);
    }

    /**
     * 检查录制管理器是否存在，若不存在则创建
     */
    private void checkOrCreateRecorderManagerable() {
        if (mManager == null) {
            mManager = RecorderManager.newInstance();
        }
    }
}
