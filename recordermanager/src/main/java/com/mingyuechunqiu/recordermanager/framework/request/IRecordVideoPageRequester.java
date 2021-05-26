package com.mingyuechunqiu.recordermanager.framework.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultCallback;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 录制视频页面请求者接口
 *              继承自IRMRequester
 *     version: 1.0
 * </pre>
 */
public interface IRecordVideoPageRequester extends IRMRequester {

    /**
     * 以默认配置打开录制视频界面
     *
     * @param activity Activity
     * @param callback 视频录制结果回调
     */
    void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultCallback callback);

    /**
     * 以默认配置打开录制视频界面
     *
     * @param fragment Fragment
     * @param callback 视频录制结果回调
     */
    void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultCallback callback);

    /**
     * 打开录制视频界面
     *
     * @param activity Activity
     * @param option   视频录制请求配置信息类
     * @param callback 视频录制结果回调
     */
    void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option);

    /**
     * 打开录制视频界面
     *
     * @param fragment Fragment
     * @param option   视频录制请求配置信息类
     * @param callback 视频录制结果回调
     */
    void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option);
}
