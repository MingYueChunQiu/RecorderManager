package com.mingyuechunqiu.recordermanager.feature.interpect;

import android.hardware.Camera;

import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   : 相机相关拦截器接口
 *     version: 1.0
 * </pre>
 */
public interface CameraInterceptable {

    /**
     * 拦截本机支持的相机预览大小参数设置
     *
     * @param list 本机支持的相机预览大小集合
     * @return 拦截不设置返回true，否则返回false
     */
    boolean interceptSettingPreviewSize(List<Camera.Size> list);

    /**
     * 拦截本机支持的相机图片大小参数设置
     *
     * @param list 本机支持的相机图片大小集合
     * @return 拦截不设置返回true，否则返回false
     */
    boolean interceptSettingPictureSize(List<Camera.Size> list);

    /**
     * 拦截相机预览方向
     *
     * @param degrees 预览相机角度（竖屏设置90，横屏设置0）
     * @return 返回相机的预览方向角度
     */
    int interceptCameraDisplayOrientation(int degrees);
}
