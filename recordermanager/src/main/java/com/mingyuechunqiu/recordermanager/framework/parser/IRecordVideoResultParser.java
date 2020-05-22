package com.mingyuechunqiu.recordermanager.framework.parser;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2020/5/22 9:14 PM
 *      Desc:       录制视频结果解析器接口
 *                  继承自IParser
 *      Version:    1.0
 * </pre>
 */
public interface IRecordVideoResultParser extends IParser {

    @Nullable
    RecordVideoResultInfo parseRecordVideoResult(@Nullable Intent data);
}
