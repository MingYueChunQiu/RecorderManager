package com.mingyuechunqiu.recordermanager.feature.record;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.feature.interpect.IRecorderManagerInterceptor;
import com.mingyuechunqiu.recordermanager.framework.parser.IRecordVideoResultParser;
import com.mingyuechunqiu.recordermanager.framework.request.IRecordVideoRequest;

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
public final class RecorderManagerFactory {

    private RecorderManagerFactory() {
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance() {
        return newInstance(new RecorderHelper());
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @param intercept 录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderManagerInterceptor intercept) {
        return newInstance(new RecorderHelper(), intercept);
    }

    /**
     * 创建录制管理类实例
     *
     * @param helper 实际录制类
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderHelper helper) {
        return newInstance(helper, null);
    }

    /**
     * 创建录制管理类实例
     *
     * @param helper    实际录制类
     * @param intercept 录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderHelper helper, @Nullable IRecorderManagerInterceptor intercept) {
        return new RecorderManager(helper, intercept);
    }

    @NonNull
    public static IRecordVideoRequest getRecordVideoRequest() {
        return new RecordVideoPageRequest();
    }

    @NonNull
    public static IRecordVideoResultParser getRecordVideoResultParser() {
        return new RecordVideoResultParser();
    }
}
