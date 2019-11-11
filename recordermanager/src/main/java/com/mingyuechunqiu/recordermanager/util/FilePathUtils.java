package com.mingyuechunqiu.recordermanager.util;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.SUFFIX_MP4;

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2019-10-27 10:56
 *      Desc:       文件路径工具类
 *      Version:    1.0
 * </pre>
 */
public class FilePathUtils {

    /**
     * 获取保存文件路径
     *
     * @param context 上下文
     * @return 返回文件路径，如果有误返回""
     */
    @NonNull
    public static String getSaveFilePath(@Nullable Context context) {
        if (context == null) {
            return "";
        }
        File file = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (file != null) {
            return file.getAbsolutePath() +
                    File.separator + System.currentTimeMillis() + SUFFIX_MP4;
        }
        return "";
    }
}
