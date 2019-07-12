package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.constants.Constants;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerable;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.CameraType.CAMERA_NOT_SET;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/29
 *     desc   : 录制视频MVP中P层
 *              继承自RecordVideoContract.Presenter
 *     version: 1.0
 * </pre>
 */
class RecordVideoPresenter extends RecordVideoContract.Presenter<RecordVideoContract.View> {

    private static final int MSG_STOP_RECORD = 0x00;

    private WeakReference<SurfaceView> svVideoRef;
    private WeakReference<AppCompatTextView> tvTimingRef;
    private WeakReference<CircleProgressButton> cpbRecordRef;
    private WeakReference<AppCompatImageView> ivFlipCameraRef, ivPlayRef, ivCancelRef, ivConfirmRef, ivBackRef;

    private RecorderManagerable mManager;
    private RecordVideoOption mOption;
    private Camera mCamera;
    private boolean isRecording;//标记是否正在录制中
    private boolean isReleaseRecord;//标记是否已经释放了资源
    private long mTiming;//计时数值
    private boolean needStopDelayed;//标记是否需要延迟停止
    private Disposable mTimingDisposable;//用于计时
    private boolean isInPlayingState;//标记是否处于播放视频状态
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private boolean hasHandledReleaseRecord;//标记是否处理了录制释放事件
    private int mVideoDuration;//录制视频时长（毫秒）
    private Constants.CameraType mCameraType;//摄像头类型

    @Override
    void initView(@NonNull AppCompatTextView tvTiming, @NonNull SurfaceView svVideo, @NonNull CircleProgressButton cpbRecord, @NonNull AppCompatImageView ivFlipCamera, @NonNull AppCompatImageView ivPlay, @NonNull AppCompatImageView ivCancel, @NonNull AppCompatImageView ivConfirm, @NonNull AppCompatImageView ivBack, @NonNull RecordVideoOption option) {
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
    void startPreview() {
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
    boolean pressToStartRecordVideo() {
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
                        if (!checkViewRefIsNull() && mViewRef.get().getCurrentContext() != null &&
                                tvTimingRef.get() != null) {
                            tvTimingRef.get().setText(mViewRef.get().getCurrentContext().getString(
                                    R.string.rm_fill_record_timing, sbTiming.toString()));
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
    void releaseToStopRecordVideo(final boolean isCancel) {
        //释放方法会执行两次，进行过滤
        if (hasHandledReleaseRecord) {
            return;
        }
        hasHandledReleaseRecord = true;
        //防止用户按下就抬起，导致MediaRecorder初始化还没完成就release导致报错

        if (mHandler == null) {
            mHandler = new RecordVideoPresenter.MyHandler(this);
        } else {
            mHandler.removeMessages(MSG_STOP_RECORD);
        }
        if (mTiming < 1) {
            needStopDelayed = true;
        }
        Message message = mHandler.obtainMessage();
        message.what = MSG_STOP_RECORD;
        message.arg1 = isCancel ? 1 : 0;
        mHandler.sendMessageDelayed(message, needStopDelayed ? 1200 : 0);
    }

    @Override
    void flipCamera() {
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
    void startRecordVideo() {
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
    boolean stopRecordVideo() {
        if (!isRecording) {
            return false;
        }
        releaseRecorderManager();
        boolean isRecordSuccessful = true;//标记记录录制是否成功
        if (mTiming < 1) {
            if (!checkViewRefIsNull() && mViewRef.get().getCurrentContext() != null) {
                Toast.makeText(mViewRef.get().getCurrentContext(), R.string.rm_warn_record_time_too_short, Toast.LENGTH_SHORT).show();
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
    void releaseTiming() {
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
    void playVideo() {
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
                    onCompleteRecordVideo();
                }
            });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isInPlayingState = true;
    }

    @Override
    void pausePlayVideo(boolean controlViews) {
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
    void resumePlayVideo(boolean controlViews) {
        if (isInPlayingState && mMediaPlayer != null && svVideoRef.get() != null) {
            mMediaPlayer.setDisplay(svVideoRef.get().getHolder());
            mMediaPlayer.start();
            if (ivPlayRef.get() != null) {
                ivPlayRef.get().setVisibility(View.GONE);
            }
        }
    }

    @Override
    void controlPlayOrPauseVideo() {
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
    void releaseMediaPlayer() {
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
    void releaseCamera() {
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
    void resetResource() {
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
    void controlRecordOrPlayVisibility(boolean isInPlayingState) {
        if (checkViewRefIsNull() || mViewRef.get().getCurrentContext() == null ||
                tvTimingRef.get() == null || cpbRecordRef.get() == null ||
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
            tvTimingRef.get().setText(mViewRef.get().getCurrentContext().getString(R.string.rm_fill_record_timing, "00"));
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
    void onSurfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        surfaceHolder.setKeepScreenOn(true);
        if (!isInPlayingState) {
            startPreview();
            return;
        }
        mMediaPlayer.setDisplay(surfaceHolder);
        resumePlayVideo(false);
    }

    /**
     * 点击确认录制视频事件
     */
    @Override
    void onClickConfirm() {
        if (mOption.getOnRecordVideoListener() != null) {
            mOption.getOnRecordVideoListener().onClickConfirm(mOption.getRecorderOption().getFilePath(), mVideoDuration);
        }
    }

    /**
     * 点击取消按钮事件
     */
    @Override
    void onClickCancel() {
        if (mOption.getOnRecordVideoListener() != null) {
            mOption.getOnRecordVideoListener().onClickCancel(mOption.getRecorderOption().getFilePath(), mVideoDuration);
        }
    }

    /**
     * 点击返回键事件
     */
    @Override
    void onClickBack() {
        if (mOption.getOnRecordVideoListener() != null) {
            if (isInPlayingState) {
                resetResource();
                mOption.getOnRecordVideoListener().onClickCancel(mOption.getRecorderOption().getFilePath(), mVideoDuration);
            } else {
                mOption.getOnRecordVideoListener().onClickBack();
            }
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
        needStopDelayed = false;
        mTiming = 0;
        mVideoDuration = 0;
        releaseMediaPlayer();
        mManager = null;
        mOption = null;
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

    /**
     * 当完成一次录制时回调
     */
    private void onCompleteRecordVideo() {
        if (mOption.getOnRecordVideoListener() != null) {
            mOption.getOnRecordVideoListener().onCompleteRecordVideo(mOption.getRecorderOption().getFilePath(), mVideoDuration);
        }
    }

    private static class MyHandler extends Handler {

        private RecordVideoPresenter mPresenter;

        MyHandler(RecordVideoPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mPresenter == null) {
                return;
            }
            if (msg.what == MSG_STOP_RECORD) {
                if (mPresenter.needStopDelayed) {
                    mPresenter.needStopDelayed = false;
                    //小于规定时长，不进入播放环节，重置资源
                    if (!mPresenter.checkViewRefIsNull() &&
                            mPresenter.mViewRef.get().getCurrentContext() != null) {
                        Toast.makeText(mPresenter.mViewRef.get().getCurrentContext(), R.string.rm_warn_record_time_too_short, Toast.LENGTH_SHORT).show();
                    }
                    mPresenter.releaseRecorderManager();
                    mPresenter.releaseTiming();
                    mPresenter.isRecording = false;
                    mPresenter.resetResource();
                } else {
                    if (mPresenter.stopRecordVideo() && msg.arg1 == 0) {
                        mPresenter.playVideo();
                        mPresenter.controlRecordOrPlayVisibility(true);
                    }
                }
            }
        }
    }
}
