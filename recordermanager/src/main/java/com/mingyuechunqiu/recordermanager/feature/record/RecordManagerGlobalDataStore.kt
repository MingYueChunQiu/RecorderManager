package com.mingyuechunqiu.recordermanager.feature.record

import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultObserver

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/24 10:14 下午
 *      Desc:       录制管理全局数据存储类（单例）
 *      Version:    1.0
 * </pre>
 */
internal object RecordManagerGlobalDataStore {

    var recordVideoResultObserver: RMRecordVideoResultObserver? = null
}