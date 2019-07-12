package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/6/10
 *     desc   : 请求权限Fragment，空界面，只用于权限申请
 *              继承自Fragment
 *     version: 1.0
 * </pre>
 */
public class RequestPermissionFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private RecordVideoRequestOption mOption;
    private int mRequestCode;
    private WeakReference<FragmentActivity> mActivityRef;
    private WeakReference<Fragment> mFragmentRef;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (RecordPermissionUtils.checkRecordPermissions(this)) {
            startRecordVideoPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOption = null;
        mActivityRef = null;
        mFragmentRef = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startRecordVideoPage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    public static RequestPermissionFragment newInstance(RecordVideoRequestOption option, int requestCode,
                                                        FragmentActivity activity, Fragment fragment) {
        RequestPermissionFragment permissionFragment = new RequestPermissionFragment();
        permissionFragment.mOption = option;
        permissionFragment.mRequestCode = requestCode;
        permissionFragment.mActivityRef = new WeakReference<>(activity);
        permissionFragment.mFragmentRef = new WeakReference<>(fragment);
        return permissionFragment;
    }

    /**
     * 打开录制视频界面
     */
    private void startRecordVideoPage() {
        if (getContext() == null) {
            return;
        }
        Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        intent.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, mOption);
        FragmentManager fragmentManager = null;
        if (mActivityRef != null && mActivityRef.get() != null) {
            fragmentManager = mActivityRef.get().getSupportFragmentManager();
            mActivityRef.get().startActivityForResult(intent, mRequestCode);
        } else if (mFragmentRef != null && mFragmentRef.get() != null) {
            fragmentManager = mFragmentRef.get().getChildFragmentManager();
            mFragmentRef.get().startActivityForResult(intent, mRequestCode);
        } else {
            if (getContext() != null) {
                Toast.makeText(getContext(), getContext().getString(R.string.rm_error_start_record_video_page), Toast.LENGTH_SHORT).show();
            }
        }
        removeRequestPermissionPage(fragmentManager);
    }

    /**
     * 从父布局中移除申请权限界面
     *
     * @param fragmentManager 碎片管理器
     */
    private void removeRequestPermissionPage(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        fragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }
}
