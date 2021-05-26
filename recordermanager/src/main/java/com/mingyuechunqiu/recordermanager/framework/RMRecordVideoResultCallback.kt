package com.mingyuechunqiu.recordermanager.framework

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo
import com.mingyuechunqiu.recordermanager.data.exception.RecorderManagerException

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/26 9:36 下午
 *      Desc:       录制视频结果回调
 *      Version:    1.0
 * </pre>
 */
interface RMRecordVideoResultCallback {

    fun onResponseRecordVideoResult(info: RecordVideoResultInfo)

    fun onFailure(e: RecorderManagerException)
}