package com.mingyuechunqiu.recordermanager.feature.record

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     MingYueChunQiu
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2020/9/19 1:02 PM
 *      Desc:       录制相关调度中心（单例类）
 *                  实现IRecordDispatcher
 *      Version:    1.0
 * </pre>
 */
internal object RecordDispatcher : IRecordDispatcher {

    private var mOnRecordVideoListener: RecordVideoOption.OnRecordVideoListener? = null

    fun registerOnRecordVideoListener(listener: RecordVideoOption.OnRecordVideoListener) {
        mOnRecordVideoListener = listener
    }

    fun unregisterOnRecordVideoListener() {
        mOnRecordVideoListener = null
    }

    override fun onCompleteRecordVideo(filePath: String?, videoDuration: Int) {
        mOnRecordVideoListener?.onCompleteRecordVideo(filePath, videoDuration)
    }

    override fun onClickConfirm(filePath: String?, videoDuration: Int) {
        mOnRecordVideoListener?.onClickConfirm(filePath, videoDuration)
    }

    override fun onClickCancel(filePath: String?, videoDuration: Int) {
        mOnRecordVideoListener?.onClickCancel(filePath, videoDuration)
    }

    override fun onClickBack() {
        mOnRecordVideoListener?.onClickBack()
    }
}