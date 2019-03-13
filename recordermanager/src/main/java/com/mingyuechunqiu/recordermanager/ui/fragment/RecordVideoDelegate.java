package com.mingyuechunqiu.recordermanager.ui.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.constants.Constants;
import com.mingyuechunqiu.recordermanager.record.RecorderManagerFactory;
import com.mingyuechunqiu.recordermanager.record.RecorderManagerable;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.mingyuechunqiu.recordermanager.constants.Constants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.constants.Constants.CameraType.CAMERA_NOT_SET;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/29
 *     desc   : 录制视频代理类
 *              实现RecordVideoDelegateable
 *     version: 1.0
 * </pre>
 */
class RecordVideoDelegate implements RecordVideoDelegateable {

    private WeakReference<SurfaceView> svVideoRef;
    private WeakReference<AppCompatTextView> tvTimingRef;
    private WeakReference<CircleProgressButton> cpbRecordRef;
    private WeakReference<AppCompatImageView> ivFlipCameraRef, ivPlayRef, ivCancelRef, ivConfirmRef, ivBackRef;
    private WeakReference<Context> mContextRef;

    private RecorderManagerable mManager;
    private RecordVideoOption mOption;
    private Camera mCamera;
    private boolean isRecording;//标记是否正在录制中
    private boolean isReleaseRecord;//标记是否已经释放了资源
    private long mTiming;//计时数值
    private Disposable mTimingDisposable;//用于计时
    private boolean isInPlayingState;//标记是否处于播放视频状态
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private boolean hasHandledReleaseRecord;//标记是否处理了录制释放事件
    private int mVideoDuration;//录制视频时长（毫秒）
    private Constants.CameraType mCameraType;//摄像头类型

    RecordVideoDelegate(@NonNull Context context, @NonNull AppCompatTextView tvTiming,
                        @NonNull SurfaceView svVideo, @NonNull CircleProgressButton cpbRecord,
                        @NonNull AppCompatImageView ivFlipCamera,
                        @NonNull AppCompatImageView ivPlay, @NonNull AppCompatImageView ivCancel,
                        @NonNull AppCompatImageView ivConfirm, @NonNull AppCompatImageView ivBack,
                        @NonNull RecordVideoOption option) {
        mContextRef = new WeakReference<>(context);
        tvTimingRef = new WeakReference<>(tvTiming);
        svVideoRef = new WeakReference<>(svVideo);
        cpbRecordRef = new WeakReference<>(cpbRecord);
        ivFlipCameraRef = new WeakReference<>(ivFlipCamera);
        ivPlayRef = new WeakReference<>(ivPlay);
        ivCancelRef = new WeakReference<>(ivCancel);
        ivConfirmRef = new WeakReference<>(ivConfirm);
        ivBackRef = new WeakReference<>(ivBack);
        mOption = option;
        mCameraType = CAMERA_NOT_SET;
    }

    /**
     * 开始图像预览
     */
    @Override
    public void startPreview() {
        if (svVideoRef.get() == null) {
            return;
        }
        checkOrCreateRecorderManagerable();
        if (mCamera == null) {
            mCamera = mManager.initCamera(mCameraType, svVideoRef.get().getHolder());
            mCameraType = mManager.getCameraType();
        }
    }

    /**
     * 按下按钮开始录制视频
     *
     * @return 如果成功开始录制返回true，否则返回false
     */
    @Override
    public boolean pressToStartRecordVideo() {
        checkOrCreateRecorderManagerable();
        if (mCamera == null) {
            startPreview();
        }
        if (isRecording) {
            return false;
        }
        hasHandledReleaseRecord = false;
        if (ivFlipCameraRef.get() != null) {
            ivFlipCameraRef.get().setVisibility(View.GONE);
        }
        if (ivBackRef.get() != null) {
            ivBackRef.get().setVisibility(View.GONE);
        }
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
                        if (mContextRef.get() != null && tvTimingRef.get() != null) {
                            tvTimingRef.get().setText(mContextRef.get().getString(
                                    R.string.fill_record_timing, sbTiming.toString()));
                        }
                    }
                });
        startRecordVideo();
        return true;
    }

    /**
     * 释放按钮停止录制视频
     */
    @Override
    public void releaseToStopRecordVideo(final boolean isCancel) {
        //释放方法会执行两次，进行过滤
        if (hasHandledReleaseRecord) {
            return;
        }
        hasHandledReleaseRecord = true;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        //防止用户按下就抬起，导致MediaRecorder初始化还没完成就release导致报错
        int delayMillis = 0;
        if (mTiming < 1) {
            delayMillis = 1000;
        }
        final int finalDelayMillis = delayMillis;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finalDelayMillis == 1000) {
                    //小于规定时长，不进入播放环节，重置资源
                    if (mContextRef.get() != null) {
                        Toast.makeText(mContextRef.get(), R.string.warn_record_time_too_short, Toast.LENGTH_SHORT).show();
                    }
                    releaseRecorderManager();
                    releaseTiming();
                    isRecording = false;
                    resetResource();
                } else {
                    if (stopRecordVideo() && !isCancel) {
                        playVideo();
                        controlRecordOrPlayVisibility(true);
                    }
                }
            }
        }, finalDelayMillis);
    }

    @Override
    public void flipCamera() {
        if (svVideoRef.get() == null) {
            return;
        }
        checkOrCreateRecorderManagerable();
        mCamera = mManager.flipCamera(svVideoRef.get().getHolder());
        mCameraType = mManager.getCameraType();
    }

    /**
     * 开始录制视频
     */
    @Override
    public void startRecordVideo() {
        if (svVideoRef.get() == null) {
            return;
        }
        checkOrCreateRecorderManagerable();
        if (mCamera == null) {
            mCamera = mManager.initCamera(mCameraType, svVideoRef.get().getHolder());
        }
        if (mCameraType == CAMERA_FRONT) {
            mOption.getRecorderOption().setOrientationHint(270);
        } else {
            mOption.getRecorderOption().setOrientationHint(90);
        }
        isRecording = mManager.recordVideo(mCamera, svVideoRef.get().getHolder().getSurface(), mOption.getRecorderOption());
    }

    /**
     * 停止录制视频，返回是否录制成功
     *
     * @return 录制成功返回true，否则返回false
     */
    @Override
    public boolean stopRecordVideo() {
        if (!isRecording) {
            return false;
        }
        releaseRecorderManager();
        boolean isRecordSuccessful = true;//标记记录录音是否成功
        if (mTiming < 1) {
            if (mContextRef.get() != null) {
                Toast.makeText(mContextRef.get(), R.string.warn_record_time_too_short, Toast.LENGTH_SHORT).show();
            }
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
    @Override
    public void releaseTiming() {
        if (mTimingDisposable != null && !mTimingDisposable.isDisposed()) {
            mTimingDisposable.dispose();
            mTimingDisposable = null;
            mTiming = 0;
        }
    }

    /**
     * 播放录制好的视频
     */
    @Override
    public void playVideo() {
        if (svVideoRef.get() == null) {
            return;
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
        try {
            mMediaPlayer.setDataSource(mOption.getRecorderOption().getFilePath());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setDisplay(svVideoRef.get().getHolder());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mVideoDuration = mp.getDuration();
                }
            });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isInPlayingState = true;
    }

    @Override
    public void pausePlayVideo(boolean controlViews) {
        if (isInPlayingState && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (ivPlayRef.get() != null) {
                ivPlayRef.get().setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 恢复播放视频
     */
    @Override
    public void resumePlayVideo(boolean controlViews) {
        if (isInPlayingState && mMediaPlayer != null && svVideoRef.get() != null) {
            mMediaPlayer.setDisplay(svVideoRef.get().getHolder());
            mMediaPlayer.start();
            if (ivPlayRef.get() != null) {
                ivPlayRef.get().setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void controlPlayOrPauseVideo() {
        if (isInPlayingState && mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                pausePlayVideo(true);
            } else {
                resumePlayVideo(true);
            }
        }
    }

    /**
     * 释放播放资源
     */
    @Override
    public void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        isInPlayingState = false;
    }

    /**
     * 释放相机资源
     */
    @Override
    public void releaseCamera() {
        if (mManager == null) {
            return;
        }
        mManager.releaseCamera();
        mCamera = null;
    }

    /**
     * 重置资源进行下次拍摄
     */
    @Override
    public void resetResource() {
        releaseMediaPlayer();
        startPreview();
        controlRecordOrPlayVisibility(false);
    }

    /**
     * 控制录制或播放的控件可见性
     *
     * @param isInPlayingState 是否正在播放
     */
    @Override
    public void controlRecordOrPlayVisibility(boolean isInPlayingState) {
        if (mContextRef.get() == null || tvTimingRef.get() == null || cpbRecordRef.get() == null ||
                ivPlayRef.get() == null || ivCancelRef.get() == null || ivConfirmRef.get() == null) {
            return;
        }
        int playViewsVisibility, recordViewsVisibility;
        if (isInPlayingState) {
            playViewsVisibility = View.VISIBLE;
            recordViewsVisibility = View.GONE;
        } else {
            playViewsVisibility = View.GONE;
            recordViewsVisibility = View.VISIBLE;
            tvTimingRef.get().setText(mContextRef.get().getString(R.string.fill_record_timing, "00"));
            ivFlipCameraRef.get().setVisibility(recordViewsVisibility);
            ivPlayRef.get().setVisibility(playViewsVisibility);
        }
        ivCancelRef.get().setVisibility(playViewsVisibility);
        ivConfirmRef.get().setVisibility(playViewsVisibility);
        tvTimingRef.get().setVisibility(recordViewsVisibility);
        cpbRecordRef.get().setVisibility(recordViewsVisibility);
        ivBackRef.get().setVisibility(recordViewsVisibility);
    }

    /**
     * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
     *
     * @param surfaceHolder 图层控制
     */
    @Override
    public void onSurfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        surfaceHolder.setKeepScreenOn(true);
        if (!isInPlayingState) {
            startPreview();
            return;
        }
        mMediaPlayer.setDisplay(surfaceHolder);
        resumePlayVideo(false);
    }

    /**
     * 进入完成录制视频事件
     */
    @Override
    public void onCompleteRecordVideo() {
        if (mOption.getOnRecordVideoListener() != null) {
            mOption.getOnRecordVideoListener().onCompleteRecordVideo(mOption.getRecorderOption().getFilePath(), mVideoDuration);
        }
    }

    /**
     * 进入点击返回键时间
     */
    @Override
    public void onClickBack() {
        if (mOption.getOnRecordVideoListener() != null) {
            mOption.getOnRecordVideoListener().onClickBack();
        }
    }

    @Override
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        stopRecordVideo();
        isRecording = false;
        isReleaseRecord = false;
        mTiming = 0;
        mVideoDuration = 0;
        releaseMediaPlayer();
        mManager = null;
        mOption = null;
        mContextRef = null;
        svVideoRef = null;
        tvTimingRef = null;
        cpbRecordRef = null;
        ivFlipCameraRef = null;
        ivPlayRef = null;
        ivCancelRef = null;
        ivConfirmRef = null;
        ivBackRef = null;
        mCameraType = CAMERA_NOT_SET;
    }

    /**
     * 检查录制管理器是否存在，若不存在则创建
     */
    private void checkOrCreateRecorderManagerable() {
        if (mManager == null) {
            mManager = RecorderManagerFactory.newInstance();
        }
    }

    /**
     * 释放录制管理器
     */
    private void releaseRecorderManager() {
        if (!isReleaseRecord && mManager != null) {
            mManager.release();
            mManager = null;
            mCamera = null;
            isReleaseRecord = true;
        }
    }
}
