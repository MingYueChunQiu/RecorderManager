package com.mingyuechunqiu.recordermanager.feature.record;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.feature.interpect.RecorderManagerInterceptable;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_RESULT_INFO;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/2/21
 *     desc   : 录制管理工厂类
 *     version: 1.0
 * </pre>
 */
public class RecorderManagerFactory {

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @return 返回录制管理类实例
     */
    @NonNull
    public static RecorderManagerable newInstance() {
        return newInstance(new RecorderHelper());
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @param intercept 录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static RecorderManagerable newInstance(RecorderManagerInterceptable intercept) {
        return newInstance(new RecorderHelper(), intercept);
    }

    /**
     * 创建录制管理类实例
     *
     * @param recorderable 实际录制类
     * @return 返回录制管理类实例
     */
    @NonNull
    public static RecorderManagerable newInstance(Recorderable recorderable) {
        return newInstance(recorderable, null);
    }

    /**
     * 创建录制管理类实例
     *
     * @param recorderable 实际录制类
     * @param intercept    录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static RecorderManagerable newInstance(Recorderable recorderable, RecorderManagerInterceptable intercept) {
        return new RecorderManager(recorderable, intercept);
    }

    @NonNull
    public static RequestRecordVideoPageable getRecordVideoRequest() {
        return new RecordVideoPageRequest();
    }

    @Nullable
    public static RecordVideoResultInfo getRecordVideoResult(@Nullable Intent data) {
        RecordVideoResultInfo info = null;
        if (data != null) {
            info = data.getParcelableExtra(EXTRA_RECORD_VIDEO_RESULT_INFO);
        }
        return info;
    }
}
