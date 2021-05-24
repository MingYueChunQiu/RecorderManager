package com.mingyuechunqiu.recordermanager.framework

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerProvider

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/23 10:36 下午
 *      Desc:       录制视频结果观察者
 *                  实现LifecycleEventObserver
 *      Version:    1.0
 * </pre>
 */
class RMRecordVideoResultObserver(val callback: RMRecordVideoResultCallback) :
    LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            RecorderManagerProvider.getRecordManagerGlobalDataStore().recordVideoResultObserver =
                null
        }
    }

    interface RMRecordVideoResultCallback {

        fun onGetRecordVideoResult(info: RecordVideoResultInfo)
    }
}