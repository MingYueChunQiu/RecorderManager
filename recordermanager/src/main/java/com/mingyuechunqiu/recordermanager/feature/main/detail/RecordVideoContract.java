package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mingyuechunqiu.recordermanager.base.presenter.BaseAbstractPresenter;
import com.mingyuechunqiu.recordermanager.base.view.IBaseView;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/29
 *     desc   : 录制视频相关契约类，约定相互能实现调用的api
 *     version: 1.0
 * </pre>
 */
interface RecordVideoContract {

    interface View<P extends Presenter> extends IBaseView<P> {

        Context getCurrentContext();
    }

    abstract class Presenter<V extends View> extends BaseAbstractPresenter<V> {

        abstract void initView(@NonNull AppCompatTextView tvTiming,
                               @NonNull SurfaceView svVideo, @NonNull CircleProgressButton cpbRecord,
                               @NonNull AppCompatImageView ivFlipCamera,
                               @NonNull AppCompatImageView ivPlay, @NonNull AppCompatImageView ivCancel,
                               @NonNull AppCompatImageView ivConfirm, @NonNull AppCompatImageView ivBack,
                               @NonNull RecordVideoOption option);

        /**
         * 开始相机预览
         */
        abstract void startPreview();

        /**
         * 按下按钮开始录制视频
         *
         * @return 如果成功开始录制返回true，否则返回false
         */
        abstract boolean pressToStartRecordVideo();

        /**
         * 释放按钮停止录制视频
         *
         * @param isCancel 是否是动态权限导致ACTION_CANCEL事件使得录制停止
         */
        abstract void releaseToStopRecordVideo(boolean isCancel);

        /**
         * 翻转摄像头
         */
        abstract void flipCamera();

        /**
         * 开始录制视频
         */
        abstract void startRecordVideo();

        /**
         * 停止录制视频，返回是否录制成功
         *
         * @return 录制成功返回true，否则返回false
         */
        abstract boolean stopRecordVideo();

        /**
         * 释放计时资源
         */
        abstract void releaseTiming();

        /**
         * 播放录制好的视频
         */
        abstract void playVideo();

        /**
         * 暂停播放视频
         *
         * @param controlViews 是否要控制控件
         */
        abstract void pausePlayVideo(boolean controlViews);

        /**
         * 恢复播放视频
         *
         * @param controlViews 是否要控制控件
         */
        abstract void resumePlayVideo(boolean controlViews);

        /**
         * 控制播放或暂停视频
         */
        abstract void controlPlayOrPauseVideo();

        /**
         * 释放播放资源
         */
        abstract void releaseMediaPlayer();

        /**
         * 释放相机资源
         */
        abstract void releaseCamera();

        /**
         * 重置资源进行下次拍摄
         */
        abstract void resetResource();

        /**
         * 控制录制或播放的控件可见性
         *
         * @param isInPlayingState 是否正在播放
         */
        abstract void controlRecordOrPlayVisibility(boolean isInPlayingState);

        /**
         * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
         *
         * @param surfaceHolder 图层控制
         */
        abstract void onSurfaceCreated(@NonNull SurfaceHolder surfaceHolder);

        /**
         * 点击确认录制视频事件
         */
        abstract void onClickConfirm();

        /**
         * 点击取消按钮事件
         */
        abstract void onClickCancel();

        /**
         * 点击返回键事件
         */
        abstract void onClickBack();
    }
}
