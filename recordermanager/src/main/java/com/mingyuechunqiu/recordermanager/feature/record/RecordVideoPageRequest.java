package com.mingyuechunqiu.recordermanager.feature.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.mingyuechunqiu.recordermanager.feature.main.container.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_FILE_PATH;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_MAX_DURATION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 录制视频页面请求类
 *              实现StartRecordVideoPageable
 *     version: 1.0
 * </pre>
 */
class RecordVideoPageRequest implements RequestRecordVideoPageable {

    @Override
    public void startRecordVideo(Activity activity, int requestCode) {
        startRecordVideo(activity, requestCode, -1, null);
    }

    @Override
    public void startRecordVideo(Fragment fragment, int requestCode) {
        startRecordVideo(fragment, requestCode, -1, null);
    }

    @Override
    public void startRecordVideo(Activity activity, int requestCode, int maxDuration, String filePath) {
        if (!RecordPermissionUtils.checkRecordPermissions(activity)) {
            return;
        }
        activity.startActivityForResult(getRecordVideoIntent(activity,
                maxDuration, filePath), requestCode);
    }

    @Override
    public void startRecordVideo(Fragment fragment, int requestCode, int maxDuration, String filePath) {
        if (!RecordPermissionUtils.checkRecordPermissions(fragment) || fragment.getContext() == null) {
            return;
        }
        fragment.startActivityForResult(getRecordVideoIntent(fragment.getContext(),
                maxDuration, filePath), requestCode);
    }

    /**
     * 获取打开录制视频界面Intent
     *
     * @param context     上下文
     * @param maxDuration 最大时长
     * @param filePath    保存文件路径
     * @return 返回启动启动意图
     */
    private Intent getRecordVideoIntent(Context context, int maxDuration, String filePath) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        if (maxDuration > 0) {
            intent.putExtra(EXTRA_RECORD_VIDEO_MAX_DURATION, maxDuration);
        }
        if (!TextUtils.isEmpty(filePath)) {
            intent.putExtra(EXTRA_RECORD_VIDEO_FILE_PATH, filePath);
        }
        return intent;
    }
}
