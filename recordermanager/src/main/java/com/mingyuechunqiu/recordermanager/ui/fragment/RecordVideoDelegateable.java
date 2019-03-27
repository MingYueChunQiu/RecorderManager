package com.mingyuechunqiu.recordermanager.ui.fragment;

import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/29
 *     desc   : 录制视频代理接口
 *     version: 1.0
 * </pre>
 */
interface RecordVideoDelegateable {

    /**
     * 开始相机预览
     */
    void startPreview();

    /**
     * 按下按钮开始录制视频
     *
     * @return 如果成功开始录制返回true，否则返回false
     */
    boolean pressToStartRecordVideo();

    /**
     * 释放按钮停止录制视频
     *
     * @param isCancel 是否是动态权限导致ACTION_CANCEL事件使得录制停止
     */
    void releaseToStopRecordVideo(boolean isCancel);

    /**
     * 翻转摄像头
     */
    void flipCamera();

    /**
     * 开始录制视频
     */
    void startRecordVideo();

    /**
     * 停止录制视频，返回是否录制成功
     *
     * @return 录制成功返回true，否则返回false
     */
    boolean stopRecordVideo();

    /**
     * 释放计时资源
     */
    void releaseTiming();

    /**
     * 播放录制好的视频
     */
    void playVideo();

    /**
     * 暂停播放视频
     *
     * @param controlViews 是否要控制控件
     */
    void pausePlayVideo(boolean controlViews);

    /**
     * 恢复播放视频
     *
     * @param controlViews 是否要控制控件
     */
    void resumePlayVideo(boolean controlViews);

    /**
     * 控制播放或暂停视频
     */
    void controlPlayOrPauseVideo();

    /**
     * 释放播放资源
     */
    void releaseMediaPlayer();

    /**
     * 释放相机资源
     *
     * @param giveUpCamera 是否放弃相机
     */
    void releaseCamera(boolean giveUpCamera);

    /**
     * 重置资源进行下次拍摄
     */
    void resetResource();

    /**
     * 控制录制或播放的控件可见性
     *
     * @param isInPlayingState 是否正在播放
     */
    void controlRecordOrPlayVisibility(boolean isInPlayingState);

    /**
     * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
     *
     * @param surfaceHolder 图层控制
     */
    void onSurfaceCreated(@NonNull SurfaceHolder surfaceHolder);

    /**
     * 进入完成录制视频事件
     */
    void onCompleteRecordVideo();

    /**
     * 进入点击返回键事件
     */
    void onClickBack();

    /**
     * 释放所有资源
     */
    void release();
}
