package com.mingyuechunqiu.recordermanager.feature.interpect;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.feature.record.IRecorderHelper;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   : 录制管理器拦截接口
 *              继承自CameraInterceptable
 *     version: 1.0
 * </pre>
 */
public interface IRecorderManagerInterceptor extends ICameraInterceptor {

    /**
     * 录制音频
     *
     * @param path 文件存储路径
     */
    void recordAudio(@NonNull String path);

    /**
     * 录制音频
     *
     * @param option 存储录制信息的对象
     */
    void recordAudio(@NonNull RecorderOption option);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param path    文件存储路径
     */
    void recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable String path);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param option  存储录制信息的对象
     */
    void recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable RecorderOption option);

    /**
     * 释放资源
     */
    void release();

    /**
     * 获取录制器
     *
     * @return 返回实例对象
     */
    @NonNull
    MediaRecorder getMediaRecorder(@NonNull MediaRecorder recorder);

    /**
     * 获取配置信息对象
     *
     * @return 返回实例对象
     */
    @Nullable
    RecorderOption getRecorderOption(@Nullable RecorderOption option);

    /**
     * 设置录制对象
     *
     * @param helper 录制对象实例
     */
    @NonNull
    IRecorderHelper setRecorderable(@NonNull IRecorderHelper helper);

    /**
     * 获取录制对象
     *
     * @return 返回录制对象实例
     */
    @NonNull
    IRecorderHelper getRecorderable(@NonNull IRecorderHelper helper);

    /**
     * 初始化相机对象
     *
     * @param holder Surface持有者
     * @return 返回初始化好的相机对象
     */
    @Nullable
    Camera initCamera(@NonNull SurfaceHolder holder);

    /**
     * 初始化相机对象
     *
     * @param cameraType 指定的摄像头类型
     * @param holder     Surface持有者
     * @return 返回初始化好的相机对象
     */
    @Nullable
    Camera initCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder);

    /**
     * 打开或关闭闪光灯
     *
     * @param turnOn true表示打开，false关闭
     */
    void switchFlashlight(boolean turnOn);

    /**
     * 翻转摄像头
     *
     * @param holder Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    @Nullable
    Camera flipCamera(@NonNull SurfaceHolder holder);

    /**
     * 翻转到指定类型摄像头
     *
     * @param cameraType 摄像头类型
     * @param holder     Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    @Nullable
    Camera flipCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder);

    /**
     * 获取当前摄像头类型
     *
     * @return 返回摄像头类型
     */
    @NonNull
    RecorderManagerConstants.CameraType getCameraType(@NonNull RecorderManagerConstants.CameraType cameraType);

    /**
     * 释放相机资源
     */
    void releaseCamera();
}
