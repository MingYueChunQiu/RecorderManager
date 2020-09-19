package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.data.constants.KeyPrefixConstants;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;

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

    private static final String BUNDLE_EXTRA_REQUEST_CODE = KeyPrefixConstants.KEY_BUNDLE + "request_code";

    private RecordVideoRequestOption mOption;
    private int mRequestCode;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mOption = args.getParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION);
            mRequestCode = args.getInt(BUNDLE_EXTRA_REQUEST_CODE);
        }
        if (RecordPermissionUtils.checkRecordPermissions(this)) {
            startRecordVideoPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOption = null;
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
        RecorderManagerFactory.getRecordDispatcher().unregisterOnRecordVideoListener();
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    public static RequestPermissionFragment newInstance(@Nullable RecordVideoRequestOption option, int requestCode) {
        RequestPermissionFragment permissionFragment = new RequestPermissionFragment();
        if (option != null && option.getRecordVideoOption() != null &&
                option.getRecordVideoOption().getOnRecordVideoListener() != null) {
            RecorderManagerFactory.getRecordDispatcher().registerOnRecordVideoListener(
                    option.getRecordVideoOption().getOnRecordVideoListener());
        }
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION, option);
        args.putInt(BUNDLE_EXTRA_REQUEST_CODE, requestCode);
        permissionFragment.setArguments(args);
        return permissionFragment;
    }

    /**
     * 打开录制视频界面
     */
    private void startRecordVideoPage() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        intent.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, mOption);
        FragmentManager fragmentManager = null;
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            fragmentManager = parentFragment.getChildFragmentManager();
            parentFragment.startActivityForResult(intent, mRequestCode);
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                fragmentManager = activity.getSupportFragmentManager();
                activity.startActivityForResult(intent, mRequestCode);
            }
        }
        if (fragmentManager == null) {
            Toast.makeText(context, getContext().getString(R.string.rm_error_start_record_video_page), Toast.LENGTH_SHORT).show();
        }
        removeRequestPermissionPage(fragmentManager);
    }

    /**
     * 从父布局中移除申请权限界面
     *
     * @param fragmentManager 碎片管理器
     */
    private void removeRequestPermissionPage(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        fragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }
}
