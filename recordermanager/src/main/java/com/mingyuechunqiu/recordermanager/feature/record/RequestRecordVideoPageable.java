package com.mingyuechunqiu.recordermanager.feature.record;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;

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
    void startRecordVideo(@NonNull FragmentActivity activity, int requestCode);

    /**
     * 以默认配置打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     */
    void startRecordVideo(@NonNull Fragment fragment, int requestCode);

    /**
     * 打开录制视频界面
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param option      视频录制请求配置信息类
     */
    void startRecordVideo(@NonNull FragmentActivity activity, int requestCode, RecordVideoRequestOption option);

    /**
     * 打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param option      视频录制请求配置信息类
     */
    void startRecordVideo(@NonNull Fragment fragment, int requestCode, RecordVideoRequestOption option);
}
