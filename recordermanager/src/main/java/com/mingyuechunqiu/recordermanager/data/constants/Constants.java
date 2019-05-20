package com.mingyuechunqiu.recordermanager.data.constants;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 常量类
 *     version: 1.0
 * </pre>
 */
public class Constants {

    public static final String PREFIX_EXTRA = "EXTRA_";//Intent数据前缀

    //录制视频请求配置信息
    public static final String EXTRA_RECORD_VIDEO_REQUEST_OPTION = PREFIX_EXTRA + "record_video_request_option";
    //录制视频结果
    public static final String EXTRA_RECORD_VIDEO_RESULT_INFO = PREFIX_EXTRA + "record_video_result_info";

    public static final String SUFFIX_MP4 = ".mp4";//MP4视频格式后缀

    /**
     * 摄像头类型
     */
    public enum CameraType {
        CAMERA_NOT_SET, CAMERA_FRONT, CAMERA_BACK
    }
}
