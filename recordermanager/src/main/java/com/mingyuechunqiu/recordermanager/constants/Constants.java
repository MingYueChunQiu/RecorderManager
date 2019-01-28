package com.mingyuechunqiu.recordermanager.constants;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Constants {

    public static final String PREFIX_EXTRA = "EXTRA_";//Intent数据前缀

    //录制视频文件路径
    public static final String EXTRA_RECORD_VIDEO_FILE_PATH = PREFIX_EXTRA + "record_video_file_path";
    //录制视频最大时长
    public static final String EXTRA_RECORD_VIDEO_MAX_DURATION = PREFIX_EXTRA + "record_video_max_duration";

    public static final String SUFFIX_MP4 = ".mp4";//MP4视频格式后缀
}
