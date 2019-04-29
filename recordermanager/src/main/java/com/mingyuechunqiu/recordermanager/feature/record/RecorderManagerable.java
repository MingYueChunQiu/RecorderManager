package com.mingyuechunqiu.recordermanager.feature.record;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.mingyuechunqiu.recordermanager.data.constants.Constants;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制管理类接口
 *              继承自Recorderable
 *     version: 1.0
 * </pre>
 */
public interface RecorderManagerable extends Recorderable {

    /**
     * 设置录制对象
     *
     * @param recorderable 录制对象实例
     */
    void setRecorderable(Recorderable recorderable);

    /**
     * 获取录制对象
     *
     * @return 返回录制对象实例
     */
    Recorderable getRecorderable();

    /**
     * 初始化相机对象
     *
     * @param holder Surface持有者
     * @return 返回初始化好的相机对象
     */
    Camera initCamera(SurfaceHolder holder);

    /**
     * 初始化相机对象
     *
     * @param cameraType 指定的摄像头类型
     * @param holder     Surface持有者
     * @return 返回初始化好的相机对象
     */
    Camera initCamera(Constants.CameraType cameraType, SurfaceHolder holder);

    /**
     * 翻转摄像头
     *
     * @param holder Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    Camera flipCamera(SurfaceHolder holder);

    /**
     * 翻转到指定类型摄像头
     *
     * @param cameraType 摄像头类型
     * @param holder     Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    Camera flipCamera(Constants.CameraType cameraType, SurfaceHolder holder);

    /**
     * 获取当前摄像头类型
     *
     * @return 返回摄像头类型
     */
    Constants.CameraType getCameraType();

    /**
     * 释放相机资源
     */
    void releaseCamera();

}
