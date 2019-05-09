package com.mingyuechunqiu.recordermanager.feature.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;

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
        startRecordVideo(activity, requestCode, null);
    }

    @Override
    public void startRecordVideo(Fragment fragment, int requestCode) {
        startRecordVideo(fragment, requestCode, null);
    }

    @Override
    public void startRecordVideo(Activity activity, int requestCode, RecordVideoRequestOption option) {
        if (!RecordPermissionUtils.checkRecordPermissions(activity)) {
            return;
        }
        activity.startActivityForResult(getRecordVideoIntent(activity, option), requestCode);
    }

    @Override
    public void startRecordVideo(Fragment fragment, int requestCode, RecordVideoRequestOption option) {
        if (!RecordPermissionUtils.checkRecordPermissions(fragment) || fragment.getContext() == null) {
            return;
        }
        fragment.startActivityForResult(getRecordVideoIntent(fragment.getContext(), option), requestCode);
    }

    /**
     * 获取打开录制视频界面Intent
     *
     * @param context 上下文
     * @param option  视频录制请求配置信息类
     * @return 返回启动启动意图
     */
    private Intent getRecordVideoIntent(Context context, RecordVideoRequestOption option) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        intent.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, option);
        return intent;
    }
}
