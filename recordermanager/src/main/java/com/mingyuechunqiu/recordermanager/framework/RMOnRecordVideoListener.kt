package com.mingyuechunqiu.recordermanager.framework

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     MingYueChunQiu
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2020/9/20 12:24 PM
 *      Desc:       视频录制监听器
 *      Version:    1.0
 * </pre>
 */
interface RMOnRecordVideoListener {

    /**
     * 当完成一次录制时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onCompleteRecordVideo(filePath: String?, videoDuration: Int)

    /**
     * 当点击确认录制结果按钮时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onClickConfirm(filePath: String?, videoDuration: Int)

    /**
     * 当点击取消按钮时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onClickCancel(filePath: String?, videoDuration: Int)

    /**
     * 当点击返回按钮时回调
     */
    fun onClickBack()
}