package com.mingyuechunqiu.recordermanager.feature.record

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     MingYueChunQiu
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2020/9/19 1:26 PM
 *      Desc:       录制相关调度中心接口
 *                  继承自RecordVideoOption.OnRecordVideoListener
 *      Version:    1.0
 * </pre>
 */
interface IRecordDispatcher : RecordVideoOption.OnRecordVideoListener {

    fun registerOnRecordVideoListener(listener: RecordVideoOption.OnRecordVideoListener)

    fun unregisterOnRecordVideoListener()

    fun isRegisteredOnRecordVideoListener(): Boolean
}