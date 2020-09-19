package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType.CAMERA_NOT_SET;

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
class RecordVideoPresenter extends RecordVideoContract.Presenter<RecordVideoContract.View<?>> {

    //停止录制
    private static final int MSG_STOP_RECORD = 0x00;

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
    private MyHandler mHandler;
    private boolean hasHandledReleaseRecord;//标记是否处理了录制释放事件
    private int mVideoDuration;//录制视频时长（毫秒）
    private RecorderManagerConstants.CameraType mCameraType;//摄像头类型

    @Override
    void initView(@NonNull RecordVideoOption option) {
        mOption = option;
        mCameraType = mOption.getCameraType();
    }

    /**
     * 开始图像预览
     */
    @Override
    void startPreview(@Nullable SurfaceHolder holder) {
        if (holder == null) {
            return;
        }
        checkOrCreateRecorderManager();
        if (mCamera == null) {
            mCamera = mManager.initCamera(mCameraType, holder);
            mCameraType = mManager.getCameraType();
        }
    }

    /**
     * 按下按钮开始录制视频
     *
     * @return 如果成功开始录制返回true，否则返回false
     */
    @Override
    boolean pressToStartRecordVideo(@Nullable SurfaceHolder holder, @NonNull AppCompatImageView ivFlipCamera,
                                    @NonNull AppCompatImageView ivBack) {
        if (checkViewRefIsNull()) {
            return false;
        }
        checkOrCreateRecorderManager();
        if (mCamera == null) {
            startPreview(holder);
        }
        if (isRecording) {
            return false;
        }
        hasHandledReleaseRecord = false;
        ivFlipCamera.setVisibility(View.GONE);
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
                        if (!checkViewRefIsNull() && mViewRef.get().getCurrentContext() != null) {
                            mViewRef.get().showTimingText(sbTiming.toString());
                        }
                    }
                });
        startRecordVideo(holder);
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
            mHandler = new MyHandler(this);
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
    void flipCamera(@Nullable SurfaceHolder holder) {
        if (holder == null) {
            return;
        }
        checkOrCreateRecorderManager();
        mCamera = mManager.flipCamera(holder);
        mCameraType = mManager.getCameraType();
    }

    /**
     * 开始录制视频
     */
    private void startRecordVideo(@Nullable SurfaceHolder holder) {
        if (holder == null) {
            return;
        }
        Surface surface = holder.getSurface();
        if (surface == null) {
            return;
        }
        checkOrCreateRecorderManager();
        if (mCamera == null) {
            mCamera = mManager.initCamera(mCameraType, holder);
        }
        if (mCameraType == CAMERA_FRONT) {
            mOption.getRecorderOption().setOrientationHint(270);
        } else {
            mOption.getRecorderOption().setOrientationHint(90);
        }
        isRecording = mManager.recordVideo(mCamera, surface, mOption.getRecorderOption());
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
            showErrorToast();
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
        if (checkViewRefIsNull()) {
            return;
        }
        SurfaceHolder holder = mViewRef.get().getSurfaceHolder();
        if (holder == null) {
            return;
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
        try {
            mMediaPlayer.setDataSource(mOption.getRecorderOption().getFilePath());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setDisplay(holder);
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
    void pausePlayVideo(boolean controlViews, @NonNull AppCompatImageView ivPlay) {
        if (isInPlayingState && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            ivPlay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 恢复播放视频
     */
    @Override
    void resumePlayVideo(boolean controlViews, @NonNull AppCompatImageView ivPlay, @Nullable SurfaceHolder holder) {
        if (isInPlayingState && mMediaPlayer != null && holder != null) {
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.start();
            ivPlay.setVisibility(View.GONE);
        }
    }

    @Override
    void controlPlayOrPauseVideo(@NonNull AppCompatImageView ivPlay, @Nullable SurfaceHolder holder) {
        if (isInPlayingState && mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                pausePlayVideo(true, ivPlay);
            } else {
                resumePlayVideo(true, ivPlay, holder);
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
        if (!checkViewRefIsNull()) {
            startPreview(mViewRef.get().getSurfaceHolder());
            mViewRef.get().controlRecordOrPlayVisibility(false);
        }
    }

    /**
     * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
     *
     * @param holder 图层控制
     */
    @Override
    void onSurfaceCreated(@NonNull SurfaceHolder holder, @NonNull AppCompatImageView ivPlay) {
        holder.setKeepScreenOn(true);
        if (!isInPlayingState) {
            startPreview(holder);
            return;
        }
        mMediaPlayer.setDisplay(holder);
        resumePlayVideo(false, ivPlay, holder);
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

    @NonNull
    @Override
    String getTimingHint(@NonNull String timing) {
        if (checkViewRefIsNull()) {
            return "";
        }
        Context context = mViewRef.get().getCurrentContext();
        if (context == null) {
            return "";
        }
        String timingHint = mOption.getTimingHint();
        if (TextUtils.isEmpty(mOption.getTimingHint())) {
            timingHint = context.getString(R.string.rm_fill_record_timing, timing);
        }
        return timingHint != null ? timingHint : "";
    }

    @Override
    void switchFlashlightState(boolean turnOn) {

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
        mCameraType = CAMERA_NOT_SET;
    }

    /**
     * 检查录制管理器是否存在，若不存在则创建
     */
    private void checkOrCreateRecorderManager() {
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

    private void showErrorToast() {
        if (checkViewRefIsNull()) {
            return;
        }
        Context context = mViewRef.get().getCurrentContext();
        if (context == null) {
            return;
        }
        String msg = mOption.getErrorToastMsg();
        if (TextUtils.isEmpty(msg)) {
            msg = context.getString(R.string.rm_warn_record_time_too_short);
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void setRecordOrPlayVisible() {
        if (checkViewRefIsNull()) {
            return;
        }
        mViewRef.get().controlRecordOrPlayVisibility(true);
    }

    private static class MyHandler extends Handler {

        private RecordVideoPresenter mPresenter;

        MyHandler(RecordVideoPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mPresenter == null) {
                return;
            }
            if (msg.what == MSG_STOP_RECORD) {
                if (mPresenter.needStopDelayed) {
                    mPresenter.needStopDelayed = false;
                    //小于规定时长，不进入播放环节，重置资源
                    mPresenter.showErrorToast();
                    mPresenter.releaseRecorderManager();
                    mPresenter.releaseTiming();
                    mPresenter.isRecording = false;
                    mPresenter.resetResource();
                } else {
                    if (mPresenter.stopRecordVideo() && msg.arg1 == 0) {
                        mPresenter.playVideo();
                        mPresenter.setRecordOrPlayVisible();
                    }
                }
            }
        }
    }
}
