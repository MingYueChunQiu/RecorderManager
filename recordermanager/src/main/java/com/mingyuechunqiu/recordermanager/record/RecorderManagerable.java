package com.mingyuechunqiu.recordermanager.record;

import android.hardware.Camera;

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
     * 初始化相机对象
     *
     * @return 返回设置好参数的相机
     */
    Camera initCamera();

    /**
     * 释放相机资源
     *
     * @param camera 相机
     */
    void releaseCamera(Camera camera);
}
