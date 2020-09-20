package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.content.Context;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.mingyuechunqiu.recordermanager.base.presenter.BaseAbstractPresenter;
import com.mingyuechunqiu.recordermanager.base.view.IBaseView;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.framework.RMOnRecordVideoListener;

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

    interface View<P extends Presenter<?>> extends IBaseView<P>, RMOnRecordVideoListener {

        Context getCurrentContext();

        /**
         * 显示计时控件信息
         *
         * @param text 文本信息
         */
        void showTimingText(@NonNull String text);

        @Nullable
        SurfaceHolder getSurfaceHolder();

        /**
         * 控制录制或播放的控件可见性
         *
         * @param isInPlayingState 是否正在播放
         */
        void controlRecordOrPlayVisibility(boolean isInPlayingState);

        /**
         * 控制闪光灯显示
         *
         * @param show true表示显示，false隐藏
         */
        void showFlashlightButton(boolean show);
    }

    abstract class Presenter<V extends View<?>> extends BaseAbstractPresenter<V> {

        /**
         * 初始化配置
         *
         * @param option 配置信息对象
         */
        abstract void initConfiguration(@NonNull RecordVideoOption option);

        /**
         * 开始相机预览
         *
         * @param holder 图层控制
         */
        abstract void startPreview(@Nullable SurfaceHolder holder);

        /**
         * 按下按钮开始录制视频
         *
         * @param holder       图层控制
         * @param ivFlipCamera 翻转摄像头控件
         * @param ivFlashlight 闪光灯控件
         * @param ivBack       返回录制控件
         * @return 如果成功开始录制返回true，否则返回false
         */
        abstract boolean pressToStartRecordVideo(@Nullable SurfaceHolder holder, @NonNull AppCompatImageView ivFlipCamera,
                                                 @NonNull AppCompatImageView ivFlashlight, @NonNull AppCompatImageView ivBack);

        /**
         * 释放按钮停止录制视频
         *
         * @param isCancel 是否是动态权限导致ACTION_CANCEL事件使得录制停止
         */
        abstract void releaseToStopRecordVideo(boolean isCancel);

        /**
         * 翻转摄像头
         *
         * @param holder 图层控制
         */
        abstract void flipCamera(@Nullable SurfaceHolder holder);

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
         * @param ivPlay       播放控件
         */
        abstract void pausePlayVideo(boolean controlViews, @NonNull AppCompatImageView ivPlay);

        /**
         * 恢复播放视频
         *
         * @param controlViews 是否要控制控件
         * @param ivPlay       播放控件
         * @param holder       图层控制
         */
        abstract void resumePlayVideo(boolean controlViews, @NonNull AppCompatImageView ivPlay, @Nullable SurfaceHolder holder);

        /**
         * 控制播放或暂停视频
         *
         * @param ivPlay 播放控件
         * @param holder 图层控制
         */
        abstract void controlPlayOrPauseVideo(@NonNull AppCompatImageView ivPlay, @Nullable SurfaceHolder holder);

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
         * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
         *
         * @param holder 图层控制
         * @param ivPlay 播放控件
         */
        abstract void onSurfaceCreated(@NonNull SurfaceHolder holder, @NonNull AppCompatImageView ivPlay);

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

        /**
         * 获取计时提示信息
         *
         * @param timing     计时信息
         * @param isInTiming 是否正在计时，true表示
         * @return 返回最终文本
         */
        @NonNull
        abstract String getTimingHint(@NonNull String timing, boolean isInTiming);

        /**
         * 切换闪光灯状态
         *
         * @param turnOn true表示打开，false关闭
         * @return 如果切换状态成功返回true，否则返回false
         */
        abstract boolean switchFlashlightState(boolean turnOn);
    }
}
