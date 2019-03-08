package com.mingyuechunqiu.recordermanager.interpect;

import android.hardware.Camera;

import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   :
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
}
