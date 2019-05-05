package com.mingyuechunqiu.recordermanager.feature.record;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 打开录制视频页面能力接口
 *     version: 1.0
 * </pre>
 */
public interface RequestRecordVideoPageable {

    /**
     * 以默认配置打开录制视频界面
     *
     * @param activity    Activity
     * @param requestCode 请求码
     */
    void startRecordVideo(Activity activity, int requestCode);

    /**
     * 以默认配置打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     */
    void startRecordVideo(Fragment fragment, int requestCode);

    /**
     * 打开录制视频界面
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param maxDuration 最大时长
     * @param filePath    保存文件路径
     */
    void startRecordVideo(Activity activity, int requestCode, int maxDuration, String filePath);

    /**
     * 打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param maxDuration 最大时长
     * @param filePath    保存文件路径
     */
    void startRecordVideo(Fragment fragment, int requestCode, int maxDuration, String filePath);
}
