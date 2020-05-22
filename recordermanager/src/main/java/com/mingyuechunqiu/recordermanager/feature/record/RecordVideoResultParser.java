package com.mingyuechunqiu.recordermanager.feature.record;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.framework.parser.IRecordVideoResultParser;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_RESULT_INFO;

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2020/5/22 9:25 PM
 *      Desc:       录制视频结果解析类
 *                  实现IRecordVideoResultParser
 *      Version:    1.0
 * </pre>
 */
final class RecordVideoResultParser implements IRecordVideoResultParser {

    @Nullable
    @Override
    public RecordVideoResultInfo parseRecordVideoResult(@Nullable Intent data) {
        RecordVideoResultInfo info = null;
        if (data != null) {
            info = data.getParcelableExtra(EXTRA_RECORD_VIDEO_RESULT_INFO);
        }
        return info;
    }
}
