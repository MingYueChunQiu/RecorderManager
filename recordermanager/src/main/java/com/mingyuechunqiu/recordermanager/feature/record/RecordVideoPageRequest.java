package com.mingyuechunqiu.recordermanager.feature.record;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RequestPermissionFragment;

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
    public void startRecordVideo(@NonNull FragmentActivity activity, int requestCode) {
        startRecordVideo(activity, requestCode, null);
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, int requestCode) {
        startRecordVideo(fragment, requestCode, null);
    }

    @Override
    public void startRecordVideo(@NonNull FragmentActivity activity, int requestCode, RecordVideoRequestOption option) {
        RequestPermissionFragment permissionFragment = RequestPermissionFragment.newInstance(option);
        addRequestPermissionPage(activity, permissionFragment);
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, int requestCode, RecordVideoRequestOption option) {
        RequestPermissionFragment permissionFragment = RequestPermissionFragment.newInstance(option);
        if (fragment.getActivity() != null) {
            addRequestPermissionPage(fragment.getActivity(), permissionFragment);
        }
    }

    /**
     * 添加申请权限界面到主界面中
     *
     * @param activity           界面
     * @param permissionFragment 申请权限界面
     */
    private void addRequestPermissionPage(@NonNull FragmentActivity activity, RequestPermissionFragment permissionFragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(permissionFragment, RequestPermissionFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }
}
