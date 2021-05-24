package com.mingyuechunqiu.recordermanager.data.constants;

import static com.mingyuechunqiu.recordermanager.data.constants.KeyPrefixConstants.KEY_EXTRA;

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
public class RecorderManagerConstants {

    //录制视频请求配置信息
    public static final String EXTRA_RECORD_VIDEO_REQUEST_OPTION = KEY_EXTRA + "record_video_request_option";

    public static final String SUFFIX_MP4 = ".mp4";//MP4视频格式后缀

    public static final int DEFAULT_RECORD_VIDEO_DURATION = 30;//默认录制视频持续时长

    /**
     * 摄像头类型
     */
    public enum CameraType {
        CAMERA_NOT_SET, CAMERA_FRONT, CAMERA_BACK
    }
}
