package com.mingyuechunqiu.recordermanager.feature.record;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RequestPermissionFragment;
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultObserver;
import com.mingyuechunqiu.recordermanager.framework.request.IRecordVideoPageRequester;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 录制视频页面请求者类
 *              实现IRecordVideoPageRequester
 *     version: 1.0
 * </pre>
 */
final class RecordVideoPageRequester implements IRecordVideoPageRequester {

    @Override
    public void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultObserver.RMRecordVideoResultCallback callback) {
        startRecordVideo(activity, callback, null);
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultObserver.RMRecordVideoResultCallback callback) {
        startRecordVideo(fragment, callback, null);
    }

    @Override
    public void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultObserver.RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option) {
        addRequestPermissionPage(activity.getSupportFragmentManager(), RequestPermissionFragment.newInstance(option, callback));
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultObserver.RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option) {
        addRequestPermissionPage(fragment.getChildFragmentManager(), RequestPermissionFragment.newInstance(option, callback));
    }

    /**
     * 添加申请权限界面到主界面中
     *
     * @param fragmentManager    碎片管理器
     * @param permissionFragment 申请权限界面
     */
    private void addRequestPermissionPage(@NonNull FragmentManager fragmentManager, @NonNull RequestPermissionFragment permissionFragment) {
        fragmentManager
                .beginTransaction()
                .add(permissionFragment, RequestPermissionFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }
}
