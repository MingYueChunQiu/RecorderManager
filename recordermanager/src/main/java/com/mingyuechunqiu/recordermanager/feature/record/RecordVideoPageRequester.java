package com.mingyuechunqiu.recordermanager.feature.record;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.mingyuechunqiu.recordermanager.BuildConfig;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RequestPermissionsFragment;
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultCallback;
import com.mingyuechunqiu.recordermanager.framework.requester.IRecordVideoPageRequester;

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
    public void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultCallback callback) {
        startRecordVideo(activity, callback, null);
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultCallback callback) {
        startRecordVideo(fragment, callback, null);
    }

    @Override
    public void startRecordVideo(@NonNull FragmentActivity activity, @NonNull RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option) {
        if (!activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            return;
        }
        addRequestPermissionPage(activity, activity.getSupportFragmentManager(), RequestPermissionsFragment.newInstance(option, callback));
    }

    @Override
    public void startRecordVideo(@NonNull Fragment fragment, @NonNull RMRecordVideoResultCallback callback, @Nullable RecordVideoRequestOption option) {
        addRequestPermissionPage(fragment, fragment.getChildFragmentManager(), RequestPermissionsFragment.newInstance(option, callback));
    }

    /**
     * 添加申请权限界面到主界面中
     *
     * @param fragmentManager    碎片管理器
     * @param permissionFragment 申请权限界面
     */
    private void addRequestPermissionPage(@NonNull LifecycleOwner owner, @NonNull FragmentManager fragmentManager, @NonNull RequestPermissionsFragment permissionFragment) {
        if (!owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            if (BuildConfig.DEBUG) {
                Log.e("RecorderManager", "RecordVideoPageRequester addRequestPermissionPage: lifecycleOwner lifecycle state must least than resumed");
            }
            return;
        }
        if (fragmentManager.findFragmentByTag(RequestPermissionsFragment.class.getSimpleName()) != null) {
            return;
        }
        if (permissionFragment.isAdded() || permissionFragment.isStateSaved()) {
            return;
        }
        fragmentManager
                .beginTransaction()
                .add(permissionFragment, RequestPermissionsFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }
}
